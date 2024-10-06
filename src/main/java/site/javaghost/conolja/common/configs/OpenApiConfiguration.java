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
		// API에 대한 보안 요구 사항을 정의. 여기서는 "Bearer Token"을 사용하여
		// JWT 토큰 인증을 요구하는 것을 나타냄.
		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("Bearer Token");

		// JWT 토큰을 사용하는 보안 스키마를 정의.
		// 이 스키마는 HTTP 인증 방식으로, 헤더에 JWT 토큰을 포함하도록 설정함.
		SecurityScheme apiKey = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP) // 보안 방식은 HTTP로 설정
			.in(SecurityScheme.In.HEADER) // JWT 토큰을 헤더에 포함
			.name(jwtProperties.header()) // 헤더의 이름을 설정
			.scheme(jwtProperties.prefix()) // JWT 접두사를 설정 (예: "Bearer")
			.description("JWT 토큰을 입력하세요.") // JWT 토큰 사용에 대한 설명
			.bearerFormat("JWT"); // JWT 형식임을 명시

		// OpenAPI 설정을 생성하여 다음을 포함:
		// 1. 보안 스키마 구성 (Bearer Token)
		// 2. 보안 요구 사항 (JWT 인증 필요)
		// 3. API 정보 (타이틀, 버전 등)
		return new OpenAPI()
			.components(new Components().addSecuritySchemes("Bearer Token", apiKey)) // 보안 스키마 추가
			.addSecurityItem(securityRequirement) // 보안 요구 사항 추가
			.info(apiInfo()); // API 정보 추가
	}


	private Info apiInfo() {
		return new Info()
			.title("코놀자 API") // API 의 제목
			.version("1.0.0"); // API 의 버전
	}
}
