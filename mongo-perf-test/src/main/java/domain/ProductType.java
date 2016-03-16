package domain;

import core.framework.api.mongo.MongoEnumValue;

/**
 * @author rainbow.cai
 */
public enum ProductType {
    @MongoEnumValue("NORMAL")
    NORMAL,
    @MongoEnumValue("STANDALONE")
    STANDALONE
}
