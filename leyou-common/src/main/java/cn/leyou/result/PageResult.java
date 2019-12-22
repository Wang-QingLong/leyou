package cn.leyou.result;

import lombok.*;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 19:33
 * @description: 页面结果类 ：返回页面数据，总条数，总页数
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult<T> {

    private List<T> items;  //当前页面数据
    private long total;      //总条数
    private long totalPage;  //总页数

}
