package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty; // Validación para campos no vacíos
import jakarta.validation.constraints.NotNull; // Validación para campos no nulos
import jakarta.validation.constraints.Size; // Validación para el tamaño de los campos
import lombok.AllArgsConstructor; // Generador de constructor con todos los parámetros
import lombok.Data; // Genera getters, setters y otros métodos
import lombok.NoArgsConstructor; // Generador de constructor sin parámetros

import java.util.List;

/**
 * La clase `Province` representa una entidad que modela una provincia dentro de la base de datos.
 * Contiene campos para identificar y describir la provincia, incluyendo su código, nombre y región.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "provinces") // Define el nombre de la tabla asociada a esta entidad.
@Data // Genera automáticamente métodos getter y setter.
@NoArgsConstructor // Constructor sin parámetros para JPA y otros usos.
@AllArgsConstructor // Constructor con todos los parámetros.
public class Province {

    // Campo que almacena el identificador único de la provincia. Es autogenerado y clave primaria.
    @Id // Indica que este campo es la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Especifica que el valor se generará automáticamente.
    private Integer id;

    // Campo que almacena el código de la provincia, normalmente una cadena corta que identifica la provincia.
    // Ejemplo: "23" para Jaén.
    @NotEmpty(message = "{msg.province.code.notEmpty}") // Validación para no permitir códigos vacíos.
    @Size(max = 2, message = "{msg.province.code.size}") // Validación para el tamaño máximo del código.
    @Column(name = "code", nullable = false, length = 2) // Define la columna correspondiente en la tabla.
    private String code;

    // Campo que almacena el nombre completo de la provincia, como "Sevilla" o "Jaén".
    @NotEmpty(message = "{msg.province.name.notEmpty}") // Validación para no permitir nombres vacíos.
    @Size(max = 100, message = "{msg.province.name.size}") // Validación para el tamaño máximo del nombre.
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String name;

    // Relación uno a muchos con la entidad `Location`. Una provincia puede tener muchas ubicaciones.
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    // Relación con la entidad `Region`, representando la comunidad autónoma a la que pertenece la provincia.
    @NotNull(message = "{msg.province.region.notNull}") // Validación para asegurar que la región no sea nula.
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchas provincias a una región; carga perezosa.
    @JoinColumn(name = "region_id", nullable = false) // Clave foránea en la tabla provinces que referencia a la tabla regions.
    private Region region;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Province`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarla en la base de datos).
     *
     * @param code Código de la provincia.
     * @param name Nombre de la provincia.
     * @param region La región a la que pertenece la provincia.
     */
    public Province(String code, String name, Region region) {
        this.code = code; // Asigna el código
        this.name = name; // Asigna el nombre
        this.region = region; // Asigna la región
    }
}
