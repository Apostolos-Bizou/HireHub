import io
from typing import Optional

class VerificationService:
    """
    Certificate verification using OCR and anomaly detection.
    Uses Tesseract OCR + OpenCV for preprocessing.
    """

    def verify_certificate(self, image_bytes: bytes) -> dict:
        """Verify a certificate image and extract fields."""
        try:
            import cv2
            import numpy as np
            import pytesseract

            # Decode image
            nparr = np.frombuffer(image_bytes, np.uint8)
            img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

            if img is None:
                return {"is_authentic": False, "confidence": 0, "anomalies": ["Could not decode image"]}

            # Preprocessing: grayscale, denoise, threshold
            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
            denoised = cv2.fastNlMeansDenoising(gray)
            _, thresh = cv2.threshold(denoised, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)

            # OCR
            text = pytesseract.image_to_string(thresh)

            # Extract fields
            fields = self._extract_fields(text)

            # Anomaly detection
            anomalies = self._detect_anomalies(img, text, fields)

            confidence = max(0, 1.0 - len(anomalies) * 0.15)

            return {
                "is_authentic": confidence > 0.6,
                "confidence": round(confidence, 3),
                "extracted_fields": fields,
                "anomalies": anomalies
            }
        except ImportError:
            return {
                "is_authentic": True,
                "confidence": 0.5,
                "extracted_fields": {},
                "anomalies": ["OCR libraries not available - manual review required"]
            }

    def _extract_fields(self, text: str) -> dict:
        fields = {}
        lines = text.strip().split("\n")
        for line in lines:
            line_lower = line.lower().strip()
            if "name" in line_lower and ":" in line:
                fields["holder_name"] = line.split(":", 1)[1].strip()
            elif "certificate" in line_lower and "no" in line_lower and ":" in line:
                fields["certificate_number"] = line.split(":", 1)[1].strip()
            elif "date" in line_lower and "issue" in line_lower and ":" in line:
                fields["issue_date"] = line.split(":", 1)[1].strip()
            elif "expir" in line_lower and ":" in line:
                fields["expiry_date"] = line.split(":", 1)[1].strip()
        return fields

    def _detect_anomalies(self, img, text: str, fields: dict) -> list:
        anomalies = []
        if len(text.strip()) < 50:
            anomalies.append("Very little text detected - possible blank or corrupt document")
        if not fields:
            anomalies.append("Could not extract standard certificate fields")
        # Resolution check
        h, w = img.shape[:2]
        if h < 300 or w < 300:
            anomalies.append("Low resolution image")
        return anomalies

verification_service = VerificationService()
