package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Province;

import java.sql.SQLException;
import java.util.List;

public interface ProvinceDAO {

    List<Province> listAllProvinces() throws SQLException; // Cambiado a listAllProvinces
    void insertProvince(Province province) throws SQLException; // Cambiado a insertProvince
    void updateProvince(Province province) throws SQLException; // Cambiado a updateProvince
    void deleteProvince(int id) throws SQLException; // Cambiado a deleteProvince
    Province getProvinceById(int id) throws SQLException; // Cambiado a getProvinceById
    boolean existsProvinceByCode(String code) throws SQLException; // Cambiado a existsProvinceByCode
    boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException; // Cambiado a existsProvinceByCodeAndNotId

}
