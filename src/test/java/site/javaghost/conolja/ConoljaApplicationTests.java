package site.javaghost.conolja;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ConoljaApplicationTests {

	@MockBean
	private SecurityFilterChain securityFilterChain;

	@Test
	void contextLoads() {
	}
}
