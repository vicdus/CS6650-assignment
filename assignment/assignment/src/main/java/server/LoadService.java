package server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("load/resortID/{resortID}/dayNum/{dayNum}/timestamp/{timestamp}/skierID/{skierID}/liftID/{liftID}")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class LoadService {

    @POST
    public Response getMsg(@PathParam("resortID") String resortID,
                           @PathParam("dayNum") String dayNum,
                           @PathParam("timestamp") String timestamp,
                           @PathParam("skierID") String skierID,
                           @PathParam("liftID") String liftID
    ) {
        System.out.println(resortID + dayNum + timestamp + skierID + liftID);
        return Response
                .status(200)
                .entity("alive")
                .build();
    }
}
