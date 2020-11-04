package org.javaschool;

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

    private String arrivalTime;

    private String departureTime;

    private boolean direction;

    private String endStation;
}