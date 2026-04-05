from app.services.matching_engine import matching_engine

def test_perfect_match_scores_high():
    candidate = {
        "current_rank": "3rd Officer",
        "preferred_vessel_type": "OIL_CHEMICAL_TANKER",
        "years_of_experience": 6,
        "verified_status": "VERIFIED",
        "profile_completeness": 95,
        "available_from": "2026-05-01",
        "certificates": [{"cert_type": "COC"}, {"cert_type": "STCW"}]
    }
    criteria = {
        "rank": "3rd Officer",
        "vessel_type": "OIL_CHEMICAL_TANKER",
        "min_experience": 4,
        "available_before": "2026-06-01",
        "required_certs": ["COC", "STCW"]
    }
    score, breakdown = matching_engine.compute_score(candidate, criteria)
    assert score >= 85
    assert breakdown["rank"] == 100.0
    assert breakdown["vessel"] == 100.0
    assert breakdown["verified"] == 100.0

def test_no_match_scores_low():
    candidate = {
        "current_rank": "AB",
        "preferred_vessel_type": "GENERAL_CARGO",
        "years_of_experience": 1,
        "verified_status": "UNVERIFIED",
        "profile_completeness": 30,
        "certificates": []
    }
    criteria = {
        "rank": "Master",
        "vessel_type": "LNG_CARRIER",
        "min_experience": 10,
        "required_certs": ["COC", "STCW", "IGF_CODE"]
    }
    score, breakdown = matching_engine.compute_score(candidate, criteria)
    assert score < 30
    assert breakdown["rank"] == 0.0

def test_criteria_hash_deterministic():
    c1 = {"rank": "Master", "vessel_type": "BULK"}
    c2 = {"vessel_type": "BULK", "rank": "Master"}
    assert matching_engine.criteria_hash(c1) == matching_engine.criteria_hash(c2)

def test_criteria_hash_different_for_different_criteria():
    c1 = {"rank": "Master"}
    c2 = {"rank": "3rd Officer"}
    assert matching_engine.criteria_hash(c1) != matching_engine.criteria_hash(c2)

def test_score_in_valid_range():
    candidate = {"current_rank": "AB", "years_of_experience": 3, "verified_status": "UNVERIFIED", "profile_completeness": 50, "certificates": []}
    score, _ = matching_engine.compute_score(candidate, {})
    assert 0 <= score <= 100
