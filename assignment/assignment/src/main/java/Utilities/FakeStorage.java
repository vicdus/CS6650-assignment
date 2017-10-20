package Utilities;

import bsdsass2testdata.RFIDLiftData;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FakeStorage {
    public static Queue<RFIDLiftData> foo = new ConcurrentLinkedQueue<>();
}
