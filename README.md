## Tecnologías usadas

tecnologías que utiliza mi API:

- **Lenguaje y Framework Principal:**
    - **Java 17**: Es la versión del lenguaje configurada para compilar el proyecto.
    - **Spring Boot (versión 3.4.1)**: Es el framework base sobre el cual está construida toda la aplicación.
    - **Spring Web**: Utilizado para crear los endpoints RESTful de la API.
    - **Spring Data JPA**: Utilizado para la persistencia de datos y el mapeo objeto-relacional (ORM).
- **Seguridad y Autenticación:**
    - **Spring Security**: Encargado de proteger las rutas y manejar los roles de usuario.
    - **JSON Web Tokens (JWT)**: Utilizado para la autenticación sin estado a través de la librería `jjwt` (versión 0.11.5).
    - **Variables de Entorno**: La firma de los tokens está protegida mediante una variable de entorno llamada `JWT_SECRET`.
- **Bases de Datos:**
    - **Oracle Database**: Base de datos principal, ejecutándose a través de Docker con la imagen.
    - **Oracle JDBC (`ojdbc11`)**: El controlador oficial de Oracle para conectar tu aplicación Java a la base de datos.
    - **H2 Database**: Una base de datos en memoria configurada para tiempo de ejecución, muy probablemente utilizada para tu perfil de pruebas (`spring.profiles.active=test`).
- **Comunicación de Microservicios:**
    - **Spring Cloud OpenFeign**: Configurado (en su versión 2024.0.0) para consumir de forma declarativa otras APIs REST o microservicios externos.
- **Documentación:**
    - **Swagger / OpenAPI 3**: Implementado a través de `springdoc-openapi-starter-webmvc-ui` (versión 2.8.4) para generar la interfaz gráfica de tu API.
    - **Rutas personalizadas**: La interfaz de Swagger está disponible en `/swagger-ui.html` y los datos crudos en `/api-docs`.
- **Herramientas Auxiliares y DevTools:**
    - **Lombok**: Utilizado para evitar escribir código repetitivo (como Getters, Setters y Constructores) mediante anotaciones.
    - **JavaFaker**: Librería (versión 1.0.2) utilizada para generar datos de prueba realistas para tu base de datos.
    - **Spring Boot Mail**: Dependencia lista para ser utilizada en el envío de correos electrónicos (como la recuperación de contraseñas).
    - **Docker Compose**: Utilizado para orquestar y levantar simultáneamente el contenedor de tu aplicación y el contenedor de tu base de datos Oracle.

## ¿Que hace mi API?

"Esta es una API RESTful robusta y segura construida con Spring Boot, diseñada para manejar el registro, la autenticación y el control de acceso de usuarios en cualquier aplicación (Web o Móvil). Funciona como un sistema centralizado de identidades, protegiendo recursos mediante JSON Web Tokens (JWT) y separando los permisos entre usuarios normales y administradores.”

Características Principales (Lo que puede hacer):

**1. Autenticación y Seguridad Avanzada (JWT)**

- Los usuarios no inician sesión guardando datos en la memoria del servidor (Stateless), sino que reciben un Token JWT cifrado.
- Implementa un sistema de **Refresh Tokens**, lo que permite mantener la sesión iniciada de forma segura por días sin pedir la contraseña a cada rato.

**2. Soporte Multiplataforma (Web y Móvil)**

- La API está preparada para servir a múltiples frentes. Tiene un inicio de sesión tradicional con correo/contraseña para páginas web, y un inicio de sesión optimizado por nombre de usuario y contraseña exclusivo para la aplicación de Android.

**3. Flujos Completos de Cuenta**

- **Registro de usuarios:** Creación de cuentas nuevas de forma segura (encriptando contraseñas con BCrypt).
- **Recuperación de contraseñas:** Generación de tokens temporales de un solo uso para cuando el usuario olvida su clave.
- **Gestión de perfil:** Los usuarios pueden actualizar sus datos y cambiar su contraseña actual.

**4. Panel de Control Administrativo**

- Sistema de roles estrictos (`USER` y `ADMIN`).
- Los administradores tienen acceso a endpoints exclusivos, como un listado completo de todos los usuarios registrados en el sistema, optimizado con **paginación de base de datos** para soportar miles de registros sin colapsar la memoria.
- Puede crear usuarios.
- modificar información relacionada al usuario.
- eliminar usuarios.

### Tipos de Usuarios (Actores)

La API divide el tráfico en tres niveles de permisos:

