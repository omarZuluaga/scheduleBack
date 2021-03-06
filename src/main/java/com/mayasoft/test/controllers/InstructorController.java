package com.mayasoft.test.controllers;

import com.mayasoft.test.controllers.VO.EventVO;
import com.mayasoft.test.controllers.VO.InstructorVO;
import com.mayasoft.test.models.entities.Event;
import com.mayasoft.test.models.entities.Instructor;
import com.mayasoft.test.services.EventService;
import com.mayasoft.test.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<InstructorVO>> getInstructors() {

      if(!Optional.ofNullable(instructorService.getInstructors()).isPresent()){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

      List<InstructorVO> instructorVOS = mapInstructors(instructorService.getInstructors());

      return ResponseEntity.ok().body(instructorVOS);

    }

    @GetMapping("/{instructorId}")
    public ResponseEntity<InstructorVO> getInstructorById(@PathVariable Long instructorId){
        Optional<InstructorVO> instructor = Optional.ofNullable(mapInstructor(instructorService.getInstructorById(instructorId)));

        instructor.get().setOverallEventsDuration(getOverallEventsDuration(instructor.get().getEvents()));

        return new ResponseEntity<>(instructor.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InstructorVO> saveInstructor(@RequestBody InstructorVO instructorVO){

        if(!Optional.ofNullable(instructorVO).isPresent()) return ResponseEntity.notFound().build();

        Instructor instructor = new Instructor(instructorVO.getFirstName(), instructorVO.getLastName(),
                instructorVO.getBirthday());


        instructorService.saveInstructor(instructor);

        instructorVO.setId(instructor.getId());

        return ResponseEntity.ok().body(instructorVO);
    }

    @PutMapping("/{instructorId}")
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

    @DeleteMapping("/{instructorId}")
    public ResponseEntity<String> deleteInstructor(@PathVariable Long instructorId) {

        if(!Optional.ofNullable(instructorService.getInstructorById(instructorId)).isPresent()) return ResponseEntity.notFound().build();

        instructorService.deleteInstructor(instructorId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

  @DeleteMapping("/{instructorId}/events/{eventId}")
  public ResponseEntity<InstructorVO> deleteEvent(@PathVariable Long instructorId, @PathVariable Long eventId){

      Instructor instructor = isInstructor(instructorId);

      if( instructor == null){
        return ResponseEntity.notFound().build();
      }

      Optional<Event> event = Optional.ofNullable(eventService
      .getEventById(eventId));
      try {
        event.orElseThrow(NullPointerException::new);
      } catch (NullPointerException e) {
        e.printStackTrace();
        return ResponseEntity.notFound().build();
      }

      instructor.getEvents().remove(event.get());
      instructorService.updateInstructor(instructor);
      return ResponseEntity.ok().body(mapInstructor(instructor));
  }

  @GetMapping("/{instructorId}/events")
  public ResponseEntity<List<EventVO>> getEvents (@PathVariable Long instructorId) {

      Instructor instructor = isInstructor(instructorId);
      return ResponseEntity.ok().body(mapEvents(instructor.getEvents()));
  }

  @GetMapping("/{instructorId}/events/{eventId}")
  public ResponseEntity<EventVO> getEventById (@PathVariable Long instructorId, @PathVariable Long eventId) {

    if(isInstructor(instructorId) == null) {
      return ResponseEntity.notFound().build();
    }

    Optional<Event> event = Optional.ofNullable(eventService
      .getEventById(eventId));

    try {
      event.orElseThrow(NullPointerException::new);
    } catch (NullPointerException e) {
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(mapEvent(event.get()));
  }

  @PostMapping("/{instructorId}/events")
  public ResponseEntity<?> saveEvent (@PathVariable Long instructorId, @RequestBody EventVO eventVO) {

    Instructor instructor = isInstructor(instructorId);


    if(!Optional.ofNullable(eventVO.getInstructorVO().getId()).isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if(eventVO.getStart().after(eventVO.getEnd())){
        return ResponseEntity.badRequest().body("the start date cannot be greater than end date");
    }

    if(getDaysBetween(eventVO.getStart(), eventVO.getEnd()) > 7){
        return ResponseEntity.badRequest().body("The event cannot last more than a week (7 days)");
    }

    Event event = new Event(eventVO.getStart(), eventVO.getEnd(), eventVO.getType(),
      Optional.ofNullable(eventVO.getDescription()).isPresent() ? eventVO.getDescription() : "");
    eventService.saveEvent(event);

    eventVO.setId(event.getId());
    eventVO.setDaysBetween(getDaysBetween(eventVO.getStart(), eventVO.getEnd()));

    instructor.getEvents().add(event);

    instructorService.updateInstructor(instructor);

    return ResponseEntity.ok().body(eventVO);
  }

  @PutMapping("/{instructorId}/events/{eventId}")
  public ResponseEntity<EventVO> updateEvent(@PathVariable Long instructorId, @PathVariable Long eventId, @RequestBody EventVO eventVO){

      if(isInstructor(instructorId) == null){
        return ResponseEntity.notFound().build();
      }

      Event event = eventService.getEventById(eventId);

      if(event == null) {
        return ResponseEntity.notFound().build();
      }

      handleEventUpdate(event, eventVO);

      eventService.updateEvent(event);
      eventVO.setId(eventId);

      return ResponseEntity.ok().body(eventVO);

  }

  private void handleEventUpdate(Event event, EventVO eventVO) {
    if (eventVO.getDescription() != null) event.setDescription(eventVO.getDescription());
    if(eventVO.getEnd() != null) event.setEnd(eventVO.getEnd());
    if(eventVO.getStart() != null) event.setStart(eventVO.getStart());
    if(eventVO.getType() != null) event.setType(eventVO.getType());
  }


  private EventVO mapEvent(Event event) {

    EventVO eventVO = new EventVO();

    eventVO.setId(event.getId());
    eventVO.setDescription(event.getDescription());
    eventVO.setStart(event.getStart());
    eventVO.setEnd(event.getEnd());
    eventVO.setType(event.getType());
    eventVO.setDaysBetween(getDaysBetween(event.getStart(), event.getEnd()));
      return eventVO;
  }

  private List<EventVO> mapEvents(List<Event> events) {
    List<EventVO> eventVOS = new ArrayList<>();
    events.forEach(event -> {
      eventVOS.add(mapEvent(event));
    });

    return eventVOS;
  }

  private Long getDaysBetween(Date start, Date end) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date startDate = sdf.parse(sdf.format(start));
        System.out.println(startDate);
        Date endDate = sdf.parse(sdf.format(end));

        return ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());
    }catch (ParseException e){
        e.printStackTrace();
    }
    return null;
  }

  private Long getOverallEventsDuration(List<EventVO> eventVOS){
        AtomicReference<Long> overallDuration = new AtomicReference<>(0L);

        eventVOS.forEach(eventVO -> {
            overallDuration.updateAndGet(v -> v + eventVO.getDaysBetween());
        });

        return overallDuration.get();
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
        instructorVO.setFirstName(instructor.getFirstName());
        instructorVO.setLastName(instructor.getLastName());
        instructorVO.setEvents(instructor.getEvents()
        .stream()
        .map(event -> new EventVO(event.getId(), event.getStart(), event.getEnd(),
          event.getType(), event.getDescription(), getDaysBetween(event.getStart(), event.getEnd()))).collect(Collectors.toList()));
        instructorVO.setOverallEventsDuration(getOverallEventsDuration(mapEvents(instructor.getEvents())));
        System.out.println(instructorVO.getOverallEventsDuration());
        return instructorVO;
    }

    private Instructor isInstructor ( Long instructorId ){
      Optional<Instructor> instructor = Optional.ofNullable(instructorService.getInstructorById(instructorId));
      try {
        instructor.orElseThrow(NullPointerException::new);
        return instructor.get();
      } catch (NullPointerException e){
        e.printStackTrace();
        return null;
      }
    }

}
