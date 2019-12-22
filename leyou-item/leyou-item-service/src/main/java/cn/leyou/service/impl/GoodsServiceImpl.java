package cn.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.leyou.dto.SkuDTO;
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
import cn.leyou.service.GoodsService;
import cn.leyou.service.SpuService;
import cn.leyou.utils.BeanHelper;
import cn.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 17:11
 * @description:
 */
@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;


    /**
     * 添加商品信息
     *
     * @param spuDTO
     */
    @Override
    @Transactional
    public void addGoods(SpuDTO spuDTO) {


        //插入spu数据并伴有主键回显
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);

        try {
            this.spuMapper.insertSelective(spu);
        } catch (Exception e) {
            log.error("spu表数据插入失败");
            MySqlExceptionUtils.CheckMySqlException(e);
        }


        //获取spuDetail数据
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        spuDetail.setSpuId(spu.getId());
        //插入数据
        try {
            this.spuDetailMapper.insertSelective(spuDetail);
        } catch (Exception e) {
            log.error("spudetail表数据插入");
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        //获取sku数据
        //挨个插入
        List<SkuDTO> skus = spuDTO.getSku();

        if (CollUtil.isNotEmpty(skus)) {

            List<Sku> skuList = skus.stream().map(skuDTO -> {
                skuDTO.setSpuId(spu.getId());
                return BeanHelper.copyProperties(skuDTO, Sku.class);
            }).collect(Collectors.toList());

            //插入sku数据
            try {
                this.skuMapper.insertList(skuList);
            } catch (Exception e) {
                MySqlExceptionUtils.CheckMySqlException(e);
            }
        }
    }


    /**
     * 更新商品信息
     *
     * @param spuDTO
     */
    @Override
    @Transactional
    public void UpdateGoods(SpuDTO spuDTO) {
        Long spuId = spuDTO.getId();
        if (spuId == null) {
            // 请求参数有误
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        // 1.删除sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        // 查询数量
        int size = skuMapper.selectCount(sku);
        if(size > 0) {
            // 删除
            int count = skuMapper.delete(sku);
            if(count != size){
                throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
            }
        }

        // 2.更新spu
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
        spu.setSaleable(null);
        spu.setCreateTime(null);
        spu.setUpdateTime(null);
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

        // 3.更新spuDetail
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        spuDetail.setSpuId(spuId);
        spuDetail.setCreateTime(null);
        spuDetail.setUpdateTime(null);
        count = spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        if (count != 1) {
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }

        // 4.新增sku
        List<SkuDTO> dtoList = spuDTO.getSku();

        if (CollUtil.isNotEmpty(dtoList)) {
            // 处理dto
            List<Sku> skuList = dtoList.stream().map(dto -> {
                dto.setEnable(false);
                // 添加spu的id
                dto.setSpuId(spuId);
                return BeanHelper.copyProperties(dto, Sku.class);
            }).collect(Collectors.toList());

            count = skuMapper.insertList(skuList);
            if (count != skuList.size()) {
                throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
            }
        }
    }
}
