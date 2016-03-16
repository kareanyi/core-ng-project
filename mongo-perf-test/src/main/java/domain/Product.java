package domain;

import core.framework.api.mongo.Collection;
import core.framework.api.mongo.Field;
import core.framework.api.mongo.Id;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rainbow.cai
 */
@Collection(name = "product")
public class Product {
    @Id
    public String id;

    @Field(name = "category_id")
    public String categoryId;

    @Field(name = "name")
    public String name;

    @Field(name = "description")
    public String description;

    @Field(name = "images")
    public List<ProductImage> images;

    @Field(name = "brand_name")
    public String brandName;

    @Field(name = "eligible_country_codes")
    public List<String> eligibleCountryCodes;

    @Field(name = "updated_time")
    public LocalDateTime updatedTime;

    @Field(name = "created_time")
    public LocalDateTime createdTime;

    @Field(name = "type")
    public ProductType type;

    @Field(name = "fee")
    public Double fee;

    @Field(name = "add_fee")
    public Double addFee;

    @Field(name = "harmonized_code")
    public String harmonizedCode;
}
