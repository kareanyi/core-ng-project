package service;

import core.framework.api.mongo.MongoCollection;
import core.framework.api.util.Lists;
import core.framework.api.util.Randoms;
import core.framework.impl.async.ThreadPools;
import domain.Product;
import domain.ProductImage;
import domain.ProductType;
import domain.Sku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

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
//        createEmptyProducts();
//        deleteProducts();
        createFullProducts();

        executor.shutdown();
    }

    private void deleteProducts() throws InterruptedException {
        logger.info("delete all products");
        int size = 1000000;
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            executor.submit(() -> {
                productCollection.delete("neo-test-" + finalI);
                latch.countDown();
                return null;
            });
        }
        latch.await();
    }

    private void createEmptyProducts() throws InterruptedException {
        logger.info("create empty product");
        int size = 1000000;
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            executor.submit(() -> {
                Product entity = new Product();
                entity.id = "neo-test-" + finalI;
                productCollection.replace(entity);
                latch.countDown();
                return null;
            });
        }
        latch.await();
    }

    private void createFullProducts() throws InterruptedException {
        logger.info("create full products");
        LocalDateTime now = LocalDateTime.now();
        int size = 1000000;
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            final int id = (int) Randoms.number(0, size);
            executor.submit(() -> {
                Product entity = new Product();
                entity.id = "neo-test-" + id;
                entity.categoryId = "watchessunglassesjewelry-jewelry-women-" + id;
                entity.name = "Aurelie Bidermann Monteroso Leaf Bangle";
                entity.description = "A hammered, gold-plated Aurelie Bidermann cuff with elegant appeal. A hit of colorful thread accentuates the intricate leaf design. Malleable. Gold plate. Made in France. Measurements Diameter: 2.5in / 6.5cm";
                entity.images = Lists.newArrayList(image("main", "product/V00013/ABIDE30085/main-1245260503.jpeg"),
                    image("other_image_url1", "product/V00013/ABIDE30085/other-image-url1-1245260503.jpeg"),
                    image("other_image_url2", "product/V00013/ABIDE30085/other-image-url1-1245260503.jpeg"),
                    image("other_image_url3", "product/V00013/ABIDE30085/other-image-url1-1245260503.jpeg"),
                    image("other_image_url4", "product/V00013/ABIDE30085/other-image-url1-1245260503.jpeg"),
                    image("other_image_url5", "product/V00013/ABIDE30085/other-image-url1-1245260503.jpeg"));
                entity.brandName = "Aurelie Bidermann";
                entity.eligibleCountryCodes = Lists.newArrayList("CO", "GT");
                entity.updatedTime = now;
                entity.createdTime = now;
                entity.type = ProductType.NORMAL;
                entity.fee = 0.15;
                entity.addFee = 0d;
                entity.harmonizedCode = "7117.90.00";
                productCollection.replace(entity);
                latch.countDown();
                return null;
            });
        }
        latch.await();
    }

    private ProductImage image(String name, String key) {
        ProductImage image1 = new ProductImage();
        image1.name = name;
        image1.imageKey = key;
        return image1;
    }
}
