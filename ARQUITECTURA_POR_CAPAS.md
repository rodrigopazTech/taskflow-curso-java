# Arquitectura por capas con Spring Boot

## Guía de preparación para la clase

En esta clase el proyecto `TaskFlow` deja de colocar todas las responsabilidades en `TaskController` y presenta una separación básica por capas. El objetivo no es añadir más funcionalidades a la API, sino organizar mejor el código que ya existe.

Al finalizar, el flujo principal de una petición es el siguiente:

```text
Cliente HTTP -> Controller -> Service -> datos temporales en memoria
```

Más adelante, los datos dejarán de vivir en memoria y se introducirá un **Repository**, responsable de comunicarse con la fuente de datos, por ejemplo una base de datos. En esta clase solo se menciona ese siguiente paso; el foco está en Controller y Service.

---

## 1. ¿Qué es la arquitectura por capas?

La arquitectura por capas consiste en repartir las responsabilidades de una aplicación entre clases con propósitos claros. Esto evita que una sola clase crezca demasiado y permite modificar, probar y entender el proyecto con mayor facilidad.

En nuestra API intervienen dos capas principales:

| Capa | Responsabilidad principal |
| --- | --- |
| **Controller** | Recibe las solicitudes HTTP, obtiene datos de la URL o del cuerpo de la petición y devuelve la respuesta HTTP. |
| **Service** | Contiene las reglas y operaciones de la aplicación: crear, buscar, actualizar o eliminar tareas. |

Una idea útil para explicarlo en clase es: el Controller es la puerta de entrada de la API; el Service es quien realiza el trabajo solicitado.

### Lo que vive en el Controller

El controlador conoce HTTP y las rutas de la API. Por eso contiene anotaciones como `@GetMapping`, `@PostMapping` y `@PathVariable`. Su tarea es recibir la petición y delegar el trabajo.

Ejemplo: el controlador recibe `GET /api/tasks/3`, toma el `3` de la URL y llama al servicio.

```java
@GetMapping("/{id}")
public Task obtener(@PathVariable Long id) {
    return taskService.obtener(id);
}
```

El Controller no recorre la lista ni decide cómo localizar la tarea. Solo coordina la entrada y salida HTTP.

### Lo que vive en el Service

El servicio contiene la lógica de la aplicación. En el estado actual del proyecto guarda las tareas temporalmente en una `List`, asigna IDs, busca por ID, actualiza y elimina.

```java
@Service
public class TaskService {
    private final List<Task> tasks = new ArrayList<>();

    public Task crear(Task task) {
        task.setId((long) tasks.size() + 1);
        tasks.add(task);
        return task;
    }
}
```

También es el Service quien decide qué sucede si se intenta actualizar una tarea que no existe:

```java
throw tareaNoEncontrada();
```

Separar esta lógica evita repetirla si en el futuro existen otros controladores o entradas para la misma funcionalidad.

---

## 2. El `TaskService` y la inyección de dependencias

### `@Service`: registrar la clase como servicio

La anotación `@Service` indica a Spring que `TaskService` es una clase de servicio. Al iniciar la aplicación, Spring crea y administra una instancia de esa clase.

```java
@Service
public class TaskService {
    // ...
}
```

Esto permite usar el servicio desde el controlador sin escribir `new TaskService()` manualmente. Esta forma de trabajo se llama **inyección de dependencias**.

### ¿Qué hace la línea 10 de `TaskController`?

La línea 10 actual es:

```java
private final TaskService taskService;
```

Esta línea **declara una variable de instancia** llamada `taskService`. No crea el objeto por sí sola, porque no tiene `new`. Su función es reservar una referencia para que el Controller pueda utilizar el servicio.

- `private`: solo se utiliza dentro de `TaskController`.
- `final`: la referencia se asigna una vez y no puede cambiar a otro servicio después.
- `TaskService`: es el tipo de objeto que el controlador necesita.
- `taskService`: es el nombre con el que se usará dentro de la clase.

La instancia real la crea Spring gracias a `@Service` y la entrega al Controller mediante el constructor.

### ¿Por qué se necesita un constructor que reciba un Service?

El constructor es el punto donde se asigna el servicio al atributo declarado en la línea 10:

```java
public TaskController(TaskService taskService) {
    this.taskService = taskService;
}
```

Cuando Spring necesita crear `TaskController`, observa que su constructor solicita un `TaskService`. Como existe una instancia administrada por Spring, la proporciona automáticamente.

`this.taskService` se refiere al atributo de la clase; `taskService` sin `this` es el parámetro que llegó al constructor.

```text
Spring crea TaskService
        ↓
Spring crea TaskController y le entrega ese TaskService
        ↓
El Controller puede delegar: taskService.listar()
```

Esta forma es preferible a crear el servicio dentro del Controller porque hace explícita la dependencia, permite reutilizar el servicio y facilita las pruebas.

