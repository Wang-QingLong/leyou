package cn.leyou.controller;

import cn.leyou.dto.SpecParamDTO;
import cn.leyou.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 17:35
 * @description:
 */
@RestController
@RequestMapping("/spec")
public class SpecParamController {

    @Autowired
    private SpecParamService specParamService;


    /**
     * 根据规格组Id查询具体的参数
     *
     * @param gid
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParamDTO>> findParams(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid ) {

        return ResponseEntity.ok(specParamService.findParams(gid,cid));
    }
}
