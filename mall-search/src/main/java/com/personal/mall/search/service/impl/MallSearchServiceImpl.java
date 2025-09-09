package com.personal.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.personal.common.dto.SkuESModel;
import com.personal.mall.search.config.MallElasticSearchConfiguration;
import com.personal.mall.search.constant.ESConstant;
import com.personal.mall.search.service.MallSearchService;
import com.personal.mall.search.vo.SearchParam;
import com.personal.mall.search.vo.SearchResult;
import lombok.var;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResult search(SearchParam param) throws IOException {
        // 根据搜索条件param，去ES中查询数据，将查询结果封装到SearchResult中
        // 构建检索条件
        SearchRequest searchRequest  = buildSearchRequest(param);

        SearchResponse response = client.search(searchRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        SearchResult searchResult = buildSearchResult(response,param);

        return searchResult;
    }

    private SearchResult buildSearchResult(SearchResponse response, SearchParam param) {
//        System.out.println(response);
        SearchResult searchResult = new SearchResult();
        SearchHit[] hits = response.getHits().getHits();

        // 设置商品上架基本信息
        List<SkuESModel> skuESModels = new ArrayList<>();
        if (hits != null) {
            for (SearchHit hit : hits) {
                String source = hit.getSourceAsString();
                SkuESModel skuESModel = JSON.parseObject(source, SkuESModel.class);
                // 设置高亮
                if (StringUtils.isNotEmpty(param.getKeyword())) {
                    HighlightField subTitle = hit.getHighlightFields().get("subTitle");
                    String highlight = subTitle.getFragments()[0].toString();
                    skuESModel.setSubTitle(highlight);
                }
                skuESModels.add(skuESModel);
            }
        }
        // "highlight":{"subTitle":["<b style='color:red'>test</b>"]}}]},
        searchResult.setProducts(skuESModels);

        Aggregations aggregations = response.getAggregations();
        // 属性
        ParsedNested attrAgg = aggregations.get("attrAgg");
        ParsedLongTerms attrAggId = attrAgg.getAggregations().get("attrAggId");
        List<SearchResult.AttrVO> attrVOS = new ArrayList<>();
        List<? extends Terms.Bucket> attrBuckets = attrAggId.getBuckets();
        if (attrBuckets != null && !attrBuckets.isEmpty()) {
            attrBuckets.forEach(bucket -> {
                SearchResult.AttrVO attrVO = new SearchResult.AttrVO();

                String attrId = bucket.getKeyAsString();
                attrVO.setAttrId(Long.parseLong(attrId));

                ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
                String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
                attrVO.setAttrName(attrName);

                ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attrValueAgg");
                List<? extends Terms.Bucket> buckets = attrValueAgg.getBuckets();
                if (buckets != null && !buckets.isEmpty()) {
                    List<String> attrValue = buckets.stream()
                            .map(Terms.Bucket::getKeyAsString)
                            .collect(Collectors.toList());
                    attrVO.setAttrValue(attrValue);
                }
                attrVOS.add(attrVO);
            });
            searchResult.setAttrs(attrVOS);
        }

        // 品牌
        ArrayList<SearchResult.BrandVO> brandVOS = new ArrayList<>();
        ParsedLongTerms brandAgg = aggregations.get("brandAgg");
        List<? extends Terms.Bucket> brandAggBuckets = brandAgg.getBuckets();

        if (brandAggBuckets != null && !brandAggBuckets.isEmpty()) {
            brandAggBuckets.forEach(bucket -> {
                SearchResult.BrandVO brandVO = new SearchResult.BrandVO();

                String brandId = bucket.getKeyAsString();
                brandVO.setBrandId(Long.parseLong(brandId));

                ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brandImgAgg");
                List<? extends Terms.Bucket> buckets = brandImgAgg.getBuckets();
                if (buckets != null && !buckets.isEmpty()) {
                    String brandImg = buckets.get(0).getKeyAsString();
                    brandVO.setBrandImg(brandImg);
                }

                ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brandNameAgg");
                String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
                brandVO.setBrandName(brandName);

                brandVOS.add(brandVO);
            });
            searchResult.setBrands(brandVOS);
        }

        // 类别
        ParsedLongTerms categoryAgg = aggregations.get("categoryAgg");
        List<? extends Terms.Bucket> categoryAggBuckets = categoryAgg.getBuckets();
        ArrayList<SearchResult.CatalogVO> catalogVOS = new ArrayList<>();
        if (categoryAggBuckets != null && !categoryAggBuckets.isEmpty()) {
            categoryAggBuckets.forEach(bucket -> {
                SearchResult.CatalogVO catalogVO = new SearchResult.CatalogVO();

                String categoryId = bucket.getKeyAsString();
                catalogVO.setCatalogId(Long.parseLong(categoryId));

                ParsedStringTerms categoryNameAgg = bucket.getAggregations().get("categoryNameAgg");
                String categoryName = categoryNameAgg.getBuckets().get(0).getKeyAsString();
                catalogVO.setCatalogName(categoryName);

                catalogVOS.add(catalogVO);
            });
        }
        searchResult.setCatalogs(catalogVOS);

        // 分页信息
        searchResult.setPageNum(param.getPageNum()); // 当前页
        long total = Objects.requireNonNull(response.getHits().getTotalHits()).value;
        searchResult.setTotal(total); // 总记录数

        long totalPage = total % ESConstant.PRODUCT_PAGRSIZE == 0 ? total / ESConstant.PRODUCT_PAGRSIZE : (total / ESConstant.PRODUCT_PAGRSIZE + 1);
        searchResult.setTotalPages((int)totalPage); // 总页数

        return searchResult;
    }

    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchRequest searchRequest = new SearchRequest().indices(ESConstant.PRODUCT_INDEX);
        // 设置检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("subTitle", param.getKeyword()));
        }
        // filter 过滤
        if (param.getCatalog3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        if (param.getHasStock() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }
        if (param.getBrandId() != null && !param.getBrandId().isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        // 根据价格区间检索
        if (StringUtils.isNotEmpty(param.getSkuPrice())) {
            String[] priceSplit = param.getSkuPrice().split("_");
            RangeQueryBuilder skuPrice = QueryBuilders.rangeQuery("skuPrice");
            if (priceSplit.length == 2) {
                skuPrice.gte(priceSplit[0]).lte(priceSplit[1]);
            } else {
                if (param.getSkuPrice().endsWith("_")) {
                    skuPrice.gte(priceSplit[0]);
                } else {
                   skuPrice.lte(priceSplit[0]);
                }
            }
            boolQueryBuilder.filter(skuPrice);
        }
        // 属性的检索条件
        if (param.getAttrs() != null && !param.getAttrs().isEmpty()) {
            for (String attr : param.getAttrs()) {
                BoolQueryBuilder nestedQuery = QueryBuilders.boolQuery();
                String[] attrSplit = attr.split("_");
                String[] split = attrSplit[1].split(":");
                nestedQuery.must(QueryBuilders.termQuery("attrs.attrId", attrSplit[0]));
                nestedQuery.must(QueryBuilders.termsQuery("attrs.attrValue", split));
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", nestedQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            }
        }
        sourceBuilder.query(boolQueryBuilder);
        // 排序
        if (StringUtils.isNotEmpty(param.getSort())) {
            String[] split = param.getSort().split("_");
            sourceBuilder.sort(split[0], split[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
        }
        // 分页
        if (param.getPageNum() != null) {
            // pageNum:1 from:0 [0,1,2,3,4]
            // pageNum:2 from:5 [5,6,7,8,9]
            sourceBuilder.from((param.getPageNum() - 1) * ESConstant.PRODUCT_PAGRSIZE);
            sourceBuilder.size(ESConstant.PRODUCT_PAGRSIZE);
        }
        // 设置高亮
        if (StringUtils.isNotEmpty(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("subTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        // 聚合 品牌、类别、属性
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brandAgg").field("brandId").size(50);
        brandAgg.subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName").size(10));
        brandAgg.subAggregation(AggregationBuilders.terms("brandImgAgg").field("brandImg").size(10));
        sourceBuilder.aggregation(brandAgg);

        TermsAggregationBuilder categoryAgg = AggregationBuilders.terms("categoryAgg").field("catalogId").size(10);
        categoryAgg.subAggregation(AggregationBuilders.terms("categoryNameAgg").field("catalogName").size(10));
        sourceBuilder.aggregation(categoryAgg);

        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        TermsAggregationBuilder attrAggId = AggregationBuilders.terms("attrAggId").field("attrs.attrId").size(10);
        attrAggId.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(10));
        attrAggId.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(10));
        attrAgg.subAggregation(attrAggId);
        sourceBuilder.aggregation(attrAgg);

        System.out.println(sourceBuilder.toString());
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }
}
