# Spring Boot RabbitMQ Microservices Demo

This project demonstrates a microservices architecture using Spring Boot, RabbitMQ for message queuing, and MongoDB for data persistence. It consists of three services that communicate through RabbitMQ:

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
├── order-service/        # Service for creating orders
├── payment-service/      # Service for processing payments
├── inventory-service/    # Service for managing inventory
└── docker-compose.yml    # Docker composition file
```

## Quick Start

## Running with pre-built Docker images

The easiest way to run this project is using the pre-built Docker images:

```bash
# Clone the repository to get the docker-compose file
git clone https://github.com/yulku32/springRMQ.git
cd springRMQ

# Pull and run all containers
docker-compose up



This will start:
- RabbitMQ (accessible at http://localhost:15672 - guest/guest)
- MongoDB (port 27018)
- Order Service (port 8080)
- Payment Service (port 8081)
- Inventory Service (port 8082)

## Testing the Services

### Order Service Examples

1. Check if the service is running:
```bash
curl http://localhost:8080/test
```

2. Create a new order:
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"orderId":"order123","customerName":"Alice","totalPrice":150.00}'
```

3. Get all orders:
```bash
curl http://localhost:8080/orders
```

4. Get order by ID:
```bash
curl http://localhost:8080/orders/order123
```

5. Get orders by customer:
```bash
curl http://localhost:8080/orders/customer/Alice
```

### Payment Service Examples

1. Get a list of all customers:
```bash
curl http://localhost:8081/customers
```

2. Get a specific customer:
```bash
curl http://localhost:8081/customers/Alice
```

3. Create a new customer:
```bash
curl -X POST http://localhost:8081/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Charlie","balance":500.00}'
```

4. Update a customer's balance:
```bash
curl -X PUT http://localhost:8081/customers/Charlie \
  -H "Content-Type: application/json" \
  -d '{"balance":750.00}'
```

5. View customer's payment history:
```bash
curl http://localhost:8081/customers/Alice/payments
```

## Service Endpoints

### Order Service (8080)
- GET `/test` - Check if service is running
- POST `/orders` - Create a new order
- GET `/orders` - Get all orders
- GET `/orders/{orderId}` - Get order by ID
- GET `/orders/customer/{customerName}` - Get orders by customer

### Payment Service (8081)
- GET `/customers` - Get all customers
- GET `/customers/{name}` - Get customer by name
- POST `/customers` - Create a new customer
- PUT `/customers/{name}` - Update customer details
- GET `/customers/{name}/payments` - Get customer payment history

### Inventory Service (8082)
- Listens to RabbitMQ queues for inventory processing

## Architecture

The system uses a combination of message queuing and database persistence:

1. **Message Queue Flow**:
   - Order Service publishes order messages to a RabbitMQ fanout exchange
   - Both Payment and Inventory services receive copies of each message
   - Services process their responsibilities independently

2. **Data Storage**:
   - MongoDB stores data for each service in separate databases:
     - `orderdb` for Order Service
     - `paymentdb` for Payment Service
     - `inventorydb` for Inventory Service

3. **Communication Pattern**:
   - Service-to-service communication happens asynchronously through RabbitMQ
   - Payment success/failure notifications use a dedicated exchange

## MongoDB Configuration

MongoDB runs in a Docker container with these settings:
- Port: 27018 (mapped from container's 27017)
- Authentication: enabled
- Username: root
- Password: example
- Data persistence: volume mounted at mongodb_data

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
   - Restart RabbitMQ if needed: `docker-compose restart rabbitmq`

2. If queues aren't being created:
   - Check RabbitMQ management console for queue definitions
   - Verify service logs: `docker-compose logs [service-name]`
   - Ensure all services have proper RabbitMQ configuration

3. MongoDB connection issues:
   - Check MongoDB logs: `docker-compose logs mongodb`
   - Verify connection strings in service configurations
   - Test MongoDB connection: `docker exec -it mongodb mongosh -u root -p example`

4. If a service fails to start:
   - Check for errors in logs: `docker-compose logs [service-name]`
   - Verify environment variables in docker-compose.yml
   - Rebuild the service: `docker-compose up -d --build [service-name]`

