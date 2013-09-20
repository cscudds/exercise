package com.example.web.facade;

import com.example.core.repository.LongRunningLockedProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component
@Path("/process")
public class ProcessResource {
    @Autowired
    LongRunningLockedProcess process;

    @POST
    public Response processPost() {
        process.doSomething();
        return Response.ok().build();
    }

}
