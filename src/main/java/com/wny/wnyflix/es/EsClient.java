package com.wny.wnyflix.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.instrumentation.OpenTelemetryForElasticsearch;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import lombok.With;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class EsClient {

    @Value("${elastic.host}")
    private String host;
    @Value("${elastic.port}")
    private int port;
    @Value("${elastic.name}")
    private String name;
    @Value("${elastic.pwd}")
    private String pwd;
//    @WithSpan
    @Bean
    public ElasticsearchClient client() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                .loadTrustMaterial(null, (X509Certificates, s) -> true);
        final SSLContext sslContext = sslContextBuilder.build();

        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials(name, pwd)
        );

        RestClient restClient = RestClient
                .builder(new HttpHost(host, port, "https"))
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .setDefaultCredentialsProvider(credsProv)
                )
                .build();

        OpenTelemetry customOtel = OpenTelemetrySdk.builder().build();

        OpenTelemetryForElasticsearch esOtelInstrumentation = new OpenTelemetryForElasticsearch(customOtel, true);

        // Create the transport and the API client
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(), null, esOtelInstrumentation);

        return new ElasticsearchClient(transport);
    }
}
