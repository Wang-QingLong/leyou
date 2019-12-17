package cn.leyou.service;

import org.springframework.stereotype.Service;
import cn.leyou.pojo.Item;

import java.util.Random;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 21:27
 * @description:
 */
@Service
public class ItemService {

    public Item saveItem(Item item) {
        int id = new Random().nextInt(100);
        item.setId(id);
        return item;
    }
}
