from fastapi import APIRouter, UploadFile, File, Depends
from ..models.schemas import VerificationResult
from ..services.verification_service import verification_service
from ..core.security import verify_token

router = APIRouter()

@router.post("/certificate", response_model=VerificationResult)
async def verify_certificate(
    certificate_id: str,
    file: UploadFile = File(...),
    token: dict = Depends(verify_token)
):
    image_bytes = await file.read()
    result = verification_service.verify_certificate(image_bytes)
    return VerificationResult(
        certificate_id=certificate_id,
        is_authentic=result["is_authentic"],
        confidence=result["confidence"],
        extracted_fields=result.get("extracted_fields", {}),
        anomalies=result.get("anomalies", [])
    )

@router.post("/batch")
async def verify_batch(certificate_ids: list[str], token: dict = Depends(verify_token)):
    """Batch verification placeholder - triggers async processing."""
    return {
        "status": "queued",
        "certificates": certificate_ids,
        "message": f"{len(certificate_ids)} certificates queued for verification"
    }
