# Spring Boot RabbitMQ Microservices Demo

This project demonstrates a microservices architecture using Spring Boot and RabbitMQ for message queuing. It consists of three services that communicate through RabbitMQ:

- **Order Service**: Creates and publishes orders
- **Payment Service**: Processes payments for orders
- **Inventory Service**: Manages inventory updates for orders

## Prerequisites

- Docker and Docker Compose installed
- Git (to clone the repository)

## Project Structure

```
project-root/
├── order-commons/        # Shared library with common configurations
├── order-service/       # Service for creating orders
├── payment-service/     # Service for processing payments
├── inventory-service/   # Service for managing inventory
└── docker-compose.yml   # Docker composition file
```

## Quick Start

1. Clone the repository:
```bash
git clone https://github.com/yulku32/springRMQ.git
cd springRMQ
```

2. Run with Docker Compose:
```bash
docker-compose up --build
```

This will start:
- RabbitMQ (accessible at http://localhost:15672 - guest/guest)
- Order Service (port 8080)
- Payment Service (port 8081)
- Inventory Service (port 8082)

## Testing the Services

1. Create a new order:
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"orderId":"123","customerName":"John","totalPrice":100.00}'
```

2. Check RabbitMQ Management Console:
- Open http://localhost:15672
- Login with guest/guest
- Navigate to Queues tab to see messages being processed

## Service Endpoints

### Order Service (8080)
- POST `/orders` - Create a new order

### Payment Service (8081)
- Listens to RabbitMQ queue: `orders.payment.queue`

### Inventory Service (8082)
- Listens to RabbitMQ queue: `orders.inventory.queue`

## Architecture

The system uses a fanout exchange pattern in RabbitMQ:
1. Order Service publishes messages to an exchange
2. Both Payment and Inventory services receive copies of each message
3. Services process their respective responsibilities independently

## Stopping the Services

To stop all services:
```bash
docker-compose down
```

To remove all containers and volumes:
```bash
docker-compose down -v
```

## Troubleshooting

1. If services can't connect to RabbitMQ:
   - Ensure RabbitMQ is healthy: `docker ps`
   - Check logs: `docker-compose logs rabbitmq`

2. If queues aren't being created:
   - Check RabbitMQ management console
   - Verify service logs: `docker-compose logs [service-name]`