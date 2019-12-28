package cn.leyou;

import cn.leyou.dto.SpuDTO;
import cn.leyou.result.PageResult;
import cn.leyou.search.clients.GoodsFeignService;
import cn.leyou.search.pojo.Goods;
import cn.leyou.search.repository.GoodsRepository;
import cn.leyou.search.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/23 22:22
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class atest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsFeignService goodsFeignService;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private IndexService indexService;

    @Test
    public void CreateRepositoryIndex() {
         //创建索引库
        elasticsearchTemplate.createIndex(Goods.class);
        //创建映射关系
        elasticsearchTemplate.putMapping(Goods.class);

    }


    @Test
    public void addData() {

        //调用方法查询数据库数据
        int page = 1;
        while (true) {
            //死循环，分页查询spu
            PageResult<SpuDTO> spuDTOPageResult = goodsFeignService.querySpuByPage(page, 50, true, null);

            page++;

            //查询为空，则中止查询
            if (spuDTOPageResult == null) {
                break;
            }
            List<SpuDTO> items = spuDTOPageResult.getItems();

            //把spuDTO转换为goods
            List<Goods> goodsList = items.stream()  //Stream<SpuDTO>
                    .map(item -> this.indexService.buildGoods(item))  //Stream<Goods>
                    .collect(Collectors.toList());   //List<Goods>


            //批量保存goods对象
            this.goodsRepository.saveAll(goodsList);
        }
    }
}
