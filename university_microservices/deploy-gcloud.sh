#!/bin/bash
# ═══════════════════════════════════════════════════════════════
# deploy-gcloud.sh — Déploiement complet sur Google Cloud GKE
# ═══════════════════════════════════════════════════════════════
# Usage : ./deploy-gcloud.sh TON-PROJECT-ID
# Exemple: ./deploy-gcloud.sh my-university-project
# ═══════════════════════════════════════════════════════════════

PROJECT_ID=$1

if [ -z "$PROJECT_ID" ]; then
  echo "❌ Erreur : tu dois passer ton Project ID en argument"
  echo "   Usage : ./deploy-gcloud.sh TON-PROJECT-ID"
  exit 1
fi

echo "🚀 Déploiement sur Google Cloud — Project: $PROJECT_ID"
echo "══════════════════════════════════════════════════════"

# ── Étape 1 : Authentification ─────────────────────────────────
echo ""
echo "📋 Étape 1 : Connexion à Google Cloud..."
gcloud auth login
gcloud config set project $PROJECT_ID

# ── Étape 2 : Créer le cluster GKE ────────────────────────────
echo ""
echo "📋 Étape 2 : Création du cluster Kubernetes..."
gcloud container clusters create university-cluster \
  --num-nodes=2 \
  --machine-type=e2-medium \
  --zone=us-central1-a

# Connecter kubectl au cluster
gcloud container clusters get-credentials university-cluster --zone=us-central1-a

# ── Étape 3 : Compiler les services Maven ─────────────────────
echo ""
echo "📋 Étape 3 : Compilation des services Java..."

for service in student-service course-service enrollment-service api-gateway; do
  echo "  → Compilation de $service..."
  cd $service
  mvn clean package -DskipTests -q
  cd ..
done

# ── Étape 4 : Build et Push des images Docker ─────────────────
echo ""
echo "📋 Étape 4 : Build et push des images Docker..."

SERVICES=("student-service" "course-service" "enrollment-service" "api-gateway")

for service in "${SERVICES[@]}"; do
  echo "  → Build image : $service"
  docker build -t gcr.io/$PROJECT_ID/$service:1.0 ./$service

  echo "  → Push image : $service"
  docker push gcr.io/$PROJECT_ID/$service:1.0
done

# ── Étape 5 : Mettre à jour les fichiers K8s avec le bon Project ID ──
echo ""
echo "📋 Étape 5 : Mise à jour des fichiers Kubernetes..."

for file in k8s/*.yml; do
  sed -i "s/TON-PROJECT-ID/$PROJECT_ID/g" $file
done

# ── Étape 6 : Déployer sur Kubernetes ─────────────────────────
echo ""
echo "📋 Étape 6 : Déploiement sur Kubernetes GKE..."
kubectl apply -f k8s/

# ── Étape 7 : Attendre que les pods démarrent ─────────────────
echo ""
echo "📋 Étape 7 : Attente du démarrage des pods..."
kubectl wait --for=condition=ready pod -l app=student-service --timeout=120s
kubectl wait --for=condition=ready pod -l app=course-service --timeout=120s
kubectl wait --for=condition=ready pod -l app=enrollment-service --timeout=120s
kubectl wait --for=condition=ready pod -l app=api-gateway --timeout=120s

# ── Étape 8 : Récupérer l'IP publique ─────────────────────────
echo ""
echo "📋 Étape 8 : Récupération de l'IP publique..."
echo "   (patiente ~2 minutes que Google Cloud assigne l'IP)"
sleep 30

EXTERNAL_IP=""
while [ -z "$EXTERNAL_IP" ]; do
  EXTERNAL_IP=$(kubectl get service api-gateway \
    -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null)
  if [ -z "$EXTERNAL_IP" ]; then
    echo "   Attente de l'IP publique..."
    sleep 15
  fi
done

# ── Résultat final ─────────────────────────────────────────────
echo ""
echo "══════════════════════════════════════════════════════"
echo "✅ DÉPLOIEMENT RÉUSSI !"
echo "══════════════════════════════════════════════════════"
echo ""
echo "🌍 IP publique Google Cloud : $EXTERNAL_IP"
echo ""
echo "📮 Endpoints Postman (remplace localhost par l'IP) :"
echo "   GET  http://$EXTERNAL_IP/api/students"
echo "   GET  http://$EXTERNAL_IP/api/courses"
echo "   POST http://$EXTERNAL_IP/api/enrollments?studentId=1&courseId=1"
echo ""
echo "🔍 Vérifier l'état des pods :"
echo "   kubectl get pods"
echo "   kubectl get services"
echo ""
echo "📄 Voir les logs d'un service :"
echo "   kubectl logs -l app=student-service"
echo "   kubectl logs -l app=enrollment-service"
