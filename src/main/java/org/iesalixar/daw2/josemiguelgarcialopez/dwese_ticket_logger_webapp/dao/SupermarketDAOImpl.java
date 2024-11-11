package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Marca esta clase como un componente de acceso a datos
@Transactional // Indica que los métodos de esta clase están dentro de una transacción
public class SupermarketDAOImpl implements SupermarketDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(SupermarketDAOImpl.class);

    @PersistenceContext // Inyección del EntityManager para gestionar la persistencia
    private EntityManager entityManager;

    /**
     * Lista todos los supermercados de la base de datos.
     * @return Lista de supermercados
     */
    @Override
    public List<Supermarket> listAllSupermarkets() {
        logger.info("Listing all supermarkets from the database.");
        // Consulta para obtener todos los supermercados
        List<Supermarket> supermarkets = entityManager.createQuery("SELECT s FROM Supermarket s", Supermarket.class).getResultList();
        logger.info("Retrieved {} supermarkets from the database.", supermarkets.size()); // Registro del tamaño de la lista
        return supermarkets; // Retornar la lista de supermercados
    }

    /**
     * Inserta un nuevo supermercado en la base de datos.
     * @param supermarket Supermercado a insertar
     */
    @Override
    public void insertSupermarket(Supermarket supermarket) {
        logger.info("Inserting supermarket with name: {}", supermarket.getName());
        entityManager.persist(supermarket); // Persistir el nuevo supermercado en la base de datos
        logger.info("Inserted supermarket with ID: {}", supermarket.getId()); // Registro del ID del nuevo supermercado
    }

    /**
     * Actualiza un supermercado existente en la base de datos.
     * @param supermarket Supermercado a actualizar
     */
    @Override
    public void updateSupermarket(Supermarket supermarket) {
        logger.info("Updating supermarket with id: {}", supermarket.getId());
        entityManager.merge(supermarket); // Actualiza el supermercado existente en la base de datos
        logger.info("Updated supermarket with id: {}", supermarket.getId()); // Registro de la actualización
    }

    /**
     * Elimina un supermercado de la base de datos.
     * @param id ID del supermercado a eliminar
     */
    @Override
    public void deleteSupermarket(int id) {
        logger.info("Deleting supermarket with id: {}", id);
        Supermarket supermarket = entityManager.find(Supermarket.class, id); // Busca el supermercado por ID
        if (supermarket != null) {
            entityManager.remove(supermarket); // Elimina el supermercado encontrado
            logger.info("Deleted supermarket with id: {}", id); // Registro de la eliminación
        } else {
            logger.warn("Supermarket with id: {} not found.", id); // Advertencia si el supermercado no se encuentra
        }
    }

    /**
     * Recupera un supermercado por su ID.
     * @param id ID del supermercado
     * @return Supermercado correspondiente al ID
     */
    @Override
    public Supermarket getSupermarketById(int id) {
        logger.info("Retrieving supermarket by id: {}", id);
        Supermarket supermarket = entityManager.find(Supermarket.class, id); // Busca el supermercado por ID
        if (supermarket != null) {
            logger.info("Supermarket retrieved: {}", supermarket.getName()); // Registro del supermercado encontrado
        } else {
            logger.warn("No supermarket found with id: {}", id); // Advertencia si no se encuentra el supermercado
        }
        return supermarket; // Retorna el supermercado o null
    }

    /**
     * Verifica si un supermercado con el nombre especificado ya existe en la base de datos.
     * @param name el nombre del supermercado a verificar.
     * @return true si un supermercado con el nombre ya existe, false de lo contrario.
     */
    @Override
    public boolean existsSupermarketByName(String name) {
        logger.info("Checking if supermarket with name: {} exists", name);
        // Consulta para contar supermercados con el nombre dado (en mayúsculas)
        Long count = entityManager.createQuery("SELECT COUNT(s) FROM Supermarket s WHERE UPPER(s.name) = :name", Long.class)
                .setParameter("name", name.toUpperCase()) // Usar nombre en mayúsculas para la comparación
                .getSingleResult(); // Obtener el resultado
        boolean exists = (count != null && count > 0); // Verifica si existe al menos un supermercado
        logger.info("Supermarket with name: {} exists: {}", name, exists);
        return exists; // Retorna true si existe
    }

    /**
     * Verifica si un supermercado con el nombre especificado ya existe en la base de datos,
     * excluyendo un supermercado con un ID específico.
     * @param name el nombre del supermercado a verificar.
     * @param id   el ID del supermercado a excluir de la verificación.
     * @return true si un supermercado con el nombre ya existe (y no es el supermercado con el ID dado),
     *         false de lo contrario.
     */
    @Override
    public boolean existsSupermarketByNameAndNotId(String name, int id) {
        logger.info("Checking if supermarket with name: {} exists excluding id: {}", name, id);
        // Consulta para contar supermercados con el nombre dado, excluyendo el ID especificado
        Long count = entityManager.createQuery("SELECT COUNT(s) FROM Supermarket s WHERE UPPER(s.name) = :name AND s.id != :id", Long.class)
                .setParameter("name", name.toUpperCase()) // Usar nombre en mayúsculas
                .setParameter("id", id) // Excluir el ID proporcionado
                .getSingleResult(); // Obtener el resultado
        boolean exists = (count != null && count > 0); // Verifica si existe al menos un supermercado
        logger.info("Supermarket with name: {} exists excluding id {}: {}", name, id, exists);
        return exists; // Retorna true si existe
    }
}
