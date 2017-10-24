package server;

import Utilities.Counter;
import bsdsass2testdata.RFIDLiftData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("load/resortID/{resortID}/dayNum/{dayNum}/timestamp/{timestamp}/skierID/{skierID}/liftID/{liftID}")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class LoadService {

    @POST
    public Response getMsg(@PathParam("resortID") int resortID,
                           @PathParam("dayNum") int dayNum,
                           @PathParam("timestamp") int timestamp,
                           @PathParam("skierID") int skierID,
                           @PathParam("liftID") int liftID
    ) {
        System.out.println(Counter.increase());
        LoadBuffer.put(new RFIDLiftData(resortID, dayNum, skierID, liftID, timestamp));
        return Response.ok("success").build();
    }
}