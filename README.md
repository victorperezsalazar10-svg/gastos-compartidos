# 💸 Gastos Compartidos

Aplicación web para gestionar y dividir gastos entre grupos de personas. Permite registrar grupos, agregar miembros, registrar gastos con distintos tipos de división y calcular automáticamente las liquidaciones necesarias para saldar deudas.

---

## Tecnologías utilizadas

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.15 |
| PostgreSQL | 17 |
| Springdoc OpenAPI (Swagger) | 2.8.8 |
| Maven | 3.9+ (o Wrapper incluido) |

---

## Requisitos previos

- Java 21 o superior
- Maven 3.x (o usar el wrapper `./mvnw` incluido)
- PostgreSQL instalado y corriendo

---

## Configuración

Editar el archivo `src/main/resources/application.properties` con los datos de tu base de datos:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gastos_compartidos_db
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8081
```

Asegurarse de que la base de datos `gastos_compartidos_db` esté creada en PostgreSQL antes de iniciar la aplicación.

---

## Cómo ejecutar

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd gastos-compartidos

# Compilar y ejecutar
./mvnw spring-boot:run
```

La aplicación quedará disponible en: **http://localhost:8081**

---

## Datos de ejemplo

Al iniciar la aplicación por primera vez (con la base de datos vacía), se cargan automáticamente datos de ejemplo:

- **Usuarios:** Juan, Ana, Pedro
- **Grupos:** "Viaje a la Playa" y "Departamento"
- **Gastos:** Hotel, Comida, Cena, Gasolina, Snacks, Internet, Luz, Agua, Limpieza, Supermercado — todos divididos en partes iguales

Si ya hay datos en la base de datos, la carga de ejemplos se omite.

---

## Modelo de datos

```
Usuario
  └── pertenece a muchos Grupos (via MiembroGrupo)

Grupo
  ├── tiene un Creador (Usuario)
  ├── tiene Miembros (MiembroGrupo)
  └── tiene Gastos

Gasto
  ├── tiene un Pagador (Usuario)
  ├── pertenece a un Grupo
  ├── tiene un TipoDivision (IGUAL | PORCENTUAL | PERSONALIZADA)
  └── tiene DetallesGasto (monto asignado por usuario)

Liquidacion
  ├── tiene un Deudor (Usuario)
  ├── tiene un Acreedor (Usuario)
  └── pertenece a un Grupo
```

---

## Tipos de división de gastos

| Tipo | Descripción |
|---|---|
| `IGUAL` | El monto se divide en partes iguales entre todos los miembros del grupo |
| `PORCENTUAL` | Cada miembro paga un porcentaje del total (debe sumar 100%) |
| `PERSONALIZADA` | Se asigna un monto fijo a cada participante (debe sumar el total) |

---

## Cálculo de liquidaciones

Al registrar o eliminar un gasto, el sistema recalcula automáticamente las liquidaciones del grupo. El algoritmo:

1. Suma lo que cada usuario **pagó** vs. lo que **consumió**.
2. Clasifica a los usuarios en **acreedores** (pagaron más de lo que consumieron) y **deudores** (consumieron más de lo que pagaron).
3. Genera las transferencias mínimas necesarias para saldar todas las deudas.

---

## Interfaz web

La aplicación incluye una interfaz web con Thymeleaf accesible desde el navegador:

| URL | Vista |
|---|---|
| `http://localhost:8081/` | Página de inicio |
| `http://localhost:8081/grupos-page` | Gestión de grupos |
| `http://localhost:8081/gastos-page` | Gestión de gastos |

---

## API REST

La API REST completa está disponible en las siguientes rutas:

### Usuarios — `/usuarios`
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/usuarios` | Listar todos los usuarios |
| GET | `/usuarios/{id}` | Buscar usuario por ID |
| GET | `/usuarios/buscar-email?email=` | Buscar usuario por email |
| POST | `/usuarios` | Crear usuario |
| PUT | `/usuarios/{id}` | Actualizar usuario |
| DELETE | `/usuarios/{id}` | Eliminar usuario |

### Grupos — `/grupos`
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/grupos` | Listar todos los grupos |
| GET | `/grupos/{id}` | Buscar grupo por ID |
| GET | `/grupos/{id}/resumen` | Obtener resumen financiero del grupo |
| GET | `/grupos/{id}/ranking` | Ranking de gastos por usuario |
| POST | `/grupos` | Crear grupo |
| PUT | `/grupos/{id}` | Actualizar grupo |
| DELETE | `/grupos/{id}` | Eliminar grupo |

### Miembros de grupo — `/miembros-grupo`
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/miembros-grupo` | Listar todos los miembros |
| POST | `/miembros-grupo` | Agregar miembro manualmente |
| POST | `/miembros-grupo/unirse` | Unirse a un grupo por código de invitación |
| DELETE | `/miembros-grupo/grupo/{grupoId}/usuario/{usuarioId}` | Salir de un grupo |

### Gastos — `/gastos`
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/gastos` | Listar todos los gastos |
| GET | `/gastos/{id}` | Buscar gasto por ID |
| GET | `/gastos/grupo/{grupoId}` | Listar gastos de un grupo |
| GET | `/gastos/{id}/detalles` | Ver detalles de división de un gasto |
| GET | `/gastos/grupo/{grupoId}/exportar` | Exportar gastos del grupo como CSV |
| POST | `/gastos` | Registrar nuevo gasto |
| DELETE | `/gastos/{id}` | Eliminar gasto |

### Liquidaciones — `/liquidaciones`
| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/liquidaciones/grupo/{grupoId}/calcular` | Calcular liquidaciones de un grupo |
| GET | `/liquidaciones/grupo/{grupoId}/historial` | Ver liquidaciones de un grupo |

### Detalle de gastos — `/detalle-gastos`
| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/detalle-gastos` | Listar todos los detalles de gastos |

---

## Documentación interactiva (Swagger)

La documentación de la API está disponible en:

```
http://localhost:8081/swagger-ui/index.html
```

---

## Tests

El proyecto incluye tests unitarios para los servicios principales:

```bash
./mvnw test
```

Clases de test incluidas:
- `UsuarioServiceTest`
- `GrupoServiceTest`
- `GastoServiceTest`
- `LiquidacionServiceTest`

---

## Estructura del proyecto

```
src/
├── main/
│   ├── java/com/gastos/gastos_compartidos/
│   │   ├── conf/           # DataLoader (datos de ejemplo)
│   │   ├── controller/     # Controladores REST y web
│   │   ├── dto/            # Objetos de transferencia de datos
│   │   ├── entity/         # Entidades JPA
│   │   ├── enums/          # TipoDivision
│   │   ├── exception/      # Manejo global de errores
│   │   ├── repository/     # Repositorios Spring Data
│   │   └── service/        # Lógica de negocio
│   └── resources/
│       ├── application.properties
│       └── templates/      # Vistas Thymeleaf
└── test/                   # Tests unitarios
```
