package com.coderfy.taskflow;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    // Temporalmente cree una objeto
    private final List<Task> tasks = new ArrayList<>();

    @GetMapping
    public List<Task> listar() {
        return tasks;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task crear(@RequestBody Task task){
        task.setId((long) tasks.size() + 1);
        tasks.add(task);
        return task;
    }
}
