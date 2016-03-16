package service;

import core.framework.api.async.Executor;
import core.framework.api.mongo.MongoCollection;
import domain.Product;
import domain.Sku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author neo
 */
public class MongoLoadTest {
    private final Logger logger = LoggerFactory.getLogger(MongoLoadTest.class);

    @Inject
    MongoCollection<Product> productCollection;

    @Inject
    Executor executor;

    @Inject
    MongoCollection<Sku> skuCollection;

    public void execute() throws ExecutionException, InterruptedException {
        logger.info("create empty product");
        int size = 100000;
        List<Future<Void>> futures = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            futures.add(executor.submit("insert", () -> {
                Product entity = new Product();
                entity.id = "neo-test-" + finalI;
                productCollection.replace(entity);
                return null;
            }));
        }
        for (Future<Void> future : futures) {
            future.get();
        }
    }
}
