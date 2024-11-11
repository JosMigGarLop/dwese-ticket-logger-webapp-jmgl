package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.demo;

// Importa las clases necesarias para el controlador y el registro
import org.slf4j.Logger; // Para el registro de eventos
import org.slf4j.LoggerFactory; // Para crear el logger
import org.springframework.web.bind.annotation.GetMapping; // Para manejar las solicitudes GET
import org.springframework.web.bind.annotation.RequestParam; // Para extraer parámetros de la solicitud
import org.springframework.web.bind.annotation.RestController; // Para marcar la clase como un controlador REST

// Anotación que indica que esta clase es un controlador REST
@RestController
public class HelloController {

    // Creación del logger para esta clase, que permitirá registrar mensajes en diferentes niveles (INFO, DEBUG, ERROR, etc.)
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    /**
     * Método que responde a una petición GET en la ruta "/hello".
     * Este método recibe un parámetro opcional "name" y devuelve un saludo personalizado.
     *
     * @param name Parámetro opcional que representa el nombre a saludar. Si no se pasa, por defecto es "World".
     * @return Una cadena de texto con el saludo personalizado.
     */
    @GetMapping("/hello") // Mapea las solicitudes GET a la ruta "/hello"
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        // Loguea cuando se llama al método hello, mostrando el nombre recibido
        logger.info("Request received to /hello endpoint with parameter name: {}", name);

        // Formatea y devuelve el mensaje de saludo
        String greeting = String.format("Hello %s!", name);

        // Loguea el mensaje de saludo que se va a devolver
        logger.debug("Greeting message to be returned: {}", greeting);

        return greeting; // Devuelve el saludo personalizado
    }
}
