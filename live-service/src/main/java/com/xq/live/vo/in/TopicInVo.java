package com.xq.live.vo.in;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 13:59
 * @copyright:hbxq
 **/
public class TopicInVo extends BaseInVo {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String zanUserId;//点赞中,当前用户的user_id
    private Long userId;//我的文章中,当前用户的user_id
    private Integer zanSourceType;//用户浏览来源
    private String zanUserIp;
    private String zanUserName;
    private Integer topicType;//文章类型
    private Integer sortType;//排序类型 0时间 1浏览量  默认是时间
    private Integer choiceType;//筛选类型 1商家 2美食写手
    private Integer shopId;//商家id

    private Integer shopType;

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getZanUserId() {
        return zanUserId;
    }

    public void setZanUserId(String zanUserId) {
        this.zanUserId = zanUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getZanSourceType() {
        return zanSourceType;
    }

    public void setZanSourceType(Integer zanSourceType) {
        this.zanSourceType = zanSourceType;
    }

    public String getZanUserIp() {
        return zanUserIp;
    }

    public void setZanUserIp(String zanUserIp) {
        this.zanUserIp = zanUserIp;
    }

    public String getZanUserName() {
        return zanUserName;
    }

    public void setZanUserName(String zanUserName) {
        this.zanUserName = zanUserName;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public Integer getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(Integer choiceType) {
        this.choiceType = choiceType;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
