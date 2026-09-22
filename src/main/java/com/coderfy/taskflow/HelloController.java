package com.coderfy.taskflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // Etiqueta que indica a Spring que esta clase atiende solicitudes HTTP
@RequestMapping("/api") // Prefijo común para las rutas de nuesta API
public class HelloController {
    @GetMapping("/hello")  //GET es la operación HTTP para consultar informacion
    public Map<String, String> saludar(){
        return Map.of("mensaje","Taskflow API esta funcionando");
    }
    @GetMapping("/profile")
    public Map<String,String> profile(){
        return Map.of("nombre","Taskflow API esta funcionando",
                "curso","Taskflow API esta funcionando",
                "mensaje","Taskflow API esta funcionando");
    }
}
