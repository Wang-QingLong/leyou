package cn.leyou.controller;

import cn.leyou.dto.CategoryDTO;
import cn.leyou.pojo.Category;
import cn.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/16 21:08
 * @description:
 */
//@CrossOrigin(allowedHeaders = "*",allowCredentials = "true")
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有
     *
     * @param pid
     */
    @GetMapping("/of/parent")
    public ResponseEntity<List<Category>> findAll(@RequestParam("pid") Long pid) {

        List<Category> categoryList = categoryService.findCateGoryById(pid);

        return ResponseEntity.ok(categoryList);
    }


    /**
     * 根据品牌查询商品类目
     *
     * @param brandId
     * @return
     */
    @GetMapping("/of/brand")
    public ResponseEntity<List<CategoryDTO>> queryByBrandId(@RequestParam(value = "id") Long brandId) {
        return ResponseEntity.ok(this.categoryService.queryListByBrandId(brandId));
    }

}
