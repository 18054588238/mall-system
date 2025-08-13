package com.personal.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.personal.common.exception.groups.AddGroupsInterface;
import com.personal.common.exception.groups.UpdateGroupsInterface;
import com.personal.common.valid.ListValue;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 品牌
 * 
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-28 18:41:30
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@Null(message = "新增时id必须为null",groups = AddGroupsInterface.class)
	@NotNull(message = "更新时id不能为null",groups = UpdateGroupsInterface.class)
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名称不能为null",groups = AddGroupsInterface.class)
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "logo不能为null",groups = AddGroupsInterface.class)
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "检索首字母不能为null",groups = AddGroupsInterface.class)
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为null",groups = AddGroupsInterface.class)
	private Integer sort;

}
