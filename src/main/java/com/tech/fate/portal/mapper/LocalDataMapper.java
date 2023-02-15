package com.tech.fate.portal.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tech.fate.portal.dto.LocalDataDto;
import com.tech.fate.portal.model.FileInfo;
import com.tech.fate.portal.model.FileSliceInfo;
import org.springframework.lang.Nullable;

import java.sql.SQLException;
import java.util.List;


/**
 * @author Iwi
 * @date 2022.7.15
 */
public interface LocalDataMapper {

    IPage<LocalDataDto> queryLocalDataDtoList(@Nullable IPage<LocalDataDto> localDataDtoPage) throws SQLException;

    LocalDataDto queryLocalDataByUuid(String uuid);

    int addLocalData(LocalDataDto localDataDto) throws SQLException;

    int deleteLocalDataByUuid(String uuid) throws SQLException;

    int saveFileSlice(FileSliceInfo fileSliceInfo);

    List<FileSliceInfo> queryFileSliceList(FileSliceInfo fileSliceInfo);

    int saveFile(FileInfo fileInfo);

    FileInfo queryFileInfo(FileInfo fileInfo);
}
