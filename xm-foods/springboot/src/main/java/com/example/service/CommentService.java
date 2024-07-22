package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 评论表业务处理
 **/
@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    /**
     * 新增
     */
    public void add(Comment comment) {
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);

        // 更新rootId
        if (comment.getPid() == null) {
            comment.setRootId(comment.getId());
        } else {
            Comment pComment = commentMapper.selectById(comment.getPid());  // 根据父节点的id查出对应的父节点对象
            if (pComment != null) {
                comment.setRootId(pComment.getRootId());
            }
        }
        this.updateById(comment);
    }

    /**
     * 递归删除
     */
    public void deleteById(Integer id) {
        this.deepDelete(id);
    }

    private void deepDelete(Integer pid) {
        List<Comment> children = commentMapper.selectByPid(pid);
        commentMapper.deleteById(pid);   // 先执行删除  再去找子节点  然后递归删除
        if (CollUtil.isNotEmpty(children)) {
            for (Comment child : children) {
                deepDelete(child.getId());
            }
        }
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

    public Integer selectCount(Integer fid, String module) {
        return commentMapper.selectCount(fid, module);
    }

    public List<Comment> selectTree(Integer fid, String module) {
        List<Comment> rootList = commentMapper.selectRoot(fid, module);
        for (Comment root : rootList) {
            Integer rootId = root.getRootId();
            List<Comment> children = commentMapper.selectByRootId(rootId);
            root.setChildren(children);
        }
        return rootList;
    }

}