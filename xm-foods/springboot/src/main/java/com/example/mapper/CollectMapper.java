package com.example.mapper;

import com.example.entity.Collect;
import com.example.entity.Foods;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CollectMapper {

    List<Collect> selectAll(Collect collect);

    List<Collect> selectCollect(Collect collect);

    @Delete("delete from collect where fid = #{fid} and user_id = #{userId} and module = #{module}")
    void delete(Collect collect);

    @Insert("insert into collect (fid, user_id, module) values(#{fid}, #{userId}, #{module})")
    void insert(Collect collect);

}
