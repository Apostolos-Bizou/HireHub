import hashlib
import json
from typing import Optional
from datetime import date
from ..core.config import settings

class MatchingEngine:
    """
    Weighted multi-factor scoring engine.
    Weights from tracker spec: rank 25%, vessel 20%, experience 15%,
    certs 15%, availability 10%, verified 10%, recency 5%.
    """

    def compute_score(self, candidate: dict, criteria: dict) -> tuple[float, dict]:
        breakdown = {}

        # Rank match (25%)
        breakdown["rank"] = 100.0 if self._match_rank(candidate, criteria) else 0.0

        # Vessel type match (20%)
        breakdown["vessel"] = 100.0 if self._match_vessel(candidate, criteria) else 0.0

        # Experience (15%)
        breakdown["experience"] = self._score_experience(candidate, criteria)

        # Certificates (15%)
        breakdown["certificates"] = self._score_certs(candidate, criteria)

        # Availability (10%)
        breakdown["availability"] = self._score_availability(candidate, criteria)

        # Verified status (10%)
        breakdown["verified"] = 100.0 if candidate.get("verified_status") == "VERIFIED" else 0.0

        # Recency / activity (5%)
        breakdown["recency"] = self._score_recency(candidate)

        # Weighted total
        total = (
            breakdown["rank"] * settings.weight_rank +
            breakdown["vessel"] * settings.weight_vessel +
            breakdown["experience"] * settings.weight_experience +
            breakdown["certificates"] * settings.weight_certs +
            breakdown["availability"] * settings.weight_availability +
            breakdown["verified"] * settings.weight_verified +
            breakdown["recency"] * settings.weight_recency
        )

        return round(total, 2), {k: round(v, 2) for k, v in breakdown.items()}

    def criteria_hash(self, criteria: dict) -> str:
        normalized = json.dumps(criteria, sort_keys=True, default=str)
        return hashlib.sha256(normalized.encode()).hexdigest()[:16]

    def _match_rank(self, candidate: dict, criteria: dict) -> bool:
        if not criteria.get("rank"):
            return True
        return (candidate.get("current_rank", "").lower() == criteria["rank"].lower())

    def _match_vessel(self, candidate: dict, criteria: dict) -> bool:
        if not criteria.get("vessel_type"):
            return True
        return (candidate.get("preferred_vessel_type", "").upper() == criteria["vessel_type"].upper())

    def _score_experience(self, candidate: dict, criteria: dict) -> float:
        years = candidate.get("years_of_experience", 0) or 0
        min_req = criteria.get("min_experience", 0) or 0
        if min_req == 0:
            return min(years * 10, 100)
        if years >= min_req:
            return min(100, 50 + (years - min_req) * 10)
        return max(0, (years / min_req) * 50)

    def _score_certs(self, candidate: dict, criteria: dict) -> float:
        required = criteria.get("required_certs", [])
        if not required:
            return 80.0
        held = {c.get("cert_type", "").upper() for c in candidate.get("certificates", [])}
        matched = sum(1 for r in required if r.upper() in held)
        return (matched / len(required)) * 100

    def _score_availability(self, candidate: dict, criteria: dict) -> float:
        avail = candidate.get("available_from")
        deadline = criteria.get("available_before")
        if not deadline or not avail:
            return 50.0
        if isinstance(avail, str):
            avail = date.fromisoformat(avail)
        if isinstance(deadline, str):
            deadline = date.fromisoformat(deadline)
        if avail <= deadline:
            return 100.0
        days_late = (avail - deadline).days
        return max(0, 100 - days_late * 2)

    def _score_recency(self, candidate: dict) -> float:
        # Placeholder: profile completeness as proxy
        completeness = candidate.get("profile_completeness", 50) or 50
        return min(completeness, 100)

matching_engine = MatchingEngine()
