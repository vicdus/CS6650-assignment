package server;

import Utilities.DBConnectionPoolWrapper;
import Utilities.RFIDLiftDataDAO;

import org.apache.commons.dbutils.DbUtils;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.SQLException;

@Path("/reset")
@Produces(MediaType.TEXT_PLAIN)
public class ResetService {
    @DELETE
    public Response resetAll() {
        Connection conn = null;
        try {
            conn = DBConnectionPoolWrapper.getConnection();
            new RFIDLiftDataDAO(conn).reset();
            return Response.ok().build();
        } catch (SQLException e) {
            return Response.serverError().build();
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }
}
