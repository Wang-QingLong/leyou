package cn.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.leyou.dto.BrandDTO;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import cn.leyou.mapper.BrandMapper;
import cn.leyou.pojo.Brand;
import cn.leyou.result.PageResult;
import cn.leyou.service.BrandService;
import cn.leyou.utils.BeanHelper;
import cn.leyou.utils.MySqlExceptionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 19:50
 * @description:
 */
@Slf4j
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

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
    @Override
    public PageResult<Brand> findPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化example对象 example相当于是where后面的条件，而criteria是条件容器，可以放置多个对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(key)) {
            //根据name模糊查询，或者根据首字母查询
            criteria.andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        //添加分页条件
        PageHelper.startPage(page, rows);

        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        //通过通用mapper查询
        List<Brand> brands = brandMapper.selectByExample(example);
        //判断是否有值
        if (CollUtil.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_FINDPAGE_BE_NULL);
        }
        //通过分页助手分页
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brands);

        List<BrandDTO> list = BeanHelper.copyWithCollection(brands, BrandDTO.class);

        return new PageResult(list, brandPageInfo.getTotal(), brandPageInfo.getPages());
    }


    /**
     * 添加品牌：记住：品牌分类和品牌之间有中间表关系，虽然数据库里面没有
     * 但是自己应该时刻记住
     *
     * @param name
     * @param image
     * @param cids
     * @param letter
     */
    @Override
    @Transactional
    public void addBrand(String name, String image, List<Long> cids, Character letter) {
        //分2步走，第一步新增品牌数据，第二步往中间表添加数据
        //往品牌表brand添加数据

        Brand brand = new Brand(null, name, image, letter);
        int i = brandMapper.insertSelective(brand);
        //如果返回不为1则表示插入失败
        if (i != 1) {
            throw new LyException(ExceptionEnum.BRAND_ADD_FAIL);
        }

        //往中间表插入数据
        int count = brandMapper.addBrandCategory(brand.getId(), cids);

        if (count != cids.size()) {
            throw new LyException(ExceptionEnum.BRANDANDCATEGORY_ADD_FAIL);
        }
    }


    /**
     * 修改品牌
     *
     * @param brandDTO
     * @param ids
     */
    @Override
    @Transactional
    public void updateBrand(BrandDTO brandDTO, List<Long> ids) {


        Brand brand = BeanHelper.copyProperties(brandDTO, Brand.class);
        // 修改品牌
        int count = brandMapper.updateByPrimaryKeySelective(brand);
        if (count != 1) {
            // 更新失败，抛出异常
            throw new LyException(ExceptionEnum.UPDATE_BRAND_FAIL);
        }
        // 删除中间表数据
        brandMapper.deleteCategoryBrand(brand.getId());

        // 重新插入中间表数据
        count = brandMapper.addBrandCategory(brand.getId(), ids);
        if (count != ids.size()) {
            // 新增失败，抛出异常
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

    }

    /**
     * 根据品牌Id查询是否存在中间表信息
     *
     * @param brandId
     * @return
     */
    @Override
    public int findBrandAndCategoryByBrandId(Long brandId) {
        return brandMapper.findBrandAndCategory(brandId);
    }

    /**
     * 根据品牌Id删除中间表信息
     *
     * @param brandId
     */
    @Override
    public void deleteBrandCategory(Long brandId) {
        brandMapper.deleteCategoryBrand(brandId);

    }

    /**
     * 删除品牌Id
     *
     * @param brandId
     */
    @Override
    public void deleteById(Long brandId) {

        brandMapper.deleteByPrimaryKey(brandId);
    }


    /**
     * id查询品牌
     *
     * @param id
     * @return
     */
    @Override
    public BrandDTO queryBrandById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        return BeanHelper.copyProperties(brand, BrandDTO.class);
    }

    /**
     * 根据categoryId查询品牌
     *
     * @param cid
     * @return
     */
    @Override
    public List<BrandDTO> findBrandByCategoryId(Long cid) {


        List<Brand> brandlist = null;
        try {
            brandlist = brandMapper.findBrandByCategoryId(cid);
        } catch (Exception e) {
            log.error("findBrandByCategoryId根据categoryId查询品牌异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }


        return  BeanHelper.copyWithCollection(brandlist,BrandDTO.class);
    }
}
