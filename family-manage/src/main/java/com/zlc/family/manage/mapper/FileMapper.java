package com.zlc.family.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.family.manage.domain.FamilyFile;
import com.zlc.family.manage.domain.query.FileQuery;
import com.zlc.family.manage.domain.vo.FileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件属性表 Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-05-15
 */
public interface FileMapper extends BaseMapper<FamilyFile> {

    List<FileVo> listFile(@Param("query") FileQuery query);
}
