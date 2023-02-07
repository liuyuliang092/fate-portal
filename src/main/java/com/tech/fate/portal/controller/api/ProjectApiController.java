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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.util.TokenUtils;
import com.tech.fate.portal.dto.*;
import com.tech.fate.portal.service.ProjectService;
import com.tech.fate.portal.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

/**
 * 项目相关功能
 *
 * @author: 刘欣怡
 * @date: 2022年07月06日 15:17
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class ProjectApiController {

    @Autowired
    ProjectService projectService;

    @PostMapping("/project")
    public ApiResponse newProject(@RequestBody BasicProjectInfoDto project, HttpServletRequest request) {
        String userName = TokenUtils.getLoginUserName(request);
        try {
            projectService.newProject(project, userName);
        } catch (Exception e) {
            return ApiResponse.fail("新建项目失败！");
        }
        return ApiResponse.ok("success");
    }

    @GetMapping("/project")
    public ApiResponse queryProjectList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        QueryOwnProjectsVo queryOwnProjectsVo;
        String userName = TokenUtils.getLoginUserName(request);
        try {
            queryOwnProjectsVo = projectService.queryProjectList(pageNo, pageSize);
        } catch (Exception e) {
            log.error("query project list error,message = ", e);
            return ApiResponse.fail("failed");
        }
        return ApiResponse.ok("success", queryOwnProjectsVo);
    }

    @GetMapping("/project/{uuid}/participant")
    public ApiResponse queryProjectParticipantList(@PathVariable String uuid,
                                                   @RequestParam(required = false) boolean all) {
        if (all) {
            List<ParticipantVo> page;
            try {
                page = projectService.queryProjectParticipantListByFML(uuid);
            } catch (Exception e) {
                return ApiResponse.fail(e.getMessage());
            }
            return ApiResponse.ok("success", page);
        } else {
            List<ProjectParticipantsVo> projectParticipantsVoList;
            try {
                projectParticipantsVoList = projectService.projectParticipantList(uuid);
            } catch (Exception e) {
                return ApiResponse.fail("failed");
            }
            return ApiResponse.ok("success", projectParticipantsVoList);
        }
    }

    @GetMapping("/project/{uuid}")
    public ApiResponse getProjectDetailInfo(@PathVariable String uuid) {

        ProjectDetailVo projectDetailVo;
        try {
            projectDetailVo = projectService.getProjectDetailInfo(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("failed");
        }
        return ApiResponse.ok("success", projectDetailVo);
    }

    @GetMapping("/project/{uuid}/job")
    public ApiResponse getProjectJobList(@PathVariable String uuid,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<ProjectJobVo> pageList = new Page<>(pageNo, pageSize);
        try {
            pageList = projectService.getProjectJobList(pageList, uuid);
        } catch (Exception e) {
            return ApiResponse.fail("failed");
        }
        return ApiResponse.ok("success", pageList);
    }

    @PostMapping("/project/{uuid}/job")
    public ApiResponse createNewJob(@PathVariable String uuid, @RequestBody JobRequestDto jobRequestDto, HttpServletRequest request) {
        ApiResponse apiResponse;
        String userName = TokenUtils.getLoginUserName(request);
        try {
            apiResponse = projectService.createNewJob(uuid, jobRequestDto, userName);
        } catch (Exception e) {
            log.error("create job error,project uuid = {}", uuid, e);
            return ApiResponse.fail("failed");
        }
        return apiResponse;
    }

    @PostMapping("/project/{uuid}/data")
    public ApiResponse associateDataToProject(@PathVariable String uuid, @RequestBody AssociateDataRequestDto request) {
        try {
            return projectService.associateDataToProject(uuid, request);
        } catch (Exception e) {
            log.error("associate local data to project error", e);
            return ApiResponse.fail("failed");
        }
    }

    @PostMapping("/project/{uuid}/join")
    public ApiResponse joinInvitedProject(@PathVariable String uuid) {
        ApiResponse apiResponse;
        try {
            apiResponse = projectService.joinInvitedProject(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("failed");
        }

        return apiResponse;
    }

    @PostMapping("/project/internal/invitation/{uuid}/accept")
    public ApiResponse invitationAcceptResponseFml(@PathVariable String uuid) {
        try {
            projectService.invitationAcceptResponseFml(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("failed");
        }

        return ApiResponse.ok("success");
    }

    @PostMapping("/project/internal/invitation/{uuid}/reject")
    public ApiResponse invitationRejectResponseFml(@PathVariable String uuid) {
        try {
            projectService.invitationRejectResponseFml(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("failed");
        }

        return ApiResponse.ok("success");
    }

    @PostMapping("/project/internal/{uuid}/data/associate")
    public ApiResponse associateRemoteData(@PathVariable String uuid, @RequestBody String data) {
        try {
            List<RemoteDataRequestDto> dataList = JSONObject.parseArray(data, RemoteDataRequestDto.class);
            projectService.associateRemoteData(uuid, dataList);
        } catch (SQLException e) {
            return ApiResponse.fail("failed");
        }

        return ApiResponse.ok("success");
    }

    @GetMapping("/project/{uuid}/data/local")
    public ApiResponse getProjectLocalData(@PathVariable String uuid,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryProjectLocalDataVo projectLocalDataVo;
        try {
            projectLocalDataVo = projectService.getProjectLocalData(uuid, pageNo, pageSize);
        } catch (Exception e) {
            return ApiResponse.fail("failed");
        }
        return ApiResponse.ok("success", projectLocalDataVo);

    }

    @GetMapping("/project/{uuid}/getProjectParticipantAndDataList")
    public ApiResponse getProjectParticipantAndDataList(@PathVariable String uuid) {
        List<ProjectParticipantAndDataVo> projectParticipantAndDataVos;
        try {
            projectParticipantAndDataVos = projectService.getProjectParticipantAndDataList(uuid);
        } catch (Exception e) {
            log.error("query project participants and data list error,project uuid = {}", uuid, e);
            return ApiResponse.fail("query project participants and data list error");
        }
        return ApiResponse.ok("success", projectParticipantAndDataVos);
    }

}
