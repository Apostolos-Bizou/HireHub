-- V7__fix_company_ratings_id_type.sql
-- Fix: id column was UUID, needs to be BIGSERIAL
DROP TABLE IF EXISTS company_ratings CASCADE;

CREATE TABLE company_ratings (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    reviewer_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    salary_fairness     SMALLINT NOT NULL CHECK (salary_fairness BETWEEN 1 AND 5),
    crew_welfare        SMALLINT NOT NULL CHECK (crew_welfare BETWEEN 1 AND 5),
    safety_culture      SMALLINT NOT NULL CHECK (safety_culture BETWEEN 1 AND 5),
    career_growth       SMALLINT NOT NULL CHECK (career_growth BETWEEN 1 AND 5),
    management_quality  SMALLINT NOT NULL CHECK (management_quality BETWEEN 1 AND 5),
    review_text         VARCHAR(1000),
    anonymous           BOOLEAN NOT NULL DEFAULT FALSE,
    overall_score       NUMERIC(5,2) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_company_rating_per_user UNIQUE (company_id, reviewer_id)
);
CREATE INDEX IF NOT EXISTS idx_company_ratings_company_id ON company_ratings(company_id);