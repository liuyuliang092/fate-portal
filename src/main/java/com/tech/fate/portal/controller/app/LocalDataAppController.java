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
package com.tech.fate.portal.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.service.LocalDataService;
import com.tech.fate.portal.vo.DetailedInfoOfDataVo;
import com.tech.fate.portal.vo.LocalDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Iwi
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class LocalDataAppController {
    @Autowired
    private LocalDataService localDataService;

    @GetMapping("/data")
    public ApiResponse listAllDataRecords(@RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        IPage<LocalDataVo> localDataVoPage = new Page<>(pageIndex, pageSize);
        try {
            localDataVoPage = localDataService.listAllDataRecords(localDataVoPage);
        } catch (Exception e) {
            return ApiResponse.fail("find failed");
        }
        return ApiResponse.ok("success", localDataVoPage);
    }

    @GetMapping("/data/{uuid}/columns")
    public ApiResponse getDataColumn(@PathVariable String uuid) {
        String[] columns;
        try {
            columns = localDataService.getDataColumn(uuid);
        } catch (Exception e) {
            log.error(e.toString());
            return ApiResponse.fail("find failed");
        }
        return ApiResponse.ok("success", columns);
    }

    @PostMapping("/data")
    public ApiResponse uploadData(@RequestPart("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("description") String description) {
        try {
            localDataService.uploadData(file, name, description);
        } catch (Exception e) {
            log.error("upload data error", e);
            return ApiResponse.fail("upload failed");
        }
        return ApiResponse.ok("success");
    }

    @GetMapping("/data/{uuid}")
    public ApiResponse getDataRecordsDetailedInfo(@PathVariable String uuid) {
        DetailedInfoOfDataVo detailedInfoOfDataVo;
        try {
            detailedInfoOfDataVo = localDataService.getDataRecordsDetailedInfo(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("find failed");
        }
        return ApiResponse.ok("success", detailedInfoOfDataVo);
    }

    @DeleteMapping("/data/{uuid}")
    public ApiResponse deleteData(@PathVariable String uuid) {
        try {
            localDataService.deleteData(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("delete failed");
        }
        return ApiResponse.ok("success");
    }

    @GetMapping("/data/{uuid}/file")
    public ApiResponse downloadDataFile(@PathVariable String uuid) {

        try {
            localDataService.downloadData(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("download failed");
        }
        return ApiResponse.ok("success");
    }
}
