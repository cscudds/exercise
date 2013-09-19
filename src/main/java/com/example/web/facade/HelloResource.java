package com.example.web.facade;

import com.example.core.usecase.Greeter;
import com.example.web.model.GreetingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Produces(MediaType.APPLICATION_XML)
@Path("/hello")
public class HelloResource {
    @Autowired
    Greeter greeter;

    @GET
    public Response sayHello() {
        GreetingResponse greeting = new GreetingResponse(greeter.getGreeting());
        return Response.ok().entity(greeting).build();
    }
}
