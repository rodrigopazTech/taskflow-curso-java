package com.coderfy.taskflow;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 100, message = "El título admite máximo 100 caracteres")
        String titulo) {
}