package org.acme;

import java.util.Map;
import java.util.StringJoiner;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/cfg")
public class GreetingResource {

    @Inject
    DemoConfig config;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        StringJoiner j = new StringJoiner("\n");
        for (Map.Entry<String, DemoConfig.Inner> entry : config.inners().entrySet()) {
            j.add("Key: " + entry.getKey());
            var foo = entry.getValue();
            j.add("  ID: " + foo.id());
            foo.data().ifPresent(data -> j.add("  Data: " + data));
        }
        return j.toString();
    }
}
