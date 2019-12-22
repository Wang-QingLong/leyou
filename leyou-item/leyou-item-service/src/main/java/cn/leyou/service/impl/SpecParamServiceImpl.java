package cn.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.leyou.dto.SpecParamDTO;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import cn.leyou.mapper.SpecParamMapper;
import cn.leyou.pojo.SpecParam;
import cn.leyou.service.SpecParamService;
import cn.leyou.utils.BeanHelper;
import cn.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 17:38
 * @description:
 */
@Slf4j
@Service
public class SpecParamServiceImpl implements SpecParamService {
    @Autowired
    private SpecParamMapper specParamMapper;


    /**根据id查询参数
     * @param gid
     * @param cid
     * @return
     */
    @Override
    public List<SpecParamDTO> findParams(Long gid,Long cid) {

        //通用mapper天生就是一个动态sql，谁的值不为null谁查，两个都不为null并列查

        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);

        List<SpecParam> specParams = null;
        try {
            //根据规则组Id查询参数
            specParams = specParamMapper.select(specParam);
        } catch (Exception e) {
            //判断异常
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        if (CollUtil.isEmpty(specParams)) {
            throw new LyException(ExceptionEnum.PARAM_FIND_FILED);
        }

        return BeanHelper.copyWithCollection(specParams, SpecParamDTO.class);

    }
}
