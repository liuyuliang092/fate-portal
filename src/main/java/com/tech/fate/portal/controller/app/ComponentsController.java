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

import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.dto.ComponentsDto;
import com.tech.fate.portal.dto.ComponentsParamsSettingsDto;
import com.tech.fate.portal.service.node.ComponentsService;
import com.tech.fate.portal.vo.ComponentsParams;
import com.tech.fate.portal.vo.ComponentsParamsSettings;
import com.tech.fate.portal.vo.ComponentsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ComponentsController {

    @Autowired
    private ComponentsService componentsService;

    @GetMapping("/algorithmComponents")
    public ApiResponse algorithmComponents(@RequestParam String status) {
        List<ComponentsVo> components = componentsService.algorithmComponents();
        return ApiResponse.ok("查询成功", components);
    }

    @GetMapping("/algorithmParams")
    public ApiResponse algorithmParams(@RequestParam String nodeId) {
        ComponentsParams componentsParams = componentsService.queryAlgorithmComponentsParams(nodeId);
        return ApiResponse.ok("查询成功", componentsParams);
    }

    @GetMapping("/algorithmParamsSettings")
    public ApiResponse algorithmParamsSettings(@RequestParam String projectUuid, String taskUuid, String dslNodeId) {
        List<ComponentsParamsSettings> componentsParamsSettings = componentsService.queryAlgorithmComponentsParamsSettings(projectUuid, taskUuid, dslNodeId);
        return CollectionUtils.isEmpty(componentsParamsSettings) ? ApiResponse.fail("failed") : ApiResponse.ok("success", componentsParamsSettings.get(0));
    }

    @PostMapping("/saveAlgorithmParamsSettings")
    public ApiResponse saveAlgorithmParamsSettings(@RequestBody ComponentsParamsSettings componentsParamsSettings) {
        try {
            componentsService.saveAlgorithmComponentsParamsSettings(componentsParamsSettings);
        } catch (Exception e) {
            log.error("save AlgorithmParamsSettings fail,settings = {}", componentsParamsSettings, e);
            return ApiResponse.fail("保存失败");
        }
        return ApiResponse.ok("保存成功");
    }

}
