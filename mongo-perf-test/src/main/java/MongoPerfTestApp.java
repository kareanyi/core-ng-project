import core.framework.api.App;
import domain.Product;
import domain.Sku;
import service.MongoLoadTest;
import service.MongoPreWarmer;

/**
 * @author neo
 */
public class MongoPerfTestApp extends App {
    @Override
    protected void initialize() {
        mongo().uri("mongodb://10.0.0.110/main");
        mongo().collection(Product.class);
        mongo().collection(Sku.class);

        bind(MongoLoadTest.class);
        bind(MongoPreWarmer.class);
    }
}
