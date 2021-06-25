package com.mayasoft.test.controllers;

import com.mayasoft.test.controllers.VO.EventVO;
import com.mayasoft.test.models.entities.Event;
import com.mayasoft.test.models.entities.Instructor;
import com.mayasoft.test.services.EventService;
import com.mayasoft.test.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("events/")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private InstructorService instructorService;

    @GetMapping
    public ResponseEntity<List<EventVO>> getEvents () {

        return ResponseEntity.ok().body(map(eventService.getEvents()));
    }

    @GetMapping("getEventById/{id}")
    public ResponseEntity<EventVO> getEventById (@PathVariable Long id) {
        Event event = eventService.getEventById(id);

        if(event == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(map(event));
    }

    @PostMapping("saveEvent")
    public ResponseEntity<String> saveEvent (@RequestBody EventVO eventVO){
        Instructor instructor = instructorService.getInstructorById(eventVO.getInstructorVO().getId());


        Event event = new Event(eventVO.getStart(), eventVO.getEnd(), eventVO.getType(), eventVO.getType(), instructor);
        eventService.saveEvent(event);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("updateEvent/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody EventVO eventVO){
        Event event = eventService.getEventById(id);

        if(event == null) {
            return ResponseEntity.notFound().build();
        }

        handleEventUpdate(event, eventVO);

        eventService.updateEvent(event);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("deleteEvent/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }

    private void handleEventUpdate(Event event, EventVO eventVO) {
        if (eventVO.getDescription() != null) event.setDescription(eventVO.getDescription());
        if(eventVO.getEnd() != null) event.setEnd(eventVO.getEnd());
        if(eventVO.getStart() != null) event.setStart(eventVO.getStart());
        if(eventVO.getType() != null) event.setType(eventVO.getType());
    }


    private EventVO map(Event event){
        EventVO eventVO = new EventVO();

        eventVO.setId(event.getId());
        eventVO.setDescription(eventVO.getDescription());
        eventVO.setStart(event.getStart());
        eventVO.setEnd(event.getEnd());
        eventVO.setType(event.getType());
        return eventVO;
    }

    private List<EventVO> map(List<Event> events) {
        List<EventVO> eventVOS = new ArrayList<>();
        events.forEach(event -> {
            eventVOS.add(map(event));
        });

        return eventVOS;
    }
}
