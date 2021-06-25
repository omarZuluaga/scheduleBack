package com.mayasoft.test.controllers;

import com.mayasoft.test.controllers.VO.EventVO;
import com.mayasoft.test.controllers.VO.InstructorVO;
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

    @GetMapping("getInstructorById/{/id}")
    public ResponseEntity<InstructorVO> getInstructorById(@PathVariable Long instructorId){
        Optional<InstructorVO> instructor = Optional.ofNullable(mapInstructor(instructorService.getInstructorById(instructorId)));

        return instructor.isPresent() ? ResponseEntity.ok().body(instructor.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("saveInstructor")
    public ResponseEntity<String> saveInstructor(@RequestBody InstructorVO instructorVO){

        //Optional.ofNullable(instructorVO).isPresent() ? return instructorService.saveInstructor(new Instructor(instructorVO.getFirstName()));
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
