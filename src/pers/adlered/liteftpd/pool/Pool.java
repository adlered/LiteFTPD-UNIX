package pers.adlered.liteftpd.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <h3>LiteFTPD-UNIX</h3>
 * <p>Public class to store thread pools.</p>
 *
 * @author : https://github.com/AdlerED
 * @date : 2019-09-19 09:21
 **/
public class Pool {
    public static ExecutorService handlerPool = Executors.newCachedThreadPool();
}
