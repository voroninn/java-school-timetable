package org.javaschool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableService implements Serializable {

    private List<Schedule> schedules = new ArrayList<>();

    @PostConstruct
    public void init() {
        updateSchedules();
        log.info("Timetable successfully initialized");
    }

    public void updateSchedules() {
        log.info("Sending request to 1st app");
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8880/timetable/Naboo");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        String schedulesJson = response.getEntity(String.class);
        log.info("Received response from 1st app: " + schedulesJson);
        schedules = parseSchedules(schedulesJson);
    }

    public List<Schedule> parseSchedules(String schedulesJson) {
        Type collectionType = new TypeToken<ArrayList<Schedule>>(){}.getType();
        return new Gson().fromJson(schedulesJson, collectionType);
    }
}