package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Location;
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Province;
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LocationDAOImpl implements LocationDAO {

    private static final Logger logger = LoggerFactory.getLogger(LocationDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;

    // Inyección de JdbcTemplate
    public LocationDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lista todas las ubicaciones de la base de datos.
     * @return Lista de ubicaciones
     */
    @Override
    public List<Location> listAllLocations() {
        logger.info("Listing all locations from the database.");
        String sql = "SELECT l.id, l.address, l.city, p.id as province_id, p.code as province_code, p.name as province_name, " +
                "s.id as supermarket_id, s.name as supermarket_name " +
                "FROM locations l " +
                "JOIN provinces p ON l.province_id = p.id " +
                "JOIN supermarkets s ON l.supermarket_id = s.id";
        return jdbcTemplate.query(sql, new LocationRowMapper());
    }

    /**
     * Inserta una nueva ubicación en la base de datos.
     * @param location Ubicación a insertar
     */
    @Override
    public void insertLocation(Location location) {
        logger.info("Inserting location with address: {}", location.getAddress());
        String sql = "INSERT INTO locations (address, city, province_id, supermarket_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, location.getAddress(), location.getCity(), location.getProvince().getId(), location.getSupermarket().getId());
    }

    /**
     * Actualiza una ubicación existente en la base de datos.
     * @param location Ubicación a actualizar
     */
    @Override
    public void updateLocation(Location location) {
        logger.info("Updating location with id: {}", location.getId());
        String sql = "UPDATE locations SET address = ?, city = ?, province_id = ?, supermarket_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, location.getAddress(), location.getCity(), location.getProvince().getId(), location.getSupermarket().getId(), location.getId());
    }

    /**
     * Elimina una ubicación de la base de datos.
     * @param id ID de la ubicación a eliminar
     */
    @Override
    public void deleteLocation(int id) {
        logger.info("Deleting location with id: {}", id);
        String sql = "DELETE FROM locations WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Recupera una ubicación por su ID.
     * @param id ID de la ubicación a recuperar
     * @return Ubicación encontrada o null si no existe
     */
    @Override
    public Location getLocationById(int id) {
        logger.info("Retrieving location by id: {}", id);
        String sql = "SELECT l.id, l.address, l.city, p.id as province_id, p.code as province_code, p.name as province_name, " +
                "s.id as supermarket_id, s.name as supermarket_name " +
                "FROM locations l " +
                "JOIN provinces p ON l.province_id = p.id " +
                "JOIN supermarkets s ON l.supermarket_id = s.id WHERE l.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new LocationRowMapper(), id);
        } catch (Exception e) {
            logger.warn("No location found with id: {}", id);
            return null;
        }
    }

    /**
     * Verifica si una ubicación con la dirección especificada ya existe en la base de datos.
     * @param address la dirección de la ubicación a verificar.
     * @return true si una ubicación con la dirección ya existe, false de lo contrario.
     */
    @Override
    public boolean existsLocationByAddress(String address) {
        logger.info("Checking if location with address: {} exists", address);
        String sql = "SELECT COUNT(*) FROM locations WHERE UPPER(address) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, address.toUpperCase());
        return count != null && count > 0;
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
        String sql = "SELECT COUNT(*) FROM locations WHERE UPPER(address) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, address.toUpperCase(), id);
        return count != null && count > 0;
    }

    /**
     * Clase interna que implementa RowMapper para mapear los resultados de la consulta SQL a la entidad Location.
     */
    private static class LocationRowMapper implements RowMapper<Location> {
        @Override
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("id"));
            location.setAddress(rs.getString("address"));
            location.setCity(rs.getString("city"));

            // Mapea la provincia asociada
            Province province = new Province();
            province.setId(rs.getInt("province_id"));
            province.setCode(rs.getString("province_code"));
            province.setName(rs.getString("province_name"));
            location.setProvince(province);

            // Mapea el supermercado asociado
            Supermarket supermarket = new Supermarket();
            supermarket.setId(rs.getInt("supermarket_id"));
            supermarket.setName(rs.getString("supermarket_name"));
            location.setSupermarket(supermarket);

            return location;
        }
    }
}