- **Visitante Anónimo (Rutas Públicas):** Solo puede registrarse, hacer login, y pedir correos de recuperación.
- **Usuario Autenticado (Rol `USER`):** Puede ver su propio perfil, editar sus datos, cerrar sesión en uno o todos sus dispositivos y cambiar su contraseña.

- **Usuario Comercial (Rol SELLER):** Tiene acceso a un panel de control comercial (`/seller/dashboard`), puede ver los detalles internos de su cuenta comercial y tiene permiso para consultar la lista paginada de clientes/usuarios del sistema. En la arquitectura de microservicios, este rol es el único autorizado para interactuar con el catálogo de productos.
- **Administrador (Rol `ADMIN`):** Tiene el control de ver a todos los usuarios del sistema y crear cuentas de forma manual.

## Información Técnica

- **Arquitectura :** Separación estricta entre Controladores, Lógica de Negocio (Services), Seguridad (Filtros) y Acceso a Datos (Repositorios).

<img width="1125" height="919" alt="image" src="https://github.com/user-attachments/assets/07016e74-96c4-4571-adc7-ed710b845a9a" />


## Diagrama arquitectura

<img width="1440" height="700" alt="image" src="https://github.com/user-attachments/assets/0d6da40e-25fc-43d3-96a9-f106164076b7" />

## Diagrama de Secuencia: Flujo de Inicio de Sesión (Generación del JWT)
Este diagrama explica cómo el usuario obtiene su token.
<pre>
    sequenceDiagram
    autonumber
    actor U as Usuario
    participant F as Frontend (React)
    participant G as API Gateway (:8080)
    participant A as Auth Service (:8081)
    participant DB as Oracle Cloud DB

    U->>F: Ingresa credenciales (email, password)
    F->>G: POST /auth/login {credenciales}
    G->>A: Enruta petición a /auth/login
    A->>DB: Busca usuario por email
    DB-->>A: Retorna datos y password hasheada
    A->>A: Valida password con Bcrypt
    A->>A: Genera y firma JWT con JWT_SECRET
    A-->>G: 200 OK + {token: "eyJhb..."}
    G-->>F: 200 OK + {token: "eyJhb..."}
    F->>F: Guarda token en localStorage
    F-->>U: Redirige a /admin/productos
</pre>





- **Documentación Interactiva:** Integración nativa con OpenAPI/Swagger, permitiendo probar la API desde el navegador con una interfaz gráfica automatizada.
- **Manejo Global de Errores:** En lugar de devolver errores técnicos feos de Java (Error 500), la API captura las excepciones y devuelve respuestas JSON limpias y legibles para el frontend.
- **Lista para Producción:** Conectada a una base de datos empresarial (Oracle Cloud / Docker) y preparada para ser consumida por un cliente desarrollado en React o Android.

## 

### Como probar mi Api Rest

Recursos necesarios:
los recursos técnicos para **levantar (ejecutar)** el servidor, y las herramientas necesarias para **consumir (utilizar)** los endpoints desde el lado del cliente.

### 1. Requisitos del Entorno (Para ejecutar la API)

Si eres un desarrollador que desea descargar este proyecto y correrlo en su máquina local o servidor, necesitarás los siguientes recursos instalados:

- **Entorno de Ejecución de Java:** Java Development Kit (JDK) 17 o superior.
- **Gestor de Dependencias:** Maven (aunque el proyecto incluye un ejecutable `mvnw` para facilitar su uso sin instalación global).
- **Motor de Contenedores:** Docker y Docker Compose. Son estrictamente necesarios para levantar la base de datos **Oracle Database Express Edition** de forma automatizada mediante el archivo `docker-compose.yml` incluido en el proyecto.
- **Variables de Entorno:** Un entorno configurado con la variable secreta para firmar los tokens (ej. `JWT_SECRET`), crucial para la seguridad de la aplicación.

### 2. Herramientas del Cliente (Para interactuar con la API)

Si eres un desarrollador Frontend (Web o Móvil) que solo necesita conectarse a la API para enviar o recibir datos, requieres lo siguiente:

- **Un Cliente HTTP:** Cualquier herramienta capaz de hacer peticiones web.
    - *Para pruebas:* Se recomienda Postman, Insomnia, Thunder Client, o utilizar la interfaz de **Swagger** preintegrada navegando a `http://localhost:8081/swagger-ui.html`.
    - *Para desarrollo:* Librerías estándar como `fetch` o `Axios` (en React/JavaScript) y `Retrofit` u `OkHttp` (en Android/Java/Kotlin).
