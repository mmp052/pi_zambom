package insper.pi_zambom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class PiZambomApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiZambomApplication.class, args);
	}

	@GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

}
