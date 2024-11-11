package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Marca esta clase como un componente de acceso a datos
@Transactional // Indica que los métodos de esta clase están dentro de una transacción
public class LocationDAOImpl implements LocationDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(LocationDAOImpl.class);

    @PersistenceContext // Inyección del EntityManager para gestionar la persistencia
    private EntityManager entityManager;

    /**
     * Lista todas las ubicaciones de la base de datos.
     * @return Lista de ubicaciones
     */
    @Override
    public List<Location> listAllLocations() {
        logger.info("Listing all locations from the database.");
        // Consulta para obtener todas las ubicaciones junto con sus provincias y supermercados
        String query = "SELECT l FROM Location l JOIN FETCH l.province JOIN FETCH l.supermarket";
        List<Location> locations = entityManager.createQuery(query, Location.class).getResultList(); // Ejecutar consulta
        logger.info("Retrieved {} locations from the database.", locations.size()); // Registro del tamaño de la lista
        return locations; // Retornar la lista de ubicaciones
    }

    /**
     * Inserta una nueva ubicación en la base de datos.
     * @param location Ubicación a insertar
     */
    @Override
    public void insertLocation(Location location) {
        logger.info("Inserting location with address: {}", location.getAddress());
        entityManager.persist(location); // Persistir la nueva ubicación en la base de datos
        logger.info("Inserted location with ID: {}", location.getId()); // Registro del ID de la nueva ubicación
    }

    /**
     * Actualiza una ubicación existente en la base de datos.
     * @param location Ubicación a actualizar
     */
    @Override
    public void updateLocation(Location location) {
        logger.info("Updating location with id: {}", location.getId());
        entityManager.merge(location); // Actualiza la ubicación existente en la base de datos
        logger.info("Updated location with id: {}", location.getId()); // Registro de la actualización
    }

    /**
     * Elimina una ubicación de la base de datos.
     * @param id ID de la ubicación a eliminar
     */
    @Override
    public void deleteLocation(int id) {
        logger.info("Deleting location with id: {}", id);
        Location location = entityManager.find(Location.class, id); // Busca la ubicación por ID
        if (location != null) {
            entityManager.remove(location); // Elimina la ubicación encontrada
            logger.info("Deleted location with id: {}", id); // Registro de la eliminación
        } else {
            logger.warn("Location with id: {} not found.", id); // Advertencia si la ubicación no se encuentra
        }
    }

    /**
     * Recupera una ubicación por su ID.
     * @param id ID de la ubicación
     * @return Ubicación correspondiente al ID
     */
    @Override
    public Location getLocationById(int id) {
        logger.info("Retrieving location by id: {}", id);
        Location location = entityManager.find(Location.class, id); // Busca la ubicación por ID
        if (location != null) {
            logger.info("Location retrieved: {}", location.getAddress()); // Registro de la ubicación encontrada
        } else {
            logger.warn("No location found with id: {}", id); // Advertencia si no se encuentra la ubicación
        }
        return location; // Retorna la ubicación o null
    }

    /**
     * Verifica si una ubicación con la dirección especificada ya existe en la base de datos.
     * @param address la dirección de la ubicación a verificar.
     * @return true si una ubicación con la dirección ya existe, false de lo contrario.
     */
    @Override
    public boolean existsLocationByAddress(String address) {
        logger.info("Checking if location with address: {} exists", address);
        // Consulta para contar ubicaciones con la dirección dada (en mayúsculas)
        Long count = entityManager.createQuery("SELECT COUNT(l) FROM Location l WHERE UPPER(l.address) = :address", Long.class)
                .setParameter("address", address.toUpperCase()) // Usar dirección en mayúsculas para la comparación
                .getSingleResult(); // Obtener el resultado
        return count != null && count > 0; // Retorna true si existe al menos una ubicación
    }

    /**
     * Verifica si una ubicación con la dirección especificada ya existe en la base de datos,
     * excluyendo una ubicación con un ID específico.
     * @param address la dirección de la ubicación a verificar.
     * @param id   el ID de la ubicación a excluir de la verificación.
     * @return true si una ubicación con la dirección ya existe (y no es la ubicación con el ID dado),
     *         false de lo contrario.
     */
    @Override
    public boolean existsLocationByAddressAndNotId(String address, int id) {
        logger.info("Checking if location with address: {} exists excluding id: {}", address, id);
        // Consulta para contar ubicaciones con la dirección dada, excluyendo el ID especificado
        Long count = entityManager.createQuery("SELECT COUNT(l) FROM Location l WHERE UPPER(l.address) = :address AND l.id != :id", Long.class)
                .setParameter("address", address.toUpperCase()) // Usar dirección en mayúsculas
                .setParameter("id", id) // Excluir el ID proporcionado
                .getSingleResult(); // Obtener el resultado
        return count != null && count > 0; // Retorna true si existe al menos una ubicación
    }
}
