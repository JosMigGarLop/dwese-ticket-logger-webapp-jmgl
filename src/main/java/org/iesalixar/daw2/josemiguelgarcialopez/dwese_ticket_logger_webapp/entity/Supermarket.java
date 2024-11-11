package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty; // Validación para campos no vacíos
import lombok.AllArgsConstructor; // Generador de constructor con todos los parámetros
import lombok.Data; // Genera getters, setters y otros métodos
import lombok.NoArgsConstructor; // Generador de constructor sin parámetros

import java.util.List;

/**
 * La clase `Supermarket` representa una entidad que modela un supermercado.
 * Contiene campos para identificar y describir el supermercado, incluyendo su nombre.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "supermarkets") // Especifica el nombre de la tabla asociada a esta entidad.
@Data // Genera automáticamente métodos getter y setter.
@NoArgsConstructor // Constructor sin parámetros para JPA y otros usos.
@AllArgsConstructor // Constructor con todos los parámetros.
public class Supermarket {

    // Identificador único del supermercado. Es autogenerado y clave primaria.
    @Id // Indica que este campo es la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que el valor se generará automáticamente.
    private Integer id;

    // Nombre del supermercado. No puede estar vacío.
    @NotEmpty(message = "{msg.supermarket.name.notEmpty}") // Validación para no permitir nombres vacíos.
    @Column(name = "name", nullable = false) // Define la columna correspondiente en la tabla.
    private String name;

    // Relación uno a muchos con la entidad `Location`.
    // Un supermercado puede tener muchas ubicaciones.
    @OneToMany(mappedBy = "supermarket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Supermarket`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarlo en la base de datos).
     *
     * @param name Nombre del supermercado.
     */
    public Supermarket(String name) {
        this.name = name; // Asigna el nombre
    }
}
