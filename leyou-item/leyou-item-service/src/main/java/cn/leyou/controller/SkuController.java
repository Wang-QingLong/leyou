package cn.leyou.controller;

import cn.leyou.dto.SkuDTO;
import cn.leyou.service.SkuService;
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
 * @date: 2019/12/22 20:24
 * @description:
 */
@RestController
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    private SkuService skuService;

    @GetMapping("/of/spu")
    public ResponseEntity<List<SkuDTO>> findSkusBySpuId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(this.skuService.findSkusBySpuId(id));
    }
}
