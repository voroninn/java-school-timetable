package org.javaschool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    private int id;

    private String stationName;

    private String trainName;

    private String trainStatus;

    private String arrivalTime;

    private String departureTime;

    private boolean direction;

    private String endStation;

    public String getColorForTimetable() {
        switch (this.getTrainStatus()) {
            case "On Schedule":
                return "forestgreen";
            case "Delayed":
                return "goldenrod";
            case "Cancelled":
                return "red";
            default:
                return "black";
        }
    }
}