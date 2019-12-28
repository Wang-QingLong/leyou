package cn.leyou.search.clients;

import cn.leyou.dto.*;
import cn.leyou.result.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/24 17:07
 * @description:
 */
@FeignClient(value = "item-service", fallback = GoodsFeignServiceFallback.class, configuration = FeignLogger.class)
public interface GoodsFeignService {

    /**
     * 查询商品列表
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<SpuDTO> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);


    /**
     * 根据Ids查询数据
     *
     * @param ids
     * @return
     */
    @GetMapping("/category/list")
    List<CategoryDTO> queryCategoriesByIds(@RequestParam("ids") List<Long> ids);


    /**
     * 根据spuId查询sku数据
     *
     * @param id
     * @return
     */
    @GetMapping("sku/of/spu")
    List<SkuDTO> findSkusBySpuId(@RequestParam("id") Long id);


    /**
     * 根据规格组Id,或者分类Id,或者是否可搜索，查询对应的规格参数
     *
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("/spec/params")
    List<SpecParamDTO> findParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) boolean searching);


    /**
     * 根据spuId查询spudetail数据
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail")
    SpuDetailDTO findSpuDetailBySpuId(@RequestParam("id") Long spuId);


    /**
     * 根据多个id查询品牌
     *
     * @param ids
     * @return
     */
    @GetMapping("/brand/list")
    List<BrandDTO> queryBrandByIds(@RequestParam("ids") List<Long> ids);


}
