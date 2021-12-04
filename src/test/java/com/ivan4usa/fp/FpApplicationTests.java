package com.ivan4usa.fp;

import com.ivan4usa.fp.services.TemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@ContextConfiguration(classes = FpApplication.class)
class FpApplicationTests {

	@Test
	void contextLoads() {
	}

}
