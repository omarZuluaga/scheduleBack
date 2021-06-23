package com.mayasoft.test.services;

import com.mayasoft.test.entities.Event;

import java.util.List;

public interface EventService {

    List<Event> getEvents();
    Event getEventById(Long id);
    void saveEvent(Event event);
    void updateEvent(Event event);
    void deleteEvent(Long id);
}
