package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid; // Importa las anotaciones de validación
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao.LocationDAO; // DAO para gestionar ubicaciones
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao.ProvinceDAO; // DAO para gestionar provincias
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao.SupermarketDAO; // DAO para gestionar supermercados
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Location; // Entidad de ubicación
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Province; // Entidad de provincia
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Supermarket; // Entidad de supermercado
import org.slf4j.Logger; // Logger para registrar información
import org.slf4j.LoggerFactory; // Factoria para crear loggers
import org.springframework.beans.factory.annotation.Autowired; // Para la inyección de dependencias
import org.springframework.context.MessageSource; // Para la internacionalización de mensajes
import org.springframework.stereotype.Controller; // Indica que esta clase es un controlador
import org.springframework.ui.Model; // Modelo para pasar datos a la vista
import org.springframework.validation.BindingResult; // Resultado de la validación de formularios
import org.springframework.web.bind.annotation.*; // Anotaciones para manejar solicitudes web
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Para manejar redirecciones y mensajes flash
import java.util.List; // Lista de elementos
import java.util.Locale; // Para la localización de mensajes

@Controller // Define esta clase como un controlador
@RequestMapping("/locations") // Ruta base para las operaciones de ubicación
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class); // Logger para esta clase

    @Autowired // Inyección automática de dependencias
    private LocationDAO locationDAO; // DAO para ubicaciones

    @Autowired // Inyección automática de dependencias
    private ProvinceDAO provinceDAO; // DAO para provincias

    @Autowired // Inyección automática de dependencias
    private SupermarketDAO supermarketDAO; // DAO para supermercados

    @Autowired // Inyección automática de dependencias
    private MessageSource messageSource; // Para mensajes internacionalizados

    /**
     * Lista todas las ubicaciones y las pasa como atributo al modelo para que sean accesibles en la vista `location.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de ubicaciones.
     */
    @GetMapping // Maneja solicitudes GET a /locations
    public String listLocations(Model model) {
        logger.info("Solicitando la lista de todas las ubicaciones..."); // Registro de la acción
        List<Location> listLocations = locationDAO.listAllLocations(); // Recupera la lista de ubicaciones
        logger.info("Se han cargado {} ubicaciones.", listLocations.size()); // Registro de la cantidad de ubicaciones
        model.addAttribute("listLocations", listLocations); // Agrega la lista al modelo
        return "location"; // Devuelve la vista para mostrar la lista de ubicaciones
    }

    /**
     * Muestra el formulario para crear una nueva ubicación.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new") // Maneja solicitudes GET a /locations/new
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva ubicación."); // Registro de la acción
        List<Province> listProvinces = provinceDAO.listAllProvinces(); // Recupera la lista de provincias
        List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets(); // Recupera la lista de supermercados
        model.addAttribute("location", new Location()); // Crea un nuevo objeto Location
        model.addAttribute("listProvinces", listProvinces); // Agrega la lista de provincias al modelo
        model.addAttribute("listSupermarkets", listSupermarkets); // Agrega la lista de supermercados al modelo
        return "location-form.html"; // Devuelve la vista del formulario
    }

    /**
     * Muestra el formulario para editar una ubicación existente.
     *
     * @param id    ID de la ubicación a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit") // Maneja solicitudes GET a /locations/edit
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la ubicación con ID {}", id); // Registro de la acción
        Location location = locationDAO.getLocationById(id); // Busca la ubicación por ID
        List<Province> listProvinces = provinceDAO.listAllProvinces(); // Recupera la lista de provincias
        List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets(); // Recupera la lista de supermercados
        if (location == null) { // Verifica si la ubicación fue encontrada
            logger.warn("No se encontró la ubicación con ID {}", id); // Registro de advertencia
        }
        model.addAttribute("location", location); // Agrega la ubicación al modelo
        model.addAttribute("listProvinces", listProvinces); // Agrega la lista de provincias al modelo
        model.addAttribute("listSupermarkets", listSupermarkets); // Agrega la lista de supermercados al modelo
        return "location-form.html"; // Devuelve la vista del formulario
    }

    /**
     * Inserta una nueva ubicación en la base de datos.
     *
     * @param location            Objeto que contiene los datos del formulario.
     * @param result              Resultado de la validación del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @param locale              Localización para mensajes internacionalizados.
     * @param model               Modelo para pasar datos a la vista.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/insert") // Maneja solicitudes POST a /locations/insert
    public String insertLocation(@Valid @ModelAttribute("location") Location location, BindingResult result,
                                 RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Insertando nueva ubicación con dirección {}", location.getAddress()); // Registro de la acción
        if (result.hasErrors()) { // Verifica si hay errores de validación
            List<Province> listProvinces = provinceDAO.listAllProvinces(); // Recupera la lista de provincias
            List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets(); // Recupera la lista de supermercados
            model.addAttribute("listProvinces", listProvinces); // Agrega la lista de provincias al modelo
            model.addAttribute("listSupermarkets", listSupermarkets); // Agrega la lista de supermercados al modelo
            return "location-form.html"; // Devuelve el formulario para mostrar los errores
        }
        if (locationDAO.existsLocationByAddress(location.getAddress())) { // Verifica si la dirección ya existe
            logger.warn("La dirección de la ubicación {} ya existe.", location.getAddress()); // Registro de advertencia
            String errorMessage = messageSource.getMessage("msg.location-controller.insert.addressExist", null, locale); // Mensaje de error
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Agrega mensaje flash
            return "redirect:/locations/new"; // Redirige al formulario de nueva ubicación
        }
        locationDAO.insertLocation(location); // Inserta la nueva ubicación
        logger.info("Ubicación {} insertada con éxito.", location.getAddress()); // Registro de éxito
        return "redirect:/locations"; // Redirige a la lista de ubicaciones
    }

    /**
     * Actualiza una ubicación existente en la base de datos.
     *
     * @param location            Objeto que contiene los datos del formulario.
     * @param result              Resultado de la validación del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @param locale              Localización para mensajes internacionalizados.
     * @param model               Modelo para pasar datos a la vista.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/update") // Maneja solicitudes POST a /locations/update
    public String updateLocation(@Valid @ModelAttribute("location") Location location, BindingResult result,
                                 RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Actualizando ubicación con ID {}", location.getId()); // Registro de la acción
        if (result.hasErrors()) { // Verifica si hay errores de validación
            List<Province> listProvinces = provinceDAO.listAllProvinces(); // Recupera la lista de provincias
            List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets(); // Recupera la lista de supermercados
            model.addAttribute("listProvinces", listProvinces); // Agrega la lista de provincias al modelo
            model.addAttribute("listSupermarkets", listSupermarkets); // Agrega la lista de supermercados al modelo
            return "location-form.html"; // Devuelve el formulario para mostrar los errores
        }
        if (locationDAO.existsLocationByAddressAndNotId(location.getAddress(), location.getId())) { // Verifica si la dirección ya existe para otra ubicación
            logger.warn("La dirección de la ubicación {} ya existe para otra ubicación.", location.getAddress()); // Registro de advertencia
            String errorMessage = messageSource.getMessage("msg.location-controller.update.addressExist", null, locale); // Mensaje de error
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Agrega mensaje flash
            return "redirect:/locations/edit?id=" + location.getId(); // Redirige al formulario de edición
        }
        locationDAO.updateLocation(location); // Actualiza la ubicación
        logger.info("Ubicación con ID {} actualizada con éxito.", location.getId()); // Registro de éxito
        return "redirect:/locations"; // Redirige a la lista de ubicaciones
    }

    /**
     * Elimina una ubicación de la base de datos.
     *
     * @param id                  ID de la ubicación a eliminar.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de ubicaciones.
     */
    @PostMapping("/delete") // Maneja solicitudes POST a /locations/delete
    public String deleteLocation(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando ubicación con ID {}", id); // Registro de la acción
        locationDAO.deleteLocation(id); // Elimina la ubicación
        logger.info("Ubicación con ID {} eliminada con éxito.", id); // Registro de éxito
        return "redirect:/locations"; // Redirige a la lista de ubicaciones
    }
}
