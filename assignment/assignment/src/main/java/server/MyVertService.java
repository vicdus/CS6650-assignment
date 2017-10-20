package server;

import Utilities.FakeStorage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("myvert/skierID/{skierID}/dayNum/{dayNum}")
@Produces(MediaType.TEXT_PLAIN)
public class MyVertService {

    @GET
    public Response get(@PathParam("skierID") int skierID, @PathParam("dayNum") int dayNum) {
        Integer x = FakeStorage.foo
                .stream()
                .filter(r -> r.getLiftID() == skierID && r.getDayNum() == dayNum)
                .mapToInt(r -> 1)
                .sum();
        System.out.println(skierID + " " + dayNum + " " + x);
        return Response.ok(x.toString(), MediaType.TEXT_PLAIN).build();
    }

}
