package cn.leyou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 21:18
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {
    private Integer id;
    private String name;
    private Long price;
}
