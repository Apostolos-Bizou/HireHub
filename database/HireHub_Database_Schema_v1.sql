-- ============================================================================
-- HireHub — Database Schema (PostgreSQL 15+)
-- Phase 3.1 Deliverable
-- Version: 1.0 | April 2026
-- ============================================================================
-- This schema supports all 6 UI screens: Seafarer Profile, Talent Search,
-- Shortlist & Pipeline, Manning Agent Dashboard, Home Feed, Messaging.
-- Designed for the core flow: Seafarer → Profile → Shipowner Search →
-- Shortlist → Forward to Manning Agent → Deployment (outside HireHub).
-- ============================================================================

-- Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";  -- For fuzzy text search on profiles

-- ============================================================================
-- ENUMS
-- ============================================================================

CREATE TYPE user_role AS ENUM ('seafarer', 'shipowner', 'manning_agent');
CREATE TYPE subscription_tier AS ENUM ('free', 'premium', 'enterprise');
CREATE TYPE vessel_type AS ENUM (
    'oil_tanker', 'chemical_tanker', 'oil_chemical_tanker',
    'bulk_carrier', 'container', 'lpg', 'lng',
    'general_cargo', 'roro', 'passenger', 'offshore', 'other'
);
CREATE TYPE cert_type AS ENUM (
    'COC', 'STCW', 'COP', 'FLAG_ENDORSEMENT',
    'GMDSS', 'MEDICAL', 'BASIC_SAFETY', 'OTHER'
);
CREATE TYPE cert_status AS ENUM ('valid', 'expiring', 'expired', 'revoked');
CREATE TYPE company_type AS ENUM ('shipowner', 'manning_agent');
CREATE TYPE shortlist_status AS ENUM ('active', 'sent_to_agent', 'closed');
CREATE TYPE candidate_status AS ENUM ('shortlisted', 'under_review', 'sent_to_agent', 'rejected');
CREATE TYPE notification_type AS ENUM ('profile_view', 'shortlist_add', 'message', 'match', 'system');
CREATE TYPE post_type AS ENUM ('text', 'article', 'certificate', 'photo');
CREATE TYPE urgency_level AS ENUM ('standard', 'urgent', 'critical');
CREATE TYPE deployment_stage AS ENUM ('received', 'vetting', 'contract', 'deployment', 'completed', 'cancelled');

-- ============================================================================
-- 1. USERS — Core authentication & identity
-- ============================================================================
-- All three personas (seafarer, shipowner, manning agent) share this table.
-- Role determines which features and pages are accessible (role-based IA).

CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    role            user_role NOT NULL,
    email_verified  BOOLEAN NOT NULL DEFAULT FALSE,
    phone           VARCHAR(30),
    subscription    subscription_tier NOT NULL DEFAULT 'free',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_active     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role ON users (role);
CREATE INDEX idx_users_last_active ON users (last_active DESC);

-- ============================================================================
-- 2. SEAFARER_PROFILES — The heart of HireHub
-- ============================================================================
-- Maps to the Seafarer Profile mockup (Fig.7). Every field here is rendered
-- in the hero card, sidebar, or used for search filtering.

