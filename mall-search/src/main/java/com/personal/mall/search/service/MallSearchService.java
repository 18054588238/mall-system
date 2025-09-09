package com.personal.mall.search.service;

import com.personal.mall.search.vo.SearchParam;
import com.personal.mall.search.vo.SearchResult;

import java.io.IOException;

public interface MallSearchService {
    SearchResult search(SearchParam param) throws IOException;
}
