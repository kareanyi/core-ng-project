package core.framework.impl.web.response;

import core.framework.api.http.HTTPStatus;
import core.framework.api.log.ActionLogContext;
import core.framework.api.util.Exceptions;
import core.framework.api.util.Maps;
import core.framework.api.web.ResponseImpl;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.HeaderMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author neo
 */
public class ResponseHandler {
    private final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    final Map<Class, BodyHandler> handlers = Maps.newHashMap();

    final HTTPHeaderMappings headerMappings = new HTTPHeaderMappings();

    public ResponseHandler() {
        handlers.put(BeanBody.class, new BeanBodyResponseHandler());
        handlers.put(TextBody.class, new TextBodyResponseHandler());
        handlers.put(HTMLBody.class, new HTMLBodyResponseHandler());
        handlers.put(ByteArrayBody.class, new ByteArrayBodyResponseHandler());
    }

    public void handle(ResponseImpl response, HttpServerExchange exchange) {
        HTTPStatus status = response.status();
        exchange.setResponseCode(status.code);
        ActionLogContext.put("responseCode", status.code);

        HeaderMap headers = exchange.getResponseHeaders();
        response.headers.forEach((name, value) -> {
            String headerValue = String.valueOf(value);
            logger.debug("[responseHeader] {}={}", name, headerValue);
            headers.put(headerMappings.undertowHeader(name), headerValue);
        });

        if (response.cookies != null) {
            Map<String, Cookie> responseCookies = exchange.getResponseCookies();
            response.cookies.forEach((spec, value) -> {
                CookieImpl cookie = new CookieImpl(spec.name);
                if (value == null) {
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                } else {
                    cookie.setMaxAge((int) spec.maxAge.getSeconds());
                    cookie.setValue(value);
                }
                cookie.setDomain(spec.domain);
                cookie.setPath(spec.path);
                cookie.setSecure(spec.secure);
                cookie.setHttpOnly(spec.httpOnly);
                logger.debug("[responseCookie] name={}, value={}, domain={}, path={}, secure={}, httpOnly={}, maxAge={}",
                    cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(), cookie.isSecure(), cookie.isHttpOnly(), cookie.getMaxAge());
                responseCookies.put(spec.name, cookie);
            });
        }

        BodyHandler handler = handlers.get(response.body.getClass());
        if (handler == null) throw Exceptions.error("unexpected body class, body={}", response.body.getClass());
        logger.debug("responseHandlerClass={}", handler.getClass().getName());
        handler.handle(response, exchange);
    }
}