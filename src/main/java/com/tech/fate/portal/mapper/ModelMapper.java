package com.tech.fate.portal.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tech.fate.portal.dto.ModelDto;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;

/**
 * @author liuxinyi、Iwi
 */
public interface ModelMapper {
    /**
     * 查找所有模型数据
     * @return List<ModelDto>
     * @throws SQLException
     */
    IPage<ModelDto> queryModelPage(IPage<ModelDto> modelDtoPage)throws SQLException;

    /**
     * 保存模型
     * @throws SQLException
     */
    void handleModelCreation(ModelDto modelDto) throws SQLException;

    IPage<ModelDto> queryModelPageForProject(IPage<ModelDto> modelDtoPage, @Param("projectUuid") String projectUuid)throws SQLException;

    ModelDto getModelByUuid(String uuid)throws SQLException;
}
