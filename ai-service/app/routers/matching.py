from fastapi import APIRouter, Depends
from ..models.schemas import MatchRequest, MatchResponse, CandidateScore
from ..services.matching_engine import matching_engine
from ..core.security import verify_token

router = APIRouter()

# Demo candidate pool
DEMO_CANDIDATES = [
    {"id": "p1", "full_name": "Juan Dela Cruz", "current_rank": "3rd Officer", "preferred_vessel_type": "OIL_CHEMICAL_TANKER", "years_of_experience": 4, "nationality": "Filipino", "verified_status": "VERIFIED", "profile_completeness": 92, "available_from": "2026-06-15", "certificates": [{"cert_type": "COC"}, {"cert_type": "STCW"}, {"cert_type": "TANKER_FAMILIARIZATION"}]},
    {"id": "p2", "full_name": "Ruslan Goncharov", "current_rank": "3rd Officer", "preferred_vessel_type": "BULK_CARRIER", "years_of_experience": 6, "nationality": "Ukrainian", "verified_status": "VERIFIED", "profile_completeness": 88, "available_from": "2026-05-01", "certificates": [{"cert_type": "COC"}, {"cert_type": "STCW"}, {"cert_type": "GMDSS"}]},
    {"id": "p3", "full_name": "Andrei Marinescu", "current_rank": "Chief Officer", "preferred_vessel_type": "CONTAINER", "years_of_experience": 8, "nationality": "Romanian", "verified_status": "VERIFIED", "profile_completeness": 95, "available_from": "2026-07-01", "certificates": [{"cert_type": "COC"}, {"cert_type": "STCW"}]},
    {"id": "p4", "full_name": "Paolo Castillo", "current_rank": "3rd Engineer", "preferred_vessel_type": "LPG_CARRIER", "years_of_experience": 3, "nationality": "Filipino", "verified_status": "UNVERIFIED", "profile_completeness": 65, "available_from": "2026-06-01", "certificates": [{"cert_type": "COC"}]},
    {"id": "p5", "full_name": "Lazar Stoyanov", "current_rank": "AB", "preferred_vessel_type": "GENERAL_CARGO", "years_of_experience": 5, "nationality": "Bulgarian", "verified_status": "UNVERIFIED", "profile_completeness": 58, "available_from": "2026-05-15", "certificates": [{"cert_type": "STCW"}]},
]

@router.post("/score", response_model=MatchResponse)
def score_candidates(request: MatchRequest, token: dict = Depends(verify_token)):
    criteria = request.model_dump(exclude_none=True)
    c_hash = matching_engine.criteria_hash(criteria)

    scored = []
    for candidate in DEMO_CANDIDATES:
        score, breakdown = matching_engine.compute_score(candidate, criteria)
        scored.append(CandidateScore(
            profile_id=candidate["id"],
            score=score,
            breakdown=breakdown
        ))

    scored.sort(key=lambda x: x.score, reverse=True)

    return MatchResponse(criteria_hash=c_hash, candidates=scored, total=len(scored))

@router.post("/score-single")
def score_single(profile_id: str, request: MatchRequest, token: dict = Depends(verify_token)):
    criteria = request.model_dump(exclude_none=True)
    candidate = next((c for c in DEMO_CANDIDATES if c["id"] == profile_id), None)
    if not candidate:
        return {"error": "Profile not found"}
    score, breakdown = matching_engine.compute_score(candidate, criteria)
    return CandidateScore(profile_id=profile_id, score=score, breakdown=breakdown)
