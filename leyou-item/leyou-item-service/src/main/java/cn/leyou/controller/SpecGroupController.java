package cn.leyou.controller;

import cn.leyou.dto.SpecGroupDTO;
import cn.leyou.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 15:49
 * @description:
 */
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/spec")
public class SpecGroupController {
    @Autowired
    private SpecGroupService specGroupService;

    /**
     * 根据categoryId查询商品组信息
     *
     * @param id
     * @return
     */
    @GetMapping("/groups/of/category")
    public ResponseEntity<List<SpecGroupDTO>> findGroupsOfCategoryById(@RequestParam("id") Long id) {


        return ResponseEntity.ok(specGroupService.findGroupsOfCategoryById(id));
    }

}
