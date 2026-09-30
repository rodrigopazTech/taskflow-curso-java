package com.coderfy.taskflow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {

    // 1. Atributos
    public Long id;
    public String titulo;
    public String estado = "PENDIENTE";

    // Metodo constructor
    // Metodo setter -> Establecer
    // Metodos getter -> Obtener
    public Task() {
    }
}
