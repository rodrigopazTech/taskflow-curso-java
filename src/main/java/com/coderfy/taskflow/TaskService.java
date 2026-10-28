package com.coderfy.taskflow;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;
    //private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> listar(){
        return repository.findAll();
        //return tasks;
    }

    public Task crear(String titulo){
        validarTitulo(titulo);
        return repository.save(new Task(titulo));
        /*Task task = new Task();
        task.setId((long) tasks.size() + 1);
        task.setTitulo(titulo);
        return task;*/
    }

    public void validarTitulo(String titulo){
        if(titulo == null || titulo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El titulo es obligatorio");
        }
    }

    // Nuevos metodos
    public Task buscar(Long id){
        return  repository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));
    }

    public Task actualizar(Long id, String titulo){
        Task task = buscar(id);
        validarTitulo(titulo);
        task.setTitulo(titulo);
        return repository.save(task);
    }
}
