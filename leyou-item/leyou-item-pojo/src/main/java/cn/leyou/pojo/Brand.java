package cn.leyou.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 19:28
 * @description:
 */
@Data
@Table(name = "tb_brand")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @KeySql(useGeneratedKeys =true)
    private Long id;
    private String name;// 品牌名称
    private String image;// 品牌图片
    private Character letter;
}
