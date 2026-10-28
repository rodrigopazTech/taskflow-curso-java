package com.coderfy.taskflow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * ===================================================================
 * ANOTACIONES DE JPA / HIBERNATE
 * ===================================================================
 * Entity:
 * Le indica a JPA e Hibernate que esta clase representa una ENTIDAD de base de datos.
 * Esto significa que Hibernate mapeará automáticamente esta clase con una tabla en la
 * base de datos (por defecto, una tabla llamada 'task'). Cada instancia u objeto de esta
 * clase representará una fila (registro) en dicha tabla.
 *
 * ===================================================================
 * NOTA SOBRE LOMBOK PARA CONSTRUCTORES:
 * ===================================================================
 * Lombok ofrece 3 anotaciones principales para generar constructores automáticamente:
 *
 * 1. @NoArgsConstructor:
 *    Genera el constructor vacío (sin parámetros): public Task() { }
 *    Es obligatorio tener un constructor sin argumentos para JPA/Hibernate.
 *
 * 2. @AllArgsConstructor:
 *    Genera un constructor con TODOS los campos de la clase como parámetros:
 *    public Task(Long id, String titulo, String estado) { ... }
 *
 * 3. @RequiredArgsConstructor:
 *    Genera un constructor únicamente con los campos marcados como 'final' o con @NonNull.
 *
 * ¿Por qué creamos manualmente el constructor que solo recibe 'titulo'?
 * Porque Lombok no tiene una anotación para elegir atributos específicos arbitrarios
 * (como crear uno solo con 'titulo'). Por eso, se suele escribir a mano o usar el patrón
 * Builder (@Builder).
 */
@Getter
@Setter
@Entity
public class Task {

    // ===================================================================
    // 1. ATRIBUTOS Y MAPEO DE CLAVE PRIMARIA
    // ===================================================================

    /**
     * Id:
     * Marca este campo como la CLAVE PRIMARIA (Primary Key) de la entidad/tabla.
     * Es el identificador único e irrepetible para cada registro en la base de datos.
     *
     * GeneratedValue(strategy = GenerationType.IDENTITY):
     * Indica que el valor de la clave primaria será autogenerado por la base de datos.
     *
     * - strategy: Define la estrategia o mecanismo de generación.
     * - GenerationType.IDENTITY: Delega la generación a una columna autonumérica o auto-incrementable
     *   propia de la base de datos (como 'AUTO_INCREMENT' en MySQL o 'IDENTITY' en H2/PostgreSQL).
     *   La base de datos asignará 1, 2, 3... automáticamente al insertar cada nuevo registro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String estado = "PENDIENTE";

    // ===================================================================
    // 2. CONSTRUCTORES
    // ===================================================================

    /**
     * CONSTRUCTOR VACÍO (Sin argumentos):
     * ¿Por qué lo tenemos?
     * Es un REQUISITO OBLIGATORIO de la especificación JPA / Hibernate.
     * Cuando Hibernate consulta la base de datos y recupera registros, utiliza la
     * técnica de reflexión (Java Reflection) para crear instancias de la entidad.
     * Para hacerlo sin fallar, necesita invocar un constructor sin argumentos (no-arg constructor).
     * Posteriormente, Hibernate llena los datos usando los setters o accediendo a los campos.
     */
    protected Task() {
    }

    /**
     * CONSTRUCTOR CON PARÁMETRO 'titulo':
     * ¿Por qué lo tenemos?
     * Es para USO NUESTRO (conveniencia del desarrollador).
     * Cuando creamos una nueva tarea en el código de negocio:
     * - El 'id' aún no existe (lo asignará la base de datos al guardarlo).
     * - El 'estado' ya tiene su valor por defecto ("PENDIENTE").
     * Por lo tanto, el único dato que realmente necesitamos proporcionar al crear una tarea
     * es el 'titulo', por ejemplo: new Task("Comprar leche").
     */
    public Task(String titulo) {
        this.titulo = titulo;
    }
}
