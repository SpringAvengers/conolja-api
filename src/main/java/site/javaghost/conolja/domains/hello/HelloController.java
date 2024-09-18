package site.javaghost.conolja.domains.hello;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Hello API", description = "Hello, world! API endpoints")
public class HelloController {

  @GetMapping("/hello")
  @Operation(summary = "Get Hello Message",
    description = "Returns a simple 'Hello, world!' message.")
  @ApiResponse(responseCode = "200", description = "Successful response")
  public String hello() {
    return "Hello, world!";
  }
}
