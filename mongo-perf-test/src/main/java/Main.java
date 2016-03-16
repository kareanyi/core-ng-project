import service.MongoLoadTest;

/**
 * @author neo
 */
public class Main {
    public static void main(String[] args) {
        MongoPerfTestApp app = new MongoPerfTestApp();
        app.configure();

        MongoLoadTest test = app.bean(MongoLoadTest.class);
        test.execute();
    }
}
