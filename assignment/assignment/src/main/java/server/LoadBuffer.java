package server;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Utilities.Counter;
import Utilities.DBConnectionPoolWrapper;
import Utilities.OperationWrapper;
import Utilities.RFIDLiftDataDAO;
import Utilities.Stopwatch;
import bsdsass2testdata.RFIDLiftData;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

public class LoadBuffer {
    private static final BlockingQueue<RFIDLiftData> buffer = new LinkedBlockingQueue<>();
    private static final int LOAD_THREAD_SIZE = 10;

    static {
        List<LoadHandler> handlers = IntStream.
                range(0, LOAD_THREAD_SIZE)
                .mapToObj(i -> new LoadHandler(buffer))
                .collect(Collectors.toList());
        handlers.forEach(LoadHandler::start);
    }

    static void put(RFIDLiftData r) {
        buffer.add(r);
    }
}

@AllArgsConstructor
class LoadHandler extends Thread {
    private final BlockingQueue<RFIDLiftData> queue;

    @Override
    @SneakyThrows
    public void run() {
        Connection conn = DBConnectionPoolWrapper.getConnection();
        Stopwatch s = Stopwatch.createStopwatch();
        RFIDLiftDataDAO dao = new RFIDLiftDataDAO(conn);

        while (true) {
            int total = Counter.increase();
            int size = queue.size();
            RFIDLiftData r = queue.take();
            s.start();
            dao.load(r);
            if (size < 100 || size % 1000 == 0) {
                dao.commit();
                if (total % 1000 == 0)
                    System.out.println("commit data " + s.readAndReset() + ", queue size = " + size);
            }
        }
    }
}