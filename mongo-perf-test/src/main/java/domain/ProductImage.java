package domain;

import core.framework.api.mongo.Field;

/**
 * @author rainbow.cai
 */
public class ProductImage {
    @Field(name = "name")
    public String name;

    @Field(name = "image_key")
    public String imageKey;
}
