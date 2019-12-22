package cn.leyou.service;

import cn.leyou.dto.BrandDTO;
import cn.leyou.pojo.Brand;
import cn.leyou.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 19:50
 * @description:
 */

public interface BrandService {
    /**
     * 根据查询条件分页并排序查询品牌信息
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> findPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 添加品牌：记住：品牌分类和品牌之间有中间表关系，虽然数据库里面没有
     * 但是自己应该时刻记住
     *
     * @param name
     * @param image
     * @param cids
     * @param letter
     */
    void addBrand(String name, String image, List<Long> cids, Character letter);

    /**
     * 修改品牌
     *
     * @param brand
     * @param ids
     */
    void updateBrand(BrandDTO brand, List<Long> ids);

    /**
     * 品牌Id查询是否存在中间表信息
     *
     * @param brandId
     * @return
     */
    int findBrandAndCategoryByBrandId(Long brandId);

    /**
     * 根据品牌Id删除中间表信息
     *
     * @param brandId
     */
    void deleteBrandCategory(Long brandId);

    /**
     * 删除品牌Id
     *
     * @param brandId
     */
    void deleteById(Long brandId);


    /**
     * Id查询品牌
     *
     * @param id
     * @return
     */
    BrandDTO queryBrandById(Long id);

    /**
     * 根据categoryId查询品牌
     *
     * @param cid
     * @return
     */
    List<BrandDTO> findBrandByCategoryId(Long cid);
}
