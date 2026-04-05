def test_career_path_3rd_officer(client, auth_header):
    response = client.get("/ai/recommendations/career-path/3rd Officer", headers=auth_header)
    assert response.status_code == 200
    data = response.json()
    assert data["current_rank"] == "3rd Officer"
    assert data["next_rank"] == "2nd Officer"
    assert len(data["requirements"]) > 0

def test_career_path_unknown_rank(client, auth_header):
    response = client.get("/ai/recommendations/career-path/Unknown Rank", headers=auth_header)
    assert response.status_code == 200
    data = response.json()
    assert data["next_rank"] == "N/A"

def test_salary_benchmark(client, auth_header):
    response = client.get("/ai/recommendations/salary-benchmark/Master", headers=auth_header)
    assert response.status_code == 200
    data = response.json()
    assert data["min"] > 0
    assert data["max"] > data["min"]
    assert data["currency"] == "USD"

def test_bias_report(client, auth_header):
    response = client.get("/ai/audit/bias-report", headers=auth_header)
    assert response.status_code == 200
    data = response.json()
    assert data["compliant"] is True
    assert "Filipino" in data["by_nationality"]

def test_fairness_metrics(client, auth_header):
    response = client.get("/ai/audit/fairness-metrics", headers=auth_header)
    assert response.status_code == 200
    assert response.json()["overall_fairness"] == "PASS"
