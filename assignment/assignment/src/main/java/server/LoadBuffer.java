package server;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Utilities.DBConnectionPoolWrapper;
import Utilities.OperationWrapper;
import Utilities.RFIDLiftDataDAO;
import bsdsass2testdata.RFIDLiftData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

public class LoadBuffer {
    private static final List<LoadHandler> handlers;
    private static final BlockingQueue<RFIDLiftData> buffer = new LinkedBlockingQueue<>();

    static {
        int db_conns = Integer.parseInt(OperationWrapper.readConfig("./resource/db_config.yml").get("db_connection_pool_size"));
        handlers = IntStream.range(0, db_conns).mapToObj(i -> new LoadHandler(buffer)).collect(Collectors.toList());
        handlers.forEach(LoadHandler::start);
    }

    static void put(RFIDLiftData r) {
        buffer.add(r);
    }
}

@AllArgsConstructor
class LoadHandler extends Thread {
    @Getter
    private final BlockingQueue<RFIDLiftData> queue;

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            RFIDLiftData r = queue.take();
            new RFIDLiftDataDAO(DBConnectionPoolWrapper.getConnection()).load(r);
        }
    }
}