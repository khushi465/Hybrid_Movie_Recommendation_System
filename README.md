# Hybrid Movie Recommendation System

A production-style hybrid movie recommendation system built using Spring Boot, FastAPI, PostgreSQL, Redis, Kafka, and machine learning-based ranking.

The system combines:

* Content-Based Filtering
* Collaborative Filtering
* Popularity-based ranking
* Recency signals
* Real-time user interaction tracking

It is designed as a distributed, event-driven architecture with caching, resiliency, asynchronous processing, and performance optimization.

---

# Features

## Recommendation Engine

* Hybrid recommendation strategy combining:

  * Collaborative Filtering (similar users)
  * Content-Based Filtering (genre similarity)
  * Popularity scoring
  * Recency-aware ranking
* Dynamic ranking based on user interactions
* Cold-start fallback recommendations

## Distributed Backend Architecture

* Spring Boot REST API
* FastAPI ML ranking service
* Kafka asynchronous event processing
* Redis caching layer
* PostgreSQL persistence layer

## Reliability & Fault Tolerance

* Resilience4j Circuit Breaker
* ML service fallback recommendations
* Kafka Retry Topics
* Dead Letter Queue (DLQ)
* Timeout handling

## Performance Engineering

* Redis caching with cache eviction
* JMeter load testing
* P50/P95 latency tracking
* Cache hit/miss monitoring
* Throughput benchmarking

---

# Architecture Diagram

```text
                         ┌────────────────────┐
                         │       Client       │
                         └─────────┬──────────┘
                                   │
                                   ▼
                     ┌─────────────────────────┐
                     │   Spring Boot Backend   │
                     │ Recommendation API      │
                     └───────┬───────┬─────────┘
                             │       │
                Cache Lookup │       │ Async Events
                             │       ▼
                             │   ┌──────────────┐
                             │   │    Kafka     │
                             │   └──────┬───────┘
                             │          │
                             │          ▼
                             │   ┌──────────────┐
                             │   │ Kafka Consumer│
                             │   └──────┬───────┘
                             │          │
                             ▼          ▼
                    ┌────────────────────────┐
                    │         Redis          │
                    │ Recommendation Cache   │
                    └────────────────────────┘

                    ┌────────────────────────┐
                    │      PostgreSQL        │
                    │ Users / Movies / Logs  │
                    └────────────────────────┘

                             ▼
                  ┌──────────────────────┐
                  │   FastAPI ML Engine  │
                  │ Hybrid Ranking Logic │
                  └──────────────────────┘
```

---

# Recommendation Pipeline

1. User requests recommendations.
2. Spring Boot checks Redis cache.
3. If cache hit:

   * recommendations returned immediately.
4. If cache miss:

   * backend fetches movies/interactions
   * calls FastAPI ML service
   * ML ranks movies using hybrid scoring
   * results cached in Redis
5. User interactions are published asynchronously to Kafka.
6. Kafka consumer processes interactions:

   * stores interactions
   * updates popularity scores
   * evicts recommendation cache

---

# Performance Results

## Load Testing

Performance was validated using Apache JMeter with concurrent users.

### Observed Results

| Metric          | Result           |
| --------------- | ---------------- |
| Throughput      | ~50 requests/sec |
| Average Latency | ~2 ms            |
| P95 Latency     | Sub-5 ms         |
| Max Latency     | ~55 ms           |
| Error Rate      | 0%               |

---

## Redis Caching Impact

### Cold Request (No Cache)

* ~20–40 ms recommendation latency

### Warm Request (Redis Cache Hit)

* ~1–5 ms recommendation latency

### Improvement

* ~95% latency reduction using Redis caching

---

# Fault Tolerance

## Resilience4j Circuit Breaker

The ML recommendation service is protected using Resilience4j.

If the ML service:

* becomes unavailable
* exceeds timeout limits
* throws exceptions

The system automatically falls back to popularity-based recommendations.

### Benefits

* prevents cascading failures
* improves system availability
* maintains low latency during failures

---

## Kafka Retry + Dead Letter Queue (DLQ)

Kafka interaction processing uses:

* retry topics
* Dead Letter Queue (DLQ)

### Retry Flow

```text
interaction-events
        ↓
retry attempt 1
        ↓
retry attempt 2
        ↓
retry attempt 3
        ↓
Dead Letter Queue (DLQ)
```

### Benefits

* prevents event loss
* handles transient failures
* isolates bad messages safely

---

# Tech Stack

## Backend

* Java
* Spring Boot
* Spring Data JPA
* Spring Cache
* Resilience4j

## ML Service

* Python
* FastAPI
* Scikit-learn
* Pandas
* NumPy

## Infrastructure

* PostgreSQL
* Redis
* Apache Kafka
* Zookeeper

## Testing & Monitoring

* Apache JMeter
* Custom latency metrics
* Cache hit/miss tracking

---

# Database Schema

## Users

* id
* name
* email
* interactionCount

## Movies

* id
* title
* genre
* popularityScore

## Interactions

* id
* userId
* movieId
* type
* score
* timestamp

---

# Setup Instructions

## 1. Clone Repository

```bash
git clone https://github.com/khushi465/Hybrid_Movie_Recommendation_System.git
cd Hybrid_Movie_Recommendation_System
```

---

# 2. Start PostgreSQL

Create database:

```sql
CREATE DATABASE recommendation_db;
```

---

# 3. Start Redis

Default port:

```text
6379
```

---

# 4. Start Zookeeper

```bash
bin/windows/zookeeper-server-start.bat config/zookeeper.properties
```

---

# 5. Start Kafka

```bash
bin/windows/kafka-server-start.bat config/server.properties
```

---

# 6. Configure Spring Boot

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/recommendation_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.data.redis.host=localhost
spring.data.redis.port=6379

spring.kafka.bootstrap-servers=localhost:9092
```

---

# 7. Start FastAPI ML Service

Install dependencies:

```bash
pip install -r requirements.txt
```

Run service:

```bash
uvicorn main:app --reload
```

Default port:

```text
8000
```

---

# 8. Start Spring Boot Backend

```bash
mvn spring-boot:run
```

Default port:

```text
8080
```

---

# API Endpoints

## Recommendations

```http
GET /recommendations/{userId}
```

## Add Interaction

```http
POST /interactions
```

Example:

```json
{
  "userId": 1,
  "movieId": 10,
  "type": "WATCH",
  "score": 5
}
```

---

# Load Testing

Apache JMeter was used for:

* concurrency testing
* throughput analysis
* cache validation
* latency benchmarking

### Test Scenarios

* cold cache
* warm cache
* multiple users
* concurrent requests

---

# Future Improvements

* JWT Authentication
* Docker + Docker Compose deployment
* AWS deployment
* Prometheus + Grafana monitoring
* Kubernetes orchestration
* Explainable recommendations
* A/B testing

---

# Resume Highlights

* Built a hybrid recommendation engine combining collaborative filtering, content-based filtering, popularity, and recency signals.
* Reduced recommendation latency by ~95% using Redis caching.
* Achieved ~50 req/sec throughput with sub-5ms P95 latency under load.
* Implemented fault-tolerant recommendation delivery using Resilience4j Circuit Breaker.
* Built Kafka-based asynchronous interaction processing with Retry Topics and Dead Letter Queues (DLQ).
* Designed a distributed event-driven backend architecture integrating Spring Boot, FastAPI, PostgreSQL, Redis, and Kafka.
