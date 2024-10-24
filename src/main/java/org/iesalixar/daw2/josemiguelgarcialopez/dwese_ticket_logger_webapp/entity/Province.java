package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Province` representa una entidad que modela una provincia dentro de la base de datos.
 * Contiene cuatro campos: `id`, `code`, `name`, y `region`, donde `id` es el identificador único de la provincia,
 * `code` es un código asociado a la provincia, `name` es el nombre de la provincia, y `region` es la relación
 * con la entidad `Region`, representando la comunidad autónoma a la que pertenece la provincia.
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Data  // Esta anotación de Lombok genera automáticamente los getters, setters, equals, hashCode, toString, etc.
@NoArgsConstructor  // Genera un constructor vacío, requerido por JPA/Hibernate.
@AllArgsConstructor  // Genera un constructor con todos los campos.
public class Province {

    // Campo que almacena el identificador único de la provincia. Es autogenerado y clave primaria.
    private Integer id;

    // Campo que almacena el código de la provincia, normalmente una cadena corta que identifica la región.
    // Ejemplo: "23" para Jaén.
    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 2, message = "{msg.province.code.size}")
    private String code;

    // Campo que almacena el nombre completo de la provincia, como "Sevilla" o "Jaén".
    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.notEmpty}")
    private String name;

    // Relación con la entidad `Region`, representando la comunidad autónoma a la que pertenece la provincia.
    @NotNull(message = "{msg.province.region.notNull}")
    private Region region;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Province`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarla en la base de datos).
     * @param code Código de la provincia.
     * @param name Nombre de la provincia.
     * @param region La región a la que pertenece la provincia.
     */
    public Province(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }
}
