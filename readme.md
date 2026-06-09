# API-VIBRA

## 📌 Descripción

**API-VIBRA** es una API REST desarrollada para la gestión de asistencia y administración de usuarios. El sistema permite registrar usuarios, generar tarjetas digitales personalizadas, enviar dichas tarjetas por correo electrónico y controlar la asistencia mediante códigos QR.

La aplicación está diseñada siguiendo buenas prácticas de desarrollo backend, ofreciendo una arquitectura escalable, mantenible y preparada para integrarse con aplicaciones web y móviles.

---

## 🚀 Funcionalidades

### 👤 Gestión de Usuarios

* Registro de usuarios.
* Consulta de usuarios.
* Actualización de información.
* Eliminación de registros.

### 🎫 Tarjetas Digitales

* Generación automática de tarjetas digitales.
* Asociación de tarjeta con cada usuario.
* Descarga y visualización de tarjetas.

### 📧 Envío de Correos Electrónicos

* Envío automático de tarjetas digitales por email.
* Notificaciones personalizadas.

### ✅ Control de Asistencia

* Generación de códigos QR únicos.
* Registro de asistencia mediante escaneo QR.
* Consulta de historial de asistencias.

---

## 🛠️ Tecnologías Utilizadas

* Java 21
* Spring Boot 3.x
* Spring Data JPA
* Spring Web
* Spring Mail
* MySQL
* Maven
* Docker
* Swagger / OpenAPI

---

## 📚 Documentación de la API

La documentación interactiva se encuentra disponible mediante Swagger UI:

```bash
http://localhost:8080/swagger-ui/index.html
```

Documentación OpenAPI:

```bash
http://localhost:8080/v3/api-docs
```

---

## 🗄️ Base de Datos

El sistema utiliza **MySQL** como motor de base de datos.

La base de datos debe estar previamente creada y ejecutándose dentro de un contenedor Docker.

### Ejemplo de ejecución de MySQL con Docker

```bash
docker run -d \
--name mysql-vibra \
-e MYSQL_ROOT_PASSWORD=root \
-e MYSQL_DATABASE=vibra_db \
-p 3306:3306 \
mysql:8.0
```

---

## ⚙️ Configuración

Configurar el archivo `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vibra_db
    username: root
    password: root

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

---

## ▶️ Ejecución del Proyecto

Clonar el repositorio:

```bash
git clone https://github.com/usuario/api-vibra.git
```

Ingresar al proyecto:

```bash
cd api-vibra
```

Compilar:

```bash
mvn clean install
```

Ejecutar:

```bash
mvn spring-boot:run
```

---

## 📂 Arquitectura

```text
src
├── controller
├── service
├── repository
├── entity
├── dto
├── mapper
├── exception
├── config
└── util
```

---

## 🔒 Características

* Arquitectura RESTful.
* Validación de datos.
* Manejo global de excepciones.
* Documentación automática con Swagger.
* Integración con MySQL.
* Envío de correos electrónicos.
* Generación de códigos QR.
* Generación de tarjetas digitales.

---

## 👨‍💻 Autor

Desarrollado por ing.Armando & ing.Sofi
