package com.mayasoft.test.models.entities;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Date birthday;

    @Cascade({CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true )
    private List<Event> events = new ArrayList<>();

    public Instructor () {}

    public Instructor(String firstName, String lastName, Date birthday, List<Event> events) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.events = events;
    }

    public Instructor(String firstName, String lastName, Date birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
