package org.javaschool.service;

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
import org.javaschool.model.Schedule;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Stateless
@Lock(LockType.READ)
@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableService implements Serializable {

    private List<Map.Entry<String, List<Schedule>>> schedules = new ArrayList<>();

    @Inject
    @Push(channel = "timetableChannel")
    private PushContext pushContext;

    @PostConstruct
    public void init() {
        updateSchedules();
        log.info("Timetable successfully initialized");
    }

    public void updateSchedules() {
        log.info("Requesting schedules from 1st app");
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8880/timetable/all");
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);
        String schedulesJson = response.getEntity(String.class);
        log.info("Received response from 1st app: " + schedulesJson);
        schedules = parseSchedules(schedulesJson);
        pushContext.send("Timetable updated");
    }

    public List<Map.Entry<String, List<Schedule>>> parseSchedules(String schedulesJson) {
        Type collectionType = new TypeToken<TreeMap<String, List<Schedule>>>() {}.getType();
        TreeMap<String, List<Schedule>> schedules = new Gson().fromJson(schedulesJson, collectionType);
        return new ArrayList<>(schedules.entrySet());
    }
}