---

## 3. Rutas y anotaciones REST utilizadas

REST es un estilo para diseñar APIs que usan HTTP. En Spring, las anotaciones indican qué clase o método responde a cada tipo de petición.

| Anotación | Uso en el proyecto |
| --- | --- |
| `@RestController` | Declara que la clase es un controlador REST. Sus métodos devuelven datos —por ejemplo, JSON— como respuesta HTTP. |
| `@RequestMapping("/api/tasks")` | Define la ruta base compartida por los métodos del controlador. |
| `@GetMapping` | Atiende solicitudes HTTP `GET`; se usa para consultar tareas. |
| `@PostMapping` | Atiende solicitudes HTTP `POST`; se usa para crear una tarea. |
| `@ResponseStatus(HttpStatus.CREATED)` | Define el código HTTP que se devolverá si el método termina correctamente. |
| `@RequestBody` | Convierte el cuerpo JSON de la petición en un objeto Java, por ejemplo `Task`. |
| `@PathVariable` | Obtiene un valor variable escrito dentro de la ruta, como el ID. |
| `@PutMapping` | Atiende solicitudes HTTP `PUT`; se usa para reemplazar o actualizar una tarea existente. |
| `@DeleteMapping` | Atiende solicitudes HTTP `DELETE`; se usa para eliminar una tarea. |

### Rutas resultantes

Como la clase tiene `@RequestMapping("/api/tasks")`, cada método agrega su propia parte de ruta a esa base:

| Petición | Ruta | Acción |
| --- | --- | --- |
| `GET` | `/api/tasks` | Lista todas las tareas. |
| `POST` | `/api/tasks` | Crea una tarea a partir de un JSON. |
| `GET` | `/api/tasks/{id}` | Obtiene una tarea por su ID. |
| `PUT` | `/api/tasks/{id}` | Actualiza una tarea por su ID. |
| `DELETE` | `/api/tasks/{id}` | Elimina una tarea por su ID. |

---

## 4. `HttpStatus.CREATED`: crear recursos correctamente

`HttpStatus.CREATED` representa el código HTTP **201 Created**. Indica que la petición fue exitosa y que se creó un nuevo recurso.

En el proyecto se aplica al método de creación:

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Task crear(@RequestBody Task task) {
    return taskService.crear(task);
}
```

Por lo tanto, cuando un cliente envía correctamente un `POST /api/tasks`, recibe:

- código de estado `201 Created`;
- la tarea creada en el cuerpo de la respuesta, incluido el ID generado.

Sin `@ResponseStatus(HttpStatus.CREATED)`, Spring normalmente respondería con `200 OK`. Ambas respuestas indican éxito, pero `201 Created` comunica con mayor precisión que se creó un recurso nuevo.

---

## 5. Variables en la ruta y otros datos de una petición

### ¿Cómo se llama `/{id}`?

`/{id}` se llama **variable de ruta** o **path variable**. Es una parte dinámica de la URL.

```java
@GetMapping("/{id}")
public Task obtener(@PathVariable Long id) {
    return taskService.obtener(id);
}
```

Si el cliente realiza esta solicitud:

```text
GET /api/tasks/7
```

Spring toma el valor `7` de la ruta y lo asigna al parámetro `Long id` gracias a `@PathVariable`.

Se pueden tener varias variables de ruta cuando representan partes importantes del recurso. Por ejemplo:

```java
@GetMapping("/usuarios/{usuarioId}/tasks/{id}")
public Task obtenerDeUsuario(
        @PathVariable Long usuarioId,
        @PathVariable Long id) {
    // ...
}
```

### ¿Qué otros datos pueden acompañar una petición?

Además de las variables de ruta, una petición puede incluir:

| Dato | Ejemplo | Forma habitual de recibirlo en Spring |
| --- | --- | --- |
| Cuerpo de la petición | JSON con `titulo` y `estado` al crear una tarea. | `@RequestBody` |
| Parámetros de consulta | `/api/tasks?estado=PENDIENTE` | `@RequestParam` |
| Encabezados HTTP | `Authorization: Bearer ...` | `@RequestHeader` |
| Cookie | Identificador de sesión enviado por el navegador. | `@CookieValue` |

Para este controlador ya se usan dos formas:

- `@RequestBody Task task`: datos completos de la tarea en el cuerpo JSON para `POST` y `PUT`.
- `@PathVariable Long id`: ID incluido en la URL para `GET`, `PUT` y `DELETE`.

---

## 6. Idea de cierre para la clase

El cambio importante no fue modificar las rutas de la API, sino cambiar la responsabilidad de cada clase:

```text
TaskController: HTTP y rutas
TaskService: operaciones y reglas de las tareas
```

Esta primera separación prepara al proyecto para crecer. Cuando se agregue persistencia, el Service podrá colaborar con un Repository sin convertir al Controller en una clase con demasiadas responsabilidades.
