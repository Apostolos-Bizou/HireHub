import pytest
from fastapi.testclient import TestClient
from jose import jwt
from datetime import datetime, timedelta, timezone

# Generate a test JWT
def make_token(role="SHIPOWNER", user_id="test-user-123"):
    payload = {
        "sub": user_id,
        "email": "test@test.com",
        "role": role,
        "type": "access",
        "exp": datetime.now(timezone.utc) + timedelta(hours=1)
    }
    return jwt.encode(payload, "CHANGE_ME", algorithm="HS256")

@pytest.fixture
def client():
    from app.main import app
    return TestClient(app)

@pytest.fixture
def auth_header():
    return {"Authorization": f"Bearer {make_token()}"}

@pytest.fixture
def seafarer_header():
    return {"Authorization": f"Bearer {make_token('SEAFARER')}"}
