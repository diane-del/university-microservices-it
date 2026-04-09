# Sistema Universitario Microservizi — v2.0

Architettura a microservizi completa per la gestione universitaria.
Sviluppato con **Spring Boot 3.2 · Java 17 · PostgreSQL · Feign Client · Spring Cloud Gateway · Docker · Kubernetes**

---

##  Struttura completa del progetto

```
university-microservices-it/
│
├── student-service/                  ← Gestione degli studenti (porta 8081)
│   ├── src/main/java/com/university/student/
│   │   ├── StudentServiceApplication.java
│   │   ├── SwaggerConfig.java
│   │   ├── entity/Student.java               ← Stati: ATTIVO, INATTIVO, SOSPESO
│   │   ├── repository/StudentRepository.java
│   │   ├── service/StudentService.java
│   │   └── controller/StudentController.java  ← Endpoint: /api/studenti
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│   └── Dockerfile
│
├── course-service/                   ← Gestione dei corsi (porta 8083)
│   ├── src/main/java/com/university/course/
│   │   ├── CourseServiceApplication.java
│   │   ├── SwaggerConfig.java
│   │   ├── entity/Course.java                ← Stati: DISPONIBILE, PIENO, ANNULLATO
│   │   ├── repository/CourseRepository.java
│   │   ├── service/CourseService.java
│   │   └── controller/CourseController.java   ← Endpoint: /api/corsi
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│   └── Dockerfile
│
├── enrollment-service/               ← Iscrizioni (porta 8082) — Feign → Studenti + Corsi
│   ├── src/main/java/com/university/enrollment/
│   │   ├── EnrollmentServiceApplication.java  ← @EnableFeignClients
│   │   ├── SwaggerConfig.java
│   │   ├── entity/Enrollment.java             ← Stati: ATTIVA, ANNULLATA, COMPLETATA
│   │   ├── repository/EnrollmentRepository.java
│   │   ├── service/EnrollmentService.java
│   │   ├── controller/EnrollmentController.java ← Endpoint: /api/iscrizioni
│   │   └── client/
│   │       ├── StudentClient.java             ← Feign verso Servizio Studenti
│   │       └── CourseClient.java              ← Feign verso Servizio Corsi
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│   └── Dockerfile
│
├── grade-service/                    ← Voti su 30 (porta 8084) — Feign → Studenti + Corsi
│   ├── src/main/java/com/university/grade/
│   │   ├── GradeServiceApplication.java       ← @EnableFeignClients
│   │   ├── SwaggerConfig.java
│   │   ├── entity/Grade.java                  ← voto 0-30, superato se >=18, lode se 30
│   │   ├── repository/GradeRepository.java
│   │   ├── service/GradeService.java
│   │   ├── controller/GradeController.java     ← Endpoint: /api/voti
│   │   └── client/
│   │       ├── StudentClient.java             ← Feign verso Servizio Studenti
│   │       ├── CourseClient.java              ← Feign verso Servizio Corsi
│   │       └── EnrollmentClient.java          ← Feign verso Servizio Iscrizioni
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│   └── Dockerfile
│
├── report-service/                   ← Rapporti (porta 8085) — Feign → TUTTI (nessun DB)
│   ├── src/main/java/com/university/report/
│   │   ├── ReportServiceApplication.java      ← @EnableFeignClients
│   │   ├── SwaggerConfig.java
│   │   ├── service/ReportService.java
│   │   ├── controller/ReportController.java    ← Endpoint: /api/rapporti
│   │   └── client/
│   │       ├── StudentClient.java
│   │       ├── CourseClient.java
│   │       ├── EnrollmentClient.java
│   │       └── GradeClient.java
│   ├── src/main/resources/application.yml     ← Nessun datasource
│   ├── pom.xml
│   └── Dockerfile
│
├── api-gateway/                      ← Punto di ingresso unico (porta 8080)
│   ├── src/main/java/com/university/gateway/
│   │   └── ApiGatewayApplication.java
│   ├── src/main/resources/application.yml     ← Route verso i 5 servizi
│   ├── pom.xml
│   └── Dockerfile
│
├── k8s/
│   ├── student-deployment.yml
│   ├── course-deployment.yml
│   ├── enrollment-deployment.yml
│   └── gateway-deployment.yml                 ← LoadBalancer → IP pubblica Google
│
├── docker-compose.yml                ← Avvia tutto con un solo comando
├── deploy-gcloud.sh
└── README.md
```

---

