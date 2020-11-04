package org.javaschool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("timetable")
@SessionScoped
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timetable implements Serializable {

    @EJB
    private TimetableService timetableService;

    public List<Schedule> getSchedules() {
        return timetableService.getSchedules();
    }
}