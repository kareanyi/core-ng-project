package domain;

import core.framework.api.db.DBEnumValue;

/**
 * @author neo
 */
public enum ProductStatus {
    @DBEnumValue("A")
    ACTIVE,
    @DBEnumValue("I")
    INACTIVE;
}
