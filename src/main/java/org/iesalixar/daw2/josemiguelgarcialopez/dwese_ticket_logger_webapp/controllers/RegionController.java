package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.controllers;

// Importaciones necesarias para la funcionalidad del controlador
import jakarta.validation.Valid; // Para la validación de objetos
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao.RegionDAO; // DAO para las regiones
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Region; // Entidad de la región
import org.slf4j.Logger; // Para el registro de eventos
import org.slf4j.LoggerFactory; // Para la creación del logger
import org.springframework.beans.factory.annotation.Autowired; // Para la inyección de dependencias
import org.springframework.context.MessageSource; // Para la internacionalización de mensajes
import org.springframework.stereotype.Controller; // Marca la clase como un controlador de Spring MVC
import org.springframework.ui.Model; // Para pasar datos al modelo de la vista
import org.springframework.validation.BindingResult; // Para el resultado de la validación
import org.springframework.web.bind.annotation.*; // Anotaciones para el manejo de peticiones
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Para atributos de redirección
import java.util.List; // Para trabajar con listas
import java.util.Locale; // Para manejar la localización

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Region`.
 * Utiliza `RegionDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/regions") // Define el prefijo de la URL para las operaciones de regiones
public class RegionController {

    // Logger para registrar información y eventos
    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private RegionDAO regionDAO;

    // Permite al controlador gestionar la internacionalización de mensajes en la aplicación
    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las regiones y las pasa como atributo al modelo para que sean
     * accesibles en la vista `region.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de regiones.
     */
    @GetMapping // Mapea la solicitud GET a "/regions"
    public String listRegions(Model model) {
        logger.info("Solicitando la lista de todas las regiones...");
        List<Region> listRegions = regionDAO.listAllRegions(); // Llama al DAO para obtener la lista de regiones
        logger.info("Se han cargado {} regiones.", listRegions.size());
        model.addAttribute("listRegions", listRegions); // Pasa la lista de regiones al modelo
        return "region"; // Devuelve el nombre de la vista
    }

    /**
     * Muestra el formulario para crear una nueva región.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new") // Mapea la solicitud GET a "/regions/new"
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva región.");
        model.addAttribute("region", new Region()); // Crea un nuevo objeto Region
        return "region-form"; // Devuelve el nombre de la vista del formulario
    }

    /**
     * Muestra el formulario para editar una región existente.
     *
     * @param id    ID de la región a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit") // Mapea la solicitud GET a "/regions/edit"
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la región con ID {}", id);
        Region region = regionDAO.getRegionById(id); // Obtiene la región por ID
        if (region == null) {
            logger.warn("No se encontró la región con ID {}", id); // Registro si no se encuentra la región
        }
        model.addAttribute("region", region); // Agrega la región al modelo
        return "region-form"; // Devuelve el nombre de la vista del formulario
    }

    /**
     * Inserta una nueva región en la base de datos.
     *
     * @param region              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/insert") // Mapea la solicitud POST a "/regions/insert"
    public String insertRegion(@Valid @ModelAttribute("region") Region region, BindingResult result,
                               RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva región con código {}", region.getCode());
        if (result.hasErrors()) {
            return "region-form"; // Devuelve el formulario para mostrar los errores de validación
        }
        if (regionDAO.existsRegionByCode(region.getCode())) { // Verifica si el código ya existe
            logger.warn("El código de la región {} ya existe.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Mensaje de error
            return "redirect:/regions/new"; // Redirige al formulario nuevo
        }
        regionDAO.insertRegion(region); // Inserta la nueva región
        logger.info("Región {} insertada con éxito.", region.getCode());
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }

    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param region              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/update") // Mapea la solicitud POST a "/regions/update"
    public String updateRegion(@Valid @ModelAttribute("region") Region region, BindingResult result,
                               RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando región con ID {}", region.getId());
        if (result.hasErrors()) {
            return "region-form"; // Devuelve el formulario para mostrar los errores de validación
        }
        if (regionDAO.existsRegionByCodeAndNotId(region.getCode(), region.getId())) { // Verifica si el código ya existe para otra región
            logger.warn("El código de la región {} ya existe para otra región.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Mensaje de error
            return "redirect:/regions/edit?id=" + region.getId(); // Redirige al formulario de edición
        }
        regionDAO.updateRegion(region); // Actualiza la región
        logger.info("Región con ID {} actualizada con éxito.", region.getId());
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }

    /**
     * Elimina una región de la base de datos.
     *
     * @param id                 ID de la región a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/delete") // Mapea la solicitud POST a "/regions/delete"
    public String deleteRegion(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando región con ID {}", id);
        regionDAO.deleteRegion(id); // Elimina la región
        logger.info("Región con ID {} eliminada con éxito.", id);
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }
}
