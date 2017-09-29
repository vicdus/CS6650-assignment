package server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("")
public class EchoService {

    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {
        return Response.status(200).entity("alive").build();
    }

    @POST
    @Path("/{param}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response postMsg(@PathParam("param") String msg) {
        return Response.status(200).entity(((Integer) msg.length()).toString()).build();
    }

}
