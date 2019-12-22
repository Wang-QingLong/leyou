package cn.leyou.controller;

import cn.leyou.dto.BrandDTO;
import cn.leyou.pojo.Brand;
import cn.leyou.result.PageResult;
import cn.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 19:05
 * @description:
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;


    /**
     * 根据查询条件分页并排序查询品牌信息
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> findPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,  //required=false表示这是一个非必要参数
            @RequestParam(value = "desc", required = false) Boolean desc
    ) {
        PageResult<Brand> pageResult = brandService.findPage(key, page, rows, sortBy, desc);

        return ResponseEntity.ok(pageResult);

    }

    /**
     * 添加品牌
     *
     * @param name
     * @param image
     * @param cids
     * @param letter
     * @return
     */
    @PostMapping
    public ResponseEntity<String> addBrand(
            @RequestParam("name") String name,
            @RequestParam("image") String image,
            @RequestParam("cids") List<Long> cids,
            @RequestParam("letter") Character letter
    ) {

        brandService.addBrand(name, image, cids, letter);

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    /**
     * 修改品牌
     *
     * @param brand
     * @param ids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(BrandDTO brand, @RequestParam("cids") List<Long> ids) {
        brandService.updateBrand(brand, ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 删除品牌
     *
     * @param brandId
     * @return
     */
    @DeleteMapping("deleteBrandById")
    public ResponseEntity<String> deleteBrandById(@RequestParam(value = "id") Long brandId) {
        //先查询一下是否存在中间表关联
        int count = brandService.findBrandAndCategoryByBrandId(brandId);
        if (count > 0) {
            //存在关联,不可删除

            //执意要删除，先删除中间表信息
            brandService.deleteBrandCategory(brandId);
        }
//删除品牌Id
        brandService.deleteById(brandId);

        return ResponseEntity.ok("删除成功！");
    }

    /**
     * 根据categoryId查询品牌
     *
     * @param cid
     * @return
     */
    @GetMapping("/of/category")
    public ResponseEntity<List<BrandDTO>> findBrandByCategoryById(@RequestParam("id") Long cid) {

        return ResponseEntity.ok(brandService.findBrandByCategoryId(cid));
    }


}