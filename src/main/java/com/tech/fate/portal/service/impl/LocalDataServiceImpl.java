/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tech.fate.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tech.fate.portal.common.FateFlowResult;
import com.tech.fate.portal.constants.FateFlowConstants;
import com.tech.fate.portal.model.Chunk;
import com.tech.fate.portal.model.FileInfo;
import com.tech.fate.portal.model.FileSliceInfo;
import com.tech.fate.portal.service.SiteService;
import com.tech.fate.portal.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.common.FMLException;
import com.tech.fate.portal.util.DateUtils;
import com.tech.fate.portal.dto.DownloadParameterDto;
import com.tech.fate.portal.dto.LocalDataDto;
import com.tech.fate.portal.mapper.LocalDataMapper;
import com.tech.fate.portal.service.LocalDataService;
import com.tech.fate.portal.util.HttpUtils;
import com.tech.fate.portal.vo.DetailedInfoOfDataVo;
import com.tech.fate.portal.vo.LocalDataVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tech.fate.portal.common.HttpCommon.FATE_FLOW_BASE_URL;


/**
 * @author Iwi
 * @date 2022.7.15
 */
@Service
@Slf4j
public class LocalDataServiceImpl implements LocalDataService {

    @Value("${upload.folder}")
    private String uploadFolder;
    @Autowired
    private LocalDataMapper localDataMapper;
    @Autowired
    private SiteService siteService;

    @Override
    public IPage<LocalDataVo> listAllDataRecords(IPage<LocalDataVo> localDataVoPage) throws Exception {
        IPage<LocalDataDto> localDataDtoPage = new Page<>(localDataVoPage.getCurrent(), localDataVoPage.getSize());
        localDataDtoPage = localDataMapper.queryLocalDataDtoList(localDataDtoPage);

        localDataVoPage = localDataDtoPage.convert(e -> {
            LocalDataVo localDataVo = new LocalDataVo(e);
            return localDataVo;
        });
        return localDataVoPage;
    }

    @Override
    public List<LocalDataDto> queryLocalDataDtoList() throws Exception {
        //该Page无实际意义
        IPage<LocalDataDto> page = new Page<>(1, 10);
        return localDataMapper.queryLocalDataDtoList(page).getRecords();
    }

    @Override
    public String[] getDataColumn(String uuid) {
        String[] ret = null;
        LocalDataDto localDataDto = localDataMapper.queryLocalDataByUuid(uuid);
        if (localDataDto != null) {
            ret = localDataDto.getColumn().split(",");
        }
        return ret;
    }

    @Override
    public DetailedInfoOfDataVo getDataRecordsDetailedInfo(String uuid) throws Exception {
        LocalDataDto localDataDto = localDataMapper.queryLocalDataByUuid(uuid);
        if (localDataDto == null) {
            return null;
        }
        return new DetailedInfoOfDataVo(localDataDto);
    }

    @Override
    public int saveData(InputStream inputStream, String fileName, String data, String name, String description) throws Exception {
        LocalDataDto localDataDto = new LocalDataDto();
        localDataDto.setCreatedAt(DateUtils.formatDateTime());
        localDataDto.setUpdatedAt(DateUtils.formatDateTime());
        localDataDto.setDeletedAt(DateUtils.formatDateTime());
        localDataDto.setUuid(UUID.fastUUID().toString().replaceAll("-", ""));
        localDataDto.setName(name);
        localDataDto.setDescription(description);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String headLine = bufferedReader.readLine();
        Assert.notBlank(headLine);
        StringBuilder preview = new StringBuilder();
        preview.append(headLine);
        if (headLine != null) {
            localDataDto.setColumn(headLine);
        }
        int count = 0;
        String currentLine = bufferedReader.readLine();
        while (currentLine != null) {
            count++;
            if (count <= 10) {
                preview.append(currentLine);
            }
            currentLine = bufferedReader.readLine();
        }
        JSONObject rs = JSONObject.parseObject(data);
        localDataDto.setTableName(rs.getString("table_name"));
        localDataDto.setTableNamespace(rs.getString("namespace"));
        localDataDto.setCount(count);
        localDataDto.setFeatures(headLine.substring(headLine.indexOf(",") + 1));
        localDataDto.setPreview(preview.toString());
        localDataDto.setJobId(rs.getString("job_id"));
        localDataDto.setJobConf(rs.getString("runtime_conf_path"));
        localDataDto.setLocalFilePath(fileName);
        return localDataMapper.addLocalData(localDataDto);
    }

