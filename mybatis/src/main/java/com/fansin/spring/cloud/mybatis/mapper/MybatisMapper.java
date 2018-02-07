package com.fansin.spring.cloud.mybatis.mapper;

import com.fansin.spring.cloud.mybatis.entity.Mybatis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Mybatis mapper.
 *
 * @author zhaofeng on 17-4-9.
 */
@Mapper
public interface MybatisMapper {

    /**
     * 插入
     *
     * @param mybatis the mybatis
     * @return int
     */
    int insert(Mybatis mybatis);


    /**
     * 更新时间
     *
     * @param mybatisId  the mybatis id
     * @param createTime the create time
     * @return 如果返回 >0,成功,否则失败
     */
    int update(@Param("mybatisId") long mybatisId, @Param("createTime") LocalDateTime createTime);

    /**
     * 根据id查询
     *
     * @param mybatisId the mybatis id
     * @return mybatis
     */
    Mybatis queryById(long mybatisId);


    /**
     * 根据偏移量查询所有
     *
     * @param offset the offset
     * @param limit  the limit
     * @return list
     */
    List<Mybatis> queryAll(@Param("offset") int offset, @Param("limit") int limit);

}
