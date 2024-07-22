package com.example.mapper;

import com.example.entity.Material;

import java.util.List;

/**
 * 操作material相关数据接口
 */
public interface MaterialMapper {

    /**
     * 新增
     */
    int insert(Material material);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Material material);

    /**
     * 根据ID查询
     */
    Material selectById(Integer id);

    /**
     * 查询所有
     */
    List<Material> selectAll(Material material);

}