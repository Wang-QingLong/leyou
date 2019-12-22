package cn.leyou.service;

import cn.leyou.dto.SkuDTO;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 20:30
 * @description:
 */
public interface SkuService {
    /**
     * 根据spuId查询sku数据
     *
     * @param id
     * @return
     */
    List<SkuDTO> findSkusBySpuId(Long id);
}
