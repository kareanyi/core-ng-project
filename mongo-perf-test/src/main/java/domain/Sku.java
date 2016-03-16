package domain;

import core.framework.api.mongo.Collection;
import core.framework.api.mongo.Field;
import core.framework.api.mongo.Id;

import java.time.LocalDateTime;

/**
 * @author chi
 */
@Collection(name = "sku")
public class Sku {
    @Id
    public String id;

    @Field(name = "product_id")
    public String productId;

    @Field(name = "name")
    public String name;

    @Field(name = "description")
    public String description;

    @Field(name = "image_key")
    public String imageKey;

    @Field(name = "color")
    public String color;

    @Field(name = "size")
    public String size;

    @Field(name = "price")
    public Double price;

    @Field(name = "sale_price")
    public Double salePrice;

    @Field(name = "weight")
    public Double weight;

    @Field(name = "updated_time")
    public LocalDateTime updatedTime;

    @Field(name = "created_time")
    public LocalDateTime createdTime;

}
