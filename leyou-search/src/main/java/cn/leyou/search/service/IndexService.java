package cn.leyou.search.service;

import cn.leyou.dto.*;
import cn.leyou.search.clients.GoodsFeignService;
import cn.leyou.search.pojo.Goods;
import cn.leyou.utils.BeanHelper;
import cn.leyou.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/24 18:13
 * @description:
 */
@Service
public class IndexService {
    @Autowired
    private GoodsFeignService goodsFeignService;

    //以spu为模板生产goods

    public Goods buildGoods(SpuDTO spuDTO) {

        Goods goods = BeanHelper.copyProperties(spuDTO, Goods.class);


        //将Goods与SpuDTO比较,需要啥补啥

        //加入cid3
        goods.setCategoryId(spuDTO.getCid3());

        //all包含了name 品牌名称，，sku数据，品牌分类名称

        //创建时间
        goods.setCreateTime(spuDTO.getCreateTime().getTime());

        //获取品牌分类名称
        String names = goodsFeignService.queryCategoriesByIds(spuDTO.getCategoryIds())
                .stream()
                .map(CategoryDTO::getName)
                .collect(Collectors.joining(" "));

        String all = new StringBuilder().append(spuDTO.getName()).append(names).toString();
        //添加品牌分类名称到里面
        goods.setAll(all);

        //获取sku数据
       Set<Long> Skuprices = new HashSet<>();

        List<Map<String, Object>> skuMaplist = this.goodsFeignService.findSkusBySpuId(spuDTO.getId()).stream().map(skuDTO -> {
            //用map集合来封装需要展现在页面上的数据
            Map<String, Object> skumap = new HashMap<>();
            //获取sku四个属性:Id，标题，图片，价钱
            //多个图片为了不造成冗余，取第一个图片即可，用，切割
            skumap.put("id", skuDTO.getId());
            skumap.put("title", skuDTO.getTitle());
            skumap.put("image", StringUtils.substringBefore(skuDTO.getImages(), ","));
            skumap.put("price", skuDTO.getPrice());

            Skuprices.add(skuDTO.getPrice());

            return skumap;
        }).collect(Collectors.toList());


        //添加sku数据
        goods.setSkus(JsonUtils.toString(skuMaplist));

        //添加价格
        goods.setPrice(Skuprices);


//        private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
        //添加detail数据

        SpuDetailDTO spuDetail = this.goodsFeignService.findSpuDetailBySpuId(spuDTO.getId());
        //detail数据分为通用参数和具体参数

        //获取通用参数并把其转换map集合：string转map
        //key 可搜索规格参数的id,value,可搜索规格参数的值
        Map<Long, Object> genericSpecMap = JsonUtils.nativeRead(spuDetail.getGenericSpec(), new TypeReference<Map<Long,
                Object>>() {
        });


        //获取特有参数并把其转换为map集合
        //key可搜索规格参数的id,value,可搜索规格参数的可选值数组
        Map<Long, List<String>> SpecialSpecMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long,
                        List<String>>>() {
                });

        //添加searching搜索条件
        List<SpecParamDTO> params = this.goodsFeignService.findParams(null, spuDTO.getCid3(), true);

        Map<String, Object> specs = params.stream().collect(Collectors.toMap(SpecParamDTO::getName, specParamDTO ->
        {
            //可搜索规则参数的id
            Long id = specParamDTO.getId();

            Object value = null;

            if (specParamDTO.getGeneric()) {
                value = genericSpecMap.get(id);
            } else {
                value = SpecialSpecMap.get(id);
            }


            if (null == value) {
                value = "其他";
            }

                 //判断是否是数值类型
            if (specParamDTO.getNumeric()) {
                //是数字类型，分段
                value = chooseSegment(value, specParamDTO);
            }


            return value;
        }));

        goods.setSpecs(specs);


        return goods;

    }


    //因为过滤参数中有一类比较特殊，就是数值区间：还有数值单位需要添加上
    private String chooseSegment(Object value, SpecParamDTO p) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        double val = parseDouble(value.toString());
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }


}
