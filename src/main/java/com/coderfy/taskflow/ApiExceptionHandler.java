package com.coderfy.taskflow;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Esta clase contiene reglas globales para manejar respuestas y excepciones de los controladores REST
// ControllerAdvice : Permite que una clase participe en el manjejo global de todos los controladores
// ResponseBody : Convierte automaticamente lo que devuelvas a JSON
@RestControllerAdvice
public class ApiExceptionHandler {

    /*
    * 1. Spring intenta ejecutar el endpint solicitado
    * 2. ApiExceptionHandler entra en accion solamente si durante ese proceso se lanza unaexcepcion que sabe manejar
    * */

    // Esta anotacion registra el metodo validacion como el encargado de una
    // clase especifica de error "MethodArgumentNotValidException
    // Cuando spring detecta esa excepcion , ejecuta ese meotdo y le entrega la excepcion
    // como parametro, por eso utiliza el .class el @ExceptionHandler para entregar ese objeto"
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException exception){

        // Strem nos permtie utilizar la transformacion .map de uns lista ge FieldErrors
        // sin necesidad de iterarla con un for. Esto se debe a que no se puede palicar
        // un .map directamente a la lista.
        List<String> errores = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest()
                .body(new ApiError(400, "Solicitud invalida", errores));
    }

    // ResponseStatusException representa un error HTTP esperado que la aplicación lanza con throw,
    // por ejemplo, cuando no existe una tarea. Este método devuelve su código y mensaje al cliente.
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> estado(ResponseStatusException exception){
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        return ResponseEntity.status(status)
                .body(new ApiError(status.value(), exception.getReason(), List.of()));
    }

    public record ApiError(int estado, String mensaje, List<String> errores){}
}
