package com.coderfy.taskflow;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> listar() {
        return repository.findAll();
    }

    public Task buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));
    }

    public Task crear(String titulo) {
        validarTitulo(titulo);
        return repository.save(new Task(titulo));
    }

    public Task actualizar(Long id, String titulo) {
        Task task = buscar(id);
        validarTitulo(titulo);
        task.setTitulo(titulo);
        return repository.save(task);
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio");
        }
    }

    public void eliminar(Long id) {
        Task task = buscar(id);
        repository.delete(task);
    }
}