package cn.leyou.service.impl;

import cn.leyou.dto.SpecGroupDTO;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import cn.leyou.mapper.SpecGroupServiceMapper;
import cn.leyou.pojo.SpecGroup;
import cn.leyou.service.SpecGroupService;
import cn.leyou.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 16:01
 * @description:
 */
@Service
public class SpecGroupServiceImpl implements SpecGroupService {
    @Autowired
    private SpecGroupServiceMapper specGroupServiceMapper;

    /**
     * 根据categoryId查询商品规格组信息
     *
     * @param id
     * @return
     */
    @Override
    public List<SpecGroupDTO> findGroupsOfCategoryById(Long id) {
        // 查询规格组
        SpecGroup s = new SpecGroup();
        s.setCid(id);
        List<SpecGroup> list = null;
        try {
            list = specGroupServiceMapper.select(s);
        } catch (Exception e) {
            //如果产生异常，则表明是数据库连接异常或者表字段对应不上
            throw new LyException(ExceptionEnum.BRAND_SELECT_FAIL);
        }

        //判断查询结果是否为空
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        // 对象转换
        return BeanHelper.copyWithCollection(list, SpecGroupDTO.class);
    }



}
