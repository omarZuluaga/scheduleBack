package com.mayasoft.test.controllers.VO;

import com.mayasoft.test.models.entities.Event;

import java.util.Date;
import java.util.List;

public class InstructorVO {

    private Long id;
    private String firstName;
    private String lastName;
    private Date birthday;
    private List<EventVO> events;
    private Long overallEventsDuration;

    public InstructorVO () {}

    public InstructorVO(Long id, String firstName, String lastName, Date birthday, List<EventVO> events) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<EventVO> getEvents() {
        return events;
    }

    public void setEvents(List<EventVO> events) {
        this.events = events;
    }

    public Long getOverallEventsDuration() {
        return overallEventsDuration;
    }

    public void setOverallEventsDuration(Long overallEventsDuration) {
        this.overallEventsDuration = overallEventsDuration;
    }
}
