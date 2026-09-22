# Guion del profesor — Clase 5: Primera API con Spring Boot (versión ampliada)

**Duración planeada:** 60 minutos  
**Proyecto:** TaskFlow  
**Objetivo:** iniciar una aplicación Spring Boot, atender una primera solicitud HTTP `GET` y devolver datos JSON.

Esta versión conserva la práctica de la guía original, pero añade el contexto mínimo para que los estudiantes entiendan qué están construyendo y por qué se organiza así el proyecto.

## Ideas esenciales de la clase

1. Una aplicación de consola inicia, ejecuta instrucciones y termina. Una API web se mantiene encendida esperando solicitudes.
2. Una API permite que programas diferentes se comuniquen mediante reglas acordadas.
3. El navegador es un cliente; nuestra aplicación Spring Boot actúa como servidor.
4. Una solicitud HTTP tiene una ruta y un método, por ejemplo `GET /api/hello`.
5. Spring Boot descubre los controladores anotados y convierte datos Java en JSON.

## Preparación técnica antes de la clase

En una terminal nueva, desde la carpeta del proyecto:

```powershell
java -version
javac -version
mvn clean test
mvn spring-boot:run
```

`java` y `javac` deben indicar Java 21. Confirma además que esta URL responde antes de que lleguen los alumnos:

```text
http://localhost:8080/api/hello
```

Mantén disponible la ventana **Maven** de IntelliJ y un navegador.

## Vocabulario mínimo

| Término | Explicación para esta clase |
| --- | --- |
| Cliente | Programa que solicita información; hoy será el navegador. |
| Servidor | Programa que permanece disponible para recibir y responder solicitudes. |
| API | Acuerdo para que programas se comuniquen. Define qué rutas existen, qué solicitar y qué respuesta recibir. |
| API REST | Una API organizada con recursos identificados por rutas y métodos HTTP como `GET` o `POST`. |
| Solicitud HTTP | Mensaje que el cliente envía al servidor: incluye método, URL y, a veces, datos. |
| Respuesta HTTP | Mensaje que el servidor devuelve: incluye estado y datos. |
| JSON | Formato de texto con datos organizados en clave y valor. |
| Endpoint | Una dirección específica de la API, por ejemplo `GET /api/hello`. |

## Dependencia mínima

Al seleccionar **Spring Web** en Spring Initializr, se agrega la dependencia necesaria para esta clase. En Spring Boot 4 es:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

Esta dependencia aporta lo indispensable: controladores como `@RestController`, rutas como `@GetMapping`, servidor web embebido y transformación automática de objetos Java a JSON. Swagger, DevTools y Lombok no son necesarios para la primera API.

## Guion de 60 minutos

### 0–8 min — De un programa que termina a un servidor que espera

**Di:**

> “Hasta ahora ejecutábamos un programa: iniciaba, hacía una tarea y terminaba. Hoy construiremos una aplicación que se queda encendida esperando solicitudes de otros programas.”

Pregunta:

> “Cuando abren una página web, ¿quién pide la información: el navegador o el servidor?”

Conclusión: el navegador es el **cliente** y hace una solicitud; el servidor la atiende y devuelve una respuesta.

Muestra la imagen de apoyo **‘¿Cómo se comunica un cliente con una API REST?’** y señala el recorrido:

```text
Cliente / navegador  →  solicitud HTTP  →  API / servidor
Cliente / navegador  ←  respuesta HTTP  ←  API / servidor
```

### 8–14 min — Qué es una API y qué significa REST

**Di:**

> “Una API es una forma acordada para que dos programas se comuniquen. Por ejemplo, un navegador, una aplicación móvil o un sistema externo puede pedir datos a nuestro servidor.”

> “REST es una forma común de organizar esa API: usamos direcciones —rutas— y métodos HTTP para expresar qué queremos hacer.”

Presenta solo este panorama; no se requiere memorizarlo hoy:

| Método | Idea sencilla | Ejemplo futuro |
| --- | --- | --- |
| `GET` | Consultar u obtener datos | Ver tareas |
| `POST` | Crear datos | Crear una tarea |
| `PUT` | Actualizar datos | Cambiar una tarea |
| `DELETE` | Eliminar datos | Borrar una tarea |

**Aclara:**

> “Hoy usaremos `GET`. No es la única forma de atender una solicitud, pero es la más sencilla para empezar porque solo consultaremos información.”

