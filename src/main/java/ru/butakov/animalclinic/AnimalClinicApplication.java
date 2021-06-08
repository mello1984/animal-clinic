package ru.butakov.animalclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.butakov.animalclinic")
public class AnimalClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalClinicApplication.class, args);
    }

}