    @Override
    public void uploadData(File file, String name, String description, String hash) throws Exception {
        StringBuilder url = new StringBuilder();
        int head = 1;
        int partition = 16;
        String idDelimiter = ",";
        try {
            String namespace = DateUtils.date2Str(new SimpleDateFormat("yyyyMMdd"));
            String tableName = UUID.fastUUID().toString().replaceAll("-", "");
            String fateFlowUrl = siteService.getFateFlowAddr();
            url.append(fateFlowUrl)
                    .append(FateFlowConstants.DATA_UPLOAD)
                    .append("?")
                    .append("head=")
                    .append(head)
                    .append("&id_delimiter=")
                    .append(idDelimiter)
                    .append("&partition=")
                    .append(partition)
                    .append("&namespace=")
                    .append(namespace)
                    .append("&table_name=")
                    .append(tableName);
            InputStream inputStream = Files.newInputStream(file.toPath());
            String result = HttpUtils.uploadFile(url.toString(), file.getName(), inputStream);
            log.info("[fate-portal] upload data to fate result = {}", result);
            if (StringUtils.isNotBlank(result)) {
                FateFlowResult apiResponse = JSONUtil.toBean(result, FateFlowResult.class);
                if (apiResponse.getRetcode() == 0) {
                    String data = JSONUtil.toJsonStr(apiResponse.getData());
                    inputStream = Files.newInputStream(file.toPath());
                    saveData(inputStream, file.getName(), data, name, description);
                    return;
                }
            }
            throw new FMLException();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
//            if (hash != null) {
//                Path path = Paths.get(uploadFolder + hash);
//                Path pathCreate = Files.createDirectories(path);
//                FileUtil.del(pathCreate);
//            }
        }
    }

    @Override
    public void downloadData(String uuid) throws Exception {
        StringBuilder url = new StringBuilder();
        String fateFlowUrl = siteService.getFateFlowAddr();
        url.append(fateFlowUrl)
                .append(FateFlowConstants.DATA_DOWNLOAD);
        DownloadParameterDto downloadParameterDto = new DownloadParameterDto();
        downloadParameterDto.setDelimiter(",");
        downloadParameterDto.setTableName(localDataMapper.queryLocalDataByUuid(uuid).getTableName());
        downloadParameterDto.setNamespace(localDataMapper.queryLocalDataByUuid(uuid).getTableNamespace());
        downloadParameterDto.setOutputPath("/data/" + localDataMapper.queryLocalDataByUuid(uuid).getLocalFilePath());
        String parameter = JSONObject.toJSONString(downloadParameterDto);
        String result = HttpUtils.post(url.toString(), parameter);
        if (StringUtils.isNotBlank(result)) {
            FateFlowResult apiResponse = JSONUtil.toBean(result, FateFlowResult.class);
            if (apiResponse.getRetcode() != 0) {
                throw new FMLException();
            }
        } else {
            throw new FMLException();
        }
    }

