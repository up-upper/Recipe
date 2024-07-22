package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.common.enums.ModuleEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.*;
import com.example.utils.TokenUtils;
import com.example.utils.UserCF;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 美食业务处理
 **/
@Service
public class FoodsService {

    @Resource
    private FoodsMapper foodsMapper;

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private LikesMapper likesMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    CollectService collectService;

    @Resource
    private LikesService likesService;
    /**
     * 新增
     */
    public void add(Foods foods) {
        foodsMapper.insert(foods);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        foodsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            foodsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Foods foods) {
        foodsMapper.updateById(foods);
    }

    /**
     * 根据ID查询
     */
    public Foods selectById(Integer id) {
        Foods foods = foodsMapper.selectById(id);
        Account currentUser = TokenUtils.getCurrentUser();
        Integer userId = null;
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            userId = currentUser.getId();
        }
        List<Collect> collectList = collectService.selectCollect(id, userId, ModuleEnum.FOODS.getValue());
        foods.setUserCollect(CollUtil.isNotEmpty(collectList));

        List<Collect> list = collectService.selectCollect(id, null, ModuleEnum.FOODS.getValue());
        foods.setCollectCount(list.size());

        List<Likes> likesList = likesService.selectLikes(id, userId, ModuleEnum.FOODS.getValue());
        foods.setUserLike(CollUtil.isNotEmpty(likesList));

        List<Likes> allLikesList = likesService.selectLikes(id, null, ModuleEnum.FOODS.getValue());
        foods.setLikesCount(allLikesList.size());

        return foods;
    }

    /**
     * 查询所有
     */
    public List<Foods> selectAll(Foods foods) {
        Account currenUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currenUser.getRole())){
            foods.setUserId(currenUser.getId());
        }
        return foodsMapper.selectAll(foods);
    }

    /**
     * 分页查询
     */
    public PageInfo<Foods> selectPage(Foods foods, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Foods> list = foodsMapper.selectAll(foods);
        return PageInfo.of(list);
    }

    /**
     * 查询推荐的食谱
     * 浏览榜单前4
     * 从前10的食谱里面随机找到4个
     */
    public List<Foods> selectRecommend() {
        List<Foods> foodsList = foodsMapper.selectTop10();
        if (foodsList.size() <= 4) {
            return foodsList;
        }
        List<Foods> foods = new ArrayList<>();
        Set<Integer> nums = new HashSet<>();
        while (nums.size() < 4) {
            int num = RandomUtil.randomInt(0, foodsList.size() - 1); // 随机找到一个序号 这个序号就是 foodsList 其中一个元素的序号
            if (!nums.contains(num)) {
                nums.add(num);
                Foods f = foodsList.get(num);
                foods.add(f);
            }
        }
        return foods;
    }

    public List<Foods> selectCollect(Foods foods) {
        // 如果是普通用户 那么只查询自己的食谱
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            foods.setUserId(currentUser.getId());
        }
        return foodsMapper.selectCollect(foods);
    }

    public void updateCount(Integer id) {
        foodsMapper.updateCount(id);
    }

