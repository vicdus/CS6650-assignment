package utilities;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private static final String COMMIT_SQL = "COMMIT";

    private static Map<Integer, Integer> liftHeights;
    private Connection c;

    static {
        ImmutableMap.Builder<Integer, Integer> builder = ImmutableMap.builder();
        OperationWrapper
                .readConfig("./resource/lift.yml")
                .forEach((k, v) -> builder.put(Integer.parseInt(k), Integer.parseInt(v)));
        liftHeights = builder.build();
    }

    public void commit() throws SQLException {
        try (Statement s = c.createStatement()) {
            s.execute(COMMIT_SQL);
        }
    }

    public void reset() throws SQLException {
        try (Statement s = c.createStatement()) {
            s.execute(RESET_SQL);
        }
    }

    public Integer[] vert(int skierID, int dayNum) throws SQLException {
        try (CallableStatement p = c.prepareCall(VERT_SQL)) {
            p.setInt(1, skierID);
            p.setInt(2, dayNum);
            try (ResultSet rs = p.executeQuery()) {
                int res = 0;
                int count = 0;
                while (rs.next()) {
                    int lift = rs.getInt(1);
                    res += lift * liftHeights.get(lift);
                    count++;
                }
                DbUtils.closeQuietly(null, p, rs);
                return new Integer[]{count, res};
            }
        }
    }

    public void load(RFIDLiftData data) throws SQLException {
        Preconditions.checkArgument(c != null, "not init yet!");
        try (PreparedStatement p = c.prepareStatement(LOAD_SQL)) {
            p.setInt(1, data.getResortID());
            p.setInt(2, data.getDayNum());
            p.setInt(3, data.getSkierID());
            p.setInt(4, data.getLiftID());
            p.setInt(5, data.getTime());
            p.execute();
            DbUtils.closeQuietly(null, p, null);
        }
    }
}
