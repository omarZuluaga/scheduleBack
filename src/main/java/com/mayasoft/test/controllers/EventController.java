package com.mayasoft.test.controllers;

import com.mayasoft.test.controllers.VO.EventVO;
import com.mayasoft.test.models.entities.Event;
import com.mayasoft.test.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<EventVO> getEvents () {

        return map(eventService.getEvents());
    }

    @GetMapping("/{id}")
    public EventVO getEventById (@PathVariable Long id) {
        Event event = eventService.getEventById(id);

        return map(event);
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
