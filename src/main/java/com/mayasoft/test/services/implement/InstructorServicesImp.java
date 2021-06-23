package com.mayasoft.test.services.implement;

import com.mayasoft.test.entities.Instructor;
import com.mayasoft.test.entities.repositories.InstructorRepository;
import com.mayasoft.test.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstructorServicesImp implements InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Instructor> getInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void saveInstructor(Instructor instructor) {
        instructorRepository.save(instructor);
    }

    @Override
    @Transactional
    public void updateInstructor(Instructor instructor) {
        saveInstructor(instructor);
    }

    @Override
    @Transactional
    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }
}
