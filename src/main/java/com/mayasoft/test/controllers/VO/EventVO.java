package com.mayasoft.test.controllers.VO;

import java.util.Date;

public class EventVO {

    private Long id;

    private Date start;
    private Date end;
    private String type;
    private String description;
    private InstructorVO instructorVO;
    private Long daysBetween;

    public EventVO() {}

    public EventVO(Long id, Date start, Date end, String type, String description) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.type = type;
        this.description = description;
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

    public InstructorVO getInstructorVO() {
        return instructorVO;
    }

    public void setInstructorVO(InstructorVO instructorVO) {
        this.instructorVO = instructorVO;
    }

  public Long getDaysBetween() {
    return daysBetween;
  }

  public void setDaysBetween(Long daysBetween) {
    this.daysBetween = daysBetween;
  }
}
