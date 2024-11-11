package org.iesalixar.daw2.josemiguelgarcialopez.dwese_ticket_logger_webapp;

// Importa las clases necesarias de Spring Boot
import org.springframework.boot.SpringApplication; // Para arrancar la aplicación Spring
import org.springframework.boot.autoconfigure.SpringBootApplication; // Para habilitar la configuración automática de Spring Boot

// Anotación que marca esta clase como una aplicación Spring Boot
@SpringBootApplication
public class DweseTicketLoggerWebappApplication {

	// Método principal que se ejecuta al iniciar la aplicación
	public static void main(String[] args) {
		// Arranca la aplicación Spring, configurando el contexto de la aplicación
		SpringApplication.run(DweseTicketLoggerWebappApplication.class, args);
	}
}
