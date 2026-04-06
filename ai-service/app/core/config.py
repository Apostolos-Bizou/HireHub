from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env")

    database_url: str = "postgresql://hirehub:hirehub@localhost:5432/hirehub"
    jwt_secret: str = "CHANGE_ME"
    backend_url: str = "http://localhost:8080/api/v1"
    tesseract_path: str = "/usr/bin/tesseract"
    cors_origins: str = "http://localhost:5173,http://localhost:8080"
    weight_rank: float = 0.25
    weight_vessel: float = 0.20
    weight_experience: float = 0.15
    weight_certs: float = 0.15
    weight_availability: float = 0.10
    weight_verified: float = 0.10
    weight_recency: float = 0.05

settings = Settings()
