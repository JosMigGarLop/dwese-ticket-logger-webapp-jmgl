package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Region;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository // Indica que esta clase es un componente de acceso a datos
@Transactional // Asegura que los métodos se ejecutan dentro de una transacción
public class RegionDAOImpl implements RegionDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(RegionDAOImpl.class);

    @PersistenceContext // Inyección del EntityManager para operaciones de JPA
    private EntityManager entityManager;

    /**
     * Lista todas las regiones de la base de datos.
     * @return Lista de regiones
     */
    @Override
    public List<Region> listAllRegions() {
        logger.info("Listing all regions from the database.");
        String query = "SELECT r FROM Region r"; // Consulta JPQL para obtener todas las regiones
        List<Region> regions = entityManager.createQuery(query, Region.class).getResultList();
        logger.info("Retrieved {} regions from the database.", regions.size()); // Registro del tamaño de la lista
        return regions;
    }

    /**
     * Inserta una nueva región en la base de datos.
     * @param region Región a insertar
     */
    @Override
    public void insertRegion(Region region) {
        logger.info("Inserting region with code: {} and name: {}", region.getCode(), region.getName());
        entityManager.persist(region); // Persistir la nueva región en la base de datos
        logger.info("Inserted region with ID: {}", region.getId()); // Registro del ID de la nueva región
    }

    /**
     * Actualiza una región existente en la base de datos.
     * @param region Región a actualizar
     */
    @Override
    public void updateRegion(Region region) {
        logger.info("Updating region with id: {}", region.getId());
        entityManager.merge(region); // Actualiza la región existente en la base de datos
        logger.info("Updated region with id: {}", region.getId()); // Registro de la actualización
    }

    /**
     * Elimina una región de la base de datos.
     * @param id ID de la región a eliminar
     */
    @Override
    public void deleteRegion(int id) {
        logger.info("Deleting region with id: {}", id);
        Region region = entityManager.find(Region.class, id); // Busca la región por ID
        if (region != null) {
            entityManager.remove(region); // Elimina la región encontrada
            logger.info("Deleted region with id: {}", id); // Registro de la eliminación
        } else {
            logger.warn("Region with id: {} not found.", id); // Advertencia si la región no se encuentra
        }
    }

    /**
     * Recupera una región por su ID.
     * @param id ID de la región a recuperar
     * @return Región encontrada o null si no existe
     */
    @Override
    public Region getRegionById(int id) {
        logger.info("Retrieving region by id: {}", id);
        Region region = entityManager.find(Region.class, id); // Busca la región por ID
        if (region != null) {
            logger.info("Region retrieved: {} - {}", region.getCode(), region.getName()); // Registro de la región encontrada
        } else {
            logger.warn("No region found with id: {}", id); // Advertencia si no se encuentra la región
        }
        return region; // Retorna la región o null
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de datos.
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     */
    @Override
    public boolean existsRegionByCode(String code) {
        logger.info("Checking if region with code: {} exists", code);
        String query = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code"; // Consulta para contar regiones
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("code", code.toUpperCase()) // Usar código en mayúsculas para la comparación
                .getSingleResult(); // Obtener el resultado
        boolean exists = count != null && count > 0; // Verificar si existen regiones
        logger.info("Region with code: {} exists: {}", code, exists); // Registro del resultado
        return exists; // Retorna true o false
    }

    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * excluyendo una región con un ID específico.
     * @param code el código de la región a verificar.
     * @param id   el ID de la región a excluir de la verificación.
     * @return true si una región con el código ya existe (y no es la región con el ID dado),
     *         false de lo contrario.
     */
    @Override
    public boolean existsRegionByCodeAndNotId(String code, int id) {
        logger.info("Checking if region with code: {} exists excluding id: {}", code, id);
        String query = "SELECT COUNT(r) FROM Region r WHERE UPPER(r.code) = :code AND r.id != :id"; // Consulta para contar regiones excluyendo ID
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("code", code.toUpperCase()) // Usar código en mayúsculas
                .setParameter("id", id) // Excluir el ID proporcionado
                .getSingleResult(); // Obtener el resultado
        boolean exists = count != null && count > 0; // Verificar si existen regiones
        logger.info("Region with code: {} exists excluding id {}: {}", code, id, exists); // Registro del resultado
        return exists; // Retorna true o false
    }
}
