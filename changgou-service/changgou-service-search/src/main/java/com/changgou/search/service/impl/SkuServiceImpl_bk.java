//package com.changgou.search.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.changgou.goods.feign.SkuFeign;
//import com.changgou.goods.pojo.Sku;
//import com.changgou.search.dao.SkuEsMapper;
//import com.changgou.search.pojo.SkuInfo;
//import com.changgou.search.service.SkuService;
//import entity.Result;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.*;
//
//@Service
//public class SkuServiceImpl_bk implements SkuService {
//
//    @Autowired
//    private SkuFeign skuFeign;
//
//    @Autowired
//    private SkuEsMapper skuEsMapper;
//
//    @Autowired
//    private ElasticsearchTemplate esTemplate;
//
//
//
//    /**
//     * 导入sku数据到es
//     */
//    @Override
//    public void importSku(){
//        //调用changgou-service-goods微服务
//        Result<List<Sku>> skuListResult = skuFeign.findByStatus("1");
//        //将数据转成search.Sku
//        List<SkuInfo> skuInfos=  JSON.parseArray(JSON.toJSONString(skuListResult.getData()),SkuInfo.class);
//        for(SkuInfo skuInfo:skuInfos){
//            Map<String, Object> specMap= JSON.parseObject(skuInfo.getSpec()) ;
//            skuInfo.setSpecMap(specMap);
//        }
//        skuEsMapper.saveAll(skuInfos);
//    }
//
//
//
////    public Map search(Map<String, String> searchMap) {
////
////        //1.获取关键字的值
////        String keywords = searchMap.get("keywords");
////
////        if (StringUtils.isEmpty(keywords)) {
////            keywords = "华为";//赋值给一个默认的值
////        }
////        //2.创建查询对象 的构建对象
////        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
////
////        //3.设置查询的条件
////
////        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", keywords));
////
////        //4.构建查询对象
////        NativeSearchQuery query = nativeSearchQueryBuilder.build();
////
////        //5.执行查询
////        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class);
////
////
////        //6.返回结果
////        Map resultMap = new HashMap<>();
////        resultMap.put("rows", skuPage.getContent());
////        resultMap.put("total", skuPage.getTotalElements());
////        resultMap.put("totalPages", skuPage.getTotalPages());
////
////        return resultMap;
////    }
//
//    public Map search(Map<String, String> searchMap) {
//
//        //1.获取关键字的值
//        String keywords = searchMap.get("keywords");
//
//        if (StringUtils.isEmpty(keywords)) {
//            keywords = "华为";//赋值给一个默认的值
//        }
//        //2.创建查询对象 的构建对象
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//
//        //3.设置查询的条件
//
//        //设置分组条件  商品分类
//        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("categoryName").size(50));
//
//        //设置分组条件  商品品牌
//        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brandName").size(50));
//
//        //设置分组条件  商品的规格
//        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(100));
//
//        //设置主关键字查询
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", keywords));
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//
//        if (!StringUtils.isEmpty(searchMap.get("brand"))) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
//        }
//
//        if (!StringUtils.isEmpty(searchMap.get("category"))) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", searchMap.get("category")));
//        }
//
//        //规格过滤查询
//        if (searchMap != null) {
//            for (String key : searchMap.keySet()) {
//                if (key.startsWith("spec_")) {
//                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key)));
//                }
//            }
//        }
//
//        //构建过滤查询
//        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
//
//        //4.构建查询对象
//        NativeSearchQuery query = nativeSearchQueryBuilder.build();
//
//        //5.执行查询
//        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class);
//
//        //获取分组结果
//        StringTerms stringTermsCategory = (StringTerms) skuPage.getAggregation("skuCategorygroup");
//
//        //获取分组结果  商品品牌
//        StringTerms stringTermsBrand = (StringTerms) skuPage.getAggregation("skuBrandgroup");
//
//        //获取分组结果  商品规格数据
//        StringTerms stringTermsSpec = (StringTerms) skuPage.getAggregation("skuSpecgroup");
//
//        List<String> categoryList = getStringsCategoryList(stringTermsCategory);
//
//        List<String> brandList = getStringsBrandList(stringTermsBrand);
//
//        Map<String, Set<String>> specMap = getStringSetMap(stringTermsSpec);
//
//        //6.返回结果
//        Map resultMap = new HashMap<>();
//        resultMap.put("specMap", specMap);
//        resultMap.put("categoryList", categoryList);
//        resultMap.put("brandList", brandList);
//        resultMap.put("rows", skuPage.getContent());
//        resultMap.put("total", skuPage.getTotalElements());
//        resultMap.put("totalPages", skuPage.getTotalPages());
//
//        return resultMap;
//    }
//
//    /**
//     * 获取规格列表数据
//     *
//     * @param stringTermsSpec
//     * @return
//     */
//    private Map<String, Set<String>> getStringSetMap(StringTerms stringTermsSpec) {
//        Map<String, Set<String>> specMap = new HashMap<String, Set<String>>();
//        Set<String> specList = new HashSet<>();
//        if (stringTermsSpec != null) {
//            for (StringTerms.Bucket bucket : stringTermsSpec.getBuckets()) {
//                specList.add(bucket.getKeyAsString());
//            }
//        }
//        for (String specjson : specList) {
//            Map<String, String> map = JSON.parseObject(specjson, Map.class);
//            for (Map.Entry<String, String> entry : map.entrySet()) {//
//                String key = entry.getKey();        //规格名字
//                String value = entry.getValue();    //规格选项值
//                //获取当前规格名字对应的规格数据
//                Set<String> specValues = specMap.get(key);
//                if (specValues == null) {
//                    specValues = new HashSet<String>();
//                }
//                //将当前规格加入到集合中
//                specValues.add(value);
//                //将数据存入到specMap中
//                specMap.put(key, specValues);
//            }
//        }
//        return specMap;
//    }
//
//    /**
//     * 获取品牌列表
//     *
//     * @param stringTermsBrand
//     * @return
//     */
//    private List<String> getStringsBrandList(StringTerms stringTermsBrand) {
//        List<String> brandList = new ArrayList<>();
//        if (stringTermsBrand != null) {
//            for (StringTerms.Bucket bucket : stringTermsBrand.getBuckets()) {
//                brandList.add(bucket.getKeyAsString());
//            }
//        }
//        return brandList;
//    }
//
//
//    /**
//     * 获取分类列表数据
//     *
//     * @param stringTerms
//     * @return
//     */
//    private List<String> getStringsCategoryList(StringTerms stringTerms) {
//        List<String> categoryList = new ArrayList<>();
//        if (stringTerms != null) {
//            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
//                String keyAsString = bucket.getKeyAsString();//分组的值
//                categoryList.add(keyAsString);
//            }
//        }
//        return categoryList;
//    }
//}
