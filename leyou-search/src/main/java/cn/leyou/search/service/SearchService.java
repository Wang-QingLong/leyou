package cn.leyou.search.service;

import cn.hutool.core.collection.CollUtil;
import cn.leyou.dto.BrandDTO;
import cn.leyou.dto.CategoryDTO;
import cn.leyou.dto.SpecParamDTO;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import cn.leyou.result.PageResult;
import cn.leyou.search.clients.GoodsFeignService;
import cn.leyou.search.dto.GoodsDTO;
import cn.leyou.search.dto.SearchRequest;
import cn.leyou.search.pojo.Goods;
import cn.leyou.utils.BeanHelper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {


    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private GoodsFeignService goodsFeignService;


    /**
     * 分页数据查询
     *
     * @param request
     * @return
     */
    public PageResult<GoodsDTO> search(SearchRequest request) {

        // 1.创建原生搜索构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 2.组织条件    
        // 2.0.source过滤，控制字段数量    
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
//
//        //时间排序
//        FieldSortBuilder createTime = SortBuilders.fieldSort("createTime");
//        //不为空则加入时间排序条件
//        if (createTime != null) {
//            queryBuilder.withSort(SortBuilders.fieldSort("createTime"));
//        }


        // 2.1.搜索条件
        QueryBuilder queryBuilder1 = builderBasicQuery(request);

        queryBuilder.withQuery(queryBuilder1);

        // 2.2.分页条件    
        int page = request.getPage() - 1;
        int size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 3.搜索结果
        AggregatedPage<Goods> result = esTemplate.queryForPage(queryBuilder.build(), Goods.class);
        // 4.解析结果    
        //4.1.解析分页数据    
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> list = result.getContent();
        // 4.2.转换DTO    
        List<GoodsDTO> dtoList = BeanHelper.copyWithCollection(list, GoodsDTO.class);
        // 5.封装并返回    
        return new PageResult<>(total, totalPage, dtoList);
    }


    /**
     * 公关测试部分
     *
     * @param searchRequest
     * @return
     */
    public QueryBuilder builderBasicQuery(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        //健壮性判断
        if (StringUtils.isBlank(key)) {
            throw new LyException(ExceptionEnum.INVALID_REQUEST_PARAM);
        }

        //filter过滤必须发生在bool查询中
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //添加查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", key).operator(Operator.AND));

        //获取请求提交的所有的过滤条件
        Map<String, String> filter = searchRequest.getFilter();

        filter.entrySet().forEach(filterEntry -> {
            String key1 = filterEntry.getKey();
            String value1 = filterEntry.getValue();

            if ("品牌".equals(key1)) {
                key1 = "brandId";
            } else if ("分类".equals(key1)) {
                key1 = "categoryId";
            } else {
                key1 = "specs." + key1 + ".keyword";
            }

            //添加过滤条件
            queryBuilder.filter(QueryBuilders.termQuery(key1, value1));
        });

        //添加查询条件，查询all字段
        return queryBuilder;
    }


    /**
     * 根据过滤条件聚合分桶查询数据
     *
     * @param request
     * @return
     */
    public Map<String, List<?>> queryFilters(SearchRequest request) {


        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //查询条件
        QueryBuilder query = builderBasicQuery(request);

        //添加查询条件
        queryBuilder.withQuery(query);

        //springData强行要求分页展示必须至少展示1个，
        queryBuilder.withPageable(PageRequest.of(0, 1));

        String brandAggName = "brands";

        String categoryAggName = "categories";

        //添加品牌聚合条件
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        //添加分类聚合条件
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("categoryId"));

        //执行查询聚合
        AggregatedPage<Goods> goodsAggregatedPage = this.esTemplate.queryForPage(queryBuilder.build(), Goods.class);

        //从查询结果中取出所有的聚合结果
        Aggregations aggregations = goodsAggregatedPage.getAggregations();

        //根据品牌聚合名称，获取聚合的结果
        LongTerms brandTerms = aggregations.get(brandAggName);

        //获取聚合分桶
        List<Long> brandIds = brandTerms.getBuckets().stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());

        //根据分类聚合的名称，获取分类聚合的结果
        LongTerms categoryTerms = aggregations.get(categoryAggName);

        List<Long> categoryIds = categoryTerms.getBuckets().stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());

        Map<String, List<?>> result = new LinkedHashMap<>();

        List<CategoryDTO> categoryDTOS = this.goodsFeignService.queryCategoriesByIds(categoryIds);
        //健壮性判断
        if (CollUtil.isNotEmpty(categoryDTOS)) {
            result.put("分类", categoryDTOS);
        }
        List<BrandDTO> brandDTOS = this.goodsFeignService.queryBrandByIds(brandIds);
        //健壮性判断
        if (CollUtil.isNotEmpty(brandDTOS)) {
            result.put("品牌", brandDTOS);
        }

        //除了品牌和分类之外，还要展示其他可搜索规格参数，展示的实际，只有一个，确定分类时候，

        if (null != categoryIds && categoryIds.size() == 1) {
            //获取其他可搜索规格参数以及其聚合值，1，分类id;2，查询条件;3返回结果
            getSpecs(categoryIds.get(0), query, result);
        }

        return result;
    }


    //处理可搜索规格参数，对应的所有的聚合结果值
    private void getSpecs(Long cid, QueryBuilder query, Map<String, List<?>> result) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加分页
        queryBuilder.withPageable(PageRequest.of(0, 1));
        //添加查询条件
        queryBuilder.withQuery(query);

        //先根据分类id，查询可搜索规格参数
        List<SpecParamDTO> specParamDTOS = this.goodsFeignService.findParams(null, cid, true);

        //循环添加聚合条件
        specParamDTOS.forEach(specParamDTO -> {
            //可搜索规格参数的名称，也是聚合的名称
            String name = specParamDTO.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        });

        //执行聚合查询
        AggregatedPage<Goods> goodsAggregatedPage = this.esTemplate.queryForPage(queryBuilder.build(), Goods.class);

        //获取所有的聚合
        Aggregations aggregations = goodsAggregatedPage.getAggregations();

        //循环解析聚合
        specParamDTOS.forEach(specParamDTO -> {
            String name = specParamDTO.getName();
            //根据聚合名称获取聚合结果
            StringTerms stringTerms = aggregations.get(name);

            List<String> options = stringTerms.getBuckets().stream()
                    .map(StringTerms.Bucket::getKeyAsString)
                    .collect(Collectors.toList());

            //把解析出来的结果封装返回到页面中
            result.put(name, options);
        });
    }


}