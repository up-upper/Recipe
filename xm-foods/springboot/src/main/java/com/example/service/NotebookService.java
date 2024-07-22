package com.example.service;

import cn.hutool.core.collection.CollUtil;
import com.example.common.enums.ModuleEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.entity.Likes;
import com.example.entity.Notebook;
import com.example.mapper.NotebookMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 美食笔记业务处理
 **/
@Service
public class NotebookService {

    @Resource
    private NotebookMapper notebookMapper;

    @Resource
    private CollectService collectService;

    @Resource
    private LikesService likesService;

    /**
     * 新增
     */
    public void add(Notebook notebook) {
        notebookMapper.insert(notebook);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        notebookMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            notebookMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Notebook notebook) {
        notebookMapper.updateById(notebook);
    }

    /**
     * 根据ID查询
     */
    public Notebook selectById(Integer id) {
        Notebook notebook = notebookMapper.selectById(id);
        Account currentUser = TokenUtils.getCurrentUser();
        Integer userId = null;
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            userId = currentUser.getId();
        }
        List<Collect> collectList = collectService.selectCollect(id, userId, ModuleEnum.NOTEBOOK.getValue());
        notebook.setUserCollect(CollUtil.isNotEmpty(collectList));

        List<Collect> list = collectService.selectCollect(id, null, ModuleEnum.NOTEBOOK.getValue());
        notebook.setCollectCount(list.size());

        List<Likes> likesList = likesService.selectLikes(id, userId, ModuleEnum.NOTEBOOK.getValue());
        notebook.setUserLike(CollUtil.isNotEmpty(likesList));

        List<Likes> allLikesList = likesService.selectLikes(id, null, ModuleEnum.NOTEBOOK.getValue());
        notebook.setLikesCount(allLikesList.size());
        return notebook;
    }

    /**
     * 查询所有
     */
    public List<Notebook> selectAll(Notebook notebook) {
        // 如果是普通用户 那么只查询自己的食谱
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            notebook.setUserId(currentUser.getId());
        }
        return notebookMapper.selectAll(notebook);
    }

    /**
     * 分页查询
     */
    public PageInfo<Notebook> selectPage(Notebook notebook, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Notebook> list = notebookMapper.selectAll(notebook);
        return PageInfo.of(list);
    }

    public void updateCount(Integer id) {
        notebookMapper.updateCount(id);
    }

    public List<Notebook> selectCollect(Notebook notebook) {
        Integer userId = null;
        // 如果是普通用户 那么只查询自己的食谱
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            userId = currentUser.getId();
        }
        return notebookMapper.selectCollect(userId);

    }
}