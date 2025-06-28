package com.kalavastra.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${app.fe.images-path}")
	private String imagesPath;

	@Value("${app.fe.images-url}")
	private String imagesUrl;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// map URL /assets/images/products/** → filesystem
		// ../saree-ecommerce/src/assets/images/products/**
		registry.addResourceHandler(imagesUrl + "/**").addResourceLocations("file:" + imagesPath + "/");
	}
}
