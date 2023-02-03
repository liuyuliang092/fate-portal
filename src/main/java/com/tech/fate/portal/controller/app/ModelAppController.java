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
import com.tech.fate.portal.service.ModelService;
import com.tech.fate.portal.vo.ModelDetailInfoVo;
import com.tech.fate.portal.vo.ModelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/api/v1")
public class ModelAppController {
    @Autowired
    ModelService modelService;
    @GetMapping("/model")
    public ApiResponse queryModelPage(@RequestParam(name="pageIndex", defaultValue="1") Integer pageIndex,
                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        IPage<ModelVo> modelVoPage=new Page<>(pageIndex,pageSize);
        try {
            modelVoPage=modelService.queryModelPage(modelVoPage);
        }
        catch (Exception e){
            log.error(e.toString());
            return ApiResponse.fail("query Model List failed");
        }
        return ApiResponse.ok("success",modelVoPage);
    }
    @GetMapping("/model/{uuid}")
    public ApiResponse  getModelDetailInfo(@PathVariable String uuid){
        ModelDetailInfoVo modelDetailInfoVo;
        try {
            modelDetailInfoVo=modelService.getModelDetailInfo(uuid);
        }
        catch (Exception e){
            return ApiResponse.fail("get model detail info failed");
        }
        return ApiResponse.ok("success",modelDetailInfoVo);
    }
}
