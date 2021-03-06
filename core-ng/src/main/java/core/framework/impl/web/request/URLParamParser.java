package core.framework.impl.web.request;

import core.framework.api.util.Exceptions;
import core.framework.api.util.JSON;
import core.framework.api.util.Strings;
import core.framework.api.web.exception.BadRequestException;

/**
 * @author neo
 */
public class URLParamParser {    // parse query param and path param
    @SuppressWarnings("unchecked")
    static <T> T parse(String param, Class<T> valueClass) {
        if (String.class.equals(valueClass)) {
            return (T) param;
        } else if (Integer.class.equals(valueClass)) {
            return (T) toInt(param);
        } else if (Long.class.equals(valueClass)) {
            return (T) toLong(param);
        } else if (Enum.class.isAssignableFrom(valueClass)) {
            return (T) toEnum(param, (Class<? extends Enum>) valueClass);
        }
        throw Exceptions.error("not supported path param type, please contact arch team, type={}", valueClass.getCanonicalName());
    }

    private static <T extends Enum> T toEnum(String value, Class<T> valueClass) {
        try {
            return JSON.fromEnumValue(valueClass, value);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Strings.format("failed to parse enum, enumClass={}, value={}", valueClass.getCanonicalName(), value), BadRequestException.DEFAULT_ERROR_CODE, e);
        }
    }

    private static Long toLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException(Strings.format("failed to parse long, value={}", value), BadRequestException.DEFAULT_ERROR_CODE, e);
        }
    }

    private static Integer toInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException(Strings.format("failed to parse int, value={}", value), BadRequestException.DEFAULT_ERROR_CODE, e);
        }
    }
}
