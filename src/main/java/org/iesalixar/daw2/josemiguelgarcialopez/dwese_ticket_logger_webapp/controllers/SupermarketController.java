package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid; // Importa la validación de objetos
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao.SupermarketDAO; // Importa la interfaz DAO para supermercados
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Supermarket; // Importa la entidad Supermarket
import org.slf4j.Logger; // Importa el logger para el registro de eventos
import org.slf4j.LoggerFactory; // Importa la fábrica para crear loggers
import org.springframework.beans.factory.annotation.Autowired; // Para la inyección de dependencias
import org.springframework.context.MessageSource; // Para la internacionalización de mensajes
import org.springframework.stereotype.Controller; // Marca esta clase como un controlador de Spring
import org.springframework.ui.Model; // Para pasar datos al modelo de la vista
import org.springframework.validation.BindingResult; // Para manejar el resultado de la validación
import org.springframework.web.bind.annotation.*; // Importa las anotaciones para manejar peticiones HTTP
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Para pasar atributos de redirección

import java.util.List; // Para trabajar con listas
import java.util.Locale; // Para manejar la localización

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Supermarket`.
 * Utiliza `SupermarketDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/supermarkets") // Mapea todas las rutas bajo /supermarkets
public class SupermarketController {

    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class); // Crea un logger para esta clase

    // Inyección de dependencia para el DAO de supermercados
    @Autowired
    private SupermarketDAO supermarketDAO;

    // Inyección de dependencia para el manejo de mensajes internacionales
    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los supermercados y los pasa como atributo al modelo para que sean
     * accesibles en la vista `supermarket.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de supermercados.
     */
    @GetMapping // Maneja las peticiones GET a /supermarkets
    public String listSupermarkets(Model model) {
        logger.info("Solicitando la lista de todos los supermercados..."); // Log de inicio de la solicitud
        List<Supermarket> listSupermarkets = supermarketDAO.listAllSupermarkets(); // Obtiene la lista de supermercados
        logger.info("Se han cargado {} supermercados.", listSupermarkets.size()); // Log del tamaño de la lista
        model.addAttribute("listSupermarkets", listSupermarkets); // Agrega la lista al modelo
        return "supermarket"; // Devuelve el nombre de la plantilla a renderizar
    }

    /**
     * Muestra el formulario para crear un nuevo supermercado.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new") // Maneja las peticiones GET a /supermarkets/new
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo supermercado."); // Log del formulario nuevo
        model.addAttribute("supermarket", new Supermarket()); // Crea un nuevo objeto Supermarket para el formulario
        return "supermarket-form"; // Devuelve el nombre de la plantilla del formulario
    }

    /**
     * Muestra el formulario para editar un supermercado existente.
     *
     * @param id    ID del supermercado a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit") // Maneja las peticiones GET a /supermarkets/edit
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para el supermercado con ID {}", id); // Log del ID
        Supermarket supermarket = supermarketDAO.getSupermarketById(id); // Obtiene el supermercado por ID
        if (supermarket == null) {
            logger.warn("No se encontró el supermercado con ID {}", id); // Log de advertencia si no se encuentra
        }
        model.addAttribute("supermarket", supermarket); // Agrega el supermercado al modelo
        return "supermarket-form"; // Devuelve el nombre de la plantilla del formulario
    }

    /**
     * Inserta un nuevo supermercado en la base de datos.
     *
     * @param supermarket         Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/insert") // Maneja las peticiones POST a /supermarkets/insert
    public String insertSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket, BindingResult result,
                                    RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo supermercado con nombre {}", supermarket.getName()); // Log del nombre del supermercado
        if (result.hasErrors()) {
            return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (supermarketDAO.existsSupermarketByName(supermarket.getName())) {
            logger.warn("El nombre del supermercado {} ya existe.", supermarket.getName()); // Log de advertencia si el nombre ya existe
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.insert.nameExist", null, locale); // Obtiene el mensaje de error
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Agrega el mensaje a los atributos de redirección
            return "redirect:/supermarkets/new"; // Redirige al formulario nuevo
        }
        supermarketDAO.insertSupermarket(supermarket); // Inserta el nuevo supermercado
        logger.info("Supermercado {} insertado con éxito.", supermarket.getName()); // Log de éxito
        return "redirect:/supermarkets"; // Redirige a la lista de supermercados
    }

    /**
     * Actualiza un supermercado existente en la base de datos.
     *
     * @param supermarket         Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/update") // Maneja las peticiones POST a /supermarkets/update
    public String updateSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket, BindingResult result,
                                    RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando supermercado con ID {}", supermarket.getId()); // Log del ID del supermercado
        if (result.hasErrors()) {
            return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (supermarketDAO.existsSupermarketByNameAndNotId(supermarket.getName(), supermarket.getId())) {
            logger.warn("El nombre del supermercado {} ya existe para otro supermercado.", supermarket.getName()); // Log de advertencia
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.update.nameExist", null, locale); // Obtiene el mensaje de error
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Agrega el mensaje a los atributos de redirección
            return "redirect:/supermarkets/edit?id=" + supermarket.getId(); // Redirige al formulario de edición
        }
        supermarketDAO.updateSupermarket(supermarket); // Actualiza el supermercado
        logger.info("Supermercado con ID {} actualizado con éxito.", supermarket.getId()); // Log de éxito
        return "redirect:/supermarkets"; // Redirige a la lista de supermercados
    }

    /**
     * Elimina un supermercado de la base de datos.
     *
     * @param id                 ID del supermercado a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/delete") // Maneja las peticiones POST a /supermarkets/delete
    public String deleteSupermarket(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando supermercado con ID {}", id); // Log del ID del supermercado
        supermarketDAO.deleteSupermarket(id); // Elimina el supermercado
        logger.info("Supermercado con ID {} eliminado con éxito.", id); // Log de éxito
        return "redirect:/supermarkets"; // Redirige a la lista de supermercados
    }
}
