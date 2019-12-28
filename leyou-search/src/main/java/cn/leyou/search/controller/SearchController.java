package cn.leyou.search.controller;

import cn.leyou.dto.BrandDTO;
import cn.leyou.result.PageResult;
import cn.leyou.search.dto.GoodsDTO;
import cn.leyou.search.dto.SearchRequest;
import cn.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/25 18:35
 * @description:
 */
@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchService;


    /**
     * 查询
     *
     * @param searchRequest
     * @return
     */
    @PostMapping("/page")
    public ResponseEntity<PageResult<GoodsDTO>> search(@RequestBody SearchRequest searchRequest) {

        return ResponseEntity.ok(this.searchService.search(searchRequest));
    }


    /**
     * 根据过滤条件聚合分桶查询数据
     *
     * @param request
     * @return
     */
    @PostMapping("/filter")
    public ResponseEntity<Map<String, List<?>>> queryFilters(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.queryFilters(request));
    }

}
