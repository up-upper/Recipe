package com.example.service;

import com.example.entity.Material;
import com.example.entity.Plan;
import com.example.mapper.PlanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 美食业务处理
 **/
@Service
public class PlanService {

    @Resource
    private PlanMapper planMapper;

    public void add(Plan plan) {
        planMapper.insert(plan);
    }

    public void deleteById(Integer id) {
        planMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            planMapper.deleteById(id);
        }
    }

    public void updateById(Plan plan) {
        planMapper.updateById(plan);
    }

    public Plan selectById(Integer id) {
        return planMapper.selectById(id);
    }

    public List<Plan> selectAll(Plan plan) {
        return planMapper.selectAll(plan);
    }

    public PageInfo<Plan> selectPage(Plan plan, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Plan> list = planMapper.selectAll(plan);
        return PageInfo.of(list);
    }

}