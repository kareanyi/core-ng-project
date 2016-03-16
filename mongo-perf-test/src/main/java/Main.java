import service.MongoLoadTest;

import java.util.concurrent.ExecutionException;

/**
 * @author neo
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MongoPerfTestApp app = new MongoPerfTestApp();
        app.configure();

//        MongoPreWarmer preWarm = app.bean(MongoPreWarmer.class);
//        preWarm.execute();

        MongoLoadTest test = app.bean(MongoLoadTest.class);
        test.execute();
    }
}