    @Override
    public ApiResponse saveFile(byte[] bytes, String hash, String fileName, Integer seq, String type) throws Exception {
        RandomAccessFile randomAccessFile = null;
        try {
            if (!this.checkFileSlice(hash, seq)) {
                Path path = Paths.get(uploadFolder + hash);
                Path pathCreate = Files.createDirectories(path);
                randomAccessFile = new RandomAccessFile(pathCreate + "\\" + fileName + "." + type + seq, "rw");
                randomAccessFile.write(bytes);
                this.saveFileSlice(hash, fileName, seq, type);
            }
        } catch (IOException e) {
            log.error("[fate-portal] Failed to save split file,fileNme = {},seq = {}", fileName, seq, e);
            return ApiResponse.fail("failed");
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
        return ApiResponse.ok("success");
    }

    @Override
    public ApiResponse mergeFile(String fileName, String type, String hash, String name, String description) throws IOException {
        Path path = Paths.get(uploadFolder + hash);
        Path pathCreate = Files.createDirectories(path);
        FileChannel out = null;
        FileChannel in = null;
        try {
            in = new RandomAccessFile(pathCreate + "\\" + fileName + "." + type, "rw").getChannel();
            int index = 1;
            long start = 0;
            while (true) {
                String fileSlice = pathCreate + "\\" + fileName + "." + type + index;
                if (!new File(fileSlice).exists()) {
                    break;
                }
                out = new RandomAccessFile(fileSlice, "r").getChannel();
                in.transferFrom(out, start, start + out.size());
                start += out.size();
                out.close();
                out = null;
                index++;
            }
            in.close();
            File file = new File(pathCreate + "\\" + fileName + "." + type);
            this.saveFile(hash, fileName, pathCreate + "\\" + fileName + "." + type);
            this.uploadData(file, name, description, hash);
        } catch (Exception e) {
            log.error("[fate-portal] failed to merge file,filename = {},hash = {},", fileName, hash, e);
            return ApiResponse.fail("failed");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ApiResponse.ok("success");
    }

    @Override
    public ApiResponse checkChunk(FileInfo fileInfo) {
        FileInfo file = localDataMapper.queryFileInfo(fileInfo);
        if (file == null || StringUtils.isBlank(file.getFileName())) {
            return ApiResponse.fail("There is no fragment file, start re-upload");
        }
        return ApiResponse.ok("fast upload success,watting upload to fate");
    }

    @Override
    public void uploadDataFast(String name, String description, String hash) throws Exception {
        try {
            FileInfo params = new FileInfo();
            params.setFileHash(hash);
            FileInfo fileInfo = localDataMapper.queryFileInfo(params);
            if (fileInfo != null) {
                String localFullFilePath = fileInfo.getFilePath();
                File file = new File(localFullFilePath);
                this.uploadData(file, name, description, hash);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private List<FileSliceInfo> queryFileSliceList(FileSliceInfo fileSliceInfo) {
        return localDataMapper.queryFileSliceList(fileSliceInfo);
    }

    @Override
    public void deleteData(String uuid) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(siteService.getFateFlowAddr())
                .append(FateFlowConstants.TABLE_DELETE);
        cn.hutool.json.JSONObject para = new cn.hutool.json.JSONObject();
        LocalDataDto localDataDto = localDataMapper.queryLocalDataByUuid(uuid);
        para.set("table_name", localDataDto.getTableName());
        para.set("namespace", localDataDto.getTableNamespace());
        String parameter = JSONUtil.toJsonStr(para);
        String result = HttpUtils.post(url.toString(), parameter);
        if (StringUtils.isNotBlank(result)) {
            FateFlowResult apiResponse = JSONUtil.toBean(result, FateFlowResult.class);
            if (apiResponse.getRetcode() == 0) {
                deleteLocalData(uuid);
            }
        } else {
            throw new FMLException();
        }
    }

    public void deleteLocalData(String uuid) throws Exception {
        localDataMapper.deleteLocalDataByUuid(uuid);
    }

    private void saveFileSlice(String hash, String fileName, Integer seq, String type) {
        FileSliceInfo fileSliceInfo = new FileSliceInfo();
        fileSliceInfo.setFileName(fileName);
        fileSliceInfo.setFileType(type);
        fileSliceInfo.setUuid(IdUtil.simpleUUID());
        fileSliceInfo.setFileHash(hash);
        fileSliceInfo.setFileSeq(seq);
        localDataMapper.saveFileSlice(fileSliceInfo);
    }

    private void saveFile(String hash, String fileName, String filePath) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setUuid(IdUtil.simpleUUID());
        fileInfo.setFileHash(hash);
        fileInfo.setFilePath(filePath);
        localDataMapper.saveFile(fileInfo);
    }

    private boolean checkFileSlice(String hash, Integer seq) {
        FileSliceInfo fileSliceInfo = new FileSliceInfo();
        fileSliceInfo.setFileHash(hash);
        fileSliceInfo.setFileSeq(seq);
        List<FileSliceInfo> fileSliceInfoList = this.queryFileSliceList(fileSliceInfo);
        if (CollUtil.isEmpty(fileSliceInfoList)) {
            return false;
        }
        return true;
    }
}
