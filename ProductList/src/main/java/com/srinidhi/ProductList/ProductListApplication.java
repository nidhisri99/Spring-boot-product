package com.srinidhi.ProductList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


import java.util.Locale;

@SpringBootApplication   // this annotation contains 3 other annotation such as 1.@SpringBootConfiguration
						//                        								2.@EnableAutoConfiguration
						//														3.@ComponentScan
@RestController

public class ProductListApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductListApplication.class, args);
	}


}
