package com.mayasoft.test;

import com.mayasoft.test.models.entities.Instructor;
import com.mayasoft.test.models.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

@Component
public class DataLoader implements ApplicationRunner {

    private InstructorRepository instructorRepository;

    @Autowired
    public DataLoader(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        instructorRepository.save(new Instructor("Carlos", "Santana", new Date("1980/06/09")));
        instructorRepository.save(new Instructor("Roberto", "Cruz", new Date("1980/6/10")));
        instructorRepository.save(new Instructor("Ricardo", "Hernandez", new Date("1980/06/09")));
        instructorRepository.save(new Instructor("Pablo", "Lopez", new Date("1980/06/09")));
        instructorRepository.save(new Instructor("Armando", "Casas", new Date("1980/06/09")));
    }
}