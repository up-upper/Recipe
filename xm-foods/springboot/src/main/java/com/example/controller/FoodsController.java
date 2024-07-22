package com.example.controller;

import cn.hutool.core.util.RandomUtil;
import com.example.common.Result;
import com.example.entity.Foods;
import com.example.mapper.FoodsMapper;
import com.example.service.FoodsService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 美食前端操作接口
 **/
@RestController
@RequestMapping("/foods")
public class FoodsController {

    @Resource
    private FoodsService foodsService;


    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Foods foods) {
        foodsService.add(foods);
        return Result.success(foods);
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        foodsService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        foodsService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Foods foods) {
        foodsService.updateById(foods);
        return Result.success();
    }

    @PutMapping("/updateCount/{id}")
    public Result updateCount(@PathVariable Integer id) {
        foodsService.updateCount(id);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Foods foods = foodsService.selectById(id);
        return Result.success(foods);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Foods foods) {
        List<Foods> list = foodsService.selectAll(foods);
        return Result.success(list);
    }

    @GetMapping("/selectCollect")
    public Result selectCollect(Foods foods) {
        List<Foods> list = foodsService.selectCollect(foods);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Foods foods,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Foods> page = foodsService.selectPage(foods, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectRecommend")
    public Result selectRecommend() {
        List<Foods> list = foodsService.selectRecommend();
        return Result.success(list);
    }

    @GetMapping("/recommend")
    public Result recommend() {
        List<Foods> list = foodsService.recommend();
        return Result.success(list);
    }


}