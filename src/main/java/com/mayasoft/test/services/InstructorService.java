package com.mayasoft.test.services;

import com.mayasoft.test.models.entities.Instructor;

import java.util.List;

public interface InstructorService {

    List<Instructor> getInstructors ();
    Instructor getInstructorById (Long id);
    void saveInstructor(Instructor instructor);
    void updateInstructor(Instructor instructor);
    void deleteInstructor(Long id);
}