##  Panoramica dei servizi

| Servizio | Porta | Database | Chiama via Feign |
|---|---|---|---|
| Servizio Studenti | 8081 | PostgreSQL studentidb:5432 | — |
| Servizio Corsi | 8083 | PostgreSQL corsidb:5434 | — |
| Servizio Iscrizioni | 8082 | PostgreSQL iscrizionidb:5433 | Studenti + Corsi |
| Servizio Voti | 8084 | PostgreSQL votidb:5435 | Studenti + Corsi |
| Servizio Rapporti | 8085 | **Nessuno** (solo Feign) | Studenti + Corsi + Iscrizioni + Voti |
| API Gateway | 8080 | Nessuno | Instrada verso i 5 servizi |

---

## Tecnologie utilizzate

| Tecnologia | Utilizzo |
|---|---|
| Java 17 | Linguaggio principale |
| Spring Boot 3.2.3 | Framework microservizi |
| Spring Cloud Gateway | API Gateway (routing, CORS) |
| Spring Cloud OpenFeign | Comunicazione HTTP inter-servizi |
| Spring Data JPA | Accesso al database |
| PostgreSQL 15 | Database per servizio |
| Lombok | Riduzione del codice boilerplate |
| Springdoc OpenAPI 2.3.0 | Documentazione Swagger UI |
| Docker | Containerizzazione |
| Docker Compose | Orchestrazione locale |
| Kubernetes / Minikube | Orchestrazione container (locale) |
| Google Kubernetes Engine | Distribuzione cloud (produzione) |

---

##  Avviare il progetto

### Opzione 1 — IntelliJ + Docker (sviluppo)

Avviare i 4 database PostgreSQL:
```bash
docker run -d --name pg-studenti    -e POSTGRES_DB=studentidb   -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5432:5432 postgres:15
docker run -d --name pg-corsi       -e POSTGRES_DB=corsidb      -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5434:5432 postgres:15
docker run -d --name pg-iscrizioni  -e POSTGRES_DB=iscrizionidb -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5433:5432 postgres:15
docker run -d --name pg-voti        -e POSTGRES_DB=votidb       -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin123 -p 5435:5432 postgres:15
```

Poi avviare in IntelliJ in questo ordine:
```
1. StudentServiceApplication    → porta 8081
2. CourseServiceApplication     → porta 8083
3. EnrollmentServiceApplication → porta 8082
4. GradeServiceApplication      → porta 8084
5. ReportServiceApplication     → porta 8085
6. ApiGatewayApplication        → porta 8080
```

---

### Opzione 2 — Docker Compose CONSIGLIATO

```bash
# Compilare tutti i servizi (da university-microservices-it/)
cd student-service    && mvn clean package -DskipTests && cd .. && \
cd course-service     && mvn clean package -DskipTests && cd .. && \
cd enrollment-service && mvn clean package -DskipTests && cd .. && \
cd grade-service      && mvn clean package -DskipTests && cd .. && \
cd report-service     && mvn clean package -DskipTests && cd .. && \
cd api-gateway        && mvn clean package -DskipTests && cd ..

# Avviare tutto in modalità detached (background)
docker compose up --build -d

# Verificare che tutto funzioni
docker compose ps

# Fermare tutto
docker compose down

# Fermare + cancellare i dati dei database
docker compose down -v

# Vedere i log in tempo reale
docker compose logs -f

# Log di un solo servizio
docker compose logs -f servizio-iscrizioni
```

---

##  Documentazione Swagger UI

Ogni servizio espone la propria documentazione interattiva.
**Il professore può testare direttamente da Swagger senza Postman.**

| Servizio | URL Swagger |
|---|---|
| Servizio Studenti | http://localhost:8081/swagger-ui.html |
| Servizio Iscrizioni | http://localhost:8082/swagger-ui.html |
| Servizio Corsi | http://localhost:8083/swagger-ui.html |
| Servizio Voti | http://localhost:8084/swagger-ui.html |
| Servizio Rapporti | http://localhost:8085/swagger-ui.html |

---

##  Guida ai test — Postman

Tutte le richieste passano dal Gateway: **https://130.192.100.243/group-4/**

> ⚠️ Rispettare l'ordine — uno studente e un corso devono esistere prima di creare un'iscrizione, e un'iscrizione deve esistere prima di assegnare un voto.

---

### 1️⃣ SERVIZIO STUDENTI

