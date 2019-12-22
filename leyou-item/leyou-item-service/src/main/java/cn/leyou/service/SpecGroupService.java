package cn.leyou.service;

import cn.leyou.dto.SpecGroupDTO;
import cn.leyou.dto.SpecParamDTO;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 16:01
 * @description:
 */
public interface SpecGroupService {
    /**
     * 根据categoryId查询商品规格组信息
     *
     * @param id
     * @return
     */
    List<SpecGroupDTO> findGroupsOfCategoryById(Long id);


}