- **Formato de Datos:** La capacidad de enviar y analizar objetos en formato **JSON** (`application/json`), ya que es el único formato de intercambio que acepta y devuelve la API.
- **Manejo de Cabeceras (Headers):** La aplicación cliente debe estar programada para inyectar automáticamente la cabecera `Authorization: Bearer <tu_token>` en todas las peticiones hacia rutas privadas una vez que el usuario inicie sesión.

Mi API cuenta con **6 endpoints públicos**. Estas son las rutas a las que cualquier usuario (sin estar autenticado y sin tener un token JWT) puede acceder.

Aquí tienes la explicación detallada de cada uno y ejemplos de los cuerpos JSON que debes enviar en herramientas como Postman, Thunder Client o Swagger para probarlos.

### . Iniciar Sesión (Web)

- **Endpoint:** `POST /auth/login`
- **Para qué sirve:** Es el punto de entrada tradicional. Permite a un usuario existente autenticarse y obtener sus tokens de acceso (JWT) para poder navegar por las rutas protegidas.
- **Ejemplo de cuerpo (JSON):**JSON
    
    `{
      "email": "usuario@correo.com",
      "password": "mi_password_secreto"
    }`
    

### 2. Iniciar Sesión (App Android)

- **Endpoint:** `POST /auth/login-android`
- **Para qué sirve:** Un endpoint exclusivo creado para tu aplicación móvil. A diferencia del login web, este permite a los usuarios de Android iniciar sesión utilizando su nombre de usuario en lugar de su correo electrónico.
- **Ejemplo de cuerpo (JSON):**JSON
    
    `{
      "username": "mi_usuario",
      "password": "mi_password_secreto"
    }`
    

### 3. Registrar un Nuevo Usuario

- **Endpoint:** `POST /auth/register`
- **Para qué sirve:** Permite a un visitante crear una cuenta nueva en el sistema. Al registrarse con éxito, el sistema crea el usuario en la base de datos y, por lo general, devuelve automáticamente los tokens de sesión para que el usuario no tenga que hacer login inmediatamente después.
- **Ejemplo de cuerpo (JSON):**JSON
    
    `{
      "username": "nuevo_usuario",
      "name": "Juan Pérez",
      "email": "juan@correo.com",
      "password": "password_seguro123"
    }`
    
    ### 4. Refrescar el Token de Sesión
    
    - **Endpoint:** `POST /auth/refresh`
    - **Para qué sirve:** Por seguridad, los tokens JWT (Access Tokens) suelen tener una vida muy corta (ej. 15 minutos). Cuando ese token expira, la aplicación del cliente (React o Android) envía silenciosamente el "Refresh Token" (que dura días o semanas) a este endpoint para obtener un nuevo Access Token sin obligar al usuario a escribir su contraseña de nuevo.
    - **Ejemplo de cuerpo (JSON):**JSON
        
        `{
          "refreshtoken": "a1b2c3d4-e5f6-7890-abcd-ef1234567890" 
        }`
        
        ### 5. Solicitar Recuperación de Contraseña
        
        - **Endpoint:** `POST /auth/forgot-password`
        - **Para qué sirve:** Es el primer paso cuando un usuario olvida su contraseña. Recibe el correo electrónico del usuario y, por detrás (en tu `AuthService`), genera un token temporal de un solo uso que se le envía por email junto con un enlace de recuperación.
        - **Ejemplo de cuerpo (JSON):**JSON
            
            `{
              "email": "usuario@correo.com"
            }`
            
            ### 6. Restablecer la Contraseña
            
            - **Endpoint:** `POST /auth/reset-password`
            - **Para qué sirve:** Es el segundo y último paso de la recuperación. El usuario ingresa a la aplicación con el token de seguridad que recibió en su correo y establece su nueva contraseña.
            - **Ejemplo de cuerpo (JSON):**JSON
                
                `{
                  "token": "token_recibido_por_email_12345",
                  "newPassword": "mi_nueva_password_super_segura"
                }`
                
    
    Endpoint que requieren autenticación
    
    Para acceder a estos endpoints, **es estrictamente necesario enviar un Token JWT válido** en las cabeceras (Headers) de tu petición. Si no lo envías, o si el token está expirado, la API te rechazará con un error `403 Forbidden` o `401 Unauthorized`.
    
    - **Cabecera obligatoria en todos estos endpoints:**
        - `Authorization: Bearer tu_token_largo_aqui`

