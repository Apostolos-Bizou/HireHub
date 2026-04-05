-- HireHub Database Schema v1
-- Flyway Migration: V1__initial_schema.sql
-- PostgreSQL 15+

-- Enable extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- ============================================================================
-- ENUMS
-- ============================================================================
CREATE TYPE user_role AS ENUM ('SEAFARER', 'SHIPOWNER', 'MANNING_AGENT', 'ADMIN');
CREATE TYPE account_status AS ENUM ('ACTIVE', 'SUSPENDED', 'DEACTIVATED', 'PENDING_VERIFICATION');
CREATE TYPE vessel_type AS ENUM ('OIL_TANKER', 'CHEMICAL_TANKER', 'OIL_CHEMICAL_TANKER', 'LPG_CARRIER', 'LNG_CARRIER', 'BULK_CARRIER', 'CONTAINER', 'GENERAL_CARGO', 'RO_RO', 'PASSENGER', 'OFFSHORE', 'TUG', 'OTHER');
CREATE TYPE certificate_type AS ENUM ('COC', 'STCW', 'COP', 'FLAG_ENDORSEMENT', 'GMDSS', 'MEDICAL', 'BASIC_SAFETY', 'ADVANCED_FIREFIGHTING', 'SURVIVAL_CRAFT', 'TANKER_FAMILIARIZATION', 'TANKER_ADVANCED', 'IGF_CODE', 'POLAR_CODE', 'OTHER');
CREATE TYPE certificate_status AS ENUM ('VALID', 'EXPIRING', 'EXPIRED', 'REVOKED', 'PENDING_VERIFICATION');
CREATE TYPE verified_status AS ENUM ('UNVERIFIED', 'PENDING', 'VERIFIED', 'REJECTED');
CREATE TYPE company_type AS ENUM ('SHIPOWNER', 'MANNING_AGENT', 'MANAGER', 'OPERATOR', 'OTHER');
CREATE TYPE shortlist_status AS ENUM ('ACTIVE', 'ARCHIVED', 'COMPLETED');
CREATE TYPE candidate_status AS ENUM ('SHORTLISTED', 'UNDER_REVIEW', 'SENT_TO_AGENT', 'REJECTED', 'HIRED');
CREATE TYPE notification_type AS ENUM ('PROFILE_VIEW', 'SHORTLIST_ADDED', 'MESSAGE_RECEIVED', 'CERTIFICATE_EXPIRING', 'MATCH_FOUND', 'SYSTEM_ALERT');
CREATE TYPE post_type AS ENUM ('TEXT', 'ARTICLE', 'CERTIFICATE_SHARE', 'MILESTONE', 'NEWS');
CREATE TYPE consent_status AS ENUM ('GIVEN', 'WITHDRAWN', 'EXPIRED');

