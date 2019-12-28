package cn.leyou.search.clients;

import cn.leyou.dto.*;
import cn.leyou.pojo.Sku;
import cn.leyou.pojo.Spu;
import cn.leyou.result.PageResult;
import org.springframework.http.ResponseEntity;
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

        return new PageResult<SpuDTO>( page, rows,strings);
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

    /**
     * 根据规格组Id,或者分类Id,或者是否可搜索，查询对应的规格参数
     *
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @Override
    public List<SpecParamDTO> findParams(Long gid, Long cid, boolean searching) {
        return null;
    }

    /**根据spuId查询spudetail数据
     * @param spuId
     * @return
     */
    @Override
    public SpuDetailDTO findSpuDetailBySpuId(Long spuId) {
        return null;
    }

    @Override
    public List<BrandDTO> queryBrandByIds(List<Long> ids) {
        return null;
    }
}
