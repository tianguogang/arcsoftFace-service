package com.tgg.arcsoftfaceservice.mapper;

import com.tgg.arcsoftfaceservice.pojo.SysDictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 字典数据表 Mapper 接口
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {
    @Update("UPDATE sys_dict_data SET dict_type = #{new_type} WHERE dict_type = #{old_type};")
    Integer updateType(@Param("old_type") String old_type, @Param("new_type") String new_type);
}
