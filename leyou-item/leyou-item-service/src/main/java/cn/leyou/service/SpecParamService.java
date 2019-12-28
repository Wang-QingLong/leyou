package cn.leyou.service;

import cn.leyou.dto.SpecParamDTO;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 17:37
 * @description:
 */
public interface SpecParamService {
    /**
     * 根据规格组Id,或者分类Id,或者是否可搜索，查询对应的规格参数
     *
     * @param gid
     * @return
     */
    List<SpecParamDTO> findParams(Long gid,Long cid,boolean searching );
}
