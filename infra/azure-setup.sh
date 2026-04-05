#!/bin/bash
# HireHub Azure Infrastructure Setup
# Run once to create all Azure resources
# Prerequisites: Azure CLI installed, logged in (az login)

set -e

# Configuration
RG="hirehub-rg"
LOCATION="westeurope"
ACR="hirehub"
DB_SERVER="hirehub-db"
DB_NAME="hirehub"
DB_ADMIN="hirehub_admin"
DB_PASS="HireHub_Prod_2026!"
BACKEND_APP="hirehub-api"
FRONTEND_APP="hirehub-web"
STORAGE="hirehubstorage"

echo "🚀 Creating HireHub Azure Infrastructure..."

# 1. Resource Group
echo "📦 Creating Resource Group..."
az group create --name $RG --location $LOCATION

# 2. Container Registry
echo "🐳 Creating Container Registry..."
az acr create --resource-group $RG --name $ACR --sku Basic --admin-enabled true

# 3. PostgreSQL Flexible Server
echo "🗄️ Creating PostgreSQL..."
az postgres flexible-server create \
  --resource-group $RG \
  --name $DB_SERVER \
  --location $LOCATION \
  --admin-user $DB_ADMIN \
  --admin-password "$DB_PASS" \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --storage-size 32 \
  --version 15 \
  --public-access 0.0.0.0

# Create database
az postgres flexible-server db create \
  --resource-group $RG \
  --server-name $DB_SERVER \
  --database-name $DB_NAME

# 4. App Service Plan
echo "📱 Creating App Service Plan..."
az appservice plan create \
  --resource-group $RG \
  --name hirehub-plan \
  --is-linux \
  --sku B1

# 5. Backend App Service
echo "☕ Creating Backend App Service..."
az webapp create \
  --resource-group $RG \
  --plan hirehub-plan \
  --name $BACKEND_APP \
  --deployment-container-image-name $ACR.azurecr.io/hirehub-backend:latest

az webapp config appsettings set --resource-group $RG --name $BACKEND_APP --settings \
  DB_HOST="${DB_SERVER}.postgres.database.azure.com" \
  DB_PORT=5432 \
  DB_NAME=$DB_NAME \
  DB_USER=$DB_ADMIN \
  DB_PASS="$DB_PASS" \
  JWT_SECRET="$(openssl rand -base64 48)" \
  AI_SERVICE_URL="http://hirehub-ai.${LOCATION}.azurecontainer.io:8000" \
  SPRING_PROFILES_ACTIVE=prod

# 6. Frontend App Service
echo "🖥️ Creating Frontend App Service..."
az webapp create \
  --resource-group $RG \
  --plan hirehub-plan \
  --name $FRONTEND_APP \
  --deployment-container-image-name $ACR.azurecr.io/hirehub-frontend:latest

# 7. Blob Storage
echo "📁 Creating Blob Storage..."
az storage account create \
  --resource-group $RG \
  --name $STORAGE \
  --location $LOCATION \
  --sku Standard_LRS

az storage container create \
  --account-name $STORAGE \
  --name hirehub-files \
  --public-access off

# 8. AI Service (Container Instance)
echo "🤖 Creating AI Container Instance..."
ACR_PASS=$(az acr credential show --name $ACR --query "passwords[0].value" -o tsv)
az container create \
  --resource-group $RG \
  --name hirehub-ai \
  --image $ACR.azurecr.io/hirehub-ai:latest \
  --cpu 2 --memory 4 \
  --ports 8000 \
  --dns-name-label hirehub-ai \
  --environment-variables \
    DATABASE_URL="postgresql://${DB_ADMIN}:${DB_PASS}@${DB_SERVER}.postgres.database.azure.com:5432/${DB_NAME}?sslmode=require" \
    JWT_SECRET="$(az webapp config appsettings list -g $RG -n $BACKEND_APP --query '[?name==`JWT_SECRET`].value' -o tsv)" \
  --registry-login-server $ACR.azurecr.io \
  --registry-username $ACR \
  --registry-password "$ACR_PASS"

echo ""
echo "✅ HireHub Azure Infrastructure Created!"
echo ""
echo "📋 Resources:"
echo "  Resource Group:  $RG"
echo "  Container Reg:   $ACR.azurecr.io"
echo "  PostgreSQL:      $DB_SERVER.postgres.database.azure.com"
echo "  Backend:         https://$BACKEND_APP.azurewebsites.net"
echo "  Frontend:        https://$FRONTEND_APP.azurewebsites.net"
echo "  AI Service:      http://hirehub-ai.$LOCATION.azurecontainer.io:8000"
echo "  Blob Storage:    $STORAGE"
echo ""
echo "🔑 Next steps:"
echo "  1. Add GitHub secrets: AZURE_CREDENTIALS, AZURE_REGISTRY_USERNAME, AZURE_REGISTRY_PASSWORD"
echo "  2. Add secret: DATABASE_URL, JWT_SECRET"
echo "  3. Push to main to trigger CI/CD"
