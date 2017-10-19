package server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("myvert/skierID/{skierID}/dayNum/{dayNum}")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class MyVertService {

    @GET
    public Response postMsg(@PathParam("skierID") String skierID, @PathParam("dayNum") String dayNum) {
        return Response.status(200).entity(((Integer) skierID.length()).toString()).build();
    }


}