| Azione | Metodo | URL |
|---|---|---|
| Crea uno studente | POST | http://localhost:8080/api/studenti |
| Lista tutti gli studenti | GET | http://localhost:8080/api/studenti |
| Dettaglio studente | GET | http://localhost:8080/api/studenti/1 |
| Modifica studente | PUT | http://localhost:8080/api/studenti/1 |
| Elimina studente | DELETE | http://localhost:8080/api/studenti/1 |
| Verifica esistenza (Feign) | GET | http://localhost:8080/api/studenti/1/esiste |

**Body POST / PUT:**
```json
{
  "firstName": "Marco",
  "lastName": "Rossi",
  "email": "marco.rossi@universita.it",
  "dateOfBirth": "2000-03-15",
  "phoneNumber": "3331234567",
  "status": "ATTIVO"
}
```
**Valori per `status`:** `ATTIVO` · `INATTIVO` · `SOSPESO`

---

### 2️⃣ SERVIZIO CORSI

| Azione | Metodo | URL |
|---|---|---|
| Crea un corso | POST | http://localhost:8080/api/corsi |
| Lista tutti i corsi | GET | http://localhost:8080/api/corsi |
| Corsi disponibili | GET | http://localhost:8080/api/corsi/disponibili |
| Dettaglio corso | GET | http://localhost:8080/api/corsi/1 |
| Modifica corso | PUT | http://localhost:8080/api/corsi/1 |
| Elimina corso | DELETE | http://localhost:8080/api/corsi/1 |
| Verifica esistenza (Feign) | GET | http://localhost:8080/api/corsi/1/esiste |

**Body POST / PUT:**
```json
{
  "name": "Matematica",
  "description": "Algebra lineare e analisi matematica",
  "professor": "Prof. Ferrari",
  "credits": 6,
  "maxStudents": 30,
  "status": "DISPONIBILE"
}
```
**Valori per `status`:** `DISPONIBILE` · `PIENO` · `ANNULLATO`

---

### 3️⃣ SERVIZIO ISCRIZIONI — Feign attivo

> 🔗 Il Servizio Iscrizioni chiama Studenti e Corsi via Feign prima di iscrivere.

| Azione | Metodo | URL |
|---|---|---|
| Iscrivere studente a un corso | POST | http://localhost:8080/api/iscrizioni?studentId=1&courseId=1 |
| Tutte le iscrizioni | GET | http://localhost:8080/api/iscrizioni |
| Una iscrizione | GET | http://localhost:8080/api/iscrizioni/1 |
| Corsi di uno studente | GET | http://localhost:8080/api/iscrizioni/studente/1 |
| Studenti di un corso | GET | http://localhost:8080/api/iscrizioni/corso/1 |
| Annulla iscrizione | PATCH | http://localhost:8080/api/iscrizioni/1/annulla |
| Elimina iscrizione | DELETE | http://localhost:8080/api/iscrizioni/1 |

**Catena Feign all'iscrizione:**
```
Postman → Gateway (8080)
       → Servizio Iscrizioni (8082)
       → Servizio Studenti (8081) : GET /api/studenti/1/esiste
       → Servizio Corsi (8083)    : GET /api/corsi/1/esiste
       → Salva se tutto OK
```

---

### 4️⃣ SERVIZIO VOTI — Feign attivo · Sistema italiano su 30

> 📐 **Regola:** Voto tra 0 e 30. Superato se **>= 18**. `cumLaude = true` solo se voto **= 30**.

| Azione | Metodo | URL |
|---|---|---|
| Assegna un voto | POST | http://localhost:8080/api/voti?studentId=1&courseId=1&score=25 |
| Tutti i voti | GET | http://localhost:8080/api/voti |
| Un voto | GET | http://localhost:8080/api/voti/1 |
| Voti di uno studente | GET | http://localhost:8080/api/voti/studente/1 |
| Voti di un corso | GET | http://localhost:8080/api/voti/corso/1 |
| Voto studente/corso | GET | http://localhost:8080/api/voti/studente/1/corso/1 |
| Media di un corso | GET | http://localhost:8080/api/voti/corso/1/media |
| Modifica voto | PUT | http://localhost:8080/api/voti/1?score=28 |
| Elimina voto | DELETE | http://localhost:8080/api/voti/1 |

