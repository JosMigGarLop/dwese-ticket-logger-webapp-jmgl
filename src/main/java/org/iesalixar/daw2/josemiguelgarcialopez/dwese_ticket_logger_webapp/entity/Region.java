package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty; // Validación para campos no vacíos
import jakarta.validation.constraints.Size; // Validación para el tamaño de los campos
import lombok.AllArgsConstructor; // Generador de constructor con todos los parámetros
import lombok.Data; // Genera getters, setters y otros métodos
import lombok.NoArgsConstructor; // Generador de constructor sin parámetros
import jakarta.validation.constraints.NotNull; // Validación para campos no nulos

import java.util.List;

/**
 * La clase `Region` representa una entidad que modela una región dentro de la base de datos.
 * Contiene campos para identificar y describir la región, incluyendo su código y nombre.
 */
@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "regions") // Especifica el nombre de la tabla asociada a esta entidad.
@Data // Genera automáticamente métodos getter y setter.
@NoArgsConstructor // Constructor sin parámetros para JPA y otros usos.
@AllArgsConstructor // Constructor con todos los parámetros.
public class Region {

    // Campo que almacena el identificador único de la región.
    // Es una clave primaria autogenerada por la base de datos.
    @Id // Indica que este campo es la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que el valor se generará automáticamente.
    private Integer id;

    // Campo que almacena el código de la región, normalmente una cadena corta que identifica la región.
    // Ejemplo: "01" para Andalucía.
    @NotEmpty(message = "{msg.region.code.notEmpty}") // Validación para no permitir códigos vacíos.
    @Size(max = 2, message = "{msg.region.code.size}") // Validación para el tamaño máximo del código.
    @Column(name = "code", nullable = false, length = 2) // Define la columna correspondiente en la tabla.
    private String code;

    // Campo que almacena el nombre completo de la región, como "Andalucía" o "Cataluña".
    @NotEmpty(message = "{msg.region.name.notEmpty}") // Validación para no permitir nombres vacíos.
    @Size(max = 100, message = "{msg.region.name.size}") // Validación para el tamaño máximo del nombre.
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String name;

    // Relación uno a muchos con la entidad Province.
    // Una región puede tener muchas provincias.
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Province> provinces;

    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Region` cuando no es necesario o no se conoce el `id` de la región
     * (por ejemplo, antes de insertar la región en la base de datos, donde el `id` es autogenerado).
     *
     * @param code Código de la región.
     * @param name Nombre de la región.
     */
    public Region(String code, String name) {
        this.code = code; // Asigna el código
        this.name = name; // Asigna el nombre
    }
}
