package tz.go.samuel.danda.tutorials.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class DocsConfiguration {

    @Value("${samuel.danda.dev-url}")
    private String devUrl;

    @Value("${samuel.danda.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Dev Server URl");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Prod Server URL");

        Contact contact = new Contact();
        contact.setEmail("samuel.danda@eganet.go.tz");
        contact.setName("Samuel Danda");
        contact.setUrl("https://www.linkedin.com/in/iamsammysd/");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Tutorial Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.")
                .termsOfService("")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            var errorResponseSchema = new ObjectSchema()
                    .name("ErrorResponseBody")
                    .title("ErrorResponseBody")
                    .description("Error response object description")
                    .addProperty(
                            "httpStatus",
                            new StringSchema().example("400").description("Error http code")
                    )
                    .addProperty(
                            "errorMessage",
                            new StringSchema().example("BAD_REQUEST").description("Error message")
                    )
                    .addProperty(
                            "errorReasons",
                            new ArraySchema()
                                    .example(new String[] {
                                            "The tutorial id must be a positive number",
                                            "maximum page size allowed is 100"})
                                    .description("Error reasons")
                    );

            var schemas = openApi.getComponents().getSchemas();
            schemas.put(errorResponseSchema.getName(), errorResponseSchema);
        };
    }
}
