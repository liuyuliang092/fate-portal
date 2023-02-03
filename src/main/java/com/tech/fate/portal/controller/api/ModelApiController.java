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
package com.tech.fate.portal.controller.api;

import lombok.extern.slf4j.Slf4j;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.dto.SaveModelRequestDto;
import com.tech.fate.portal.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年08月08日 14:39
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class ModelApiController {

    @Autowired
    ModelService modelService;

    @PostMapping("/model/internal/event/create")
    public ApiResponse handleModelCreation(@RequestBody SaveModelRequestDto saveModelRequestDto){
        try{
            modelService.handleModelCreation(saveModelRequestDto);
        }catch (SQLException e){
            return ApiResponse.fail("保存模型失败");
        }
        return ApiResponse.ok("success");
    }
}
