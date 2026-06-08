package sansan.sentix.common.Config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

    private RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(5000)              // timeout TCP
                .setConnectionRequestTimeout(5000)    // timeout lấy connection từ pool
                .setSocketTimeout(15000)              // timeout đọc dữ liệu
                .build();
    }

    private PoolingHttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager manager =
                new PoolingHttpClientConnectionManager();

        manager.setMaxTotal(100);          // tổng connection
        manager.setDefaultMaxPerRoute(20); // mỗi host
        return manager;
    }

    @Bean
    public RestTemplate restTemplate() {

        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setConnectionManager(connectionManager())
                        .setDefaultRequestConfig(requestConfig())
                        .evictExpiredConnections()
                        .evictIdleConnections(30, TimeUnit.SECONDS)
                        .build();

        return new RestTemplate(
                new HttpComponentsClientHttpRequestFactory(httpClient)
        );
    }

    @Bean("unsafeRestTemplate")
    public RestTemplate unsafeRestTemplate() throws Exception {
        TrustStrategy trustAll = (chain, authType) -> true;

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, trustAll)
                .build();

        SSLConnectionSocketFactory socketFactory =
                new SSLConnectionSocketFactory(
                        sslContext,
                        NoopHostnameVerifier.INSTANCE
                );

        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setSSLSocketFactory(socketFactory)
                        .setConnectionManager(connectionManager())
                        .setDefaultRequestConfig(requestConfig())
                        .evictExpiredConnections()
                        .evictIdleConnections(30, TimeUnit.SECONDS)
                        .build();

        return new RestTemplate(
                new HttpComponentsClientHttpRequestFactory(httpClient)
        );
    }
}
