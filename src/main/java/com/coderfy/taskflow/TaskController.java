package com.coderfy.taskflow;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> listar() {
        return taskService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task crear(@RequestBody Task task){
        return taskService.crear(task);
    }

    @GetMapping("/{id}")
    public Task obtener(@PathVariable Long id) {
        return taskService.obtener(id);
    }

    @PutMapping("/{id}")
    public Task actualizar(@PathVariable Long id, @RequestBody Task taskActualizada){
        return taskService.actualizar(id, taskActualizada);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        taskService.eliminar(id);
    }
}
