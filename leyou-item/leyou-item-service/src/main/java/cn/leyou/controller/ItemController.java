package cn.leyou.controller;

import cn.leyou.service.ItemService;
import cn.leyou.enums.ExceptionEnum;
import cn.leyou.exception.LyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.leyou.pojo.Item;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 21:25
 * @description:
 */
@RestController
public class ItemController {


    @Autowired
    private ItemService itemService;

    @GetMapping("item")
    public ResponseEntity<Item> saveItem(Item item) {
       if (item.getPrice()==null){
           throw new LyException(ExceptionEnum.PRICE_CANNOT_BE_NULL);
       }
       return ResponseEntity.ok(itemService.saveItem(item));
    }
}