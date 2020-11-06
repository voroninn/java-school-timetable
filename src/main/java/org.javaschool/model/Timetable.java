package org.javaschool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.javaschool.service.TimetableService;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named("timetable")
@ApplicationScoped
@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timetable implements Serializable {

    private int activeIndex;

    @EJB
    private TimetableService timetableService;

    public List<Map.Entry<String, List<Schedule>>> getSchedules() {
        return timetableService.getSchedules();
    }

    public void onTabChange(TabChangeEvent event) {
        log.info("TabChangeEvent: current active index: " + activeIndex);
        activeIndex = ((TabView) event.getComponent()).getActiveIndex();
    }
}