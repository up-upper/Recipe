package com.example.entity;

public class RelateDTO {
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFoodsId() {
        return foodsId;
    }

    public void setFoodsId(Integer foodsId) {
        this.foodsId = foodsId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    private Integer userId;
    private Integer foodsId;

    private Integer index;

    public RelateDTO(Integer userId,Integer foodsId,Integer index){
        this.foodsId = foodsId;
        this.userId = userId;
        this.index = index;
    }
}
