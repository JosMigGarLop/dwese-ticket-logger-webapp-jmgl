package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty; // Validación para campos no vacíos
import jakarta.validation.constraints.Size; // Validación para el tamaño de los campos
import lombok.AllArgsConstructor; // Generador de constructor con todos los parámetros
import lombok.Data; // Genera getters, setters y otros métodos
import lombok.NoArgsConstructor; // Generador de constructor sin parámetros
import jakarta.validation.constraints.NotNull; // Validación para campos no nulos

/**
 * La clase `Category` representa una entidad que modela una categoría dentro de la base de datos.
 * Contiene campos para identificar y describir la categoría, incluyendo su nombre e imagen asociada.
 */
@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "categories") // Especifica el nombre de la tabla asociada a esta entidad.
@Data // Genera automáticamente métodos getter y setter.
@NoArgsConstructor // Constructor sin parámetros para JPA y otros usos.
@AllArgsConstructor // Constructor con todos los parámetros.
public class Category {

    // Campo que almacena el identificador único de la categoría.
    // Es una clave primaria autogenerada por la base de datos.
    @Id // Indica que este campo es la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que el valor se generará automáticamente.
    private Integer id;

    // Campo que almacena el nombre de la categoría, como "Electrodomésticos", "Alimentos", etc.
    @NotEmpty(message = "{msg.category.name.notEmpty}") // Validación para no permitir nombres vacíos.
    @Size(max = 100, message = "{msg.category.name.size}") // Validación para el tamaño máximo del nombre.
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String name;

    // Campo que almacena la ruta o URL de la imagen asociada con la categoría.
    @Size(max = 255, message = "{msg.category.image.size}") // Validación para el tamaño máximo de la URL de la imagen.
    @Column(name = "image", nullable = true, length = 255) // Define la columna correspondiente en la tabla.
    private String image;

    // Relación con la categoría padre. Si es nulo, es una categoría principal (padre).
    @ManyToOne(fetch = FetchType.LAZY) // Relación muchos a uno (una subcategoría tiene una categoría padre).
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true) // Establece el nombre de la columna para el vínculo con la categoría padre.
    private Category parentCategory;

    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Category` cuando no es necesario o no se conoce el `id` de la categoría
     * (por ejemplo, antes de insertar la categoría en la base de datos, donde el `id` es autogenerado).
     *
     * @param name  Nombre de la categoría.
     * @param image URL de la imagen asociada a la categoría.
     * @param parentCategory Categoría padre, puede ser nula si no es una subcategoría.
     */
    public Category(String name, String image, Category parentCategory) {
        this.name = name; // Asigna el nombre
        this.image = image; // Asigna la imagen
        this.parentCategory = parentCategory; // Asigna la categoría padre
    }
}
