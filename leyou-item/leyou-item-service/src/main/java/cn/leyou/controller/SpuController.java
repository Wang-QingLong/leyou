package cn.leyou.controller;

import cn.leyou.dto.SpuDTO;
import cn.leyou.dto.SpuDetailDTO;
import cn.leyou.result.PageResult;
import cn.leyou.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 21:10
 * @description:
 */
@RestController
@RequestMapping("/spu")
public class SpuController {
    @Autowired
    private SpuService spuService;


    /**
     * 查询商品列表
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<SpuDTO>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        return ResponseEntity.ok(spuService.querySpuByPage(page, rows, saleable, key));
    }

    /**
     * 更新上架状态
     *
     * @param id
     * @param saleable
     * @return
     */
    @PutMapping("/saleable")
    public ResponseEntity<Void> UpdateSaleable(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "saleable") boolean saleable
    ) {

        this.spuService.UpdateSaleable(id, saleable);

        return ResponseEntity.ok().build();
    }


    /**根据spuId查询spudetail，回显
     * @param spuId
     * @return
     */
    @GetMapping("/detail")
    public ResponseEntity<SpuDetailDTO> findSpuDetailBySpuId(@RequestParam("id") Long spuId) {

        return ResponseEntity.ok(this.spuService.findSpuDetailBySpuId(spuId));
    }
}