Endpoint  que requieren autenticación
Mi API divide las rutas privadas en tres grandes grupos: **Las rutas generales** (para cualquier usuario logueado, para el trabajor de la tienda) y **las rutas de Administrador** (solo para la gerencia). Aquí tienes el detalle de cada una:

### Nivel 1: Endpoints para cualquier Usuario Logueado (Rol `USER`,`SELLER` o `ADMIN`)

Estos endpoints están diseñados para que el usuario gestione su propia cuenta. La API sabe de qué usuario se trata automáticamente gracias a la firma del token (no necesitas enviar tu ID).

### 1. Ver mi propio Perfil

- **Endpoint:** `GET /auth/me`
- **Para qué sirve:** Devuelve toda la información pública del usuario que está haciendo la petición (su ID, nombre, correo, fecha de creación y rol). Es ideal para pintar el "Dashboard" o la pantalla de inicio del frontend o perfil de usuario.
- **Cuerpo de la petición:** *(No lleva JSON en el body por ser método GET)*.

### 2. Actualizar mi Perfil

- **Endpoint:** `PUT /auth/profile`
- **Para qué sirve:** Permite al usuario editar sus datos personales básicos (como cambiar su nombre o actualizar su correo).
- **Ejemplo de cuerpo (JSON):**JSON
    
    `{
      "name": "Juan Carlos Pérez",
      "email": "nuevo_correo@empresa.com"
    }`
    
    ### 3. Cambiar Contraseña Interna
    
    - **Endpoint:** `POST /auth/change-password`
    - **Para qué sirve:** Diferente al "olvidé mi clave", este endpoint se usa cuando el usuario **sí sabe** su contraseña actual, está dentro de la app y quiere actualizarla por seguridad.
    - **Ejemplo de cuerpo (JSON):**JSON
        
        `{
          "currentPassword": "mi_password_viejo_123",
          "newPassword": "mi_nueva_password_segura",
          "confirmationPassword": "mi_nueva_password_segura"
        }`
        
        ### 4. Cerrar Sesión (Logout)
        
        - **Endpoint:** `POST /auth/logout`
        - **Para qué sirve:** Elimina el "Refresh Token" de la base de datos para el dispositivo actual. Esto significa que cuando el token corto expire, la aplicación ya no podrá renovarlo y obligará al usuario a poner sus credenciales otra vez.
        - **Ejemplo de cuerpo (JSON):**JSON
            
            `{
              "token": "el_refresh_token_que_te_dieron_al_hacer_login"
            }`
            
            ### 5. Cerrar Sesión en Todos los Dispositivos
            
            - **Endpoint:** `POST /auth/logout-all`
            - **Para qué sirve:** ¡Un botón de pánico excelente! Si el usuario dejó su cuenta abierta en un cibercafé o le robaron el celular, este endpoint borra absolutamente todos sus tokens de la base de datos, cerrando su sesión en todas partes simultáneamente.
            - **Cuerpo de la petición:** *(Generalmente vacío, la API identifica al usuario por el Access Token).*

### Nivel 2: Endpoints Exclusivos (Solo Rol `ADMIN`)

Si un usuario normal (`USER`) intenta entrar a estas rutas, el "la seguridad" de Spring Security lo bloqueará con un `403 Forbidden`, aunque su token sea válido.

### 

### 6. Obtener Lista de Usuarios (Paginada)

- **Endpoint:** `GET /admin/get_users?page=0&size=10`
- **Para qué sirve:** Devuelve el directorio completo de usuarios registrados en el sistema. Utiliza parámetros en la URL (Query Params) para no colapsar la base de datos y traerlos por "páginas".
    - `page`: El número de página (Empieza en 0).
    - `size`: Cuántos usuarios quieres ver por página.
- **Cuerpo de la petición:** *(No lleva JSON, los datos van en la URL)*.

### 7. Crear un Usuario Manualmente

