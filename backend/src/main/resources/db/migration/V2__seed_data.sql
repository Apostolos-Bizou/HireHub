-- HireHub Seed Data v1
-- Flyway Migration: V2__seed_data.sql
-- Realistic demo data for all user personas

-- ============================================================================
-- Password hash = BCrypt("Demo@2026") with strength 12
-- ============================================================================
-- $2a$12$LJ3m4ys3Hz.pทDkNzVsXaOE5GOvQmJMEz6bKj5qXgT2sEPdCpFOm6
-- Using a pre-computed hash for demo purposes:
\set pwd '''$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'''

-- ============================================================================
-- 1. USERS
-- ============================================================================
INSERT INTO users (id, email, password_hash, role, account_status, email_verified, created_at) VALUES
-- Seafarers
('a0000001-0000-0000-0000-000000000001', 'juan.delacruz@email.com',      '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SEAFARER', 'ACTIVE', true, NOW() - INTERVAL '60 days'),
('a0000001-0000-0000-0000-000000000002', 'ruslan.goncharov@email.com',    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SEAFARER', 'ACTIVE', true, NOW() - INTERVAL '45 days'),
('a0000001-0000-0000-0000-000000000003', 'andrei.marinescu@email.com',    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SEAFARER', 'ACTIVE', true, NOW() - INTERVAL '30 days'),
('a0000001-0000-0000-0000-000000000004', 'paolo.castillo@email.com',      '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SEAFARER', 'ACTIVE', true, NOW() - INTERVAL '20 days'),
('a0000001-0000-0000-0000-000000000005', 'lazar.stoyanov@email.com',      '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SEAFARER', 'ACTIVE', true, NOW() - INTERVAL '15 days'),
-- Shipowners
('b0000001-0000-0000-0000-000000000001', 'nikos.k@tsakos.gr',            '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SHIPOWNER', 'ACTIVE', true, NOW() - INTERVAL '90 days'),
('b0000001-0000-0000-0000-000000000002', 'ops@dianashipping.com',         '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SHIPOWNER', 'ACTIVE', true, NOW() - INTERVAL '80 days'),
('b0000001-0000-0000-0000-000000000003', 'fleet@capitalship.gr',          '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'SHIPOWNER', 'ACTIVE', true, NOW() - INTERVAL '70 days'),
-- Manning Agents
('c0000001-0000-0000-0000-000000000001', 'ops@magsaysamaritime.com',      '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'MANNING_AGENT', 'ACTIVE', true, NOW() - INTERVAL '85 days'),
('c0000001-0000-0000-0000-000000000002', 'crew@oloship.com',              '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'MANNING_AGENT', 'ACTIVE', true, NOW() - INTERVAL '75 days'),
-- Admin
('d0000001-0000-0000-0000-000000000001', 'admin@hirehub.io',              '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', 'ACTIVE', true, NOW() - INTERVAL '120 days');

-- ============================================================================
-- 2. SEAFARER PROFILES
-- ============================================================================
INSERT INTO seafarer_profiles (id, user_id, full_name, nationality, date_of_birth, gender, current_rank, preferred_vessel_type, years_of_experience, bio, phone_number, passport_number, seamans_book_number, verified_status, profile_completeness, available_from, desired_rank, contract_preference, salary_expectation_min, salary_expectation_max, salary_currency, created_at) VALUES
('p0000001-0000-0000-0000-000000000001', 'a0000001-0000-0000-0000-000000000001', 'Juan Dela Cruz', 'Filipino', '1997-05-14', 'Male', '3rd Officer', 'OIL_CHEMICAL_TANKER', 4, 'Experienced 3rd Officer with strong tanker background. STCW compliant, ECDIS certified. Looking for long-term career with reputable fleet.', '+639171234567', 'P1234567A', 'BMN-2020-12345', 'VERIFIED', 92, CURRENT_DATE + INTERVAL '15 days', '2nd Officer', '6+2', 1800, 2200, 'USD', NOW() - INTERVAL '60 days'),
('p0000001-0000-0000-0000-000000000002', 'a0000001-0000-0000-0000-000000000002', 'Ruslan Goncharov', 'Ukrainian', '1993-11-22', 'Male', '2nd Officer', 'BULK_CARRIER', 6, 'Dedicated deck officer with extensive bulk carrier experience. Strong English skills, ISM/ISPS auditor.', '+380501234567', 'UA987654B', 'UKR-SB-67890', 'VERIFIED', 88, CURRENT_DATE + INTERVAL '30 days', 'Chief Officer', '4+2', 2500, 3200, 'USD', NOW() - INTERVAL '45 days'),
('p0000001-0000-0000-0000-000000000003', 'a0000001-0000-0000-0000-000000000003', 'Andrei Marinescu', 'Romanian', '1990-03-08', 'Male', 'Chief Officer', 'CONTAINER', 8, 'Seasoned Chief Officer with container and general cargo experience. Vetting inspection ready. Strong leadership skills.', '+40721234567', 'RO456789C', 'ROM-SB-11111', 'VERIFIED', 95, CURRENT_DATE + INTERVAL '5 days', 'Master', '6+3', 4000, 5000, 'USD', NOW() - INTERVAL '30 days'),
('p0000001-0000-0000-0000-000000000004', 'a0000001-0000-0000-0000-000000000004', 'Paolo Castillo', 'Filipino', '1998-08-30', 'Male', '3rd Engineer', 'LPG_CARRIER', 3, 'Young and motivated 3rd Engineer. LPG/LNG familiarization complete. MARPOL compliant. Eager to grow.', '+639281234567', 'P2345678B', 'BMN-2021-54321', 'PENDING', 75, CURRENT_DATE + INTERVAL '45 days', '2nd Engineer', '6+2', 1600, 2000, 'USD', NOW() - INTERVAL '20 days'),
('p0000001-0000-0000-0000-000000000005', 'a0000001-0000-0000-0000-000000000005', 'Lazar Stoyanov', 'Bulgarian', '1995-01-19', 'Male', 'AB', 'GENERAL_CARGO', 5, 'Reliable AB with general cargo and bulk experience. Certified in crane operations and rigging.', '+359881234567', 'BG654321D', 'BGR-SB-22222', 'UNVERIFIED', 60, CURRENT_DATE + INTERVAL '60 days', 'Bosun', '4+2', 1200, 1500, 'USD', NOW() - INTERVAL '15 days');

-- ============================================================================
-- 3. COMPANIES
-- ============================================================================
INSERT INTO companies (id, user_id, company_name, company_type, country, city, description, website, dmw_license_number, dmw_license_verified, fleet_size, verified_status, created_at) VALUES
-- Shipowners (Greek)
('co000001-0000-0000-0000-000000000001', 'b0000001-0000-0000-0000-000000000001', 'Tsakos Energy Navigation', 'SHIPOWNER', 'Greece', 'Athens', 'Leading international tanker company operating a diversified fleet of modern crude, product, and LNG carriers.', 'https://tenn.gr', NULL, false, 72, 'VERIFIED', NOW() - INTERVAL '90 days'),
('co000001-0000-0000-0000-000000000002', 'b0000001-0000-0000-0000-000000000002', 'Diana Shipping', 'SHIPOWNER', 'Greece', 'Athens', 'Global provider of shipping transportation services through ownership and operation of dry bulk vessels.', 'https://dianashipping.com', NULL, false, 36, 'VERIFIED', NOW() - INTERVAL '80 days'),
('co000001-0000-0000-0000-000000000003', 'b0000001-0000-0000-0000-000000000003', 'Capital Ship Management', 'SHIPOWNER', 'Greece', 'Piraeus', 'Ship management company operating a diverse fleet of tankers, bulk carriers, and container vessels.', 'https://capitalship.gr', NULL, false, 85, 'VERIFIED', NOW() - INTERVAL '70 days'),
-- Manning Agents (Filipino)
('co000001-0000-0000-0000-000000000004', 'c0000001-0000-0000-0000-000000000001', 'Magsaysay Maritime Corporation', 'MANNING_AGENT', 'Philippines', 'Manila', 'One of the largest and most reputable manning agencies in the Philippines, deploying thousands of Filipino seafarers annually.', 'https://magsaysay.com.ph', 'DMW-MAN-2024-0001', true, NULL, 'VERIFIED', NOW() - INTERVAL '85 days'),
('co000001-0000-0000-0000-000000000005', 'c0000001-0000-0000-0000-000000000002', 'OSM Maritime Group Philippines', 'MANNING_AGENT', 'Philippines', 'Manila', 'Part of the global OSM Maritime Group, providing comprehensive crew management and recruitment solutions.', 'https://osm.no', 'DMW-MAN-2024-0042', true, NULL, 'VERIFIED', NOW() - INTERVAL '75 days');

-- ============================================================================
-- 4. VESSELS
-- ============================================================================
INSERT INTO vessels (id, company_id, vessel_name, vessel_type, dwt, flag_state, imo_number, year_built, crew_capacity, created_at) VALUES
('v0000001-0000-0000-0000-000000000001', 'co000001-0000-0000-0000-000000000001', 'MT Pacific Voyager', 'OIL_TANKER', '115000', 'Greece', '9876543', 2018, 25, NOW()),
('v0000001-0000-0000-0000-000000000002', 'co000001-0000-0000-0000-000000000001', 'MT Aegean Spirit', 'CHEMICAL_TANKER', '47000', 'Marshall Islands', '9876544', 2020, 22, NOW()),
('v0000001-0000-0000-0000-000000000003', 'co000001-0000-0000-0000-000000000002', 'MV Diana Fortuna', 'BULK_CARRIER', '82000', 'Marshall Islands', '9876545', 2015, 20, NOW()),
('v0000001-0000-0000-0000-000000000004', 'co000001-0000-0000-0000-000000000002', 'MV Diana Hellas', 'BULK_CARRIER', '76000', 'Bahamas', '9876546', 2012, 20, NOW()),
('v0000001-0000-0000-0000-000000000005', 'co000001-0000-0000-0000-000000000003', 'MSC Capital Star', 'CONTAINER', '65000', 'Liberia', '9876547', 2021, 24, NOW()),
('v0000001-0000-0000-0000-000000000006', 'co000001-0000-0000-0000-000000000003', 'MT Capital Sun', 'LPG_CARRIER', '54000', 'Malta', '9876548', 2019, 22, NOW());

-- ============================================================================
-- 5. SEA SERVICE RECORDS
-- ============================================================================
INSERT INTO sea_service (id, profile_id, vessel_name, vessel_type, dwt, flag_state, rank_held, company, start_date, end_date, created_at) VALUES
-- Juan Dela Cruz
('ss000001-0000-0000-0000-000000000001', 'p0000001-0000-0000-0000-000000000001', 'MT Nordic Breeze', 'OIL_CHEMICAL_TANKER', '47000', 'Marshall Islands', '3rd Officer', 'Nordic Tankers', '2023-06-01', '2024-02-28', NOW()),
('ss000001-0000-0000-0000-000000000002', 'p0000001-0000-0000-0000-000000000001', 'MT Eastern Light', 'OIL_TANKER', '105000', 'Liberia', '3rd Officer', 'Eastern Pacific Shipping', '2022-03-15', '2023-01-30', NOW()),
('ss000001-0000-0000-0000-000000000003', 'p0000001-0000-0000-0000-000000000001', 'MT Pacific Star', 'CHEMICAL_TANKER', '39000', 'Singapore', 'Deck Cadet', 'Pacific Basin Shipping', '2021-01-10', '2021-12-20', NOW()),
-- Ruslan Goncharov
('ss000001-0000-0000-0000-000000000004', 'p0000001-0000-0000-0000-000000000002', 'MV Black Sea Trader', 'BULK_CARRIER', '82000', 'Panama', '2nd Officer', 'Oldendorff Carriers', '2023-04-01', '2024-01-15', NOW()),
('ss000001-0000-0000-0000-000000000005', 'p0000001-0000-0000-0000-000000000002', 'MV Global Fortune', 'BULK_CARRIER', '76000', 'Hong Kong', '3rd Officer', 'Golden Ocean Group', '2021-09-01', '2022-08-30', NOW()),
-- Andrei Marinescu
('ss000001-0000-0000-0000-000000000006', 'p0000001-0000-0000-0000-000000000003', 'MSC Adriatica', 'CONTAINER', '68000', 'Panama', 'Chief Officer', 'MSC Mediterranean Shipping', '2023-01-10', '2024-03-20', NOW()),
('ss000001-0000-0000-0000-000000000007', 'p0000001-0000-0000-0000-000000000003', 'CMA CGM Loire', 'CONTAINER', '54000', 'France', '2nd Officer', 'CMA CGM Group', '2020-05-01', '2022-04-30', NOW());

-- ============================================================================
-- 6. CERTIFICATES
-- ============================================================================
INSERT INTO certificates (id, profile_id, cert_type, cert_name, issuing_authority, issue_date, expiry_date, status, verified_status, document_url, created_at) VALUES
-- Juan Dela Cruz
('ct000001-0000-0000-0000-000000000001', 'p0000001-0000-0000-0000-000000000001', 'COC', 'Certificate of Competency - OIC-NW', 'MARINA Philippines', '2021-03-15', '2026-03-14', 'VALID', 'VERIFIED', NULL, NOW()),
('ct000001-0000-0000-0000-000000000002', 'p0000001-0000-0000-0000-000000000001', 'STCW', 'STCW Basic Safety Training', 'MARINA Philippines', '2020-06-01', '2025-05-31', 'VALID', 'VERIFIED', NULL, NOW()),
('ct000001-0000-0000-0000-000000000003', 'p0000001-0000-0000-0000-000000000001', 'TANKER_FAMILIARIZATION', 'Oil/Chemical Tanker Familiarization', 'MARINA Philippines', '2021-08-10', '2026-08-09', 'VALID', 'VERIFIED', NULL, NOW()),
('ct000001-0000-0000-0000-000000000004', 'p0000001-0000-0000-0000-000000000001', 'GMDSS', 'GMDSS General Operator Certificate', 'NTC Philippines', '2021-01-20', '2026-01-19', 'VALID', 'VERIFIED', NULL, NOW()),
-- Ruslan Goncharov
('ct000001-0000-0000-0000-000000000005', 'p0000001-0000-0000-0000-000000000002', 'COC', 'Certificate of Competency - OIC-NW', 'Ukrainian Maritime Administration', '2019-05-10', '2024-05-09', 'EXPIRING', 'VERIFIED', NULL, NOW()),
('ct000001-0000-0000-0000-000000000006', 'p0000001-0000-0000-0000-000000000002', 'STCW', 'STCW Basic Safety Training', 'Ukrainian Maritime Administration', '2018-03-01', '2028-02-28', 'VALID', 'VERIFIED', NULL, NOW()),
-- Andrei Marinescu
('ct000001-0000-0000-0000-000000000007', 'p0000001-0000-0000-0000-000000000003', 'COC', 'Certificate of Competency - Management Level', 'Romanian Naval Authority', '2020-09-01', '2025-08-31', 'VALID', 'VERIFIED', NULL, NOW()),
('ct000001-0000-0000-0000-000000000008', 'p0000001-0000-0000-0000-000000000003', 'STCW', 'STCW Advanced Firefighting', 'Romanian Naval Authority', '2019-11-15', '2024-11-14', 'EXPIRING', 'VERIFIED', NULL, NOW()),
-- Paolo Castillo
('ct000001-0000-0000-0000-000000000009', 'p0000001-0000-0000-0000-000000000004', 'COC', 'Certificate of Competency - OICEW', 'MARINA Philippines', '2022-04-01', '2027-03-31', 'VALID', 'UNVERIFIED', NULL, NOW()),
('ct000001-0000-0000-0000-000000000010', 'p0000001-0000-0000-0000-000000000004', 'STCW', 'STCW Basic Safety Training', 'MARINA Philippines', '2021-09-01', '2026-08-31', 'VALID', 'UNVERIFIED', NULL, NOW());

-- ============================================================================
-- 7. SEAFARER SKILLS
-- ============================================================================
INSERT INTO seafarer_skills (id, profile_id, skill_name, endorsement_count, created_at) VALUES
('sk000001-0000-0000-0000-000000000001', 'p0000001-0000-0000-0000-000000000001', 'ECDIS', 12, NOW()),
('sk000001-0000-0000-0000-000000000002', 'p0000001-0000-0000-0000-000000000001', 'GMDSS', 8, NOW()),
('sk000001-0000-0000-0000-000000000003', 'p0000001-0000-0000-0000-000000000001', 'Cargo Operations', 15, NOW()),
('sk000001-0000-0000-0000-000000000004', 'p0000001-0000-0000-0000-000000000001', 'Bridge Watch', 20, NOW()),
('sk000001-0000-0000-0000-000000000005', 'p0000001-0000-0000-0000-000000000001', 'Tanker Safety', 10, NOW()),
('sk000001-0000-0000-0000-000000000006', 'p0000001-0000-0000-0000-000000000002', 'Cargo Planning', 14, NOW()),
('sk000001-0000-0000-0000-000000000007', 'p0000001-0000-0000-0000-000000000002', 'ISM/ISPS', 11, NOW()),
('sk000001-0000-0000-0000-000000000008', 'p0000001-0000-0000-0000-000000000002', 'ECDIS', 9, NOW()),
('sk000001-0000-0000-0000-000000000009', 'p0000001-0000-0000-0000-000000000003', 'Vetting Inspections', 18, NOW()),
('sk000001-0000-0000-0000-000000000010', 'p0000001-0000-0000-0000-000000000003', 'Port State Control', 16, NOW()),
('sk000001-0000-0000-0000-000000000011', 'p0000001-0000-0000-0000-000000000003', 'Crew Management', 22, NOW());

-- ============================================================================
-- 8. EDUCATION
-- ============================================================================
INSERT INTO education (id, profile_id, institution_name, degree, field_of_study, start_year, end_year, created_at) VALUES
('ed000001-0000-0000-0000-000000000001', 'p0000001-0000-0000-0000-000000000001', 'Philippine Merchant Marine Academy', 'BS Marine Transportation', 'Nautical Science', 2016, 2020, NOW()),
('ed000001-0000-0000-0000-000000000002', 'p0000001-0000-0000-0000-000000000002', 'Odessa National Maritime University', 'Navigation Officer Diploma', 'Maritime Navigation', 2012, 2017, NOW()),
('ed000001-0000-0000-0000-000000000003', 'p0000001-0000-0000-0000-000000000003', 'Constanta Maritime University', 'BS Maritime Engineering', 'Deck Operations', 2008, 2012, NOW());

-- ============================================================================
-- 9. SHORTLISTS
-- ============================================================================
INSERT INTO shortlists (id, owner_id, agent_id, title, vessel_context, status, created_at) VALUES
('sl000001-0000-0000-0000-000000000001', 'b0000001-0000-0000-0000-000000000001', 'c0000001-0000-0000-0000-000000000001', '3rd Officer — Oil/Chemical Tanker', 'MT Pacific Voyager crew change Q2 2026', 'ACTIVE', NOW() - INTERVAL '5 days'),
('sl000001-0000-0000-0000-000000000002', 'b0000001-0000-0000-0000-000000000002', 'c0000001-0000-0000-0000-000000000002', '2nd Officer — Bulk Carrier', 'MV Diana Fortuna replacement', 'ACTIVE', NOW() - INTERVAL '3 days');

-- ============================================================================
-- 10. SHORTLIST CANDIDATES
-- ============================================================================
INSERT INTO shortlist_candidates (id, shortlist_id, profile_id, ai_score, status, added_at, created_at) VALUES
('sc000001-0000-0000-0000-000000000001', 'sl000001-0000-0000-0000-000000000001', 'p0000001-0000-0000-0000-000000000001', 92.5, 'SENT_TO_AGENT', NOW() - INTERVAL '4 days', NOW()),
('sc000001-0000-0000-0000-000000000002', 'sl000001-0000-0000-0000-000000000001', 'p0000001-0000-0000-0000-000000000004', 76.0, 'SHORTLISTED', NOW() - INTERVAL '3 days', NOW()),
('sc000001-0000-0000-0000-000000000003', 'sl000001-0000-0000-0000-000000000002', 'p0000001-0000-0000-0000-000000000002', 87.3, 'UNDER_REVIEW', NOW() - INTERVAL '2 days', NOW());

-- ============================================================================
-- 11. THREADS & MESSAGES
-- ============================================================================
INSERT INTO threads (id, shortlist_id, last_message_at, created_at) VALUES
('th000001-0000-0000-0000-000000000001', 'sl000001-0000-0000-0000-000000000001', NOW() - INTERVAL '1 day', NOW() - INTERVAL '4 days');

INSERT INTO thread_participants (id, thread_id, user_id, joined_at, created_at) VALUES
('tp000001-0000-0000-0000-000000000001', 'th000001-0000-0000-0000-000000000001', 'b0000001-0000-0000-0000-000000000001', NOW() - INTERVAL '4 days', NOW()),
('tp000001-0000-0000-0000-000000000002', 'th000001-0000-0000-0000-000000000001', 'a0000001-0000-0000-0000-000000000001', NOW() - INTERVAL '4 days', NOW());

INSERT INTO messages (id, thread_id, sender_id, body, sent_at, created_at) VALUES
('mg000001-0000-0000-0000-000000000001', 'th000001-0000-0000-0000-000000000001', 'b0000001-0000-0000-0000-000000000001',
 'Good morning Juan. I reviewed your profile on HireHub and I am impressed with your tanker experience. We have a 3rd Officer position on MT Pacific Voyager. I will be forwarding your profile to our manning agent for processing.',
 NOW() - INTERVAL '3 days', NOW()),
('mg000001-0000-0000-0000-000000000002', 'th000001-0000-0000-0000-000000000001', 'a0000001-0000-0000-0000-000000000001',
 'Good morning sir. Thank you for considering my profile. I am very interested in the MT Pacific Voyager position. I am available from next month and all my certificates are current. I look forward to hearing from your manning agent.',
 NOW() - INTERVAL '2 days', NOW());

-- ============================================================================
-- 12. NOTIFICATIONS
-- ============================================================================
INSERT INTO notifications (id, user_id, type, title, body, is_read, created_at) VALUES
('nt000001-0000-0000-0000-000000000001', 'a0000001-0000-0000-0000-000000000001', 'SHORTLIST_ADDED', 'Added to Shortlist', 'Tsakos Energy Navigation added you to a shortlist for 3rd Officer — Oil/Chemical Tanker.', false, NOW() - INTERVAL '4 days'),
('nt000001-0000-0000-0000-000000000002', 'a0000001-0000-0000-0000-000000000001', 'MESSAGE_RECEIVED', 'New Message', 'You received a message from Nikos K. at Tsakos Energy Navigation.', false, NOW() - INTERVAL '3 days'),
('nt000001-0000-0000-0000-000000000003', 'a0000001-0000-0000-0000-000000000001', 'PROFILE_VIEW', 'Profile Viewed', 'Your profile was viewed by Diana Shipping.', true, NOW() - INTERVAL '7 days'),
('nt000001-0000-0000-0000-000000000004', 'a0000001-0000-0000-0000-000000000002', 'PROFILE_VIEW', 'Profile Viewed', 'Your profile was viewed by Capital Ship Management.', false, NOW() - INTERVAL '2 days');

-- ============================================================================
-- 13. POSTS (Feed)
-- ============================================================================
INSERT INTO posts (id, author_id, post_type, content, created_at) VALUES
('po000001-0000-0000-0000-000000000001', 'a0000001-0000-0000-0000-000000000003', 'MILESTONE',
 'Promoted to Chief Officer! Proud to announce my promotion after 8 years of dedicated sea service. From cadet to C/O — the journey continues. Thank you to all my mentors and colleagues. #maritime #seafarerlife #chiefmate',
 NOW() - INTERVAL '10 days'),
('po000001-0000-0000-0000-000000000002', 'b0000001-0000-0000-0000-000000000001', 'NEWS',
 'Important update for all deck officers: new ECDIS software requirements coming into effect Q3 2026. Make sure your training certificates are up to date. Contact your flag state for specific requirements.',
 NOW() - INTERVAL '5 days'),
('po000001-0000-0000-0000-000000000003', 'c0000001-0000-0000-0000-000000000001', 'TEXT',
 'Our principals are expanding their LNG fleet and we are looking for experienced 2nd and Chief Engineers with LNG carrier experience. If you have the qualifications, make sure your HireHub profile is updated and verified!',
 NOW() - INTERVAL '2 days');

-- ============================================================================
-- 14. CONSENTS
-- ============================================================================
INSERT INTO consents (id, user_id, consent_type, consent_version, status, given_at, ip_address, created_at) VALUES
('cn000001-0000-0000-0000-000000000001', 'a0000001-0000-0000-0000-000000000001', 'DATA_PROCESSING', '1.0', 'GIVEN', NOW() - INTERVAL '60 days', '203.177.45.12', NOW()),
('cn000001-0000-0000-0000-000000000002', 'a0000001-0000-0000-0000-000000000002', 'DATA_PROCESSING', '1.0', 'GIVEN', NOW() - INTERVAL '45 days', '91.210.33.7', NOW()),
('cn000001-0000-0000-0000-000000000003', 'a0000001-0000-0000-0000-000000000003', 'DATA_PROCESSING', '1.0', 'GIVEN', NOW() - INTERVAL '30 days', '86.120.44.88', NOW());

-- ============================================================================
-- 15. PROFILE VIEWS
-- ============================================================================
INSERT INTO profile_views (id, profile_id, viewer_id, viewed_at, created_at) VALUES
('pv000001-0000-0000-0000-000000000001', 'p0000001-0000-0000-0000-000000000001', 'b0000001-0000-0000-0000-000000000001', NOW() - INTERVAL '5 days', NOW()),
('pv000001-0000-0000-0000-000000000002', 'p0000001-0000-0000-0000-000000000001', 'b0000001-0000-0000-0000-000000000002', NOW() - INTERVAL '7 days', NOW()),
('pv000001-0000-0000-0000-000000000003', 'p0000001-0000-0000-0000-000000000002', 'b0000001-0000-0000-0000-000000000003', NOW() - INTERVAL '2 days', NOW());
