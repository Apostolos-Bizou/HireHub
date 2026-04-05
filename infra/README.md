# HireHub Infrastructure

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Azure Cloud                           │
│                                                          │
│  ┌──────────┐    ┌──────────┐    ┌──────────────────┐   │
│  │ Frontend │───▶│ Backend  │───▶│  AI Microservice  │   │
│  │ App Svc  │    │ App Svc  │    │  Container Inst.  │   │
│  │ (Nginx)  │    │ (Java)   │    │  (Python/FastAPI) │   │
│  └──────────┘    └────┬─────┘    └────────┬──────────┘   │
│                       │                    │              │
│                  ┌────▼────────────────────▼──┐          │
│                  │    PostgreSQL 15            │          │
│                  │    Flexible Server          │          │
│                  └────────────────────────────┘          │
│                                                          │
│  ┌──────────────┐    ┌──────────────────────┐           │
│  │ Blob Storage │    │ Container Registry   │           │
│  │ (files/certs)│    │ (Docker images)      │           │
│  └──────────────┘    └──────────────────────┘           │
└─────────────────────────────────────────────────────────┘
```

## Local Development

```bash
cd infra
docker-compose up -d
```

Services:
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api/v1
- AI Service: http://localhost:8000/docs
- PostgreSQL: localhost:5432

## Azure Deployment

1. Run `./azure-setup.sh` to create infrastructure
2. Configure GitHub secrets (see script output)
3. Push to `main` — CI/CD deploys automatically

## GitHub Secrets Required

| Secret | Description |
|--------|-------------|
| `AZURE_CREDENTIALS` | Service principal JSON |
| `AZURE_REGISTRY_USERNAME` | ACR admin username |
| `AZURE_REGISTRY_PASSWORD` | ACR admin password |
| `DATABASE_URL` | PostgreSQL connection string |
| `JWT_SECRET` | Shared JWT signing key |
