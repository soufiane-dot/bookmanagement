package com.api.bookmanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return getOpenAPI();
	}

	private OpenAPI getOpenAPI() {
		OpenAPI openAPI = new OpenAPI();
		openAPI.setComponents(new Components());
		Tag tag = new Tag();
		tag.setName("Base");
		tag.setDescription("Technical Base API");
		openAPI.addTagsItem(tag);
		openAPI.setInfo(new Info().title("Base API").description("OpenApi Docs"));
		return openAPI;
	}
}

