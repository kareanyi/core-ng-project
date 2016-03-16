import service.MongoLoadTest;
import service.MongoPreWarm;

/**
 * @author neo
 */
public class Main {
    public static void main(String[] args) {
        MongoPerfTestApp app = new MongoPerfTestApp();
        app.configure();

        MongoPreWarm preWarm = app.bean(MongoPreWarm.class);
        preWarm.execute();

        MongoLoadTest test = app.bean(MongoLoadTest.class);
        test.execute();
    }
}
