package com.mercadolibre.bootcamp_g1_final_project;

import com.mercadolibre.bootcamp_g1_final_project.config.SpringConfig;
import com.mercadolibre.bootcamp_g1_final_project.util.ScopeUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		ScopeUtils.calculateScopeSuffix();
		new SpringApplicationBuilder(SpringConfig.class).registerShutdownHook(true)
				.run(args);
	}
}