### 14–22 min — Crear un proyecto Spring Boot sin abrumarse

En IntelliJ: **New Project > Spring Initializr**.

Elige:

- Java 21
- Maven
- Nombre: `taskflow-api`
- Paquete: `com.coderfy.taskflow`
- Dependencia: **Spring Web**

**Di mientras se crea:**

> “Spring Boot es un conjunto de herramientas que facilita crear aplicaciones web con Java. Configura muchas piezas por nosotros, trae un servidor integrado y sabe buscar clases que atienden solicitudes.”

> “Spring Initializr nos genera la estructura inicial correcta. IntelliJ la descarga y Maven organiza las librerías. No necesitamos construir todo desde cero.”

Cuando termine Maven, muestra solo:

```text
src/main/java       → nuestro código Java
pom.xml             → dependencias y configuración
TaskflowApplication → puerta de arranque
```

No expliques Maven internamente. Di únicamente:

> “Maven descarga y organiza las librerías que necesitamos.”

### 22–29 min — La clase Application y por qué no escribimos todo en `main`

Abre `TaskflowApplication.java`:

```java
@SpringBootApplication
public class TaskflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskflowApplication.class, args);
    }
}
```

**Di:**

> “Esta es la puerta de arranque. Al ejecutar esta clase, Spring Boot inicia nuestro servidor.”

Complemento importante:

> “No escribiremos cada función en `main` porque cada clase debe tener una responsabilidad clara. `TaskflowApplication` arranca la aplicación; un controlador atiende solicitudes; más adelante otras clases manejarán reglas de negocio y datos.”

> “Cuando Spring Boot inicia, busca automáticamente en este paquete y sus subpaquetes las clases con anotaciones como `@RestController`. Por eso no llamamos manualmente a `HelloController` desde `main`: Spring la detecta y la registra.”

Puedes resumirlo visualmente:

```text
main → inicia Spring Boot → Spring busca @RestController → registra las rutas
```

### 29–40 min — Crear el controlador, la ruta y la respuesta

Crea `HelloController.java` dentro del paquete `com.coderfy.taskflow`. Escribe el código por bloques. Esta es la versión didáctica, con un comentario al lado de cada import:

```java
package com.coderfy.taskflow;

import org.springframework.web.bind.annotation.GetMapping; // Permite atender solicitudes HTTP de tipo GET.
import org.springframework.web.bind.annotation.RequestMapping; // Define un prefijo común para las rutas de esta clase.
import org.springframework.web.bind.annotation.RestController; // Marca esta clase como controlador que devuelve datos HTTP.

import java.util.Map; // Representa datos organizados en pares clave-valor.

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public Map<String, String> saludar() {
        return Map.of("mensaje", "Taskflow API esta funcionando");
    }
}
```

Explica cada elemento con una frase:

| Código | Qué decir |
| --- | --- |
| `@RestController` | “Esta etiqueta indica a Spring que la clase atiende solicitudes web y devuelve datos.” |
| `@RequestMapping("/api")` | “Este es el prefijo de las rutas de esta clase.” |
| `@GetMapping("/hello")` | “Cuando llegue una solicitud GET a `/hello`, Spring ejecutará este método.” |
| `@GetMapping` | “GET es el tipo de solicitud para consultar datos; no es el único método HTTP.” |

Construye la ruta completa con el grupo:

```text
/api + /hello = /api/hello
```

### 40–46 min — `Map<String, String>`, `Map.of()` y JSON

Detente aquí antes de ejecutar. Esta explicación evita una confusión frecuente.

**Di:**

> “`Map` no es JSON. `Map` es una estructura de datos de Java que guarda pares de clave y valor. Spring Boot toma ese `Map` y lo transforma automáticamente a JSON para enviarlo por HTTP.”

Desglosa la firma:

```java
Map<String, String>
```

- `Map` significa una colección de pares **clave → valor**.
- El primer `String` indica que cada clave será texto, por ejemplo `"mensaje"`.
- El segundo `String` indica que cada valor será texto, por ejemplo `"Taskflow API esta funcionando"`.

Explica el retorno:

```java
Map.of("mensaje", "Taskflow API esta funcionando")
```

> “`Map.of()` es una forma corta de crear un `Map`. Recibe una clave y un valor. El mapa que crea no se puede modificar después, lo cual está bien para esta respuesta simple.”

