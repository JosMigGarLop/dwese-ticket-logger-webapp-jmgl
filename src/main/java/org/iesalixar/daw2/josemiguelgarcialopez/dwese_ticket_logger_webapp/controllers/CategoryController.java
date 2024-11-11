package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.dao.CategoryDAO;
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.entity.Category;
import org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FileStorageService fileStorageService;

    // Mostrar el listado de categorías
    @GetMapping
    public String listCategories(Model model) {
        try {
            logger.info("Solicitando la lista de todas las categorías...");
            List<Category> listCategories = categoryDAO.listAllCategories();
            logger.info("Se han cargado {} categorías.", listCategories.size());
            model.addAttribute("listCategories", listCategories);
        } catch (Exception e) {
            logger.error("Error al cargar las categorías", e);
            model.addAttribute("errorMessage", messageSource.getMessage("msg.category-list.error", null, Locale.getDefault()));
        }
        return "category";
    }

    // Mostrar el formulario para crear una nueva categoría
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva categoría.");
        model.addAttribute("category", new Category());
        List<Category> listCategories = categoryDAO.listAllCategories();
        model.addAttribute("listCategories", listCategories);  // Lista de categorías para el campo "parentCategory"
        return "category-form";
    }

    // Insertar una nueva categoría
    @PostMapping("/insert")
    public String insertCategory(@Valid @ModelAttribute("category") Category category, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            return "category-form";
        }

        // Manejo de la imagen
        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName);
            } else {
                String errorMessage = messageSource.getMessage("msg.category.insert.imageError", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/categories/new";
            }
        }

        // Verificar si la categoría con el mismo nombre ya existe
        if (categoryDAO.existsCategoryByName(category.getName())) {
            String errorMessage = messageSource.getMessage("msg.category.insert.nameExists", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/new";
        }

        try {
            categoryDAO.insertCategory(category);
            return "redirect:/categories";
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.category.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/new";
        }
    }

    // Mostrar el formulario de edición para una categoría existente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Category category = categoryDAO.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
            List<Category> listCategories = categoryDAO.listAllCategories();
            model.addAttribute("listCategories", listCategories); // Lista de categorías para el campo "parentCategory"
            return "category-form";
        } else {
            return "redirect:/categories";
        }
    }

    // Actualizar una categoría existente
    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("category") Category category, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            return "category-form";
        }

        // Manejo de la imagen: solo actualizar si se sube una nueva imagen
        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName); // Se actualiza con la nueva imagen
            } else {
                String errorMessage = messageSource.getMessage("msg.category.update.imageError", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/categories/edit/" + category.getId();
            }
        } else {
            // Si no se sube una nueva imagen, mantener la imagen actual
            Category existingCategory = categoryDAO.getCategoryById(category.getId());
            category.setImage(existingCategory.getImage());
        }

        // Verificar si la categoría con el mismo nombre ya existe (excluyendo la categoría actual)
        if (categoryDAO.existsCategoryByNameAndNotId(category.getName(), category.getId())) {
            String errorMessage = messageSource.getMessage("msg.category.update.nameExists", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/edit/" + category.getId();
        }

        try {
            categoryDAO.updateCategory(category);
            return "redirect:/categories";
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.category.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/edit/" + category.getId();
        }
    }


    // Eliminar una categoría
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryDAO.getCategoryById(id);
            categoryDAO.deleteCategory(id);

            // Eliminar la imagen asociada si existe
            if (category != null && category.getImage() != null && !category.getImage().isEmpty()) {
                fileStorageService.deleteFile(category.getImage());
            }

            redirectAttributes.addFlashAttribute("successMessage", "Categoría eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la categoría.");
        }
        return "redirect:/categories";
    }
}