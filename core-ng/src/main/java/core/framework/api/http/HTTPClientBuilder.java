package core.framework.api.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

/**
 * @author neo
 */
public final class HTTPClientBuilder {
    private final Logger logger = LoggerFactory.getLogger(HTTPClientBuilder.class);

    private Duration timeout = Duration.ofSeconds(120);
    private int maxConnections = 100;
    private Duration keepAliveTimeout = Duration.ofSeconds(60);
    private Duration slowTransactionThreshold = Duration.ofSeconds(30);

    public HTTPClient build() {
        logger.info("create http client");
        try {
            HttpClientBuilder builder = HttpClients.custom();
            builder.setKeepAliveStrategy((response, context) -> keepAliveTimeout.toMillis());

            builder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSslcontext(new SSLContextBuilder().loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE).build());
            // builder will use PoolingHttpClientConnectionManager by default
            builder.setDefaultSocketConfig(SocketConfig.custom()
                .setSoKeepAlive(true)
                .build());
            builder.setDefaultRequestConfig(RequestConfig.custom()
                .setSocketTimeout((int) timeout.toMillis())
                .setConnectTimeout((int) timeout.toMillis()).build());

            builder.setMaxConnPerRoute(maxConnections)
                .setMaxConnTotal(maxConnections);

            CloseableHttpClient httpClient = builder.build();
            return new HTTPClient(httpClient, slowTransactionThreshold.toMillis());
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new Error(e);
        }
    }

    public HTTPClientBuilder maxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public HTTPClientBuilder timeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    public HTTPClientBuilder keepAliveTimeout(Duration keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
        return this;
    }

    public HTTPClientBuilder slowTransactionThreshold(Duration slowTransactionThreshold) {
        this.slowTransactionThreshold = slowTransactionThreshold;
        return this;
    }
}