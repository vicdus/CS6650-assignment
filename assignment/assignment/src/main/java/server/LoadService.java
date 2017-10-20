package server;

import Utilities.Counter;
import Utilities.FakeStorage;
import bsdsass2testdata.RFIDLiftData;

import javax.ws.rs.*;
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
        System.out.println(skierID + " " + dayNum);
        System.out.println("s = " + Counter.increase());

        RFIDLiftData r = new RFIDLiftData(resortID, dayNum, timestamp, skierID, liftID);
        FakeStorage.foo.add(r);
        return Response
                .status(200)
                .entity("success")
                .build();
    }
}
