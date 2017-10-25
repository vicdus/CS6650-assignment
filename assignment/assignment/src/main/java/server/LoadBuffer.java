package server;

import java.sql.Connection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Utilities.DBConnectionPoolWrapper;
import Utilities.RFIDLiftDataDAO;
import bsdsass2testdata.RFIDLiftData;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

public class LoadBuffer {
    private static final BlockingQueue<RFIDLiftData> buffer = new LinkedBlockingQueue<>();

    static {
//        new LoadHandler(buffer).start();
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                while (true) {
                    RFIDLiftData r = buffer.take();
                    Connection conn = DBConnectionPoolWrapper.getConnection();
                    new RFIDLiftDataDAO(conn).load(r);
                    conn.close();
                }
            }
        }.start();
    }

    static void put(RFIDLiftData r) {
        buffer.add(r);
    }
}

//@AllArgsConstructor
//class LoadHandler extends Thread {
//    private final BlockingQueue<RFIDLiftData> queue;
//
//    @Override
//    @SneakyThrows
//    public void run() {
//        while (true) {
//            RFIDLiftData r = queue.take();
//            Connection conn = DBConnectionPoolWrapper.getConnection();
//            new RFIDLiftDataDAO(conn).load(r);
//            conn.close();
//        }
//    }
//}