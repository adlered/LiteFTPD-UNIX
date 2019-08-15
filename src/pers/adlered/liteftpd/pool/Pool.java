package pers.adlered.liteftpd.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pool {
    public static ExecutorService handlerPool = Executors.newCachedThreadPool();
}
