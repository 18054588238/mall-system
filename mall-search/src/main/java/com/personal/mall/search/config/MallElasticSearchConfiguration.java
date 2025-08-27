package com.personal.mall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MallElasticSearchConfiguration
 * @Author liupanpan
 * @Date 2025/8/27
 * @Description
 */
@Configuration
public class MallElasticSearchConfiguration {
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient
                .builder(new HttpHost("127.0.0.1", 9200, "http")));
    }
}
