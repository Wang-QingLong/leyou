package cn.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import cn.leyou.mapper.CategoryMapper;
import cn.leyou.pojo.Category;
import cn.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/16 21:13
 * @description:
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categorymapper;


    /**
     * Id查询
     *
     * @param pid
     * @return
     */
    @Override
    public List<Category> findCateGoryById(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
         //根据pid查询
        List<Category> categoryList = categorymapper.select(category);

        //判断集合是否为空
        if (CollUtil.isEmpty(categoryList)) {
            //查询无值
            throw new LyException(ExceptionEnum.CATEGORY_FINDALL_BE_NULL);
        }

        return categoryList;
    }
}
