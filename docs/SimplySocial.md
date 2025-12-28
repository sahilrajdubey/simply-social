# Institution-Scoped Social Infrastructure



**Product Specification v1.0**

---

## 1\. Purpose

This document defines the functional, architectural, and behavioral specification for an institution-scoped social infrastructure platform.

The system enables high-trust, low-risk communication within educational institutions while preventing cross-institution content leakage. It prioritizes contextual relevance, ephemerality, and accountability over popularity and permanence.

---

## 2\. Scope

### In Scope

* Global user registration
* Institution-scoped visibility and interaction
* Contextual discussion spaces
* Controlled anonymity
* Official institutional broadcaster accounts
* Minimal, systems-driven moderation

### Out of Scope

* Public social networking
* Cross-institution interaction
* Influencer mechanics
* Algorithmic content virality
* Dating or discovery marketplaces

---

## 3\. Core Design Constraints (Non-Negotiable)

1. **Global Identity, Local Visibility**
   Users may register globally, but all content visibility and interaction are strictly limited to the user’s institution.
2. **Hard Institutional Isolation**
   Content from one institution must never be visible or interactable by users from another institution.
3. **Context-First Interaction**
   All content must be associated with an explicit context (space, event, or stream).
4. **Ephemerality by Default**
   Content and spaces expire unless sustained by relevance or activity.
5. **Anonymity with Cost**
   Anonymous speech is permitted, but anonymous accounts are not.

---

## 4\. Terminology

* **Institution**: A logical visibility boundary derived from verified email domain mapping.
* **Context**: A container for interaction (space, event, or system stream).
* **Space**: A temporary discussion container tied to a specific topic or event.
* **Stream**: A system-generated view aggregating context-relevant content.
* **Trust Score**: An internal reputation metric affecting visibility and privileges.
* **Club Account**: A verified institutional broadcaster account.

---

## 5\. User Identity \& Access

### 5.1 Registration

* Email verification is mandatory.
* Any email domain is permitted.
* Domain is mapped to an institution identifier at signup.
* Institution identifier determines visibility scope.

### 5.2 Identity Layers

Each user has:

* One global verified account.
* One institution-scoped presence.

Within an institution, users may operate as:

* Real identity (opt-in).
* Persistent pseudonym.
* Context-scoped anonymous identity.

Anonymous identities:

* Are rate-limited.
* Are reputation-bounded.
* Cannot exist independently of a verified account.

---

## 6\. Visibility \& Isolation Model

### 6.1 Enforcement

Institutional isolation is enforced at:

* Query layer
* Permission layer
* Cache layer

UI-level filtering alone is insufficient.

### 6.2 Restrictions

Users cannot:

* View content from other institutions.
* Search across institutions.
* Interact with users outside their institution.

Each institution functions as a logical shard within a shared infrastructure.

---

## 7\. Content Model

### 7.1 Streams (Feed Replacement)

The system does not provide personal feeds.

Instead, users access **Context Streams**, including but not limited to:

* Today on Campus
* Live Now
* This Week
* Ending Soon
* Exam or Placement Periods

Stream properties:

* Institution-scoped
* Chronological with relevance decay
* Finite scrolling
* No popularity-based ranking

---

## 8\. Spaces

### 8.1 Definition

Spaces are temporary, institution-scoped discussion containers.

### 8.2 Creation

Spaces may be created by:

* Users (subject to trust thresholds)
* Official club accounts
* System-generated events

### 8.3 Lifecycle

* Default expiration time applies.
* Renewal requires sustained activity.
* Inactive spaces expire automatically.
* Expired spaces become read-only archives.

### 8.4 Permissions

Spaces may define:

* Open posting
* Reputation-gated posting
* Invite-only posting

Read access is always allowed within the institution.

---

## 9\. Posting \& Discussion

* Text-first posts
* Media permitted but not emphasized
* All posts must belong to a space or stream
* Anonymous posting permitted with cost

Interaction rules:

* Threaded discussions preferred
* No reposting
* No quote amplification
* No viral mechanics

---

## 10\. Reputation \& Reach

### 10.1 Trust Score

Each account has an internal trust score derived from:

* Report accuracy
* Participation quality
* Consistency across contexts
* Longevity without abuse

### 10.2 Effects

Trust score affects:

* Content visibility
* Posting friction
* Space creation rights

Trust scores are never publicly visible.

---

## 11\. Moderation Model

### 11.1 Moderation Scope

Handled:

* Illegal content
* Harassment
* Explicit material

Not handled:

* Political opinions
* Ideological positions
* Content that is offensive but legal

### 11.2 Enforcement

* System-driven first
* Human moderation as fallback
* Preference for throttling over banning
* All actions are reversible and auditable

---

## 12\. Official Club \& Society Accounts

### 12.1 Verification

* Verified via faculty endorsement or official institutional email
* One club account per institution

### 12.2 Capabilities

* Post official announcements
* Create event-linked spaces
* Pin official updates

### 12.3 Restrictions

* No anonymous posting
* No nudges
* No cold DMs
* No algorithmic amplification
* No cross-institution visibility

Event posts automatically generate temporary spaces that archive after conclusion.

---

## 13\. Nudges (Optional Feature)

### 13.1 Definition

A nudge is a weak, anonymous social signal.

### 13.2 Constraints

* Opt-in only
* Default disabled
* Allowed only between users who shared a context
* No profile browsing
* Anonymous, cryptic notification

### 13.3 Behavior

* Mutual nudges unlock private chat
* Single nudges expire silently
* No rejection feedback

Feature may be removed if abuse exceeds thresholds.

---

## 14\. Private Messaging

* No open DMs
* Chat unlock requires:

  * Mutual interaction
  * Mutual nudge
  * Explicit space permission

Chats retain the originating context metadata.

---

## 15\. Explicit Non-Features

The system explicitly excludes:

* Personal timelines
* Followers or following
* Global trending content
* Influencer metrics
* Public engagement statistics
* Infinite scrolling feeds

---

## 16\. Technical Highlights (Resume-Facing)

* Global authentication with institution-scoped isolation
* Logical sharding via institution identifiers
* Context-indexed content graph
* TTL-based space and post lifecycle
* Trust-weighted rate limiting
* Separation of identity, content, and visibility layers
* Soft-deletion and decay pipelines
* Reversible moderation actions with audit logging

---

## 17\. Design Rationale (Summary)

The platform prioritizes truthful, context-relevant communication over social performance by combining institutional isolation, controlled anonymity, and temporal structures. These constraints are essential to preserving signal quality and user trust in high-context environments.

---

**End of Specification**

---

If you want next steps, the *correct* follow-up is not more features.
It’s one of these:

* Data model (tables / schemas)
* Permission matrix
* Event \& space lifecycle state machine
* Backend service boundaries
* Abuse and failure mode analysis

Those are where this turns from “well-designed” into “serious engineering.”

