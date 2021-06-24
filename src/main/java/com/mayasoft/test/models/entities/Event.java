package com.mayasoft.test.models.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date start;
    private Date end;
    private String type;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private Instructor instructor;


    public Event () {}

    public Event(Date start, Date end, String type, String description, Instructor instructor) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.description = description;
        this.instructor = instructor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}
