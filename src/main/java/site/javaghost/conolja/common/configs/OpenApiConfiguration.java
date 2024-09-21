package site.javaghost.conolja.common.configs;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import site.javaghost.conolja.common.security.jwt.JwtProperties;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {

	private final JwtProperties jwtProperties;

	@Bean
	public OpenAPI openApi() {
		SecurityScheme apiKey = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.in(SecurityScheme.In.HEADER)
			.name(jwtProperties.header())
			.scheme(jwtProperties.prefix())
			.bearerFormat("JWT");

		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("Bearer Token");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("Bearer Token", apiKey)).addSecurityItem(securityRequirement)
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("코놀자 API") // API 의 제목
			.version("1.0.0"); // API 의 버전
	}
}
