package Utilities;

import java.sql.*;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;

import bsdsass2testdata.RFIDLiftData;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RFIDLiftDataDAO {
    private static final String LOAD_SQL = "INSERT INTO RFIDLiftData (resortID, dayNum, skierID, liftID, time) VALUES (?,?,?,?,?)";
    private static final String VERT_SQL = "SELECT liftID FROM RFIDLiftData WHERE skierID=? AND dayNum=?";
    private static final String RESET_SQL = "DELETE FROM RFIDLiftData";
    private static Map<Integer, Integer> liftHeights;
    private Connection c;

    static {
        ImmutableMap.Builder<Integer, Integer> builder = ImmutableMap.builder();
        OperationWrapper
                .readConfig("./resource/lift.yml")
                .forEach((k, v) -> builder.put(Integer.parseInt(k), Integer.parseInt(v)));
        liftHeights = builder.build();
    }


    public void reset() throws SQLException {
        c.createStatement().execute(RESET_SQL);
    }

    public Integer[] vert(int skierID, int dayNum) throws SQLException {
        PreparedStatement p;
        ResultSet rs;
        p = c.prepareCall(VERT_SQL);
        p.setInt(1, skierID);
        p.setInt(2, dayNum);
        rs = p.executeQuery();

        int res = 0, count = 0;
        while (rs.next()) {
            int lift = rs.getInt(1);
            res += lift * liftHeights.get(lift);
            count++;
        }
        DbUtils.closeQuietly(c, p, rs);
        return new Integer[]{count, res};
    }

    public void load(RFIDLiftData data) throws SQLException {
        Preconditions.checkArgument(c != null, "not init yet!");
        PreparedStatement p;
        p = c.prepareStatement(LOAD_SQL);
        p.setInt(1, data.getResortID());
        p.setInt(2, data.getDayNum());
        p.setInt(3, data.getSkierID());
        p.setInt(4, data.getLiftID());
        p.setInt(5, data.getTime());
        p.execute();
        DbUtils.closeQuietly(c, p, null);
    }
}
