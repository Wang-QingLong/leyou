package cn.leyou.search.clients;

import cn.leyou.dto.CategoryDTO;
import cn.leyou.dto.SkuDTO;
import cn.leyou.dto.SpuDTO;
import cn.leyou.pojo.Sku;
import cn.leyou.pojo.Spu;
import cn.leyou.result.PageResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/13 19:34
 * @description:
 */
@Component
public class GoodsFeignServiceFallback implements GoodsFeignService {

    /**
     * 分页查询页面数据
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @Override
    public PageResult<SpuDTO> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        ArrayList<SpuDTO> strings = new ArrayList<>();
        SpuDTO spuDTO = new SpuDTO();
        spuDTO.setName("elsticsearch页面查询数据失败");
        strings.add(spuDTO);

        return new PageResult<SpuDTO>(strings, page, rows);
    }

    /**
     * 根据CategoryIds查询数据
     *
     * @param ids
     * @return
     */
    @Override
    public List<CategoryDTO> queryCategoriesByIds(List<Long> ids) {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("根据CategoryIds查询数据失败");
        categoryDTOS.add(categoryDTO);


        return categoryDTOS;
    }

    /**
     * 根据SpuId查询skus数据
     *
     * @param id
     * @return
     */
    @Override
    public List<SkuDTO> findSkusBySpuId(Long id) {

        List<SkuDTO> skuDTOS = new ArrayList<>();
        Sku sku = new Sku();
        sku.setImages("根据spuId查询skus数据失败");
        return skuDTOS;
    }
}
