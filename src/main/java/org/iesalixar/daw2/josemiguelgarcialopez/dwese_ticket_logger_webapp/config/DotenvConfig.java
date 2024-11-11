package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp.config;

import io.github.cdimascio.dotenv.Dotenv; // Importa la librería Dotenv para manejar variables de entorno
import org.slf4j.Logger; // Importa el logger de SLF4J
import org.slf4j.LoggerFactory; // Importa la fábrica de loggers de SLF4J
import org.springframework.context.annotation.Configuration; // Importa la anotación de configuración de Spring

@Configuration // Marca esta clase como configuración de Spring
public class DotenvConfig {

    // Se crea un logger para la clase utilizando SLF4J
    private static final Logger logger = LoggerFactory.getLogger(DotenvConfig.class);

    // Bloque estático para inicializar y cargar las variables de entorno del archivo .env
    static {
        try {
            // Mensaje informativo al iniciar la carga del archivo .env
            logger.info("Loading environment variables from .env file...");

            // Carga el archivo .env
            Dotenv dotenv = Dotenv.configure().load();

            // Itera sobre las entradas del archivo .env
            dotenv.entries().forEach(entry -> {
                // Establece cada variable como propiedad del sistema
                System.setProperty(entry.getKey(), entry.getValue());

                // Registra el valor de cada variable cargada (nivel DEBUG)
                logger.debug("Set system property: {} = {}", entry.getKey(), entry.getValue());
            });

            // Mensaje informativo al finalizar la carga del archivo .env
            logger.info(".env file loaded successfully.");
        } catch (Exception e) {
            // Registra un error si ocurre una excepción durante la carga
            logger.error("Failed to load .env file", e);
        }
    }
}