- **Endpoint:** `POST /admin/create_user`
- **Para qué sirve:** Permite al administrador crear cuentas saltándose el proceso de registro público. Es muy útil para crear cuentas de empleados nuevos o asignar directamente el rol de `ADMIN` a otro compañero.
- **Ejemplo de cuerpo (JSON):**JSON
    
    `{
      "username": "nuevo_admin_carlos",
      "email": "carlos.admin@empresa.com",
      "password": "clave_temporal_123",
      "name": "Carlos Gerente",
      "role": "ADMIN" 
    }`
    
    ### Nivel 3: Endpoints Exclusivos (Solo Rol `SELLER`)
    
    Si un usuario normal (`USER`) intenta entrar a estas rutas, el "la seguridad" de Spring Security lo bloqueará con un `403 Forbidden`, aunque su token sea válido.
    
    ### 8. Ver Dashboard del Vendedor
    
    - **Endpoint:** `GET /seller/dashboard`
    - **Descripción:** Devuelve la información principal para inicializar la vista del vendedor. Verifica internamente que la cuenta no esté bloqueada.
    - **Cabeceras:** `Authorization: Bearer <token_del_vendedor>`
    - **Respuestas esperadas:**
        - `200 OK`: Si todo está correcto (devuelve mensaje de bienvenida).
        - `403 Forbidden`: Si el token es de Vendedor, pero la cuenta fue desactivada temporalmente (lanza la excepción `IllegalStateException`).
        
    
    ### 9. Detalles de la Cuenta Comercial
    
    - **Endpoint:** `GET /seller/account`
    - **Descripción:** Devuelve los datos de contacto y estado de verificación del vendedor logueado. No es necesario enviar el ID en la URL, la API lo lee de forma segura desde el Token JWT.
    - **Cabeceras:** `Authorization: Bearer <token_del_vendedor>`
    - **Respuestas esperadas:**
        - `200 OK`: Devuelve los detalles de la cuenta.
        
    
    ### 10. Listado de Usuarios (Paginado)
    
    - **Endpoint:** `GET /seller/get_users?page=0&size=10`
    - **Descripción:** Permite al vendedor ver una lista de usuarios registrados en el sistema, devueltos en formato de páginas para optimizar la carga y renderizado en tablas (como los DataTables en React).
    - **Parámetros de URL:**
        - `page`: El índice de la página (el primer número es 0).
        - `size`: La cantidad de usuarios que deseas traer en esa consulta.
    - **Cabeceras:** `Authorization: Bearer <token_del_vendedor>`
    - **Respuestas esperadas:**
        - `200 OK`: Devuelve un objeto JSON estructurado con la lista de usuarios y datos de paginación (`totalPages`, `totalElements`, etc.).
    
    Te recomiendo probar los endpoint en postman, es donde yo lo hago.
    
    ## Códigos de estado HTTP al probar la Api
    
    Conocer los códigos de estado HTTP es fundamental, ya que son el "idioma" con el que tu API (Backend) le dice al Frontend (React, Android) qué debe hacer a continuación (mostrar un check verde, un mensaje de error o redirigir al login).
    
    Aquí tienes la lista completa de los códigos de éxito y error que devuelve tu API, clasificados por situación:
    
    ### Códigos de Éxito (Todo salió bien)
    
    **`200 OK` (El más común)**
    
    - **Qué significa:** La petición fue recibida, entendida y procesada correctamente.
    - **Cuándo ocurre:** * Al hacer login correctamente (`/auth/login` o `/login-android`).
        - Al obtener los datos de tu perfil (`/auth/me`) o la lista de usuarios (`/admin/get_users`).
        - Al actualizar tu perfil o cambiar tu contraseña con éxito.
        
    
    **`201 Created`** 
    
    - **Qué significa:** La petición fue exitosa y, como resultado, se ha creado un nuevo recurso en la base de datos.
    - **Cuándo ocurre:** Es el código que deberías devolver al registrar un usuario (`/auth/register`) o cuando el admin crea uno manualmente (`/admin/create_user`).

### Códigos de Error del Cliente (El usuario o el Frontend se equivocó)

**`400 Bad Request` (Petición Incorrecta)**

- **Qué significa:** El servidor no procesará la petición porque el formato está mal o los datos son inválidos.
- **Cuándo ocurre:**
    - Intentas cambiar tu contraseña (`/auth/change-password`), pero la `newPassword` y la `confirmationPassword` no coinciden.
    - Envías un JSON mal formado (te faltó una coma o una llave).
    - Faltan campos obligatorios en el registro.

**`401 Unauthorized` (No Autenticado)**

- **Qué significa:** "No sé quién eres". Te falta la llave para entrar o la llave que tienes es falsa/expirada.
- **Cuándo ocurre:**
    - Intentas hacer login con una **contraseña incorrecta**.
    - Intentas acceder a rutas privadas (como `/auth/me`) pero **no enviaste el Token JWT** en la cabecera.
    - Enviaste un Token JWT, pero **ya expiró** (pasaron los 15 minutos).
    - Enviaste un Refresh Token inválido en `/auth/refresh`.

