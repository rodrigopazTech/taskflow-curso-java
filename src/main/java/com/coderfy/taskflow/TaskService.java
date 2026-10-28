package com.coderfy.taskflow;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public List<Task> listar(){
        return tasks;
    }

    public Task crear(String titulo){
        if(titulo == null || titulo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El titulo es obligatorio");
        }
        Task task = new Task();
        task.setId((long) tasks.size() + 1);
        task.setTitulo(titulo);
        return task;
    }


}
