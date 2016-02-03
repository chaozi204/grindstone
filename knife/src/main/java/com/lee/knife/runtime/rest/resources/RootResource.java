package com.lee.knife.runtime.rest.resources;

import com.lee.knife.runtime.rest.entities.Message;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {

    @GET
    @Path("/")
    public Message serverInfo() {
        return new Message(400,"You have no privilege to request.");
    }
}
