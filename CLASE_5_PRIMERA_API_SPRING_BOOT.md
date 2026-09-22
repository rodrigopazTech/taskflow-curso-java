# Guion del profesor — Clase 5: Primera API con Spring Boot

**Duración planeada:** 60 minutos  
**Proyecto:** TaskFlow  
**Nivel:** introducción a Java y Spring Boot

## Objetivo de la clase

Al terminar, cada estudiante podrá iniciar una aplicación Spring Boot, crear un endpoint `GET` y comprobar desde el navegador que una API devuelve una respuesta JSON.

La evidencia mínima de logro será visitar esta URL y recibir un JSON:

```text
http://localhost:8080/api/hello
```

## Preparación antes de que lleguen los alumnos

Haz esta comprobación en tu proyecto de demostración:

```powershell
java -version
javac -version
mvn clean test
mvn spring-boot:run
```

`java` y `javac` deben indicar versión 21. Mantén abierto el proyecto y verifica que `http://localhost:8080/api/hello` responde antes de iniciar la sesión.

Ten preparados estos elementos:

- IntelliJ con el proyecto ya abierto.
- JDK 21 seleccionado como Project SDK y para Maven.
- Navegador listo para abrir `http://localhost:8080/api/hello`.
- La ventana Maven visible por si se necesita recargar dependencias.
- El archivo `HelloController.java` creado o el código disponible para escribirlo durante la demostración.

> Consejo: evita explicar Maven, HTTP, JSON y anotaciones con profundidad en esta primera clase. Hoy el objetivo es que observen el recorrido completo de una solicitud y que lo hagan funcionar.

## Idea central para repetir durante la clase

> “Hasta ahora ejecutábamos un programa que inicia, muestra algo y termina. Hoy crearemos un programa que se queda encendido, escucha solicitudes y responde datos.”

```text
Navegador (cliente) solicita /api/hello
                 ↓
Spring Boot recibe la solicitud
                 ↓
HelloController ejecuta un método Java
                 ↓
Spring Boot devuelve datos en JSON
                 ↓
El navegador muestra la respuesta
```

## Guion de clase

### 0–10 min — Cambio de modelo mental: programas que escuchan

**Di:**

> “Hasta ahora ejecutábamos un programa: empieza, hace una tarea y termina. Una API funciona distinto: inicia, se queda encendida y espera solicitudes de otros programas.”

Pregunta al grupo:

> “Cuando abren una página web, ¿quién pide la información: el navegador o el servidor?”

Guía la respuesta hacia: el navegador es un **cliente**; realiza una solicitud. El servidor recibe esa solicitud y devuelve una respuesta.

Muestra primero la imagen de apoyo **‘¿Cómo se comunica un cliente con una API REST?’**. Señala únicamente:

1. El navegador solicita `GET /api/hello`.
2. El servidor procesa la solicitud.
3. El servidor devuelve `200 OK` y JSON.

**Frase de transición:**

> “Ahora vamos a crear ese servidor con Java y Spring Boot.”

### 10–20 min — Crear el proyecto y reconocer su estructura

Desde la pantalla de bienvenida de IntelliJ:

1. Selecciona **New Project > Spring Initializr**.
2. Elige **Java 21** y **Maven**.
3. Escribe:
   - Name: `taskflow-api`
   - Group/Package: `com.coderfy.taskflow`
4. Agrega la dependencia **Spring Web**.
5. Pulsa **Create** y espera a que Maven descargue las dependencias.

Cuando el proyecto aparezca, muestra solo estos tres elementos:

```text
src/main/java       → nuestro código Java
pom.xml             → dependencias y configuración del proyecto
TaskflowApplication → punto de inicio de la aplicación
```

**Di:**

> “Nuestro código vive en `src/main/java`. Maven descarga y organiza las librerías que el proyecto necesita. No necesitamos entender Maven por dentro hoy; solo necesitamos saber que al terminar de cargar ya podemos usar Spring.”

