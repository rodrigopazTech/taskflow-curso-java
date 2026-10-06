package com.coderfy.taskflow;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Versión previa a la arquitectura por capas.
 *
 * <p>Se conserva con fines didácticos: esta clase recibe las peticiones HTTP y,
 * al mismo tiempo, contiene la lógica para administrar las tareas. La aplicación
 * usa {@link TaskController} y {@link TaskService} como la versión por capas.</p>
 */
@RestController
@RequestMapping("/api/tasks-monolito")
public class TaskControllerMonolito {
    // Los datos y la lógica viven dentro del Controller.
    private final List<Task> tasks = new ArrayList<>();

    @GetMapping
    public List<Task> listar() {
        return tasks;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task crear(@RequestBody Task task) {
        task.setId((long) tasks.size() + 1);
        tasks.add(task);
        return task;
    }

    @GetMapping("/{id}")
    public Task obtener(@PathVariable Long id) {
        // stream() permite recorrer la lista como una secuencia de tareas. filter(...)
        // conserva solo la tarea cuyo ID coincide. findFirst() es una operación de
        // cortocircuito: toma la primera coincidencia y deja de recorrer la lista.
        // orElseThrow(...) devuelve la tarea encontrada; si no existe, lanza la excepción
        // entregada. this::tareaNoEncontrada es una referencia al método de esta misma
        // clase que crea la excepción que se debe lanzar.
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(this::tareaNoEncontrada);
    }

    @PutMapping("/{id}")
    public Task actualizar(@PathVariable Long id, @RequestBody Task taskActualizada) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id)) {
                taskActualizada.setId(id);
                tasks.set(i, taskActualizada);
                return taskActualizada;
            }
        }
        throw tareaNoEncontrada();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        boolean eliminada = tasks.removeIf(task -> task.getId().equals(id));
        if (!eliminada) {
            throw tareaNoEncontrada();
        }
    }

    private ResponseStatusException tareaNoEncontrada() {
        // ResponseStatusException es una excepción que Spring convierte en una respuesta
        // HTTP. En este caso comunica el estado 404 Not Found y su mensaje al cliente.
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada");
    }
}
