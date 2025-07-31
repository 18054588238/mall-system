package com.personal.mall.member.dao;

import com.personal.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author liupanpan
 * @email merdermi902@gmail.com
 * @date 2025-07-29 20:14:26
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
