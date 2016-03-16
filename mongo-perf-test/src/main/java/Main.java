import service.MongoLoadTest;
import service.MongoPreWarmer;

/**
 * @author neo
 */
public class Main {
    public static void main(String[] args) {
        MongoPerfTestApp app = new MongoPerfTestApp();
        app.configure();

        MongoPreWarmer preWarm = app.bean(MongoPreWarmer.class);
        preWarm.execute();

        MongoLoadTest test = app.bean(MongoLoadTest.class);
        test.execute();
    }
}
