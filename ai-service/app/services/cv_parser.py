import re
from typing import Optional

class CVParser:
    """Parse maritime CV documents and extract structured data."""

    RANK_PATTERNS = [
        "master", "chief officer", "2nd officer", "3rd officer",
        "chief engineer", "2nd engineer", "3rd engineer",
        "bosun", "ab", "oiler", "cook", "cadet"
    ]

    def parse_text(self, text: str) -> dict:
        result = {
            "full_name": self._extract_name(text),
            "rank": self._extract_rank(text),
            "nationality": self._extract_nationality(text),
            "years_experience": self._extract_experience(text),
            "sea_service": self._extract_sea_service(text),
            "certificates": self._extract_certificates(text),
            "skills": self._extract_skills(text)
        }
        return result

    def _extract_name(self, text: str) -> Optional[str]:
        lines = text.strip().split("\n")[:5]
        for line in lines:
            if line.strip() and not any(k in line.lower() for k in ["cv", "resume", "curriculum"]):
                cleaned = re.sub(r'[^a-zA-Z\s]', '', line).strip()
                if cleaned and len(cleaned.split()) >= 2:
                    return cleaned
        return None

    def _extract_rank(self, text: str) -> Optional[str]:
        text_lower = text.lower()
        for rank in self.RANK_PATTERNS:
            if rank in text_lower:
                return rank.title()
        return None

    def _extract_nationality(self, text: str) -> Optional[str]:
        nationalities = ["filipino", "ukrainian", "romanian", "greek", "indian", "russian", "chinese", "indonesian", "bulgarian", "polish"]
        text_lower = text.lower()
        for nat in nationalities:
            if nat in text_lower:
                return nat.title()
        return None

    def _extract_experience(self, text: str) -> Optional[int]:
        match = re.search(r'(\d+)\s*(?:years?|yrs?)\s*(?:of\s*)?(?:sea\s*)?(?:service|experience)', text, re.I)
        if match:
            return int(match.group(1))
        return None

    def _extract_sea_service(self, text: str) -> list:
        entries = []
        vessel_pattern = re.compile(r'(M[TV]\s+[\w\s]+)', re.I)
        for match in vessel_pattern.finditer(text):
            entries.append({"vessel_name": match.group(1).strip()})
        return entries[:10]

    def _extract_certificates(self, text: str) -> list:
        cert_keywords = ["COC", "STCW", "GMDSS", "Tanker", "Medical", "Flag Endorsement"]
        found = []
        for kw in cert_keywords:
            if kw.lower() in text.lower():
                found.append({"cert_type": kw})
        return found

    def _extract_skills(self, text: str) -> list:
        skill_keywords = ["ECDIS", "GMDSS", "cargo operations", "bridge watch", "ISM", "ISPS", "MARPOL", "tanker safety", "English"]
        return [s for s in skill_keywords if s.lower() in text.lower()]

cv_parser = CVParser()
