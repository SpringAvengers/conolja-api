package site.javaghost.conolja;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ConfigurationPropertiesScan
public class ConoljaApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConoljaApplication.class, args);
    System.out.println("Hello, Conolja!20241020");
  }

  @GetMapping("/ping")
  @Hidden
  public String ping() {
    return "ping";
  }
}
