package com.github.muirandy;

import com.github.muirandy.docs.living.api.Log;
import com.github.muirandy.docs.living.api.Logs;

import javax.ws.rs.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/log")
public class LogResource {

    private Map<String, Logs> markedLogs = createNewMarkedLogs();
    private Logs logs = new Logs();

    @GET
    @Path("/read/{marker}")
    public Logs read(@PathParam("marker") String marker) {
        if (markedLogs.containsKey(marker))
            return markedLogs.get(marker);
        return new Logs();
    }

    @GET
    @Path("/read")
    public Logs readAll() {
        Logs allLogs = new Logs();
        markedLogs.values().stream()
                  .flatMap(ls -> ls.getLogs().stream())
                  .forEach(l -> allLogs.add(l));
        logs.getLogs().stream()
            .forEach(l -> allLogs.add(l));
        return allLogs;
    }

    @POST
    public void storeLog(Log log) {
        logs.add(log);
    }

    @DELETE
    public void reset() {
        logs = new Logs();
        markedLogs = createNewMarkedLogs();
    }

    private LinkedHashMap<String, Logs> createNewMarkedLogs() {
        return new LinkedHashMap<>();
    }

    @POST
    @Path("/markEnd/{marker}")
    public void markEnd(@PathParam("marker") String marker) {
        markedLogs.put(marker, logs);
        logs = new Logs();
    }
}