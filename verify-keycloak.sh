#!/usr/bin/env bash
# verify-keycloak.sh
# Verifica que el stack de Keycloak esté corriendo correctamente.
# Uso: ./verify-keycloak.sh [puerto]

"""
Dentro de la raiz se puede ejecutar este script después de hacer el docker compose up. Sirve para confirmar que los 
4 puntos críticos están OK: contenedores corriendo, health endpoint, endpoint OIDC discovery y consola admin.
"""

#Dentro de la raiz se puede ejecutar este script después de hacer el docker compose up. Sirve para confirmar que los 
#4 puntos críticos están OK: contenedores corriendo, health endpoint, endpoint OIDC discovery y consola admin.

#También es útil para los pipelines de CI/CD, para validar que el despliegue de Keycloak fue exitoso antes de avanzar a los otros pasos

set -euo pipefail

PORT="${1:-8080}" # 8080 es por defecto, pero aquí debe de escribir el puerto que indicó en el .env (KC_HTTP_PORT) 
KC_URL="http://localhost:${PORT}"
PASS=0
FAIL=0

ok()   { echo "  ✔  $1"; ((PASS++)); }
fail() { echo "  ✖  $1"; ((FAIL++)); }
ok()   { echo "  [OK]  $1"; ((PASS+=1)); }
fail() { echo "  [FAIL]  $1"; ((FAIL+=1)); }
info() { echo "  ─  $1"; }

echo ""
echo "══════════════════════════════════════════════"
echo "  VivaEventos · Verificación Keycloak (SUB-01)"
echo "══════════════════════════════════════════════"
echo ""

# 1. Contenedores corriendo
info "Contenedores Docker"
if docker compose ps --status running 2>/dev/null | grep -q "viva_keycloak_db"; then
  ok "keycloak-db está corriendo"
else
  fail "keycloak-db NO está corriendo"
fi

if docker compose ps --status running 2>/dev/null | grep -q "viva_keycloak"; then
  ok "keycloak está corriendo"
else
  fail "keycloak NO está corriendo — intenta: docker compose logs keycloak"
fi

echo ""

# 2. Health check de Keycloak
info "Health endpoint: ${KC_URL}/health/ready"
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  --max-time 5 "${KC_URL}/health/ready" 2>/dev/null || echo "000")

if [ "$HTTP_STATUS" = "200" ]; then
  ok "Health check OK (HTTP 200)"
else
  fail "Health check falló (HTTP ${HTTP_STATUS})"
fi

# 3. OIDC discovery endpoint
info "OIDC discovery (realm master)"
OIDC_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  --max-time 5 \
  "${KC_URL}/realms/master/.well-known/openid-configuration" 2>/dev/null || echo "000")

if [ "$OIDC_STATUS" = "200" ]; then
  ok "Endpoint OIDC accesible"
else
  fail "Endpoint OIDC no responde (HTTP ${OIDC_STATUS})"
fi

# 4. Consola admin
info "Consola de administración"
ADMIN_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  --max-time 5 "${KC_URL}/admin/" 2>/dev/null || echo "000")

if [ "$ADMIN_STATUS" = "200" ] || [ "$ADMIN_STATUS" = "302" ]; then
  ok "Consola admin accesible en ${KC_URL}/admin/"
else
  fail "Consola admin no responde (HTTP ${ADMIN_STATUS})"
fi

echo ""
echo "══════════════════════════════════════════════"
printf "  Resultado: %d OK  |  %d error(es)\n" "$PASS" "$FAIL"
echo "══════════════════════════════════════════════"
echo ""

if [ "$FAIL" -gt 0 ]; then
  echo "  → Revisa los logs con: docker compose logs -f keycloak"
  echo ""
  exit 1
fi

echo "  → Consola admin: ${KC_URL}"
echo "  → Próximo paso:  SUB-02 — Crear Realm VivaEventos"
echo ""