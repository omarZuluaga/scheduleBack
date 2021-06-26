package com.mayasoft.test.controllers;

import com.mayasoft.test.controllers.VO.EventVO;
import com.mayasoft.test.controllers.VO.InstructorVO;
import com.mayasoft.test.models.entities.Event;
import com.mayasoft.test.models.entities.Instructor;
import com.mayasoft.test.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("instructor/")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping("getInstructors")
    public ResponseEntity<List<InstructorVO>> getInstructors() {
        return ResponseEntity.ok().body(mapInstructors(instructorService.getInstructors()));
    }

    @GetMapping("getInstructorById/{id}")
    public ResponseEntity<InstructorVO> getInstructorById(@PathVariable Long instructorId){
        Optional<InstructorVO> instructor = Optional.ofNullable(mapInstructor(instructorService.getInstructorById(instructorId)));

        return instructor.isPresent() ? ResponseEntity.ok().body(instructor.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("saveInstructor")
    public ResponseEntity<InstructorVO> saveInstructor(@RequestBody InstructorVO instructorVO){

        if(!Optional.ofNullable(instructorVO).isPresent()) return ResponseEntity.notFound().build();

        Instructor instructor = new Instructor(instructorVO.getFirstName(), instructorVO.getLastName(),
                instructorVO.getBirthday());

        instructorVO.getEvents().forEach(eventVO -> {
            instructor.getEvents().add(new Event(eventVO.getStart(), eventVO.getEnd(), eventVO.getType(), eventVO.getDescription(),
                    instructor));
        });


        instructorService.saveInstructor(instructor);

        instructorVO.setId(instructor.getId());

        return ResponseEntity.ok().body(instructorVO);
    }

    @PutMapping("updateInstructor/{id}")
    public ResponseEntity<InstructorVO> updateInstructor(@PathVariable Long instructorId, @RequestBody InstructorVO instructorVO){

        Optional<Instructor> instructor = Optional.ofNullable(instructorService.getInstructorById(instructorId));

        if(!instructor.isPresent()) return ResponseEntity.notFound().build();

        instructor.get().setBirthday(instructorVO.getBirthday());
        instructor.get().setFirstName(instructorVO.getFirstName());
        instructor.get().setLastName(instructorVO.getLastName());

        instructorService.saveInstructor(instructor.get());

        instructorVO.setId(instructor.get().getId());

        return ResponseEntity.ok().body(instructorVO);
    }

    @DeleteMapping("deleteMapping/{id}")
    public ResponseEntity<String> deleteInstructor(@PathVariable Long instructorId) {

        if(Optional.ofNullable(instructorService.getInstructorById(instructorId)).isPresent()) return ResponseEntity.notFound().build();

        instructorService.deleteInstructor(instructorId);

        return ResponseEntity.ok().body("Instructor deleted");
    }

    private List<InstructorVO> mapInstructors(List<Instructor> instructors) {

        List<InstructorVO> instructorVOS = new ArrayList<>();
        instructors.forEach(instructor -> {
            instructorVOS.add(mapInstructor(instructor));
        });
        return instructorVOS;
    }

    private InstructorVO mapInstructor(Instructor instructor){

        InstructorVO instructorVO = new InstructorVO();
        instructorVO.setId(instructor.getId());
        instructorVO.setBirthday(instructor.getBirthday());
        instructorVO.setEvents(instructor.getEvents()
                .stream()
                .map(event -> new EventVO(event.getId(), event.getStart(), event.getEnd(),
                        event.getType(), event.getDescription(), instructorVO)).collect(Collectors.toList()));
        instructorVO.setFirstName(instructor.getFirstName());
        instructorVO.setLastName(instructor.getLastName());
        return instructorVO;
    }

}