//    private List<Foods> getRandomFoods(int num){
//        List<Foods> list = new ArrayList<>(num);
//        List<Foods> foods = foodsMapper.selectAll(null);
//        for (int i = 0; i < num; i++){
//            int index = new Random().nextInt(foods.size());
//            list.add(foods.get(index));
//        }
//        return list;
//    }

    //只能保证除去推荐外的随机生成的不重复，但不能保证和已推荐的里面的重复；
    public List<Foods> getRandomFoods(int num) {
        List<Foods> foods = foodsMapper.selectAll(null);
        List<Foods> resultList = new ArrayList<>();
        Set<Foods> selectedSet = new HashSet<>();
        Random random = new Random();

        while (resultList.size() < num) {
            int index = random.nextInt(foods.size());
            Foods food = foods.get(index);
            // 如果food还没有被选中，则添加到结果列表中
            if (selectedSet.add(food)) {
                resultList.add(food);
            }
        }

        return resultList;
    }

    public List<Foods> recommend() {
        //用户的哪些行为可以认为和商品有关系？收藏、点赞、评论
        //获取所有收藏信息
        List<Collect> allCollect = collectMapper.selectCollect(null);
        //获取所有评论信息
        List<Comment> allComments = commentMapper.selectAll(null);
        //获取所有点赞信息
        List<Likes> allLikes = likesMapper.selectLikes(null);
        //获取所有用户信息
        List<User> allUsers = userMapper.selectAll(null);
        //获取所有食谱
        List<Foods> allFoods = foodsMapper.selectAll(null);
        //接收存储所有关系数据得list
        List<RelateDTO> alldata = new ArrayList<>();

        List<RelateDTO> data = new ArrayList<>();

        List<Foods> result = new ArrayList<>();

        //开始计算每个食谱和用户之间的关系数据
        for (Foods foods : allFoods){
            Integer foodsId = foods.getId();
            for (User user : allUsers){
                Integer userId = user.getId();
                int index = 1;
                //判断该用户有没有收藏该商品
                Optional<Collect> collectOptional = allCollect.stream()
                        .filter(x -> x.getFid().equals(foodsId) && x.getUserId().equals(userId)).findFirst();
                if (collectOptional.isPresent()){
                    index += 3;
                }
                //判断该用户有没有评论该商品
                Optional<Comment> commentOptional = allComments.stream()
                        .filter(x -> x.getFid().equals(foodsId) && x.getUserId().equals(userId)).findFirst();
                if (commentOptional.isPresent()){
                    index += 1;
                }
                //判断该用户有没有点赞该商品
                Optional<Likes> likesOptional = allLikes.stream()
                        .filter(x -> x.getFid().equals(foodsId) && x.getUserId().equals(userId)).findFirst();
                if (likesOptional.isPresent()){
                    index += 2;
                }
                //获取所有食谱合集
                RelateDTO relateDTO = new RelateDTO(userId, foodsId, index);
                alldata.add(relateDTO);
                // 如果 index 不大于 1，那么 data 和 alldata 应该是一样的
                if (index > 1) {
                    RelateDTO relateDTOd = new RelateDTO(userId, foodsId, index);
                    data.add(relateDTOd);
                }
            }
        }
        //数据准备结束，喂给推荐算法
        Account currentUser = TokenUtils.getCurrentUser();
        List<Integer> foodsIds = UserCF.recommend(currentUser.getId(),alldata,data);
        List<Foods> recommentResult = foodsIds.stream().map(foodsId -> allFoods.stream()
                .filter(x -> x.getId().equals(foodsId)).findFirst().orElse(null))
                .limit(10).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(recommentResult)){
            return getRandomFoods(4);
        }
        if (recommentResult.size()<4){
            int num = 4 -recommentResult.size();
            List<Foods> list = getRandomFoods(num);
            recommentResult.addAll(list);
            result = recommentResult;
        }
        if (recommentResult.size() >= 4) {
            Random random = new Random();
            // 生成一个随机排列的索引数组
            List<Integer> randomIndices = IntStream.range(0, recommentResult.size())
                    .boxed()
                    .collect(Collectors.toList());
            Collections.shuffle(randomIndices); // 打乱顺序
            // 使用随机排列的前4个索引来从recommentResult中获取元素
            List<Foods> overresult = randomIndices.stream()
                    .limit(4)
                    .map(recommentResult::get)
                    .collect(Collectors.toList());
            // 将result赋给最终需要返回的变量
            // 注意：这里假设你有一个外部变量result来存储最终的结果
            // 如果你的逻辑中result变量是局部变量，请确保你在适当的作用域内定义和更新它
            result = overresult; // 假设result是类的成员变量或者你需要返回的结果变量
        }
        return result;
    }
}