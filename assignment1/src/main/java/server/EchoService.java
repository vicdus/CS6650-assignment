package server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/{param}")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class EchoService {

    @GET
    public Response getMsg(@PathParam("param") String msg) {
        return Response.status(200).entity("alive").build();
    }

    @POST
    public Response postMsg(@PathParam("param") String msg) {
        return Response.status(200).entity(((Integer) msg.length()).toString()).build();
    }

}
