package cn.leyou.service.impl;

import cn.leyou.dto.BrandDTO;
import cn.leyou.dto.CategoryDTO;
import cn.leyou.dto.SpuDTO;
import cn.leyou.dto.SpuDetailDTO;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import cn.leyou.mapper.SkuMapper;
import cn.leyou.mapper.SpuDetailMapper;
import cn.leyou.mapper.SpuMapper;
import cn.leyou.pojo.Sku;
import cn.leyou.pojo.Spu;
import cn.leyou.pojo.SpuDetail;
import cn.leyou.result.PageResult;
import cn.leyou.service.BrandService;
import cn.leyou.service.CategoryService;
import cn.leyou.service.SpuService;
import cn.leyou.utils.BeanHelper;
import cn.leyou.utils.MySqlExceptionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 21:57
 * @description:
 */
@Slf4j
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;


    @Override
    public PageResult<SpuDTO> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        // 1 分页
        PageHelper.startPage(page, rows);
        // 2,获取动态sql生成器
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 2.1 搜索条件过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        // 2.2 上下架过滤
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        // 2.3 默认按时间排序
//        example.setOrderByClause("update_time DESC");
        // 3 查询结果
        List<Spu> list = null;
        try {
            list = spuMapper.selectByExample(example);
        } catch (Exception e) {
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        // 4 封装分页结果
        PageInfo<Spu> info = new PageInfo<>(list);

        // DTO转换
        List<SpuDTO> spuDTOList = BeanHelper.copyWithCollection(list, SpuDTO.class);

        //迭代分类集合，挨个取出name,并把所有的name,拼接为字符串，分隔符自定义

        spuDTOList.forEach(spuDTO -> {
            //跨业务根据Id查询分类
            List<CategoryDTO> categoryDTOS = this.categoryService.queryCategoriesByIds(spuDTO.getCategoryIds());

            String names = categoryDTOS.stream()     //List<CategoryDTO  ==> Stream<CategoryDTO> >
                    .map(CategoryDTO::getName)
                    .collect(Collectors.joining("/"));

            spuDTO.setCategoryName(names);

            spuDTO.setBrandName(this.brandService.queryBrandById(spuDTO.getBrandId()).getName());

        });

        return new PageResult<SpuDTO>( info.getTotal(), info.getPages(),spuDTOList);
    }

    /**
     * 更新上架状态
     * 注意:在修改spu属性的同时，还需要修改sku的enable属性，
     * 因为spu下架，sku也要跟着进行下架
     *
     * @param Id
     * @param saleable
     */
    @Override
    public void UpdateSaleable(Long Id, boolean saleable) {

        //我们在修改spu属性的同时，还需要修改sku的enable属性，因为spu下架，sku也要跟着进行下架
        Spu spu = new Spu();
        spu.setSaleable(saleable);
        spu.setId(Id);
        try {
            this.spuMapper.updateByPrimaryKeySelective(spu);
        } catch (Exception e) {
            log.error("更新上架状态失败");
            //如果出错则抛异常
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        //更新sku

        Sku sku = new Sku();
        sku.setEnable(saleable);
        //准备更新匹配的条件

        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", Id);
        //更新
        int i = this.skuMapper.updateByExampleSelective(sku, example);

        int count = this.skuMapper.selectCountByExample(example);

        if (i != count) {
            log.error("更新上架状态失败");
            throw new LyException(ExceptionEnum.UPDATE_SALEABLE_FAIL);
        }
    }


    /**根据spuId查询spudetail
     * @param id
     * @return
     */
    @Override
    public SpuDetailDTO findSpuDetailBySpuId(Long id) {

        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if (spuDetail == null) {
            log.error("根据spuId查询spudetail失败");
            throw new LyException(ExceptionEnum.FIND_SPUDETAIL_BY_SPUID_FAILED);
        }
        return BeanHelper.copyProperties(spuDetail, SpuDetailDTO.class);
    }
}
