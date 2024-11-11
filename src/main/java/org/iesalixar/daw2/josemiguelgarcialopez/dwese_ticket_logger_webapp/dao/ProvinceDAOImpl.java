package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository // Marca esta clase como un componente de acceso a datos
@Transactional // Indica que los métodos de esta clase están dentro de una transacción
public class ProvinceDAOImpl implements ProvinceDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);

    @PersistenceContext // Inyección del EntityManager para gestionar la persistencia
    private EntityManager entityManager;

    /**
     * Lista todas las provincias de la base de datos.
     * @return Lista de provincias
     */
    @Override
    public List<Province> listAllProvinces() {
        logger.info("Listing all provinces from the database.");
        String query = "SELECT p FROM Province p JOIN FETCH p.region"; // Consulta para obtener provincias y sus regiones
        List<Province> provinces = entityManager.createQuery(query, Province.class).getResultList();
        logger.info("Retrieved {} provinces from the database.", provinces.size()); // Registro del tamaño de la lista
        return provinces;
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     * @param province Provincia a insertar
     */
    @Override
    public void insertProvince(Province province) {
        logger.info("Inserting province with code: {} and name: {}", province.getCode(), province.getName());
        entityManager.persist(province); // Persistir la nueva provincia en la base de datos
        logger.info("Inserted province with ID: {}", province.getId()); // Registro del ID de la nueva provincia
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     * @param province Provincia a actualizar
     */
    @Override
    public void updateProvince(Province province) {
        logger.info("Updating province with id: {}", province.getId());
        entityManager.merge(province); // Actualiza la provincia existente en la base de datos
        logger.info("Updated province with id: {}", province.getId()); // Registro de la actualización
    }

    /**
     * Elimina una provincia de la base de datos.
     * @param id ID de la provincia a eliminar
     */
    @Override
    public void deleteProvince(int id) {
        logger.info("Deleting province with id: {}", id);
        Province province = entityManager.find(Province.class, id); // Busca la provincia por ID
        if (province != null) {
            entityManager.remove(province); // Elimina la provincia encontrada
            logger.info("Deleted province with id: {}", id); // Registro de la eliminación
        } else {
            logger.warn("Province with id: {} not found.", id); // Advertencia si la provincia no se encuentra
        }
    }

    /**
     * Obtiene una provincia por su ID.
     * @param id ID de la provincia
     * @return Provincia correspondiente al ID
     */
    @Override
    public Province getProvinceById(int id) {
        logger.info("Retrieving province by id: {}", id);
        Province province = entityManager.find(Province.class, id); // Busca la provincia por ID
        if (province != null) {
            logger.info("Province retrieved: {} - {}", province.getCode(), province.getName()); // Registro de la provincia encontrada
        } else {
            logger.warn("No province found with id: {}", id); // Advertencia si no se encuentra la provincia
        }
        return province; // Retorna la provincia o null
    }

    /**
     * Verifica si una provincia con el código especificado ya existe en la base de datos.
     * @param code el código de la provincia a verificar.
     * @return true si una provincia con el código ya existe, false de lo contrario.
     */
    @Override
    public boolean existsProvinceByCode(String code) {
        logger.info("Checking if province with code: {} exists", code);
        String query = "SELECT COUNT(p) FROM Province p WHERE UPPER(p.code) = :code"; // Consulta para contar provincias
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("code", code.toUpperCase()) // Usar código en mayúsculas para la comparación
                .getSingleResult(); // Obtener el resultado
        boolean exists = count != null && count > 0; // Verificar si existen provincias
        logger.info("Province with code: {} exists: {}", code, exists); // Registro del resultado
        return exists; // Retorna true o false
    }

    /**
     * Verifica si una provincia con el código especificado ya existe en la base de datos,
     * excluyendo una provincia con un ID específico.
     * @param code el código de la provincia a verificar.
     * @param id   el ID de la provincia a excluir de la verificación.
     * @return true si una provincia con el código ya existe (y no es la provincia con el ID dado),
     *         false de lo contrario.
     */
    @Override
    public boolean existsProvinceByCodeAndNotId(String code, int id) {
        logger.info("Checking if province with code: {} exists excluding id: {}", code, id);
        String query = "SELECT COUNT(p) FROM Province p WHERE UPPER(p.code) = :code AND p.id != :id"; // Consulta para contar provincias excluyendo ID
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("code", code.toUpperCase()) // Usar código en mayúsculas
                .setParameter("id", id) // Excluir el ID proporcionado
                .getSingleResult(); // Obtener el resultado
        boolean exists = count != null && count > 0; // Verificar si existen provincias
        logger.info("Province with code: {} exists excluding id {}: {}", code, id, exists); // Registro del resultado
        return exists; // Retorna true o false
    }
}
