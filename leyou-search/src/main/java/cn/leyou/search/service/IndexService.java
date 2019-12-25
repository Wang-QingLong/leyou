package cn.leyou.search.service;

import cn.leyou.dto.CategoryDTO;
import cn.leyou.dto.SkuDTO;
import cn.leyou.dto.SpuDTO;
import cn.leyou.search.clients.GoodsFeignService;
import cn.leyou.search.pojo.Goods;
import cn.leyou.utils.BeanHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

        String name = new StringBuilder().append(spuDTO.getName()).append(names).toString();
        //添加品牌分类名称到里面
        goods.setAll(name);

        //获取sku数据
        HashSet<Long> Skuprices = new HashSet<>();

        String sku = this.goodsFeignService.findSkusBySpuId(spuDTO.getId()).stream().map(skuDTO -> {
            //用map集合来封装需要展现在页面上的数据
            HashMap<String, Object> skumap = new HashMap<>();
            //获取sku四个属性:Id，标题，图片，价钱
            //多个图片为了不造成冗余，取第一个图片即可，用，切割
            skumap.put("id", skuDTO.getId());
            skumap.put("title", skuDTO.getTitle());
            skumap.put("image", StringUtils.substringBefore(skuDTO.getImages(), ","));
            skumap.put("price", skuDTO.getPrice());

            Skuprices.add(skuDTO.getPrice());

            return skumap;
        }).collect(Collectors.toList()).toString();


        //添加sku数据
        goods.setSkus(sku);

        //添加价格
        goods.setPrice(Skuprices);



        return goods;


    }


}
