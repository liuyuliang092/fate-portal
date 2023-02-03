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
import com.google.common.collect.Lists;
import com.tech.fate.portal.dto.ProjectDataDto;
import lombok.extern.slf4j.Slf4j;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.dto.ProcessInvitationBean;
import com.tech.fate.portal.service.ModelService;
import com.tech.fate.portal.service.ProjectService;
import com.tech.fate.portal.vo.InvitationInfo;
import com.tech.fate.portal.vo.ModelVo;
import com.tech.fate.portal.vo.ProjectAssociateDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Iwi
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class ProjectAppController {
    @Autowired
    ProjectService projectService;
    @Autowired
    ModelService modelService;

    @GetMapping("/project/{uuid}/data")
    public ApiResponse queryProjectDataList(@PathVariable String uuid,
                                            @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        List<ProjectAssociateDataVo> projectAssociateDataList;
        try {
            projectAssociateDataList = projectService.projectAssociateDataList(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("find failed");
        }
        return ApiResponse.ok("success", projectAssociateDataList);

    }

    @PostMapping("/project/{uuid}/invitation")
    public ApiResponse inviteOtherSite(@PathVariable String uuid, @RequestBody InvitationInfo invitationInfo) {
        try {
            projectService.invite(uuid, invitationInfo);
        } catch (Exception e) {
            log.error("invite site = {}, error", invitationInfo, e);
            return ApiResponse.fail("invite failed");
        }
        return ApiResponse.ok("success");
    }

    @PostMapping("/project/internal/invitation")
    public ApiResponse processInvitation(@RequestBody ProcessInvitationBean processInvitationBean) {
        try {
            projectService.processInvitation(processInvitationBean);
        } catch (Exception e) {
            log.error("receive invite = {}, error", processInvitationBean, e);
            return ApiResponse.fail("failed");
        }
        return ApiResponse.ok("success");

    }

    @GetMapping("/project/{uuid}/model")
    public ApiResponse getModelListForThisProject(@PathVariable String uuid,
                                                  @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage<ModelVo> modelVoPage = new Page<>(pageIndex, pageSize);
        try {
            modelVoPage = modelService.queryModelPageForProject(modelVoPage, uuid);
        } catch (Exception e) {
            return ApiResponse.fail("failed");
        }
        return ApiResponse.ok("success", modelVoPage);
    }

    @GetMapping("/project/{uuid}/dataList")
    public ApiResponse queryProjectData(@PathVariable String uuid) {
        List<ProjectAssociateDataVo> projectDataList;
        try {
            projectDataList = projectService.projectAssociateDataList(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("find failed");
        }
        return ApiResponse.ok("success", projectDataList);

    }

}
