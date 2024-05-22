package com.infobip.pmf.course.storage;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserEntityRepo uRepo;

    public DatabaseInitializer(UserEntityRepo entityManager) {
        this.uRepo = entityManager;
    }

    @Override
    @Transactional
    public void run(String... args) throws RuntimeException {

        uRepo.save(UserEntity.from("Otac", "la9psd71atbpgeg7fvvx"));
        uRepo.save(UserEntity.from("Sin", "ox9w79g2jwctzww2hcyb"));
        uRepo.save(UserEntity.from("Duh", "othyqhps18srg7fdj0p9"));
    }
}
