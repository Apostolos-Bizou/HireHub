from pydantic import BaseModel, Field
from typing import Optional
from datetime import date

class MatchRequest(BaseModel):
    rank: Optional[str] = None
    vessel_type: Optional[str] = None
    min_experience: Optional[int] = None
    nationality: Optional[str] = None
    available_before: Optional[date] = None
    required_certs: list[str] = []
    verified_only: bool = False

class CandidateScore(BaseModel):
    profile_id: str
    score: float = Field(ge=0, le=100)
    breakdown: dict[str, float] = {}

class MatchResponse(BaseModel):
    criteria_hash: str
    candidates: list[CandidateScore]
    total: int

class VerificationRequest(BaseModel):
    certificate_id: str
    document_url: str

class VerificationResult(BaseModel):
    certificate_id: str
    is_authentic: bool
    confidence: float = Field(ge=0, le=1)
    extracted_fields: dict = {}
    anomalies: list[str] = []

class CVParseRequest(BaseModel):
    document_url: str
    profile_id: Optional[str] = None

class CVParseResult(BaseModel):
    full_name: Optional[str] = None
    rank: Optional[str] = None
    nationality: Optional[str] = None
    years_experience: Optional[int] = None
    sea_service: list[dict] = []
    certificates: list[dict] = []
    skills: list[str] = []

class CareerRecommendation(BaseModel):
    current_rank: str
    next_rank: str
    requirements: list[str] = []
    estimated_timeline: str = ""
    missing_certs: list[str] = []
    salary_range: dict = {}

class BiasReport(BaseModel):
    period: str
    total_scores: int
    by_nationality: dict[str, float] = {}
    by_gender: dict[str, float] = {}
    by_age_group: dict[str, float] = {}
    flags: list[str] = []
    compliant: bool = True
