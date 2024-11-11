package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.controllers;

// Importaciones necesarias para la funcionalidad del controlador
import jakarta.validation.Valid; // Para la validación de objetos
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao.ProvinceDAO; // DAO para las provincias
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao.RegionDAO; // DAO para las regiones
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Province; // Entidad de la provincia
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

// Marca la clase como un controlador de Spring MVC
@Controller
@RequestMapping("/provinces") // Define el prefijo de la URL para las operaciones de provincias
public class ProvinceController {

    // Logger para registrar información y eventos
    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    // Inyección de dependencias para los DAOs y el MessageSource
    @Autowired
    private ProvinceDAO provinceDAO;

    @Autowired
    private RegionDAO regionDAO;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las provincias y las pasa como atributo al modelo para que sean accesibles en la vista `province.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de provincias.
     */
    @GetMapping // Mapea la solicitud GET a "/provinces"
    public String listProvinces(Model model) {
        logger.info("Solicitando la lista de todas las provincias...");
        List<Province> listProvinces = provinceDAO.listAllProvinces(); // Llama al DAO para obtener la lista de provincias
        logger.info("Se han cargado {} provincias.", listProvinces.size());
        model.addAttribute("listProvinces", listProvinces); // Agrega la lista al modelo
        return "province"; // Devuelve el nombre de la vista
    }

    /**
     * Muestra el formulario para crear una nueva provincia.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new") // Mapea la solicitud GET a "/provinces/new"
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva provincia.");
        List<Region> listRegions = regionDAO.listAllRegions(); // Obtiene la lista de regiones
        model.addAttribute("province", new Province()); // Agrega un nuevo objeto Province al modelo
        model.addAttribute("listRegions", listRegions); // Agrega la lista de regiones al modelo
        return "province-form"; // Devuelve el nombre de la vista del formulario
    }

    /**
     * Muestra el formulario para editar una provincia existente.
     *
     * @param id    ID de la provincia a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit") // Mapea la solicitud GET a "/provinces/edit"
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la provincia con ID {}", id);
        Province province = provinceDAO.getProvinceById(id); // Obtiene la provincia por ID
        List<Region> listRegions = regionDAO.listAllRegions(); // Obtiene la lista de regiones
        if (province == null) {
            logger.warn("No se encontró la provincia con ID {}", id); // Registro si no se encuentra la provincia
        }
        model.addAttribute("province", province); // Agrega la provincia al modelo
        model.addAttribute("listRegions", listRegions); // Agrega la lista de regiones al modelo
        return "province-form"; // Devuelve el nombre de la vista del formulario
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     *
     * @param province            Objeto que contiene los datos del formulario.
     * @param result              Resultado de la validación del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @param locale              Localización para mensajes internacionalizados.
     * @param model              Modelo para pasar datos a la vista.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/insert") // Mapea la solicitud POST a "/provinces/insert"
    public String insertProvince(@Valid @ModelAttribute("province") Province province, BindingResult result,
                                 RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());
        if (result.hasErrors()) { // Verifica si hay errores de validación
            List<Region> listRegions = regionDAO.listAllRegions(); // Obtiene la lista de regiones
            model.addAttribute("listRegions", listRegions); // Agrega al modelo
            return "province-form"; // Devuelve la vista del formulario
        }
        if (provinceDAO.existsProvinceByCode(province.getCode())) { // Verifica si el código ya existe
            logger.warn("El código de la provincia {} ya existe.", province.getCode());
            String errorMessage = messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Mensaje de error
            return "redirect:/provinces/new"; // Redirige al formulario nuevo
        }
        provinceDAO.insertProvince(province); // Inserta la nueva provincia
        logger.info("Provincia {} insertada con éxito.", province.getCode());
        return "redirect:/provinces"; // Redirige a la lista de provincias
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     *
     * @param province            Objeto que contiene los datos del formulario.
     * @param result              Resultado de la validación del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @param locale              Localización para mensajes internacionalizados.
     * @param model              Modelo para pasar datos a la vista.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/update") // Mapea la solicitud POST a "/provinces/update"
    public String updateProvince(@Valid @ModelAttribute("province") Province province, BindingResult result,
                                 RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Actualizando provincia con ID {}", province.getId());
        if (result.hasErrors()) { // Verifica si hay errores de validación
            List<Region> listRegions = regionDAO.listAllRegions(); // Obtiene la lista de regiones
            model.addAttribute("listRegions", listRegions); // Agrega al modelo
            return "province-form"; // Devuelve la vista del formulario
        }
        if (provinceDAO.existsProvinceByCodeAndNotId(province.getCode(), province.getId())) { // Verifica si el código ya existe para otra provincia
            logger.warn("El código de la provincia {} ya existe para otra provincia.", province.getCode());
            String errorMessage = messageSource.getMessage("msg.province-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Mensaje de error
            return "redirect:/provinces/edit?id=" + province.getId(); // Redirige al formulario de edición
        }
        provinceDAO.updateProvince(province); // Actualiza la provincia
        logger.info("Provincia con ID {} actualizada con éxito.", province.getId());
        return "redirect:/provinces"; // Redirige a la lista de provincias
    }

    /**
     * Elimina una provincia de la base de datos.
     *
     * @param id                  ID de la provincia a eliminar.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping("/delete") // Mapea la solicitud POST a "/provinces/delete"
    public String deleteProvince(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando provincia con ID {}", id);
        provinceDAO.deleteProvince(id); // Elimina la provincia
        logger.info("Provincia con ID {} eliminada con éxito.", id);
        return "redirect:/provinces"; // Redirige a la lista de provincias
    }
}
