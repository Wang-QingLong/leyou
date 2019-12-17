package cn.leyou.service;


import cn.leyou.pojo.Category;

import java.util.List;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/16 21:08
 * @description:
 */

public interface CategoryService {


    /**根据Id查询
     * @param pid
     * @return
     */
    List<Category> findCateGoryById(Long pid);
}