-- ============================================================================
-- 1. USERS
-- ============================================================================
CREATE TABLE users (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email               VARCHAR(255) NOT NULL UNIQUE,
    password_hash       VARCHAR(255) NOT NULL,
    role                VARCHAR(20) NOT NULL,
    account_status      VARCHAR(25) NOT NULL DEFAULT 'PENDING_VERIFICATION',
    email_verified      BOOLEAN NOT NULL DEFAULT FALSE,
    last_login_at       TIMESTAMPTZ,
    last_active_at      TIMESTAMPTZ,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    locked_until        TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_users_email ON users (email);

-- ============================================================================
-- 2. SEAFARER PROFILES
-- ============================================================================
CREATE TABLE seafarer_profiles (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id                 UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    full_name               VARCHAR(200) NOT NULL,
    nationality             VARCHAR(100),
    date_of_birth           DATE,
    gender                  VARCHAR(10),
    current_rank            VARCHAR(100),
    preferred_vessel_type   VARCHAR(50),
    years_of_experience     INT,
    photo_url               VARCHAR(500),
    bio                     TEXT,
    phone_number            VARCHAR(30),
    address                 VARCHAR(500),
    passport_number         VARCHAR(50),
    seamans_book_number     VARCHAR(50),
    verified_status         VARCHAR(20) NOT NULL DEFAULT 'UNVERIFIED',
    profile_completeness    INT NOT NULL DEFAULT 0,
    available_from          DATE,
    desired_rank            VARCHAR(100),
    contract_preference     VARCHAR(50),
    salary_expectation_min  INT,
    salary_expectation_max  INT,
    salary_currency         VARCHAR(3) DEFAULT 'USD',
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_sp_user ON seafarer_profiles (user_id);
CREATE INDEX idx_sp_rank ON seafarer_profiles (current_rank);
CREATE INDEX idx_sp_nationality ON seafarer_profiles (nationality);
CREATE INDEX idx_sp_name_trgm ON seafarer_profiles USING gin (full_name gin_trgm_ops);

-- ============================================================================
-- 3. SEA SERVICE
-- ============================================================================
CREATE TABLE sea_service (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id      UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    vessel_name     VARCHAR(200) NOT NULL,
    vessel_type     VARCHAR(50),
    dwt             VARCHAR(50),
    flag_state      VARCHAR(100),
    imo_number      VARCHAR(20),
    rank_held       VARCHAR(100) NOT NULL,
    company_name    VARCHAR(200),
    start_date      DATE NOT NULL,
    end_date        DATE,
    duties          TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_ss_profile ON sea_service (profile_id);

-- ============================================================================
-- 4. CERTIFICATES
-- ============================================================================
CREATE TABLE certificates (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id          UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    cert_type           VARCHAR(30) NOT NULL,
    cert_name           VARCHAR(300) NOT NULL,
    cert_number         VARCHAR(100),
    issuing_authority   VARCHAR(200),
    issue_date          DATE,
    expiry_date         DATE,
    status              VARCHAR(25) NOT NULL DEFAULT 'PENDING_VERIFICATION',
    verified_status     VARCHAR(20) NOT NULL DEFAULT 'UNVERIFIED',
    document_url        VARCHAR(500),
    verification_notes  TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_cert_profile ON certificates (profile_id);

-- ============================================================================
-- 5. SEAFARER SKILLS
-- ============================================================================
CREATE TABLE seafarer_skills (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id          UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    skill_name          VARCHAR(100) NOT NULL,
    endorsement_count   INT NOT NULL DEFAULT 0,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (profile_id, skill_name)
);

-- ============================================================================
-- 6. EDUCATION
-- ============================================================================
CREATE TABLE education (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id          UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    institution_name    VARCHAR(300) NOT NULL,
    degree              VARCHAR(200),
    field_of_study      VARCHAR(200),
    start_year          INT,
    end_year            INT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 7. COMPANIES
-- ============================================================================
CREATE TABLE companies (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id                 UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    company_name            VARCHAR(300) NOT NULL,
    company_type            VARCHAR(20) NOT NULL,
    country                 VARCHAR(100),
    city                    VARCHAR(200),
    logo_url                VARCHAR(500),
    description             TEXT,
    website                 VARCHAR(100),
    dmw_license_number      VARCHAR(50),
    dmw_license_verified    BOOLEAN NOT NULL DEFAULT FALSE,
    principals_served       INT,
    fleet_size              INT,
    verified_status         VARCHAR(20) NOT NULL DEFAULT 'UNVERIFIED',
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 8. VESSELS
-- ============================================================================
CREATE TABLE vessels (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    company_id      UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    vessel_name     VARCHAR(200) NOT NULL,
    vessel_type     VARCHAR(50),
    dwt             VARCHAR(50),
    flag_state      VARCHAR(100),
    imo_number      VARCHAR(20) UNIQUE,
    year_built      INT,
    crew_capacity   INT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 9. SHORTLISTS
-- ============================================================================
CREATE TABLE shortlists (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_id        UUID NOT NULL REFERENCES users(id),
    title           VARCHAR(300) NOT NULL,
    vessel_context  VARCHAR(500),
    agent_id        UUID,
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_sl_owner ON shortlists (owner_id);

-- ============================================================================
-- 10. SHORTLIST CANDIDATES
-- ============================================================================
CREATE TABLE shortlist_candidates (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shortlist_id    UUID NOT NULL REFERENCES shortlists(id) ON DELETE CASCADE,
    profile_id      UUID NOT NULL REFERENCES seafarer_profiles(id),
    ai_score        NUMERIC(5,2),
    status          VARCHAR(20) NOT NULL DEFAULT 'SHORTLISTED',
    added_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    notes           TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (shortlist_id, profile_id)
);

-- ============================================================================
-- 11. AI SCORES
-- ============================================================================
CREATE TABLE ai_scores (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id              UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    search_criteria_hash    VARCHAR(64) NOT NULL,
    score                   NUMERIC(5,2) NOT NULL CHECK (score BETWEEN 0 AND 100),
    criteria_breakdown      JSONB,
    computed_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at              TIMESTAMPTZ,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (profile_id, search_criteria_hash)
);

-- ============================================================================
-- 12. THREADS & MESSAGES
-- ============================================================================
CREATE TABLE threads (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shortlist_id    UUID REFERENCES shortlists(id),
    last_message_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE thread_participants (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    thread_id       UUID NOT NULL REFERENCES threads(id) ON DELETE CASCADE,
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    joined_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_read_at    TIMESTAMPTZ,
    is_muted        BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (thread_id, user_id)
);

CREATE TABLE messages (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    thread_id       UUID NOT NULL REFERENCES threads(id) ON DELETE CASCADE,
    sender_id       UUID NOT NULL REFERENCES users(id),
    body            TEXT NOT NULL,
    sent_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    edited_at       TIMESTAMPTZ,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_messages_thread ON messages (thread_id, sent_at DESC);

-- ============================================================================
-- 13. NOTIFICATIONS
-- ============================================================================
CREATE TABLE notifications (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type            VARCHAR(30) NOT NULL,
    title           VARCHAR(200) NOT NULL,
    body            TEXT,
    action_url      VARCHAR(500),
    reference_id    UUID,
    is_read         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_notif_user ON notifications (user_id, created_at DESC);

-- ============================================================================
-- 14. PROFILE VIEWS
-- ============================================================================
CREATE TABLE profile_views (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id      UUID NOT NULL REFERENCES seafarer_profiles(id) ON DELETE CASCADE,
    viewer_id       UUID NOT NULL REFERENCES users(id),
    viewed_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_pv_profile ON profile_views (profile_id);

-- ============================================================================
-- 15. POSTS (FEED)
-- ============================================================================
CREATE TABLE posts (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    author_id       UUID NOT NULL REFERENCES users(id),
    post_type       VARCHAR(20) NOT NULL DEFAULT 'TEXT',
    content         TEXT NOT NULL,
    image_url       VARCHAR(500),
    likes_count     INT NOT NULL DEFAULT 0,
    comments_count  INT NOT NULL DEFAULT 0,
    shares_count    INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_posts_author ON posts (author_id);

-- ============================================================================
-- 16. CONSENTS
-- ============================================================================
CREATE TABLE consents (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    consent_type    VARCHAR(100) NOT NULL,
    consent_version VARCHAR(20) NOT NULL,
    status          VARCHAR(15) NOT NULL DEFAULT 'GIVEN',
    given_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    withdrawn_at    TIMESTAMPTZ,
    ip_address      VARCHAR(45),
    user_agent      VARCHAR(500),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 17. AUDIT LOG
-- ============================================================================
CREATE TABLE audit_log (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID,
    action          VARCHAR(50) NOT NULL,
    entity_type     VARCHAR(50) NOT NULL,
    entity_id       UUID,
    details         JSONB,
    ip_address      VARCHAR(45),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_audit_user ON audit_log (user_id);
CREATE INDEX idx_audit_entity ON audit_log (entity_type, entity_id);
