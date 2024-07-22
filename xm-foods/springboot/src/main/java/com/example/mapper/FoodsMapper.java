package com.example.mapper;

import com.example.entity.Foods;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作foods相关数据接口
 */
public interface FoodsMapper {

    /**
     * 新增
     */
    int insert(Foods foods);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Foods foods);

    /**
     * 根据ID查询
     */
    Foods selectById(Integer id);

    /**
     * 查询所有
     */
    List<Foods> selectAll(Foods foods);

    List<Foods> selectTop10();

    @Update("update foods set count = count +1 where id = #{id}")
    void updateCount(Integer id);

    List<Foods> selectCollect(Foods foods);
}