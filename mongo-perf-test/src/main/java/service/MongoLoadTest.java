package service;

import core.framework.api.mongo.MongoCollection;
import domain.Product;
import domain.Sku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author neo
 */
public class MongoLoadTest {
    private final Logger logger = LoggerFactory.getLogger(MongoLoadTest.class);

    @Inject
    MongoCollection<Product> productCollection;

    @Inject
    MongoCollection<Sku> skuCollection;

    public void execute() {
        logger.info("start load test");


    }
}
