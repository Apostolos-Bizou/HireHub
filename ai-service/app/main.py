from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from .core.config import settings
from .routers import matching, verification, parsing, recommendations, audit, health

app = FastAPI(
    title="HireHub AI Service",
    description="AI microservice for maritime talent matching, credential verification, and career recommendations",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.cors_origins.split(","),
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(health.router, tags=["Health"])
app.include_router(matching.router, prefix="/ai/matching", tags=["Matching"])
app.include_router(verification.router, prefix="/ai/verification", tags=["Verification"])
app.include_router(parsing.router, prefix="/ai/parsing", tags=["Parsing"])
app.include_router(recommendations.router, prefix="/ai/recommendations", tags=["Recommendations"])
app.include_router(audit.router, prefix="/ai/audit", tags=["Audit"])