Abre `TaskflowApplication.java` y señala, sin profundizar:

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

### 20–35 min — Crear el controlador

En el paquete `com.coderfy.taskflow`, crea una clase llamada `HelloController`.

Escribe el código en bloques cortos, explicando cada parte después de escribirla:

```java
package com.coderfy.taskflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public Map<String, String> saludar() {
        return Map.of("mensaje", "Taskflow API esta funcionando");
    }
}
```

Explicación sugerida:

| Código | Explicación breve para decir en clase |
| --- | --- |
| `@RestController` | “Le dice a Spring que esta clase atiende solicitudes web y responde datos.” |
| `@RequestMapping("/api")` | “Es el prefijo común de las direcciones de esta API.” |
| `@GetMapping("/hello")` | “Indica que cuando llegue una consulta GET a `/hello`, debe ejecutarse este método.” |
| `Map.of(...)` | “Es una forma corta de construir datos con una clave y un valor.” |
| `Map` → JSON | “Spring transforma automáticamente estos datos Java a JSON.” |

Escribe la ruta completa en el pizarrón o en una diapositiva:

```text
/api + /hello = /api/hello
```

Pregunta rápida:

> “¿Qué pasaría si escribimos `/hello` fuera de la clase o quitamos `@RestController`?”

No busques una explicación técnica completa; concluye: Spring necesita esas anotaciones para saber qué clase y qué método deben atender la ruta.

### 35–43 min — Ejecutar y hacer la primera solicitud HTTP

Ejecuta `TaskflowApplication` con el triángulo verde de IntelliJ. Espera el mensaje similar a:

```text
Started TaskflowApplication
```

Abre el navegador en:

```text
http://localhost:8080/api/hello
```

Debe mostrarse:

```json
{
  "mensaje": "Taskflow API esta funcionando"
}
```

**Di:**

> “Esto no es una página HTML; es la respuesta de nuestro backend. El navegador hizo una solicitud HTTP y nuestra API devolvió JSON.”

Muestra ahora la imagen de apoyo **‘¿Qué es JSON?’**. Conecta la respuesta real con la imagen:

- `mensaje` es la **clave**.
- `Taskflow API esta funcionando` es el **valor**.
- Las llaves `{}` indican un objeto con datos organizados.

No introduzcas arreglos, objetos anidados o serialización manual todavía.

### 43–55 min — Práctica individual: endpoint de perfil

Presenta la consigna:

> “Crea un endpoint `GET /api/profile` que responda un JSON con tu nombre. Prueba la URL en el navegador.”

Puedes dar esta estructura, pero deja que completen el contenido:

```java
@GetMapping("/profile")
public Map<String, String> profile() {
    return Map.of("nombre", "Escribe tu nombre");
}
```

La prueba esperada es:

```text
http://localhost:8080/api/profile
```

Resultado de ejemplo:

```json
{
  "nombre": "Ana"
}
```

#### Pistas progresivas

Entrega solo la siguiente pista cuando alguien la necesite:

1. “La ruta nueva va encima de un método, con `@GetMapping`.”
2. “Como la clase ya tiene `@RequestMapping("/api")`, en el método escribe solamente `/profile`.”
3. “El método puede devolver `Map<String, String>` igual que `saludar`.”
4. “Después de cambiar código, guarda y vuelve a iniciar la aplicación si no está corriendo.”

#### Revisión rápida mientras circulas

Comprueba, en este orden:

1. La clase conserva `@RestController`.
2. La clase conserva `@RequestMapping("/api")`.
3. El método tiene `@GetMapping("/profile")`.
4. La ruta visitada es `/api/profile`, no solo `/profile`.
5. La aplicación sigue encendida antes de probarla.

### 55–60 min — Cierre y comprobación de comprensión

Haz estas preguntas y pide respuestas en una frase:

1. “¿Quién realiza la solicitud?”  
   **Respuesta esperada:** el cliente, por ejemplo el navegador.
2. “¿Qué dirección atendió nuestra API?”  
   **Respuesta esperada:** `/api/hello` o `/api/profile`.
3. “¿Qué devuelve nuestro backend?”  
   **Respuesta esperada:** datos en formato JSON.
4. “¿Qué anotación identifica al controlador?”  
   **Respuesta esperada:** `@RestController`.

**Cierre para decir:**

> “Hoy conectamos una URL con código Java y devolvimos JSON. En la siguiente clase podremos organizar mejor nuestros datos y crear más rutas.”

## Problemas frecuentes y respuesta inmediata

| Situación | Qué revisar o decir |
| --- | --- |
| Maven sigue descargando | “Esperemos a que termine; Maven está descargando las librerías por primera vez.” |
| Imports en rojo, pero el código parece correcto | Abrir `View > Tool Windows > Maven` y elegir **Reload All Maven Projects**. |
| Error por versión de Java | `File > Project Structure` y seleccionar JDK 21 como Project SDK; después configurar JDK 21 en Maven. |
| El navegador no conecta | Confirmar que la aplicación está ejecutándose y que la consola muestra `Started TaskflowApplication`. |
| Error 404 | Revisar la URL completa: `http://localhost:8080/api/hello`. |
| No aparece la ruta nueva | Guardar, reiniciar la aplicación y comprobar que el método tiene `@GetMapping`. |
| El puerto 8080 está ocupado | Detener otra aplicación Spring Boot o cambiar el puerto más adelante; en esta clase, lo más simple es cerrar el proceso anterior. |

## Respuestas a dudas probables

**¿El navegador puede consumir una API?**  
Sí. Un navegador es un cliente y puede realizar solicitudes HTTP. Más adelante también podrán consumir la API desde una app móvil, una página web o Postman.

**¿GET crea información?**  
No. En esta clase usamos `GET` para consultar u obtener información. Más adelante conocerán `POST`, `PUT` y `DELETE`.

**¿JSON es Java?**  
No. JSON es un formato de texto para intercambiar datos. Spring convierte los datos Java que devolvemos en ese formato.

**¿Por qué usamos `/api`?**  
Es una convención útil para distinguir las rutas de nuestro backend. Después podremos tener rutas como `/api/tasks` o `/api/users`.

**¿Por qué no usamos `System.out.println`?**  
Porque ahora la salida importante no va a la consola: va como respuesta HTTP al cliente que hizo la solicitud.

## Plan B si el tiempo se reduce

Si la creación o descarga del proyecto toma más de lo esperado:

1. Usa tu proyecto TaskFlow ya preparado para la demostración.
2. Enseña la estructura durante 3 minutos.
3. Crea `HelloController` y prueba `/api/hello`.
4. Conserva la práctica `/api/profile` como trabajo en clase o tarea.

La meta indispensable sigue siendo que cada estudiante vea una solicitud HTTP responder JSON.

## Extensión opcional si terminan antes

Pide que agreguen una segunda clave al `Map`:

```java
return Map.of(
        "nombre", "Ana",
        "curso", "Java"
);
```

Pregunta: “¿Qué cambió en la URL?”  
Respuesta: nada; solo cambió el JSON que la API devuelve.

## Lista de cierre del profesor

- [ ] Expliqué la diferencia entre ejecutar un programa y mantener una API esperando solicitudes.
- [ ] Mostré `TaskflowApplication`, `src/main/java` y `pom.xml` sin entrar en detalles innecesarios.
- [ ] El grupo vio funcionar `GET /api/hello` en el navegador.
- [ ] Expliqué que la respuesta es JSON y señalé una clave y un valor.
- [ ] Los estudiantes intentaron `GET /api/profile`.
- [ ] Cerré con las preguntas cliente, ruta y JSON.
