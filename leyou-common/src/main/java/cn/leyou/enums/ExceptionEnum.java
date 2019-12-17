package cn.leyou.enums;

import lombok.Getter;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/15 23:04
 * @description:
 */
@Getter
public enum ExceptionEnum {

    PRICE_CANNOT_BE_NULL(400, "价钱不能为空啊"),
    CATEGORY_FINDALL_BE_NULL(200, "查询Category表为null");
    private int status;
    private String message;


    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