**Esempi:**
```
# Voto normale superato (25 >= 18 → passed=true)
POST http://localhost:8080/api/voti?studentId=1&courseId=1&score=25&comments=Ottimo lavoro

# Voto insufficiente (15 < 18 → passed=false)
POST http://localhost:8080/api/voti?studentId=1&courseId=2&score=15

# 30 e lode (menzione speciale)
POST http://localhost:8080/api/voti?studentId=1&courseId=3&score=30&cumLaude=true

# Media del corso 1
GET http://localhost:8080/api/voti/corso/1/media
→ { "corsoId": 1, "media": 24.5, "votoMinimo": 18, "votoMassimo": 30 }
```

---

### 5️⃣ SERVIZIO RAPPORTI — Feign verso tutti · Nessun DB

> 🔗 Il Servizio Rapporti chiama TUTTI gli altri servizi. Non ha un proprio database.

| Azione | Metodo | URL |
|---|---|---|
| Rapporto globale università | GET | http://localhost:8080/api/rapporti/globale |
| Rapporto completo studente | GET | http://localhost:8080/api/rapporti/studente/1 |
| Rapporto completo corso | GET | http://localhost:8080/api/rapporti/corso/1 |

**Esempio risposta — Rapporto globale:**
```json
{
  "tipoRapporto": "GLOBALE",
  "totaleStudenti": 10,
  "totaleCorsi": 5,
  "totaleIscrizioni": 23,
  "totaleVoti": 18
}
```

---

## 🔗 Comunicazione inter-servizi — Riepilogo Feign

```
Servizio Studenti    ←── chiamato da Iscrizioni, Voti, Rapporti
Servizio Corsi       ←── chiamato da Iscrizioni, Voti, Rapporti
Servizio Iscrizioni  ←── chiamato da Voti (verifica iscrizione), Rapporti
Servizio Voti        ←── chiamato da Rapporti

Iscrizioni ──► Studenti  (verifica che lo studente esista)
Iscrizioni ──► Corsi     (verifica che il corso esista)
Voti       ──► Studenti  (verifica che lo studente esista)
Voti       ──► Corsi     (verifica che il corso esista)
Rapporti   ──► Studenti  (recupera tutti gli studenti)
Rapporti   ──► Corsi     (recupera tutti i corsi)
Rapporti   ──► Iscrizioni (recupera tutte le iscrizioni)
Rapporti   ──► Voti      (recupera tutti i voti)
```

---

## 🌍 Esporre il progetto a distanza con ngrok

```bash
# Avviare ngrok sulla porta del Gateway
.\ngrok http 8080

# Viene generato un URL pubblico, esempio:
# https://abc123.ngrok-free.app

# Il tuo amico testa da qualsiasi posto:
GET  https://abc123.ngrok-free.app/api/studenti
POST https://abc123.ngrok-free.app/api/iscrizioni?studentId=1&courseId=1

# ⚠️ Aggiungere questo header in Postman per evitare la pagina di avviso ngrok:
# ngrok-skip-browser-warning: true
```

---

## ☁️ Distribuzione su Google Cloud GKE

```bash
# Script automatico
chmod +x deploy-gcloud.sh
./deploy-gcloud.sh ID-PROGETTO

# Oppure manualmente:
gcloud auth login
gcloud container clusters create cluster-universita --num-nodes=2 --zone=us-central1-a
gcloud container clusters get-credentials cluster-universita --zone=us-central1-a

# Build + push immagini Docker
docker build -t gcr.io/ID-PROGETTO/student-service:1.0    ./student-service
docker build -t gcr.io/ID-PROGETTO/course-service:1.0     ./course-service
docker build -t gcr.io/ID-PROGETTO/enrollment-service:1.0 ./enrollment-service
docker build -t gcr.io/ID-PROGETTO/grade-service:1.0      ./grade-service
docker build -t gcr.io/ID-PROGETTO/report-service:1.0     ./report-service
docker build -t gcr.io/ID-PROGETTO/api-gateway:1.0        ./api-gateway

docker push gcr.io/ID-PROGETTO/student-service:1.0
docker push gcr.io/ID-PROGETTO/course-service:1.0
docker push gcr.io/ID-PROGETTO/enrollment-service:1.0
docker push gcr.io/ID-PROGETTO/grade-service:1.0
docker push gcr.io/ID-PROGETTO/report-service:1.0
docker push gcr.io/ID-PROGETTO/api-gateway:1.0

sed -i "s/TON-PROJECT-ID/ID-PROGETTO-REALE/g" k8s/*.yml
kubectl apply -f k8s/
kubectl get services   # → IP pubblica nella colonna EXTERNAL-IP
```

