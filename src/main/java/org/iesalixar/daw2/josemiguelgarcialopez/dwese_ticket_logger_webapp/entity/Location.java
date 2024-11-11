package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty; // Validación para campos no vacíos
import jakarta.validation.constraints.NotNull; // Validación para campos no nulos
import lombok.AllArgsConstructor; // Generador de constructor con todos los parámetros
import lombok.Data; // Genera getters, setters y otros métodos
import lombok.NoArgsConstructor; // Generador de constructor sin parámetros

/**
 * La clase `Location` representa una entidad que modela una ubicación.
 * Contiene campos para identificar y describir la ubicación, incluyendo su
 * dirección, ciudad, supermercado y provincia asociados.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "locations") // Especifica el nombre de la tabla asociada a esta entidad.
@Data // Genera automáticamente métodos getter y setter.
@NoArgsConstructor // Constructor sin parámetros para JPA y otros usos.
@AllArgsConstructor // Constructor con todos los parámetros.
public class Location {

    // Identificador único de la ubicación. Es autogenerado y clave primaria.
    @Id // Indica que este campo es la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que el valor se generará automáticamente.
    private Integer id;

    // Dirección de la ubicación. No puede estar vacía.
    @NotEmpty(message = "{msg.location.address.notEmpty}") // Validación para no permitir direcciones vacías.
    @Column(name = "address", nullable = false) // Especifica el nombre de la columna en la base de datos.
    private String address;

    // Ciudad de la ubicación. No puede estar vacía.
    @NotEmpty(message = "{msg.location.city.notEmpty}") // Validación para no permitir ciudades vacías.
    @Column(name = "city", nullable = false) // Especifica el nombre de la columna en la base de datos.
    private String city;

    // Relación con el supermercado al que pertenece la ubicación. No puede ser nulo.
    @NotNull(message = "{msg.location.supermarket.notNull}") // Validación para asegurar que el supermercado no sea nulo.
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchos a uno; carga perezosa.
    @JoinColumn(name = "supermarket_id", nullable = false) // Clave foránea a la tabla de supermercados.
    private Supermarket supermarket;

    // Relación con la provincia a la que pertenece la ubicación. No puede ser nulo.
    @NotNull(message = "{msg.location.province.notNull}") // Validación para asegurar que la provincia no sea nula.
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchos a uno; carga perezosa.
    @JoinColumn(name = "province_id", nullable = false) // Clave foránea a la tabla de provincias.
    private Province province;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Location`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarla en la base de datos).
     *
     * @param address     Dirección de la ubicación.
     * @param city        Ciudad de la ubicación.
     * @param supermarket Supermercado al que pertenece la ubicación.
     * @param province    Provincia a la que pertenece la ubicación.
     */
    public Location(String address, String city, Supermarket supermarket, Province province) {
        this.address = address; // Asigna la dirección
        this.city = city; // Asigna la ciudad
        this.supermarket = supermarket; // Asigna el supermercado
        this.province = province; // Asigna la provincia
    }
}
