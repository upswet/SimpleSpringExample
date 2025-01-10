package ru.app.gen;
/*
import com.myapp.ApiClient;
import com.myapp.api.PetApi;
*/
import com.example.api.PetsApi;
import com.example.model.Pet;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

//https://habr.com/ru/companies/spring_aio/articles/833096/
//https://www.restack.io/p/openapi-generator-gradle-plugin-answer-spring-boot-example
//https://openvalue.blog/posts/2023/11/26/communicating_our_apis_part2/
@SpringBootApplication
@RestController
public class WebGenerateApplication implements PetsApi {

    public static void main(String[] args) {
        SpringApplication.run(WebGenerateApplication.class, args);
    }


    @Override
    public ResponseEntity<Pet> getPet(Integer petId) {
        Pet pet = new Pet();
        pet.setName("Всем привет, это тестовый пример!");
        return ResponseEntity.ok(pet);
    }

    /*
    @Bean
    ApiClient apiClient(RestClient.Builder builder) {
        var apiClient = new ApiClient(builder.build());
        apiClient.setUsername("admin");
        apiClient.setPassword("admin");
        apiClient.setBasePath("http://localhost:9966/petclinic/api");
        return apiClient;
    }

    @Bean
    PetApi petApi(ApiClient apiClient) {
        return new PetApi(apiClient);
    }
     */

   /* @Bean
    ApplicationRunner applicationRunner(PetApi petApi) {
        return args -> {
            System.out.println(petApi.listPets());
        };
    }*/
}
