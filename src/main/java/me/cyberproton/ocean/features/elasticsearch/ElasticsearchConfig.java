package me.cyberproton.ocean.features.elasticsearch;

import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.http.HttpHeaders;

@AllArgsConstructor
@Configuration
public class ElasticsearchConfig {
    private final ElasticsearchExternalConfig elasticsearchExternalConfig;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(
                        elasticsearchExternalConfig.username(),
                        elasticsearchExternalConfig.password()));

        return new RestHighLevelClient(
                RestClient.builder(HttpHost.create(elasticsearchExternalConfig.uris()))
                        .setHttpClientConfigCallback(
                                httpClientBuilder -> {
                                    httpClientBuilder.disableAuthCaching();
                                    httpClientBuilder.setDefaultHeaders(
                                            List.of(
                                                    new BasicHeader(
                                                            HttpHeaders.CONTENT_TYPE,
                                                            ContentType.APPLICATION_JSON
                                                                    .toString())));
                                    httpClientBuilder.addInterceptorLast(
                                            (HttpResponseInterceptor)
                                                    (response, context) ->
                                                            response.addHeader(
                                                                    "X-Elastic-Product",
                                                                    "Elasticsearch"));
                                    return httpClientBuilder.setDefaultCredentialsProvider(
                                            credentialsProvider);
                                }));
    }

    @Bean(name = {"elasticsearchTemplate", "elasticsearchOperations"})
    public ElasticsearchOperations elasticsearchOperations() {
        return new ElasticsearchRestTemplate(restHighLevelClient());
    }
}
