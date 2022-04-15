package be.vdab.restservice;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestserviceApplication.class, args);
    }
    //Je voegt een method toe aan in RestserviceApplication. Je wijzigt daarmee de titel van het document naar Filialen.
    //Je geeft je REST service ook een versie en een omschrijving:

    @Bean
    OpenAPI openApi() {
        return new OpenAPI().info(new Info().title("Filialen").version("1.0.0").description("Toegang tot onze filialen"));
    }

}
