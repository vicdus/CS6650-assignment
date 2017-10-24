package server;

import Utilities.DBConnectionPoolWrapper;
import Utilities.Counter;
import Utilities.RFIDLiftDataDAO;
import bsdsass2testdata.RFIDLiftData;

import org.apache.commons.dbutils.DbUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.SQLException;


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
        RFIDLiftData r = new RFIDLiftData(resortID, dayNum, skierID, liftID, timestamp);
        Connection c = null;
        try {
            c = DBConnectionPoolWrapper.getConnection();
            new RFIDLiftDataDAO(c).load(r);
            return Response
                    .status(200)
                    .entity("success")
                    .build();
        } catch (SQLException e) {
            return Response.serverError().build();
        } finally {
            DbUtils.closeQuietly(c);
        }
    }
}
