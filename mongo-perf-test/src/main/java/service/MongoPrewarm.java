package service;

import core.framework.api.mongo.MongoCollection;
import core.framework.api.mongo.Query;
import domain.Product;
import domain.Sku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author neo
 */
public class MongoPreWarm {
    private final Logger logger = LoggerFactory.getLogger(MongoPreWarm.class);

    @Inject
    MongoCollection<Product> productCollection;

    @Inject
    MongoCollection<Sku> skuCollection;

    public void execute() {
        logger.info("start per warm");

        logger.info("iterate all product");
        productCollection.forEach(new Query(), product -> {
        });

        logger.info("iterate all sku");
        skuCollection.forEach(new Query(), sku -> {
        });
    }
}
