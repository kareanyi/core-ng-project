package service;

import core.framework.api.mongo.MongoCollection;
import core.framework.impl.async.ThreadPools;
import domain.Product;
import domain.Sku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author neo
 */
public class MongoLoadTest {
    private final Logger logger = LoggerFactory.getLogger(MongoLoadTest.class);
    private final ExecutorService executor = ThreadPools.cachedThreadPool(Runtime.getRuntime().availableProcessors() * 2, "executor-");
    @Inject
    MongoCollection<Product> productCollection;
    @Inject
    MongoCollection<Sku> skuCollection;

    public void execute() throws ExecutionException, InterruptedException {
        logger.info("create empty product");
        int size = 100000;
        List<Future<Void>> futures = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            Future<Void> future = executor.submit(() -> {
                Product entity = new Product();
                entity.id = "neo-test-" + finalI;
                productCollection.replace(entity);
                return null;
            });
            futures.add(future);
        }
        for (Future<Void> future : futures) {
            future.get();
        }
    }
}