Relaciona el objeto Java con la respuesta JSON:

```text
Map Java:  "mensaje" → "Taskflow API esta funcionando"
                         ↓ Spring Boot convierte la respuesta
JSON:      { "mensaje": "Taskflow API esta funcionando" }
```

Muestra la imagen de apoyo **‘¿Qué es JSON?’** y señala:

- `mensaje` es la clave.
- `Taskflow API esta funcionando` es el valor.
- Las llaves `{}` indican un objeto JSON.

### 46–50 min — Primera prueba en el navegador

Ejecuta `TaskflowApplication` con el triángulo verde. Espera un mensaje similar a:

```text
Started TaskflowApplication
```

Abre:

```text
http://localhost:8080/api/hello
```

La respuesta esperada es:

```json
{
  "mensaje": "Taskflow API esta funcionando"
}
```

**Di:**

> “El navegador hizo una solicitud `GET` a nuestra API. Spring encontró el controlador, ejecutó el método y convirtió el `Map` Java a JSON. Esa es la respuesta del backend.”

### 50–57 min — Práctica: endpoint de perfil

Consigna:

> “Crea un endpoint `GET /api/profile` que responda un JSON con tu nombre. Pruébalo en el navegador.”

Proporciona esta estructura solo si la necesitan:

```java
@GetMapping("/profile")
public Map<String, String> profile() {
    return Map.of("nombre", "Escribe tu nombre");
}
```

Prueba esperada:

```text
http://localhost:8080/api/profile
```

Resultado de ejemplo:

```json
{
  "nombre": "Ana"
}
```

Revisa en este orden:

1. La clase tiene `@RestController`.
2. La clase tiene `@RequestMapping("/api")`.
3. El método tiene `@GetMapping("/profile")`.
4. Visitan `/api/profile`, no solamente `/profile`.
5. La aplicación está corriendo.

### 57–60 min — Cierre

Pregunta:

1. “¿Quién realiza la solicitud?”  
   **Respuesta:** el cliente, por ejemplo el navegador.
2. “¿Qué tipo de solicitud usamos hoy?”  
   **Respuesta:** `GET`.
3. “¿Qué clase atiende la ruta?”  
   **Respuesta:** `HelloController`.
4. “¿Qué convierte Spring Boot a JSON?”  
   **Respuesta:** los datos Java que devolvemos, hoy un `Map`.

**Cierre para decir:**

> “Hoy conectamos una URL con código Java y devolvimos JSON. Ya tenemos una API funcionando: permanece encendida, recibe solicitudes y devuelve respuestas.”

## Solución de problemas

| Problema | Acción inmediata |
| --- | --- |
| Los imports aparecen en rojo | Abre `View > Tool Windows > Maven` y pulsa **Reload All Maven Projects**. |
| Maven no termina o falla | Confirma que hay conexión y espera la primera descarga. |
| Error de versión Java | En `File > Project Structure`, selecciona JDK 21; configura el mismo JDK para Maven. |
| El navegador no conecta | Confirma que la consola muestra `Started TaskflowApplication`. |
| Reciben 404 | Revisa la URL completa: `http://localhost:8080/api/hello`. |
| No aparece `/profile` | Guarda, reinicia la aplicación y revisa `@GetMapping("/profile")`. |
| El puerto 8080 está ocupado | Detén el proceso Spring Boot anterior con `Ctrl + C` y vuelve a iniciar. |

## Si queda tiempo

Pide que añadan una segunda clave y observen que la ruta no cambia:

```java
return Map.of(
        "nombre", "Ana",
        "curso", "Java"
);
```

La API responde más datos; sigue siendo el mismo endpoint `GET /api/profile`.

## Lista de cierre del profesor

- [ ] Expliqué API, cliente, servidor y petición HTTP con palabras simples.
- [ ] Expliqué que `GET` es un método HTTP, no la única opción.
- [ ] Presenté Spring Boot como facilitador de aplicaciones web, sin profundizar en su configuración.
- [ ] Expliqué por qué `TaskflowApplication` inicia el proyecto y Spring descubre `HelloController`.
- [ ] Diferencié claramente `Map` de JSON.
- [ ] Mostré funcionar `GET /api/hello`.
- [ ] Los alumnos intentaron crear `GET /api/profile`.
