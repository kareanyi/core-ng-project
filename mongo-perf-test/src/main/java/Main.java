import core.framework.api.mongo.Mongo;
import core.framework.impl.mongo.MongoImpl;
import service.MongoLoadTest;

import java.util.concurrent.ExecutionException;

/**
 * @author neo
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MongoPerfTestApp app = new MongoPerfTestApp();
        app.configure();

        MongoImpl mongo = (MongoImpl) app.bean(Mongo.class);
        mongo.initialize();

//        MongoPreWarmer preWarm = app.bean(MongoPreWarmer.class);
//        preWarm.execute();

        MongoLoadTest test = app.bean(MongoLoadTest.class);
        test.execute();
    }
}
