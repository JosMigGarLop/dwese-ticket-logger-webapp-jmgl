package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.controllers;

// Importa las clases necesarias para el controlador
import org.springframework.stereotype.Controller; // Para marcar la clase como un controlador
import org.springframework.ui.Model; // Para pasar datos al modelo de la vista
import org.springframework.web.bind.annotation.GetMapping; // Para manejar solicitudes GET

// Anotación que indica que esta clase es un controlador de Spring MVC
@Controller
public class HomeController {

    /**
     * Método que maneja las solicitudes GET en la raíz ("/") de la aplicación.
     * Este método se encarga de devolver la vista "index".
     *
     * @param model El modelo que se pasará a la vista. Permite agregar atributos que pueden ser utilizados en la plantilla.
     * @return El nombre de la vista que se va a renderizar (en este caso, "index").
     */
    @GetMapping("/") // Mapea las solicitudes GET a la raíz de la aplicación
    public String home(Model model) {
        // Aquí se pueden agregar atributos al modelo si es necesario
        return "index"; // Devuelve el nombre de la vista que se renderizará (index.html o index.jsp)
    }
}
