package cn.leyou.service;

import cn.leyou.dto.SpuDTO;
import cn.leyou.dto.SpuDetailDTO;
import cn.leyou.result.PageResult;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 21:57
 * @description:
 */
public interface SpuService {
    /**
     * 查询页面数据
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    PageResult<SpuDTO> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key);

    /**
     * 更新上架状态
     */
    void UpdateSaleable(Long Id, boolean saleable);

    /**
     * 根据spuId查询spudetail商品描述信息
     *
     * @param spuId
     * @return
     */
    SpuDetailDTO findSpuDetailBySpuId(Long spuId);

}
