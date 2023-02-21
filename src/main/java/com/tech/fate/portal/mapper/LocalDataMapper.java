package com.tech.fate.portal.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tech.fate.portal.dto.LocalDataDto;
import com.tech.fate.portal.model.FileInfo;
import com.tech.fate.portal.model.FileSliceInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Array
 */
public interface LocalDataMapper {

    IPage<LocalDataDto> queryLocalDataDtoList(@Nullable IPage<LocalDataDto> localDataDtoPage, @Param("localDataDto") LocalDataDto localDataDto) throws SQLException;

    LocalDataDto queryLocalDataByUuid(@Param("uuid") String uuid);

    int addLocalData(LocalDataDto localDataDto) throws SQLException;

    int deleteLocalDataByUuid(@Param("uuid") String uuid) throws SQLException;

    int saveFileSlice(FileSliceInfo fileSliceInfo);

    List<FileSliceInfo> queryFileSliceList(FileSliceInfo fileSliceInfo);

    int saveFile(FileInfo fileInfo);

    FileInfo queryFileInfo(FileInfo fileInfo);
}
