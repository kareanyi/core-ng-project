package core.framework.impl.validate;

/**
 * @author neo
 */
interface FieldValidator {
    void validate(Object instance, ValidationResult result);
}