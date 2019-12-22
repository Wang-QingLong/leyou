package cn.leyou.service;


import cn.leyou.dto.CategoryDTO;
import cn.leyou.pojo.Category;

import java.util.List;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/16 21:08
 * @description:
 */

public interface CategoryService {


    /**
     * 根据Id查询
     *
     * @param pid
     * @return
     */
    List<Category> findCateGoryById(Long pid);

    /**
     * 根据品牌查询商品类目
     *
     * @param brandId
     * @return
     */
    List<CategoryDTO> queryListByBrandId(Long brandId);


    /**根据Ids查询分类集合
     * @param ids
     * @return
     */
    List<CategoryDTO> queryCategoriesByIds(List<Long> ids);
}
