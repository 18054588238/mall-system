package com.personal.mall.search.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.common.dto.SkuESModel;
import com.personal.mall.search.config.MallElasticSearchConfiguration;
import com.personal.mall.search.constant.ESConstant;
import com.personal.mall.search.service.ProductSaveService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public Boolean saveProduct(List<SkuESModel> esModels) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuESModel esModel : esModels) {
            // 创建索引
            IndexRequest request = new IndexRequest(ESConstant.PRODUCT_INDEX);
            // 设置id
            request.id(esModel.getSkuId().toString());
            // 设置文档
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(esModel);
            request.source(json, XContentType.JSON);
            // 存储到bulk中
            bulkRequest.add(request);
        }
        BulkResponse bulk = client.bulk(bulkRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        boolean b = bulk.hasFailures();
        return !b;
    }
}
