from fastapi import APIRouter, UploadFile, File, Depends
from ..models.schemas import CVParseResult
from ..services.cv_parser import cv_parser
from ..core.security import verify_token

router = APIRouter()

@router.post("/cv", response_model=CVParseResult)
async def parse_cv(file: UploadFile = File(...), token: dict = Depends(verify_token)):
    content = await file.read()
    text = content.decode("utf-8", errors="ignore")
    result = cv_parser.parse_text(text)
    return CVParseResult(**result)

@router.post("/cv-text", response_model=CVParseResult)
def parse_cv_text(text: str, token: dict = Depends(verify_token)):
    result = cv_parser.parse_text(text)
    return CVParseResult(**result)
