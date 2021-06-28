package com.mayasoft.test.services.implement;

import com.mayasoft.test.models.entities.Event;
import com.mayasoft.test.models.repositories.EventRepository;
import com.mayasoft.test.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceImp implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long id) {
      Event event = eventRepository.getById(id);
      return event;
    }

    @Override
    @Transactional
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public void updateEvent(Event event) {
        saveEvent(event);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
