def test_score_candidates_returns_ranked_list(client, auth_header):
    response = client.post("/ai/matching/score", json={
        "rank": "3rd Officer",
        "vessel_type": "OIL_CHEMICAL_TANKER",
        "min_experience": 2,
        "verified_only": False
    }, headers=auth_header)
    assert response.status_code == 200
    data = response.json()
    assert "candidates" in data
    assert "criteria_hash" in data
    assert len(data["candidates"]) > 0
    # Verify sorted descending by score
    scores = [c["score"] for c in data["candidates"]]
    assert scores == sorted(scores, reverse=True)

def test_score_candidates_includes_breakdown(client, auth_header):
    response = client.post("/ai/matching/score", json={
        "rank": "3rd Officer"
    }, headers=auth_header)
    data = response.json()
    candidate = data["candidates"][0]
    assert "breakdown" in candidate
    breakdown = candidate["breakdown"]
    assert "rank" in breakdown
    assert "vessel" in breakdown
    assert "experience" in breakdown
    assert "certificates" in breakdown
    assert "verified" in breakdown

def test_score_single_candidate(client, auth_header):
    response = client.post("/ai/matching/score-single?profile_id=p1", json={
        "rank": "3rd Officer",
        "vessel_type": "OIL_CHEMICAL_TANKER"
    }, headers=auth_header)
    assert response.status_code == 200
    data = response.json()
    assert data["profile_id"] == "p1"
    assert 0 <= data["score"] <= 100

def test_score_unknown_profile(client, auth_header):
    response = client.post("/ai/matching/score-single?profile_id=unknown", json={
        "rank": "Master"
    }, headers=auth_header)
    assert response.status_code == 200
    assert "error" in response.json()

def test_score_without_auth_returns_401(client):
    response = client.post("/ai/matching/score", json={"rank": "Master"})
    assert response.status_code in [401, 403]

def test_empty_criteria_returns_all(client, auth_header):
    response = client.post("/ai/matching/score", json={}, headers=auth_header)
    assert response.status_code == 200
    assert len(response.json()["candidates"]) == 5