CREATE TABLE seafarer_profiles (
    id                    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id               UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    full_name             VARCHAR(200) NOT NULL,
    nationality           VARCHAR(100) NOT NULL,
    date_of_birth         DATE,
    current_rank          VARCHAR(100) NOT NULL,  -- e.g., '3rd Officer', 'Chief Engineer'
    photo_url             TEXT,
    bio                   TEXT,
    
    -- Verification (green badge on profile)
    verified_status       BOOLEAN NOT NULL DEFAULT FALSE,
    verified_at           TIMESTAMPTZ,
    
    -- Profile completeness bar (sidebar component)
    profile_completeness  SMALLINT NOT NULL DEFAULT 0 CHECK (profile_completeness BETWEEN 0 AND 100),
    
    -- Availability grid (sidebar component)
    available_from        DATE,
    contract_preference   VARCHAR(50),     -- 'permanent', 'contract', 'relief'
    desired_rank          VARCHAR(100),
    preferred_vessel_types TEXT[],          -- Array of vessel_type values
    salary_expectation_usd NUMERIC(10,2),
    
    -- Government IDs (encrypted at rest, Annex C compliance)
    passport_no           VARCHAR(50),
    seamans_book_no       VARCHAR(50),
    
    -- Stats (displayed in hero card)
    profile_views_count   INTEGER NOT NULL DEFAULT 0,
    shortlist_count       INTEGER NOT NULL DEFAULT 0,
    inquiry_count         INTEGER NOT NULL DEFAULT 0,
    
    created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_profiles_user ON seafarer_profiles (user_id);
CREATE INDEX idx_profiles_rank ON seafarer_profiles (current_rank);
CREATE INDEX idx_profiles_nationality ON seafarer_profiles (nationality);
CREATE INDEX idx_profiles_available ON seafarer_profiles (available_from);
CREATE INDEX idx_profiles_verified ON seafarer_profiles (verified_status) WHERE verified_status = TRUE;
CREATE INDEX idx_profiles_fullname_trgm ON seafarer_profiles USING gin (full_name gin_trgm_ops);

-- ============================================================================
-- 3. SEA_SERVICE — Timeline component on Seafarer Profile
-- ============================================================================
-- Each record = one contract / embarkation. Displayed as timeline with
-- blue dots (officer) or amber dots (cadet) per the design system.

CREATE TABLE sea_service (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id      UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    vessel_name     VARCHAR(200) NOT NULL,
    vessel_type     vessel_type NOT NULL,
    dwt             INTEGER,
    flag_state      VARCHAR(100),
    rank_held       VARCHAR(100) NOT NULL,
    company_name    VARCHAR(200),
    start_date      DATE NOT NULL,
    end_date        DATE,                    -- NULL = currently onboard
    is_current      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_sea_service_profile ON sea_service (profile_id);
CREATE INDEX idx_sea_service_dates ON sea_service (start_date DESC, end_date DESC);
CREATE INDEX idx_sea_service_vessel_type ON sea_service (vessel_type);

-- ============================================================================
-- 4. CERTIFICATES — Certificates & Licenses section on profile
-- ============================================================================
-- Includes Valid/Expiring badges (green/amber). AI-verified flag maps to
-- the "Verified" badge with checkmark per design system.

CREATE TABLE certificates (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id          UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    cert_type           cert_type NOT NULL,
    cert_name           VARCHAR(200) NOT NULL,
    issuing_authority   VARCHAR(200),
    issue_date          DATE,
    expiry_date         DATE,
    status              cert_status NOT NULL DEFAULT 'valid',
    ai_verified         BOOLEAN NOT NULL DEFAULT FALSE,
    ai_verified_at      TIMESTAMPTZ,
    document_url        TEXT,                -- S3/GCS path to uploaded scan
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_certs_profile ON certificates (profile_id);
CREATE INDEX idx_certs_type ON certificates (cert_type);
CREATE INDEX idx_certs_expiry ON certificates (expiry_date);
CREATE INDEX idx_certs_status ON certificates (status);

-- ============================================================================
-- 5. SKILLS — Tag/pill layout on Seafarer Profile
-- ============================================================================
-- Examples: ECDIS, GMDSS, Cargo operations, Bridge watch, ISM/ISPS

CREATE TABLE skills (
    id                UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id        UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    skill_name        VARCHAR(100) NOT NULL,
    endorsement_count INTEGER NOT NULL DEFAULT 0,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (profile_id, skill_name)
);

CREATE INDEX idx_skills_profile ON skills (profile_id);
CREATE INDEX idx_skills_name ON skills (skill_name);

-- ============================================================================
-- 6. COMPANIES — Shipowner & Manning Agent profiles
-- ============================================================================
-- Covers both Company Profile (shipowner) and Agency Profile (manning agent).
-- DMW license verification is critical for legal compliance (R.A. 12021).

CREATE TABLE companies (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    name            VARCHAR(300) NOT NULL,
    type            company_type NOT NULL,
    country         VARCHAR(100),
    city            VARCHAR(100),
    
    -- Manning agent specific (DMW compliance)
    dmw_license_no  VARCHAR(100),
    dmw_verified    BOOLEAN NOT NULL DEFAULT FALSE,
    dmw_verified_at TIMESTAMPTZ,
    principals_count INTEGER DEFAULT 0,
    
    -- Shipowner specific
    fleet_size      INTEGER DEFAULT 0,
    
    -- Shared
    logo_url        TEXT,
    website         VARCHAR(500),
    description     TEXT,
    
    -- Agent dashboard stats
    deployments_ytd INTEGER DEFAULT 0,
    placement_rate  NUMERIC(5,2),   -- Percentage, e.g., 94.50
    
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_companies_user ON companies (user_id);
CREATE INDEX idx_companies_type ON companies (type);
CREATE INDEX idx_companies_dmw ON companies (dmw_license_no) WHERE dmw_license_no IS NOT NULL;

-- ============================================================================
-- 7. VESSELS — Fleet data for shipowner Company Profile
-- ============================================================================

CREATE TABLE vessels (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_id      UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    name            VARCHAR(200) NOT NULL,
    type            vessel_type NOT NULL,
    dwt             INTEGER,
    flag_state      VARCHAR(100),
    imo_number      VARCHAR(20) UNIQUE,
    year_built      SMALLINT,
    crew_capacity   INTEGER,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_vessels_company ON vessels (company_id);
CREATE INDEX idx_vessels_type ON vessels (type);
CREATE INDEX idx_vessels_imo ON vessels (imo_number) WHERE imo_number IS NOT NULL;

-- ============================================================================
-- 8. SHORTLISTS — Bridge between discovery and deployment
-- ============================================================================
-- Maps to Shortlist & Pipeline mockup (Fig.9). The owner creates a shortlist,
-- adds candidates, and forwards to their manning agent. This is the critical
-- handoff point where HireHub's role ends.

CREATE TABLE shortlists (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_company_id    UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    agent_company_id    UUID REFERENCES companies(id),       -- NULL until agent assigned
    title               VARCHAR(300) NOT NULL,               -- e.g., "3rd Officer — Oil/Chemical Tanker"
    vessel_context      VARCHAR(300),                        -- e.g., "MT Pacific Voyager replacement"
    vessel_id           UUID REFERENCES vessels(id),
    status              shortlist_status NOT NULL DEFAULT 'active',
    urgency             urgency_level NOT NULL DEFAULT 'standard',
    crew_change_date    DATE,
    notes               TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    forwarded_at        TIMESTAMPTZ,
    closed_at           TIMESTAMPTZ,
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_shortlists_owner ON shortlists (owner_company_id);
CREATE INDEX idx_shortlists_agent ON shortlists (agent_company_id) WHERE agent_company_id IS NOT NULL;
CREATE INDEX idx_shortlists_status ON shortlists (status);
CREATE INDEX idx_shortlists_created ON shortlists (created_at DESC);

-- ============================================================================
-- 9. SHORTLIST_CANDIDATES — Candidates within a shortlist
-- ============================================================================
-- Each row = one seafarer in one shortlist. Status tracks pipeline progression:
-- shortlisted → under_review → sent_to_agent

CREATE TABLE shortlist_candidates (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shortlist_id    UUID NOT NULL REFERENCES shortlists(id) ON DELETE CASCADE,
    profile_id      UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    ai_score        NUMERIC(5,2),           -- 0.00 to 100.00 (maps to AI score ring)
    status          candidate_status NOT NULL DEFAULT 'shortlisted',
    notes           TEXT,                   -- Shipowner's private notes
    added_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    status_changed_at TIMESTAMPTZ,
    UNIQUE (shortlist_id, profile_id)
);

CREATE INDEX idx_sc_shortlist ON shortlist_candidates (shortlist_id);
CREATE INDEX idx_sc_profile ON shortlist_candidates (profile_id);
CREATE INDEX idx_sc_score ON shortlist_candidates (ai_score DESC);
CREATE INDEX idx_sc_status ON shortlist_candidates (status);

-- ============================================================================
-- 10. AGENT_PIPELINE — Manning agent deployment tracking
-- ============================================================================
-- Maps to the 4-column pipeline on Manning Agent Dashboard (Fig.10):
-- Received → Vetting → Contract → Deployment
-- This tracks what happens AFTER the shortlist is forwarded to the agent.

CREATE TABLE agent_pipeline (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    agent_company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    shortlist_id    UUID NOT NULL REFERENCES shortlists(id),
    profile_id      UUID NOT NULL REFERENCES seafarer_profiles(id),
    stage           deployment_stage NOT NULL DEFAULT 'received',
    vessel_name     VARCHAR(200),
    rank_required   VARCHAR(100),
    principal_name  VARCHAR(200),           -- e.g., "Tsakos Energy Navigation"
    crew_change_date DATE,
    notes           TEXT,
    received_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    stage_changed_at TIMESTAMPTZ,
    completed_at    TIMESTAMPTZ,
    UNIQUE (shortlist_id, profile_id)
);

CREATE INDEX idx_pipeline_agent ON agent_pipeline (agent_company_id);
CREATE INDEX idx_pipeline_stage ON agent_pipeline (stage);
CREATE INDEX idx_pipeline_received ON agent_pipeline (received_at DESC);

-- ============================================================================
-- 11. AI_SCORES — AI compatibility scores (per search context)
-- ============================================================================
-- The score ring on candidate cards (green 85+, blue 70-84, amber <70).
-- Scores are contextual — they depend on the shipowner's search criteria.
-- Cached and recomputed periodically.

CREATE TABLE ai_scores (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id              UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    search_criteria_hash    VARCHAR(64) NOT NULL,   -- SHA-256 of normalized search params
    score                   NUMERIC(5,2) NOT NULL CHECK (score BETWEEN 0 AND 100),
    criteria_breakdown      JSONB,                  -- { rank_match: 95, experience: 82, certs: 90, ... }
    computed_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at              TIMESTAMPTZ,
    UNIQUE (profile_id, search_criteria_hash)
);

CREATE INDEX idx_ai_profile ON ai_scores (profile_id);
CREATE INDEX idx_ai_hash ON ai_scores (search_criteria_hash);
CREATE INDEX idx_ai_score ON ai_scores (score DESC);

-- ============================================================================
-- 12. THREADS & MESSAGES — Messaging / InMail (Fig.12)
-- ============================================================================
-- Two-panel layout: thread list (left) + active conversation (right).
-- Threads can be linked to a shortlist for the context banner:
-- "Juan is on your shortlist for MT Pacific Voyager (3rd Officer)"

CREATE TABLE threads (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shortlist_id    UUID REFERENCES shortlists(id),  -- Optional shortlist context
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_message_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Thread participants (supports group conversations if needed)
CREATE TABLE thread_participants (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    thread_id       UUID NOT NULL REFERENCES threads(id) ON DELETE CASCADE,
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    joined_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_read_at    TIMESTAMPTZ,
    is_muted        BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (thread_id, user_id)
);

CREATE INDEX idx_tp_thread ON thread_participants (thread_id);
CREATE INDEX idx_tp_user ON thread_participants (user_id);

CREATE TABLE messages (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    thread_id       UUID NOT NULL REFERENCES threads(id) ON DELETE CASCADE,
    sender_id       UUID NOT NULL REFERENCES users(id),
    body            TEXT NOT NULL,
    sent_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    edited_at       TIMESTAMPTZ,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_messages_thread ON messages (thread_id, sent_at DESC);
CREATE INDEX idx_messages_sender ON messages (sender_id);

-- ============================================================================
-- 13. NOTIFICATIONS — Bell icon alerts
-- ============================================================================

CREATE TABLE notifications (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type            notification_type NOT NULL,
    title           VARCHAR(200) NOT NULL,
    body            TEXT,
    action_url      VARCHAR(500),
    reference_id    UUID,                   -- Generic FK to related entity
    is_read         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notif_user ON notifications (user_id, created_at DESC);
CREATE INDEX idx_notif_unread ON notifications (user_id) WHERE is_read = FALSE;

-- ============================================================================
-- 14. PROFILE_VIEWS — "347 profile views" stat on hero card
-- ============================================================================

CREATE TABLE profile_views (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id          UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    viewer_user_id      UUID REFERENCES users(id),
    viewer_company_id   UUID REFERENCES companies(id),
    viewed_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pv_profile ON profile_views (profile_id, viewed_at DESC);
CREATE INDEX idx_pv_viewer ON profile_views (viewer_company_id);

-- ============================================================================
-- 15. POSTS — Home Feed content (Fig.11)
-- ============================================================================
-- LinkedIn-style feed: training announcements, career milestones, industry news.
-- Includes maritime-specific post type "certificate" for cert sharing.

CREATE TABLE posts (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    author_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type            post_type NOT NULL DEFAULT 'text',
    body            TEXT NOT NULL,
    image_url       TEXT,
    certificate_id  UUID REFERENCES certificates(id),  -- For cert-type posts
    likes_count     INTEGER NOT NULL DEFAULT 0,
    comments_count  INTEGER NOT NULL DEFAULT 0,
    shares_count    INTEGER NOT NULL DEFAULT 0,
    is_pinned       BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_posts_author ON posts (author_id);
CREATE INDEX idx_posts_created ON posts (created_at DESC);
CREATE INDEX idx_posts_type ON posts (type);

CREATE TABLE post_likes (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    post_id     UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (post_id, user_id)
);

CREATE TABLE post_comments (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    post_id     UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    user_id     UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    body        TEXT NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_comments_post ON post_comments (post_id, created_at);

-- ============================================================================
-- 16. EDUCATION — Sidebar section on Seafarer Profile
-- ============================================================================

CREATE TABLE education (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id      UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    institution     VARCHAR(300) NOT NULL,
    degree          VARCHAR(200),
    field_of_study  VARCHAR(200),
    start_year      SMALLINT,
    end_year        SMALLINT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_education_profile ON education (profile_id);

-- ============================================================================
-- 17. SAVED_SEARCHES — Shipowner saved search filters
-- ============================================================================
-- Allows shipowners to save and re-run their talent search configurations.

CREATE TABLE saved_searches (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name            VARCHAR(200) NOT NULL,
    filters         JSONB NOT NULL,         -- { rank: "3rd Officer", vessel_type: "tanker", ... }
    result_count    INTEGER,
    last_run_at     TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_saved_user ON saved_searches (user_id);

-- ============================================================================
-- 18. CONSENT_RECORDS — Data Privacy Act compliance (R.A. 10173)
-- ============================================================================
-- Tracks consent given/withdrawn per the Seafarer Consent Agreement.
-- Required by NPC Circular 2023-04 and Section 4 of the consent agreement.

CREATE TABLE consent_records (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    consent_type    VARCHAR(100) NOT NULL,   -- e.g., 'data_processing', 'ai_processing', 'cross_border'
    consent_version VARCHAR(20) NOT NULL,    -- Agreement version consented to
    granted         BOOLEAN NOT NULL,
    ip_address      INET,
    user_agent      TEXT,
    granted_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    withdrawn_at    TIMESTAMPTZ
);

CREATE INDEX idx_consent_user ON consent_records (user_id);
CREATE INDEX idx_consent_type ON consent_records (consent_type);

-- ============================================================================
-- 19. AUDIT_LOG — System-wide audit trail
-- ============================================================================
-- Tracks data access and modifications for NPC compliance and security.

CREATE TABLE audit_log (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID REFERENCES users(id),
    action          VARCHAR(100) NOT NULL,   -- e.g., 'profile.view', 'shortlist.forward', 'data.export'
    entity_type     VARCHAR(50),
    entity_id       UUID,
    details         JSONB,
    ip_address      INET,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_user ON audit_log (user_id, created_at DESC);
CREATE INDEX idx_audit_action ON audit_log (action);
CREATE INDEX idx_audit_entity ON audit_log (entity_type, entity_id);
CREATE INDEX idx_audit_created ON audit_log (created_at DESC);

-- ============================================================================
-- HELPER FUNCTIONS
-- ============================================================================

-- Auto-update updated_at timestamp
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply to all tables with updated_at
CREATE TRIGGER trg_users_updated BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_profiles_updated BEFORE UPDATE ON seafarer_profiles
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_certs_updated BEFORE UPDATE ON certificates
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_companies_updated BEFORE UPDATE ON companies
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_shortlists_updated BEFORE UPDATE ON shortlists
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();
CREATE TRIGGER trg_posts_updated BEFORE UPDATE ON posts
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

-- ============================================================================
-- SUMMARY
-- ============================================================================
-- 19 tables total:
--   Core:       users, seafarer_profiles, companies, vessels
--   Career:     sea_service, certificates, skills, education
--   Discovery:  ai_scores, saved_searches, profile_views
--   Pipeline:   shortlists, shortlist_candidates, agent_pipeline
--   Social:     threads, thread_participants, messages, posts,
--               post_likes, post_comments, notifications
--   Compliance: consent_records, audit_log
--
-- Key relationships:
--   users 1:1 seafarer_profiles (seafarer role)
--   users 1:1 companies (shipowner/agent role)
--   seafarer_profiles 1:N sea_service, certificates, skills
--   companies 1:N vessels, shortlists
--   shortlists N:M seafarer_profiles (via shortlist_candidates)
--   shortlists → agent_pipeline (handoff to manning agent)
--   threads N:M users (via thread_participants)
-- ============================================================================
