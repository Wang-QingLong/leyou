package cn.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.leyou.dto.CategoryDTO;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import cn.leyou.mapper.CategoryMapper;
import cn.leyou.pojo.Category;
import cn.leyou.service.CategoryService;
import cn.leyou.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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


    /**
     * 根据品牌查询商品类目
     *
     * @param brandId
     * @return
     */
    @Override
    public List<CategoryDTO> queryListByBrandId(Long brandId) {
        List<Category> list = categorymapper.queryByBrandId(brandId);
        // 判断结果
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        // 使用自定义工具类，把Category集合转为DTO的集合
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }


    /**
     * 根据Ids查询分类集合
     *
     * @param ids
     * @return
     */
    @Override
    public List<CategoryDTO> queryCategoriesByIds(List<Long> ids) {
        List<Category> list = categorymapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {
            // 没找到，返回404    
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }

}
