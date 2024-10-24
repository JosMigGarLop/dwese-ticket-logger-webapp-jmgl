package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Supermarket` representa una entidad que modela un supermercado.
 * Contiene dos campos: `id` y `name`, donde `id` es el identificador único del supermercado,
 * y `name` es el nombre del supermercado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supermarket {

    // Identificador único del supermercado.
    private Integer id;

    // Nombre del supermercado. No puede estar vacío.
    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    private String name;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Supermarket`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarlo en la base de datos).
     * @param name Nombre del supermercado.
     */
    public Supermarket(String name) {
        this.name = name;
    }
}
