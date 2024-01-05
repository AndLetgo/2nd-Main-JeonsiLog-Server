package depth.jeonsilog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JeonsiLogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeonsiLogServerApplication.class, args);
    }
}
