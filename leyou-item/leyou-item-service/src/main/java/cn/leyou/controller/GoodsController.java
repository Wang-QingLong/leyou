package cn.leyou.controller;

import cn.leyou.dto.SpuDTO;
import cn.leyou.pojo.Spu;
import cn.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 17:00
 * @description:
 */
@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    @PostMapping
    public ResponseEntity<Void> findGoods(@RequestBody SpuDTO spuDTO) {

        this.goodsService.addGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }




    @PutMapping
    public ResponseEntity<Void> UpdateGoods(@RequestBody SpuDTO spuDTO) {

        this.goodsService.UpdateGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
