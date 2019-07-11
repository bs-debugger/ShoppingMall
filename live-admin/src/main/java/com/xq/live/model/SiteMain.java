package com.xq.live.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * sitemain
 * <p>
 * User: Zhang Peng
 * <p>
 * Date: 2015年11月16日
 * <p>
 * Version: 1.0
 */
public class SiteMain extends BaseModel {

	private Integer id;// 主键
	private String name;// NULL网站简称
	private String domain;// 域名 不带http://
	private String link;// 网址连接 带http://
	private Integer state;// 状态 0禁用 1可用 2审核中
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;// 创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;// 修改时间
	private Integer rank = 1;// NULL排序
	private String pic;// 图片URL
	private Integer deleted;// 删除状态 0=未删除 1=删除

	private String types; // 站点类型， 按","区分

	private int[] typeIds = {}; // 类型id

	public int[] getTypeIds() {
		return typeIds;
	}

	public void setTypeIds(int[] typeIds) {
		this.typeIds = typeIds;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

}
