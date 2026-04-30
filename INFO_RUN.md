# Proyecto_Desarrollo3

## Desarrolladores:
- Daniel Gomez Cano
- Edward Stivens Pinto Granados
- Santiago Avalo Monsalve
- Moises Uriel Medina Villa

## Requisitos previos
 
| Herramienta    | Versión mínima |
|----------------|----------------|
| Docker         | 24+            |
| Docker Compose | v2 (integrado) |
 
---
 
## Primer arranque (una sola vez)
 
```bash
# 1. Clonar / posicionarse en la raíz del proyecto
cd Proyecto_Desarrollo3/
 
# 2. Crear el archivo de entorno a partir del ejemplo
cp .env.example .env
 
# 3. Editar .env y cambiar las contraseñas
#    (KC_DB_PASSWORD y KC_ADMIN_PASSWORD como mínimo)
nano .env   # o el editor de tu preferencia
 
# 4. Crear el directorio de importación de realms
mkdir -p keycloak/realms
 
# 5. Levantar todo el stack
docker compose --env-file .env up -d
```
 
---
 
## Arranque diario del equipo
 
```bash
docker compose --env-file .env up -d
```
 
---
 
## Verificar que está listo
 
```bash
# Ver estado de los contenedores
docker compose ps
 
# Esperar a que Keycloak reporte "healthy"
docker compose --env-file .env up -d --wait
 
# Acceder a la consola de administración
start http://localhost:8080   # o el KC_HTTP_PORT que hayas configurado
#open, xdg-open para macos, linux
```
 
Iniciar sesión con las credenciales definidas en `.env` (`KC_ADMIN_USER` / `KC_ADMIN_PASSWORD`).
Una vez iniciada sesión arriba a la derecha cambian el `master` por `viva-eventos` que es el realm de trabajo.

---



### Roles del sistema

### ROLE_ADMIN
Control total del sistema.

### ROLE_EVENT_CREATOR
Puede crear y administrar sus propios eventos, incluyendo precios y promociones.

### ROLE_CLIENT
Puede visualizar eventos y comprar boletas.
 
**Ejemplos de uso en código:**

```java

@PreAuthorize("hasRole('ADMIN')")
public void eliminarUsuario() {}

@PreAuthorize("hasRole('EVENT_CREATOR')")
public void crearEvento() {}

@PreAuthorize("hasRole('CLIENT')")
public void comprarBoleta() {}

```