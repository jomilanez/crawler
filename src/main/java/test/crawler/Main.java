package test.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
@EnableScheduling
public class Main {
	
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

}
