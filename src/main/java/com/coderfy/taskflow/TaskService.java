package com.coderfy.taskflow;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    // Temporalmente se almacenan las tareas en memoria.
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> listar() {
        return tasks;
    }

    public Task crear(Task task) {
        task.setId((long) tasks.size() + 1);
        tasks.add(task);
        return task;
    }

    public Task obtener(Long id) {
        // orElseThrow(...) devuelve la tarea encontrada; si no existe, lanza la excepción
        // entregada. this::tareaNoEncontrada es una referencia al método de esta misma
        // clase que crea la excepción que se debe lanzar.
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(this::tareaNoEncontrada);
    }

    public Task actualizar(Long id, Task taskActualizada) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id)) {
                taskActualizada.setId(id);
                tasks.set(i, taskActualizada);
                return taskActualizada;
            }
        }
        throw tareaNoEncontrada();
    }

    public void eliminar(Long id) {
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
