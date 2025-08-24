package com.personal.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.personal.common.exception.groups.AddGroupsInterface;
import com.personal.common.exception.groups.UpdateGroupsInterface;
import com.personal.common.valid.ListValue;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

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
	@NotBlank(message = "品牌名称不能为null",groups = {AddGroupsInterface.class, UpdateGroupsInterface.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "logo不能为null",groups = {AddGroupsInterface.class})
	@URL(message = "请传入合法的url",groups = {AddGroupsInterface.class, UpdateGroupsInterface.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "显示状态不能为null",groups = AddGroupsInterface.class)
	@ListValue(val = {0,1},groups = {AddGroupsInterface.class, UpdateGroupsInterface.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "检索首字母不能为null",groups = AddGroupsInterface.class)
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母必须是单个字母",groups = {AddGroupsInterface.class, UpdateGroupsInterface.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不能为null",groups = AddGroupsInterface.class)
	@Min(value = 0,message = "排序不能小于0",groups = {AddGroupsInterface.class, UpdateGroupsInterface.class})
	private Integer sort;

}
