package com.coderfy.taskflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // Etiqueta que indica a Spring que esta clase atiende solicitudes HTTP
@RequestMapping("/api") // Prefijo para las rutas de nuestra API
public class HelloControllerPractice {
    @GetMapping("/hello") // GET es la operacion HTTP para consultar información
    public Map<String, String> saludar(){
        return Map.of("mensaje", "Taskflow API funcionando");
    }


}

// Que es una clase y que es un metodo en java? para que es return?

// String
// Integer
// Float / Double
// Map<>
/*
* {
*   "usuario":"urielox",
*   "edad":"18",
* }
* */
