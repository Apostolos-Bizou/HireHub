from app.services.cv_parser import cv_parser

SAMPLE_CV = """
Juan Dela Cruz
3rd Officer - Oil/Chemical Tanker
Filipino, 28 years old

Sea Service:
MT Pacific Voyager - 3rd Officer (2024-2025)
MT Golden Horizon - 3rd Officer (2023-2024)

Certificates:
COC - Certificate of Competency, MARINA
STCW Basic Safety Training
GMDSS General Operator Certificate
Tanker Familiarization

4 years of sea experience

Skills: ECDIS, GMDSS, cargo operations, bridge watch, MARPOL, English
"""

def test_extract_name():
    result = cv_parser.parse_text(SAMPLE_CV)
    assert result["full_name"] is not None
    assert "Juan" in result["full_name"]

def test_extract_rank():
    result = cv_parser.parse_text(SAMPLE_CV)
    assert result["rank"] is not None
    assert "officer" in result["rank"].lower()

def test_extract_nationality():
    result = cv_parser.parse_text(SAMPLE_CV)
    assert result["nationality"] == "Filipino"

def test_extract_experience():
    result = cv_parser.parse_text(SAMPLE_CV)
    assert result["years_experience"] == 4

def test_extract_certificates():
    result = cv_parser.parse_text(SAMPLE_CV)
    cert_types = [c["cert_type"] for c in result["certificates"]]
    assert "COC" in cert_types
    assert "STCW" in cert_types
    assert "GMDSS" in cert_types

def test_extract_skills():
    result = cv_parser.parse_text(SAMPLE_CV)
    assert "ECDIS" in result["skills"]
    assert "MARPOL" in result["skills"]

def test_extract_sea_service():
    result = cv_parser.parse_text(SAMPLE_CV)
    assert len(result["sea_service"]) >= 2

def test_empty_cv():
    result = cv_parser.parse_text("")
    assert result["full_name"] is None
    assert result["rank"] is None