**`403 Forbidden` (Prohibido)**

- **Qué significa:** "Sé quién eres (tu token es válido), pero no tienes permiso para estar aquí".
- **Cuándo ocurre:**
    - Eres un usuario normal (Rol `USER`) e intentas acceder a `/admin/get_users`. El "cadenero" (Filtro de Spring Security) te bloquea.

**`404 Not Found` (No Encontrado)**

- **Qué significa:** El recurso que estás buscando no existe en la base de datos.
- **Cuándo ocurre:**
    - Alguien intenta hacer login con un **correo que no está registrado**.
    - Alguien usa `/auth/forgot-password` e ingresa un correo inexistente.
    - Buscas un usuario por un ID que ya fue borrado.

**`409 Conflict` (Conflicto)**

- **Qué significa:** La petición entra en conflicto con el estado actual del servidor (suele pasar con datos duplicados).
- **Cuándo ocurre:**
    - Alguien intenta registrarse (`/auth/register`) con un correo electrónico o nombre de usuario que **ya existe** en tu base de datos Oracle (violación de tu restricción `@UniqueConstraint`).

### Códigos de Error del Servidor (El Backend falló)

**`500 Internal Server Error` (Error Interno)**

- **Qué significa:** El código Java falló inesperadamente ("crasheó"). Es el error que los programadores más odiamos ver.
- **Cuándo ocurre:**
    - Se cae la conexión con la base de datos Oracle.
    - Falla el servicio de envío de correos al intentar mandar el enlace de recuperar contraseña.
    - Hay un `NullPointerException` no controlado en tu código (algo estaba vacío y tu código intentó leerlo).

### Tip para el Frontend (React / Android)

- **Si viene un `200`**: Avanza a la siguiente pantalla o muestra un mensaje verde.
- **Si viene un `400 / 404 / 409`**: Muestra un texto rojo debajo del formulario diciendo "Revisa tus datos" o "El usuario ya existe".
- **Si viene un `401` en una ruta privada**: Cierra la sesión automáticamente y manda al usuario de vuelta a la pantalla de Login (porque su token expiró).
- **Si viene un `403`**: Oculta el botón de "Panel de Administración" porque no es admin.

## Términos de Uso de la API

Al integrar o consumir los endpoints de esta API de Autenticación, el cliente (aplicación Web, Móvil o terceros) acepta las siguientes condiciones de uso:

**1. Uso Aceptable y Propósito**
Esta API está diseñada exclusivamente para proveer servicios de registro, autenticación basada en JWT y gestión de identidades. Queda estrictamente prohibido utilizar los endpoints para realizar pruebas de penetración (pentesting) no autorizadas, ataques de fuerza bruta al endpoint `/auth/login` o ingeniería inversa sobre la generación de tokens.

**2. Responsabilidad de Credenciales y Tokens**
El cliente es el único responsable de almacenar de forma segura los JSON Web Tokens (JWT) devueltos por la API.

- En aplicaciones Web, se recomienda evitar el almacenamiento de tokens en `localStorage` si no se tienen mitigaciones contra ataques XSS.
- El equipo backend no se hace responsable por el secuestro de sesiones derivado de una mala gestión del token en el lado del cliente (Frontend o Mobile).

**3. Límites de Peticiones (Rate Limiting y Fair Use)**
Para garantizar la estabilidad del servicio y de la base de datos Oracle, los clientes deben implementar un consumo razonable.

- Se prohíbe la creación automatizada masiva de usuarios (bots) fuera de entornos de prueba.
- Las consultas al endpoint paginado `/admin/get_users` deben hacerse de forma secuencial y respetando los límites de los parámetros `size` para evitar sobrecargar la memoria del servidor.

**4. Manejo de Datos Sensibles**
La API procesa Información de Identificación Personal (PII) como correos electrónicos. La API garantiza que todas las contraseñas son encriptadas unidireccionalmente utilizando el algoritmo BCrypt antes de ser persistidas en la base de datos. La API nunca devolverá contraseñas en texto plano en ninguna de sus respuestas JSON.

**5. Disponibilidad del Servicio (As-Is)**
La API se proporciona "tal cual" (As-Is). Aunque se aplican las mejores prácticas de arquitectura y manejo de excepciones, no se garantizan tiempos de actividad (SLA) ininterrumpidos. Nos reservamos el derecho de revocar el acceso (Invalidación de Refresh Tokens) a cualquier cliente que comprometa la integridad del servidor.