**Comandi kubectl utili:**
```bash
kubectl get pods                            # Stato dei pod
kubectl get services                        # Servizi e IP
kubectl logs -l app=enrollment-service      # Log del Servizio Iscrizioni
kubectl logs -l app=grade-service           # Log del Servizio Voti
kubectl describe pod <nome-del-pod>         # Dettagli di un pod
kubectl delete -f k8s/                      # Eliminare il deployment
```

---

## 🗄️ Database PostgreSQL

| Servizio | Contenitore | Porta locale | Nome DB |
|---|---|---|---|
| Studenti | postgres-studenti | 5432 | studentidb |
| Iscrizioni | postgres-iscrizioni | 5433 | iscrizionidb |
| Corsi | postgres-corsi | 5434 | corsidb |
| Voti | postgres-voti | 5435 | votidb |
| Rapporti | — | — | Nessuno (solo Feign) |

---

## 📐 Modelli dei dati

### Studente
```json
{ "id":1,"firstName":"Marco","lastName":"Rossi","email":"marco@universita.it","dateOfBirth":"2000-03-15","phoneNumber":"3331234567","status":"ATTIVO" }
```

### Corso
```json
{ "id":1,"name":"Matematica","description":"Algebra","professor":"Prof. Ferrari","credits":6,"maxStudents":30,"status":"DISPONIBILE" }
```

### Iscrizione
```json
{ "id":1,"studentId":1,"courseId":1,"enrolledAt":"2024-03-15T10:30:00","status":"ATTIVA" }
```

### Voto (sistema italiano)
```json
{ "id":1,"studentId":1,"courseId":1,"score":28,"passed":true,"cumLaude":false,"comments":"Ottimo lavoro","gradedAt":"2024-06-15T14:00:00" }
```

---

## 🧪 Scenario di test completo — ordine da seguire

```bash
# 1. Creare 2 studenti
POST /api/studenti  → {"firstName":"Marco","lastName":"Rossi","email":"marco@universita.it","dateOfBirth":"2000-03-15","phoneNumber":"3331111111"}
POST /api/studenti  → {"firstName":"Sofia","lastName":"Bianchi","email":"sofia@universita.it","dateOfBirth":"2001-07-22","phoneNumber":"3332222222"}

# 2. Creare 3 corsi
POST /api/corsi → {"name":"Matematica","description":"Algebra","professor":"Prof. Ferrari","credits":6,"maxStudents":30}
POST /api/corsi → {"name":"Fisica","description":"Meccanica","professor":"Prof. Russo","credits":6,"maxStudents":25}
POST /api/corsi → {"name":"Informatica","description":"Algoritmi","professor":"Prof. Conti","credits":9,"maxStudents":20}

# 3. Iscrivere gli studenti (Feign verifica Studenti + Corsi)
POST /api/iscrizioni?studentId=1&courseId=1
POST /api/iscrizioni?studentId=1&courseId=2
POST /api/iscrizioni?studentId=2&courseId=1
POST /api/iscrizioni?studentId=2&courseId=3

# 4. Assegnare i voti (Feign verifica Studenti + Corsi)
POST /api/voti?studentId=1&courseId=1&score=28&comments=Eccellente
POST /api/voti?studentId=1&courseId=2&score=15&comments=Insufficiente
POST /api/voti?studentId=2&courseId=1&score=30&cumLaude=true
POST /api/voti?studentId=2&courseId=3&score=22

# 5. Consultare i rapporti (Feign chiama TUTTI i servizi)
GET /api/rapporti/globale           → statistiche globali
GET /api/rapporti/studente/1        → tutto su Marco
GET /api/rapporti/corso/1           → tutto su Matematica
GET /api/voti/corso/1/media         → media del corso 1
```

---

## 📦 Dipendenze principali (pom.xml)

| Dipendenza | Iscrizioni | Voti | Rapporti | Gateway |
|---|---|---|---|---|
| spring-boot-starter-web | ✅ | ✅ | ✅ | ❌ |
| spring-boot-starter-data-jpa | ✅ | ✅ | ❌ | ❌ |
| spring-cloud-starter-openfeign | ✅ | ✅ | ✅ | ❌ |
| spring-cloud-starter-gateway | ❌ | ❌ | ❌ | ✅ |
| springdoc-openapi-starter-webmvc-ui | ✅ | ✅ | ✅ | ❌ |
| springdoc-openapi-starter-webflux-ui | ❌ | ❌ | ❌ | ✅ |
| postgresql | ✅ | ✅ | ❌ | ❌ |
