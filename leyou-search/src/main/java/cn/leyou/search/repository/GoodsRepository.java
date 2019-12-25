package cn.leyou.search.repository;

import cn.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/23 20:33
 * @description:
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
