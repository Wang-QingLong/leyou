package cn.leyou.controller;

import cn.leyou.pojo.Category;
import cn.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/16 21:08
 * @description:
 */
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
}
