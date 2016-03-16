import core.framework.api.App;
import domain.Product;
import domain.Sku;
import service.MongoLoadTest;

/**
 * @author neo
 */
public class MongoPerfTestApp extends App {
    @Override
    protected void initialize() {
        mongo().uri("mongodb://52.91.96.94/main");
        mongo().collection(Product.class);
        mongo().collection(Sku.class);

        bind(MongoLoadTest.class);
    }
}
