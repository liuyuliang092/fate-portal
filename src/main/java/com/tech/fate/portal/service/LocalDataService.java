package com.tech.fate.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.dto.LocalDataDto;
import com.tech.fate.portal.model.Chunk;
import com.tech.fate.portal.vo.DetailedInfoOfDataVo;
import com.tech.fate.portal.vo.LocalDataVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Iwi
 * @date 2022.7.15
 */
public interface LocalDataService {
    /**
     * 查找所有本地数据基本信息
     *
     * @param localDataVoPage
     * @return
     * @throws SQLException
     */
    IPage<LocalDataVo> listAllDataRecords(IPage<LocalDataVo> localDataVoPage) throws Exception;

    /**
     * 查找所有本地数据详细信息
     *
     * @return
     * @throws SQLException
     */
    List<LocalDataDto> queryLocalDataDtoList() throws Exception;

    /**
     * 查询指定数据特征列
     *
     * @param uuid 指定数据
     * @return String[]
     * @throws SQLException
     */
    String[] getDataColumn(String uuid);

    /**
     * 上传数据到fateFlow
     *
     * @param file
     * @param name
     * @param description
     * @throws Exception
     */
    void uploadData(File file, String name, String description,String hash) throws Exception;

    /**
     * 通过uuid获得详细信息
     *
     * @param uuid
     * @return
     * @throws SQLException
     */
    DetailedInfoOfDataVo getDataRecordsDetailedInfo(String uuid) throws Exception;

    /**
     * 保存信息到本地
     *
     * @param file
     * @param result
     * @param description
     * @param name
     * @return
     * @throws SQLException
     * @throws IOException
     */
    int saveData(InputStream inputStream,String fileName, String result, String name, String description) throws Exception;

    void deleteData(String uuid) throws Exception;

    void downloadData(String uuid) throws Exception;

    ApiResponse saveFile(byte[] bytes, String hash, String fileName, Integer seq, String type) throws Exception;

    ApiResponse mergeFile(String fileName,String type,String hash,String name,String description) throws IOException;
}
