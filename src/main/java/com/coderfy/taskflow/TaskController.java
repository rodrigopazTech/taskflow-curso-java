package com.coderfy.taskflow;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    // Temporalmente cree una objeto
    private final List<Task> tasks = new ArrayList<>();
    private  final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task crear(@RequestBody Task task){
        return service.crear(task.getTitulo());
    }

    @PutMapping("/{id}")
    public Task actualizar(@PathVariable Long id, @RequestBody Task taskActualizada){
        // Bucle
        for(int i = 0; i < tasks.size(); i++){
            // Condicional cada elemento
            if(tasks.get(i).getId().equals(id)){
                taskActualizada.setId(id);
                tasks.set(i,taskActualizada);
                return taskActualizada;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada");
    }
}
