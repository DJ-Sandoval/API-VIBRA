package com.a.s.APIVibraBike.config.app;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
@OpenAPIDefinition(
        info = @Info(
                title = "API VIBRA",
                description = "Our app provides a operatings for system of asistence",
                termsOfService = "www.unprogramadornace.com/terminos_y_condiciones",
                version = "1.1.0",
                contact = @Contact(
                        name = "Armando Sandoval & Sofia Chavez",
                        url = "https://unprogramadornace.com",
                        email = "sandovalsantanajosearmando@mail.com"
                ),
                license = @License(
                        name = "Standard Software Use License for DevSandal",
                        url = "www.unprogramadornace.com/licence"
                )
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD SERVER",
                        url = "http://localhost:8080"
                )
        }
)
public class SwaggerConfig {
}
