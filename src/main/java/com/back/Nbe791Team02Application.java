package com.back;   // ✅ 맨 위에 이 줄만 추가

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Nbe791Team02Application {
	public static void main(String[] args) {
		SpringApplication.run(Nbe791Team02Application.class, args);
	}

	// DB 연결 확인용 테스트 Bean
//	@Bean
//	public CommandLineRunner demo(ProductRepository productRepository) {
//		return (String[] args) -> {
//			System.out.println("=== Product Table 확인 ===");
//			productRepository.findAll().forEach(product -> {
//				System.out.println(product.getName() + " / " + product.getPrice());
//			});
//		};
//	}
}
