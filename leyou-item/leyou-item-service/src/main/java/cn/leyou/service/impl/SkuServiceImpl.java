package cn.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.leyou.dto.SkuDTO;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import cn.leyou.mapper.SkuMapper;
import cn.leyou.pojo.Sku;
import cn.leyou.service.SkuService;
import cn.leyou.utils.BeanHelper;
import cn.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 20:30
 * @description:
 */
@Slf4j
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    /**
     * 根据spuId查询sku数据
     * 当有主键时,优先用selectByPrimaryKey,
     * 当根据实体类属性查询时用select,当有复杂查询时,如模糊查询,条件判断时使用selectByExample
     *
     * @param id
     * @return
     */
    @Override
    public List<SkuDTO> findSkusBySpuId(Long id) {
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",id);

        List<Sku> skus = null;
        try {
            skus = this.skuMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("selectByExample执行异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        if (CollUtil.isEmpty(skus)) {
            log.error("selectByExample查询结果为空");
            throw new LyException(ExceptionEnum.FIND_SKUS_BY_SPUID_FAILED);
        }


        return BeanHelper.copyWithCollection(skus, SkuDTO.class);
    }
}
