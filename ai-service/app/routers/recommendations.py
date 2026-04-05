from fastapi import APIRouter, Depends
from ..models.schemas import CareerRecommendation
from ..core.security import verify_token

router = APIRouter()

CAREER_PATHS = {
    "Cadet": {"next": "3rd Officer", "timeline": "6-12 months", "reqs": ["Complete cadetship", "Pass COC exam"], "missing_certs": ["COC"], "salary": {"min": 1200, "max": 1800}},
    "3rd Officer": {"next": "2nd Officer", "timeline": "18-24 months", "reqs": ["18 months sea service as 3rd Officer", "Advanced STCW courses"], "missing_certs": ["Advanced Fire Fighting", "Medical First Aid"], "salary": {"min": 2500, "max": 3500}},
    "2nd Officer": {"next": "Chief Officer", "timeline": "24-36 months", "reqs": ["24 months sea service as 2nd Officer", "Chief Mate COC"], "missing_certs": ["Chief Mate COC"], "salary": {"min": 3500, "max": 5000}},
    "Chief Officer": {"next": "Master", "timeline": "36-48 months", "reqs": ["36 months as Chief Officer", "Master COC exam"], "missing_certs": ["Master COC"], "salary": {"min": 6000, "max": 9000}},
    "3rd Engineer": {"next": "2nd Engineer", "timeline": "18-24 months", "reqs": ["18 months sea service", "2nd Engineer COC"], "missing_certs": ["2nd Engineer COC"], "salary": {"min": 2500, "max": 3500}},
}

@router.get("/career-path/{current_rank}", response_model=CareerRecommendation)
def get_career_path(current_rank: str, token: dict = Depends(verify_token)):
    path = CAREER_PATHS.get(current_rank)
    if not path:
        return CareerRecommendation(current_rank=current_rank, next_rank="N/A", estimated_timeline="Consult career advisor")
    return CareerRecommendation(
        current_rank=current_rank,
        next_rank=path["next"],
        requirements=path["reqs"],
        estimated_timeline=path["timeline"],
        missing_certs=path["missing_certs"],
        salary_range=path["salary"]
    )

@router.get("/salary-benchmark/{rank}")
def salary_benchmark(rank: str, token: dict = Depends(verify_token)):
    benchmarks = {
        "3rd Officer": {"min": 1800, "max": 2800, "median": 2200, "currency": "USD"},
        "2nd Officer": {"min": 2500, "max": 3800, "median": 3000, "currency": "USD"},
        "Chief Officer": {"min": 3500, "max": 5500, "median": 4200, "currency": "USD"},
        "Master": {"min": 6000, "max": 12000, "median": 8500, "currency": "USD"},
        "3rd Engineer": {"min": 1800, "max": 2800, "median": 2200, "currency": "USD"},
        "Chief Engineer": {"min": 5000, "max": 10000, "median": 7000, "currency": "USD"},
    }
    return benchmarks.get(rank, {"min": 0, "max": 0, "median": 0, "currency": "USD", "note": "No data for this rank"})
