package com.personal.mall.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal.common.dto.SkuESModel;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {
    Boolean saveProduct(List<SkuESModel> esModels) throws IOException;
}
