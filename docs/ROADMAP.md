# 🗺️ SimplySocial — Roadmap

---

## Phase 1: The Raw Metal Setup

**Goal:** A Java backend running on Oracle Cloud (connected to Supabase) and an Android app that can say “Hello” to it.

| Backend (Java 25 / Spring Boot) | Android (Kotlin / Compose) |
|--------------------------------|-----------------------------|
| **1.1 Oracle VM Prep**  <br> SSH into Oracle VM. Install Java 23/25 manually (sdkman or tarball). Open firewall port 8080. | **1.1 Base Shell**  <br> Create Android project. Add Retrofit + Kotlin Serialization. Hardcode Oracle VM public IP in RetrofitInstance. |
| **1.2 Database Connection**  <br> Configure application.properties with Supabase credentials. Run locally once to confirm DB connection before deploying. | **1.2 Login UI (The Gate)**  <br> Simple “Enter Email” screen. One input field, one “Verify” button. |
| **1.3 Fat JAR Build**  <br> ./mvnw clean package → scp target/backend.jar → java -jar backend.jar | **1.3 Auth Logic**  <br> Call /auth/register. Store JWT in EncryptedSharedPreferences. |
| **1.4 systemd Service**  <br> Create simplysocial.service so backend restarts on VM reboot. | **1.4 Token Interceptor**  <br> OkHttp interceptor injecting Authorization: Bearer <token> into all requests. |

**Checkpoint:**  
- “Started Application” visible in SSH logs  
- Android login returns 200 OK

---

## Phase 2: Spaces & Content 

**Goal:** Sending real data back and forth.

| Backend (Java 25 / Spring Boot) | Android (Kotlin / Compose) |
|--------------------------------|-----------------------------|
| **2.1 Space Entity**  <br> Create Space table (ID, Name, InstitutionID, Expiry). Add Create Space endpoint. | **2.1 Home Screen**  <br> Fetch spaces. Display in LazyColumn. Handle loading and error states. |
| **2.2 Posts Endpoint**  <br> GET /spaces/{id}/posts. Start with polling or pull-to-refresh. | **2.2 Chat Screen**  <br> Open Space → fetch posts. Send button POSTs to /spaces/{id}/posts. |
| **2.3 Isolation Logic**  <br> Apply institution filtering to all queries. Test cross-institution access manually. | **2.3 Navigation Arguments**  <br> Pass spaceId and spaceTitle safely via Compose Navigation. |
| **2.4 Deployment Cycle 2**  <br> Build → SCP → sudo systemctl restart simplysocial | **2.4 UI Polish**  <br> Chat bubbles: mine right, others left. Minimal styling. |

**Checkpoint:**  
- You can chat with yourself  
- Changing email domain removes all spaces (isolation confirmed)

---

## Phase 3: Anti-Viral Mechanics

**Goal:** Enforce rules that prevent this from becoming Twitter.

| Backend (Java 25 / Spring Boot) | Android (Kotlin / Compose) |
|--------------------------------|-----------------------------|
| **3.1 Identity Masking**  <br> If is_anon = true, return random alias instead of real name. | **3.1 Anonymous Toggle**  <br> Switch in chat input. Send anon flag in payload. |
| **3.2 Context Streams**  <br> Queries for tagged spaces (e.g. #exam, #events). Expose /streams/exams. | **3.2 Tabbed Navigation**  <br> Tabs: All / Exams / Events. Each calls its own endpoint. |
| **3.3 Trust Score (Basic)**  <br> Add trust_score to User. Reject post if score < 0. | **3.3 Profile View**  <br> Read-only screen showing Trust Score. |
| **3.4 Deployment Cycle 3**  <br> Build → SCP → Restart → inspect logs. | **3.4 Settings**  <br> Log out (clear storage) + About screen. |

**Checkpoint:**  
- Anonymous posting works  
- Streams filter content correctly

---

## Phase 4: Hardening 

**Goal:** Make it ready for real users.

| Backend (Java 25 / Spring Boot) | Android (Kotlin / Compose) |
|--------------------------------|-----------------------------|
| **4.1 Validation**  <br> Use @Valid everywhere. Enforce size limits. Reject bad input. | **4.1 UI Validation**  <br> Immediate client-side errors for invalid input. |
| **4.2 Cron Jobs**  <br> Delete expired Spaces hourly (expiry < NOW()). | **4.2 Local Caching**  <br> Add Room DB. Offline read-only mode. |
| **4.3 Production Config**  <br> Disable show-sql. Add JVM -Xmx limits. | **4.3 Release Build**  <br> Sign APK. Test on physical device. |

---

## Manual Deployment Cheat Sheet

```bash
./mvnw clean package -DskipTests
scp -i path/to/oracle_key.key target/simplysocial-backend.jar opc@123.45.67.89:~/
