package com.tech.fate.portal.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tech.fate.portal.dto.LocalDataDto;
import org.springframework.lang.Nullable;

import java.sql.SQLException;


/**
 * @author Iwi
 * @date 2022.7.15
 */
public interface LocalDataMapper {


    /**
     * 查找所有本地数据详细信息
     * @return List<LocalDataDto>
     * @throws SQLException
     */
    IPage<LocalDataDto> queryLocalDataDtoList(@Nullable IPage<LocalDataDto> localDataDtoPage)throws SQLException;
    /**
     * 根据uuid查找本地数据
     * @param uuid
     * @return List<LocalDataDto>
     * @throws SQLException
     */
    LocalDataDto queryLocalDataByUuid(String uuid);

    int addLocalData(LocalDataDto localDataDto)throws SQLException;

    int deleteLocalDataByUuid(String uuid)throws SQLException;
}
