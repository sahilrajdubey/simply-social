# System Architecture Specification

**Version:** 1.0

---

## 1\. Overview

This document defines the technical architecture and technology choices for the institution-scoped social infrastructure platform.

The system is designed to be:

* Mobile-first
* Institution-isolated by construction
* State-driven on the client
* Explicit, modular, and auditable on the backend

All choices prioritize correctness, maintainability, and clarity over novelty.

---

## 2\. Technology Stack

### Client (Android)

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose
* **Architecture Pattern:** MVVM with a single immutable UI state per screen
* **Concurrency:** Kotlin Coroutines + Flow

### Backend

* **Language:** Java 25
* **Framework:** Spring Boot (4.0.x)
* **API Style:** REST
* **Runtime:** JVM

### Database

* **Primary Database:** PostgreSQL
* **Data Model:** Relational with selective JSONB usage

---

## 3\. Client Architecture (Android)

### 3.1 Design Goals

* Fast onboarding
* Modern, professional UI
* Predictable state transitions
* Easy debugging of asynchronous behavior

### 3.2 UI Framework: Jetpack Compose

Jetpack Compose is used as the sole UI framework.

Rationale:

* Declarative, state-driven UI
* Eliminates XML layout complexity
* Natural fit for reactive data streams
* Enables fluid animations and transitions
* Industry-standard for modern Android development

Material Design 3 is used as the design system baseline.

---

### 3.3 Architectural Pattern: MVVM (Single UI State)

The client uses **MVVM with a single immutable UI state per screen**.

#### Core Rules

* Each screen has exactly one `UiState` data class
* UI is a pure function of `UiState`
* All mutations happen inside the ViewModel
* UI never directly performs business logic
* Side effects are isolated

#### Data Flow

```
User Action
   ↓
ViewModel Event Handler
   ↓
State Transformation
   ↓
New UiState
   ↓
Compose recomposition
```

This enforces:

* Predictable recomposition
* Atomic UI updates
* Easier reasoning about state

---

### 3.4 ViewModel Responsibilities

* Hold the single source of truth for UI state
* Translate backend/domain models into UI-ready state
* Handle user intents (clicks, refreshes, submissions)
* Coordinate async operations via coroutines

ViewModels **must not**:

* Reference UI elements
* Store multiple independent state objects
* Leak business logic into composables

---

### 3.5 State Management

Each screen defines:

* `UiState` (immutable)
* `UiEvent` (user-driven actions)
* Optional `UiEffect` (one-time effects like navigation)

State updates are:

* Explicit
* Traceable
* Testable

---

## 4\. Backend Architecture

### 4.1 Framework: Spring Boot

Spring Boot is used for backend services.

Rationale:

* Mature ecosystem
* Strong security primitives
* Excellent PostgreSQL support
* Clear layering conventions
* Widely understood and reviewable

---

### 4.2 Language: Java 25

Java 25 is used with modern language features.

Rationale:

* Long-term stability
* Strong typing and tooling
* JVM performance characteristics
* Clear resume signal for backend engineering

Strict guidelines:

* No legacy APIs
* No reflection-heavy magic
* Explicit configuration over convention abuse

---

### 4.3 Backend Layering

The backend follows a strict layered architecture:

* **Controller Layer**

  * HTTP request handling
  * Input validation
  * Institution scope enforcement

* **Service Layer**

  * Business logic
  * Permission checks
  * Trust and moderation logic

* **Domain Layer**

  * Core entities
  * Invariants
  * Domain rules

* **Persistence Layer**

  * Repositories
  * Database access
  * Query enforcement

No layer skipping is allowed.

---

### 4.4 API Design

* RESTful endpoints
* JSON request/response
* Explicit institution scoping on every request
* No endpoint exists without institutional context

Example rule:

> Any API call without an institution identifier is invalid by design.

---

### 4.5 Realtime \& Async Communication

* WebSockets or Server-Sent Events (SSE) for:

  * Live space updates
  * Real-time alerts

* Event-driven internal handling
* No premature distributed systems complexity

---

## 5\. Database Architecture

### 5.1 Database: PostgreSQL

PostgreSQL is the primary datastore.

Rationale:

* Strong relational guarantees
* ACID compliance
* Advanced indexing
* JSONB support for flexible metadata
* Proven scalability

---

### 5.2 Data Modeling Principles

* Institution ID included in all core tables
* Strong foreign key relationships
* Soft deletes preferred over hard deletes
* TTL metadata used for expiration and decay
* Separation of concerns across tables:

  * Identity
  * Content
  * Visibility
  * Moderation
  * Events

Cross-institution joins are forbidden.

---

## 6\. Security \& Isolation

### 6.1 Institution Isolation

Institution isolation is enforced at:

* API level
* Service logic
* Database query constraints
* Cache segmentation

UI filtering is **never** considered sufficient.

---

### 6.2 Identity \& Trust

* Verified email-based identity
* No anonymous accounts
* Controlled anonymity at the content level
* Trust-based privilege degradation

---

## 7\. Non-Goals (Explicit)

The system does not aim to:

* Support influencer mechanics
* Provide global content discovery
* Optimize for viral engagement
* Act as a dating platform
* Replace general-purpose social media

These exclusions are intentional and architectural.

---

## 8\. Resume-Facing Summary

**Client**

* Kotlin
* Jetpack Compose
* State-driven MVVM architecture
* Immutable UI state management

**Backend**

* Java 25
* Spring Boot
* Modular service architecture
* Institution-scoped API design

**Database**

* PostgreSQL
* Relational modeling with TTL-based decay
* Strict data isolation guarantees

**Engineering Focus**

* Predictable state transitions
* Context-first design
* Trust-aware systems
* Explicit architectural constraints

---

## 9\. Final Note

This architecture prioritizes **clarity over cleverness**.

Every component:

* Has a single responsibility
* Enforces constraints instead of relying on discipline
* Is explainable without hand-waving

This is the foundation of a serious system, whether or not the product succeeds commercially.

---

**End of Document**

---

If you want to continue *correctly*, the next steps are:

* Define **data models and schemas**
* Define **API contracts**
* Define **UI state models for 1–2 core screens**
* Define **institution isolation invariants**

That’s where architecture stops being text and becomes real.

