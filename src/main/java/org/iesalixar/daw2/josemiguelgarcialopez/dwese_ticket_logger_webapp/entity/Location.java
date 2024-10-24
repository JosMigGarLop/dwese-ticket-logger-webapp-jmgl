package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Location` representa una entidad que modela una ubicación.
 * Contiene tres campos: `id`, `address`, `city`, `supermarket` y `province`,
 * donde `id` es el identificador único de la ubicación,
 * `address` es la dirección, `city` es la ciudad, `supermarket` es la referencia
 * al supermercado al que pertenece la ubicación, y `province` es la provincia a la que pertenece.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    // Identificador único de la ubicación.
    private Integer id;

    // Dirección de la ubicación. No puede estar vacía.
    @NotEmpty(message = "{msg.location.address.notEmpty}")
    private String address;

    // Ciudad de la ubicación. No puede estar vacía.
    @NotEmpty(message = "{msg.location.city.notEmpty}")
    private String city;

    // Supermercado al que pertenece la ubicación. No puede ser nulo.
    @NotNull(message = "{msg.location.supermarket.notNull}")
    private Supermarket supermarket;

    // Provincia a la que pertenece la ubicación. No puede ser nulo.
    @NotNull(message = "{msg.location.province.notNull}")
    private Province province;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Location`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarla en la base de datos).
     * @param address Dirección de la ubicación.
     * @param city Ciudad de la ubicación.
     * @param supermarket Supermercado al que pertenece la ubicación.
     * @param province Provincia a la que pertenece la ubicación.
     */
    public Location(String address, String city, Supermarket supermarket, Province province) {
        this.address = address;
        this.city = city;
        this.supermarket = supermarket;
        this.province = province;
    }
}
