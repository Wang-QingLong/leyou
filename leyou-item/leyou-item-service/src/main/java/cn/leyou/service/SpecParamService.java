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
     * 根据规 格组Id查询参数
     *
     * @param gid
     * @return
     */
    List<SpecParamDTO> findParams(Long gid,Long cid);
}
