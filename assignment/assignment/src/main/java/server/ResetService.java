package server;

import utilities.DBConnectionPoolWrapper;
import utilities.RFIDLiftDataDAO;

import org.apache.commons.dbutils.DbUtils;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.SQLException;

@Path("/reset/{dayNum}")
@Produces(MediaType.TEXT_PLAIN)
public class ResetService {

    @DELETE
    public Response resetAll(@PathParam("dayNum") int dayNum) {
        Connection conn = null;
        try {
            conn = DBConnectionPoolWrapper.getConnection();
            RFIDLiftDataDAO dao = new RFIDLiftDataDAO(conn);
            if (dayNum != -1) {
                dao.deleteByDay(dayNum);
            } else {
                dao.reset();
            }
            return Response.ok().build();
        } catch (SQLException e) {
            return Response.serverError().build();
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }
}
