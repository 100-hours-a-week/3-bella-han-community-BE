#!/bin/bash

# Community Backend Deploy Script for Raspberry Pi
# Usage: ./deploy.sh [service_name]
# If no service_name provided, deploys all services

set -e

SERVICE=${1:-all}

echo "🚀 Starting deployment of Community services..."
echo "Service: $SERVICE"
echo "Timestamp: $(date)"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if .env file exists
if [ ! -f ".env" ]; then
    print_error ".env file not found! Please create .env file from env-example.md"
    exit 1
fi

# Load environment variables
set -a
source .env
set +a

# Function to deploy specific service
deploy_service() {
    local service=$1

    print_status "Deploying $service..."

    case $service in
        "mysql")
            print_status "Starting MySQL container..."
            docker compose up -d mysql
            print_status "Waiting for MySQL to be healthy..."
            docker compose exec -T mysql mysqladmin ping -h localhost --wait=30 --silent
            print_success "MySQL deployed successfully"
            ;;

        "backend")
            print_status "Pulling latest backend image..."
            docker compose pull backend

            print_status "Starting backend container..."
            docker compose up -d backend

            print_status "Waiting for backend to be healthy..."
            timeout=60
            while [ $timeout -gt 0 ]; do
                if curl -f http://localhost:18080/actuator/health >/dev/null 2>&1; then
                    print_success "Backend deployed successfully"
                    break
                fi
                sleep 5
                timeout=$((timeout - 5))
            done

            if [ $timeout -le 0 ]; then
                print_error "Backend health check failed!"
                exit 1
            fi
            ;;

        "frontend")
            print_status "Pulling latest frontend image..."
            docker compose pull frontend

            print_status "Starting frontend container..."
            docker compose up -d frontend

            print_status "Waiting for frontend to be ready..."
            sleep 10

            if curl -f http://localhost:18000 >/dev/null 2>&1; then
                print_success "Frontend deployed successfully"
            else
                print_warning "Frontend may not be fully ready yet"
            fi
            ;;

        *)
            print_error "Unknown service: $service"
            echo "Available services: mysql, backend, frontend"
            exit 1
            ;;
    esac
}

# Function to deploy all services
deploy_all() {
    print_status "Deploying all services..."

    # Deploy MySQL first
    deploy_service "mysql"

    # Wait a bit for MySQL to be fully ready
    sleep 5

    # Deploy backend
    deploy_service "backend"

    # Deploy frontend
    deploy_service "frontend"

    print_success "All services deployed successfully!"
}

# Function to show status
show_status() {
    print_status "Current service status:"
    echo ""
    docker ps --filter "name=community-" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    echo ""
    print_status "Disk usage:"
    docker system df
}

# Function to cleanup
cleanup() {
    print_status "Cleaning up unused Docker resources..."
    docker system prune -f
    print_success "Cleanup completed"
}

# Main logic
case $SERVICE in
    "all")
        deploy_all
        ;;
    "mysql"|"backend"|"frontend")
        deploy_service $SERVICE
        ;;
    "status")
        show_status
        ;;
    "cleanup")
        cleanup
        ;;
    "restart")
        print_status "Restarting all services..."
        docker compose restart
        print_success "Services restarted"
        ;;
    *)
        echo "Usage: $0 [service_name|command]"
        echo ""
        echo "Services:"
        echo "  mysql     - Deploy MySQL database"
        echo "  backend   - Deploy Spring Boot backend"
        echo "  frontend  - Deploy React frontend"
        echo "  all       - Deploy all services (default)"
        echo ""
        echo "Commands:"
        echo "  status    - Show current service status"
        echo "  cleanup   - Remove unused Docker resources"
        echo "  restart   - Restart all services"
        echo ""
        exit 1
        ;;
esac

# Show final status
echo ""
show_status

print_success "Deployment completed! 🎉"
echo ""
print_status "Useful commands:"
echo "  • View logs: docker compose logs -f [service]"
echo "  • Restart service: docker compose restart [service]"
echo "  • Stop all: docker compose down"
echo "  • Check status: ./deploy.sh status"
