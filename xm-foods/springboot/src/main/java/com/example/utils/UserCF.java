package com.example.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.RelateDTO;
import com.example.utils.CoreMath;

import java.util.*;
import java.util.stream.Collectors;

public class UserCF {

    /**
     * 方法描述: 推荐商品id列表
     *
     * @param userId 当前用户
     * @param list   用户商品评分数据
     * @return {@link List<Integer>}
     */
    public static List<Integer> recommend(Integer userId, List<RelateDTO> list, List<RelateDTO> filterlist) {
        //list是当前用户和所有用户和食谱的关系列表，filter是和当前用户有交集的食谱列表
        // 按用户分组，才可以算出当前用户和所有用户的关系系数
        Map<Integer, List<RelateDTO>> userMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getUserId));
        //获得与当前用户有关的食谱列表
        Map<Integer, List<RelateDTO>> dataMap = filterlist.stream().collect(Collectors.groupingBy(RelateDTO::getUserId));
        // 获取其他用户与当前用户的关系值
        Map<Integer, Double> userDisMap = CoreMath.computeNeighbor(userId, userMap, 0);
        if (CollectionUtil.isEmpty(userDisMap)) {
            return Collections.emptyList();
        }
        // 指定用户的有关食谱列表
        List<Integer> userItems = null;
        if (dataMap.containsKey(userId)) {
            userItems = dataMap.get(userId).stream().map(RelateDTO::getFoodsId).collect(Collectors.toList());
        } else {
            // 处理 dataMap 中不包含 userId 的情况
            // 例如：返回空列表，记录日志，或者抛出异常
            userItems = Collections.emptyList();
            // throw new IllegalArgumentException("User ID not found in dataMap: " + userId);
        }
        double maxValue = Collections.max(userDisMap.values());
        Set<Integer> userIds = userDisMap.entrySet().stream()
                .filter(e -> e.getValue().equals(maxValue)) // 使用 .equals() 来比较 double 值
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        Integer nearestUserId = userIds.stream().findAny().orElse(null);
        List<Integer> neighborItems;
        if (nearestUserId == null) {
            // 如果没有找到关系最近的用户，则使用 userItems 作为推荐
            neighborItems = new ArrayList<>(userItems);
        } else {
            // 如果有关系最近的用户，则获取其食谱列表并去除当前用户已经看过的食谱
            List<RelateDTO> neighborRelateDTOs = dataMap.get(nearestUserId);
            if (neighborRelateDTOs != null && !neighborRelateDTOs.isEmpty()) {
                // 获取邻居用户的所有食谱ID
                List<Integer> allNeighborItems = neighborRelateDTOs.stream()
                        .map(RelateDTO::getFoodsId)
                        .collect(Collectors.toList());
                // 去除当前用户已经看过的食谱ID
                List<Integer> finalUserItems = userItems;
                neighborItems = allNeighborItems.stream()
                        .filter(foodId -> !finalUserItems.contains(foodId))
                        .distinct() // 去除重复项
                        .collect(Collectors.toList());
            } else {
                // 如果没有找到邻居用户的食谱列表，则可能返回一个空列表或者使用 userItems 作为回退
                neighborItems = Collections.emptyList(); // 或者使用 new ArrayList<>(userItems);
            }
        }
        // 现在 neighborItems 要么是邻居用户的食谱列表（去除了当前用户看过的），要么是 userItems 的拷贝
        return neighborItems;

    }
}