package com.personal.mall.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.mall.search.config.MallElasticSearchConfiguration;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MallSearchApplicationTests {
    @Autowired
    RestHighLevelClient client;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    void contextLoads() {
        System.out.println(client);
    }

    // 1 检索
    @Test
    void search() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("blank");// 设置要检索的索引库
        // 设置检索条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("ageAgg")
                .field("age").size(10)
                .subAggregation(AggregationBuilders.avg("avgAge").field("balance"));
        builder.aggregation(aggregation);
        builder.size(10);
        searchRequest.source(builder);
//        System.out.println(builder);
        // 执行操作
        SearchResponse response = client.search(searchRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
//        System.out.println(response);
        // 处理检索后的结果
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            String source = hit.getSourceAsString();
            // json转换为对象
            ObjectMapper objectMapper = new ObjectMapper();
            Account account = objectMapper.readValue(source, Account.class);
            System.out.println(account);
        }
    }
    @ToString
    @Data
    static class Account {

        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;

    }
    // 检索 2
    @Test
    void search2() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("blank");// 设置要检索的索引库
        // 设置检索条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("ageAgg")
                .field("age").size(10);
        builder.aggregation(aggregation);
        builder.aggregation(AggregationBuilders.avg("avgAge").field("balance"));
        builder.size(0);
        searchRequest.source(builder);
        System.out.println(builder);
        // 执行操作
        SearchResponse response = client.search(searchRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        System.out.println(response);
    }

    // 测试保存文档
    @Test
    void save() throws Exception {
        IndexRequest request = new IndexRequest("system");
        request.id("1");
        User user = new User();
        user.setName("bobo");
        user.setAge(22);
        user.setGender("男");
        // 转换为json数据
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        request.source(json, XContentType.JSON);
        // 执行操作
        IndexResponse response = client.index(request, MallElasticSearchConfiguration.COMMON_OPTIONS);
        System.out.println(response);
    }
    @Data
    class User{
        private String name;
        private Integer age;
        private String gender;
    }
}
