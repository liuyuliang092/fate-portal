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

import com.tech.fate.portal.dto.JobDto;
import lombok.extern.slf4j.Slf4j;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.util.TokenUtils;
import com.tech.fate.portal.dto.JobRequestFmlDto;
import com.tech.fate.portal.dto.JobResponseDto;
import com.tech.fate.portal.service.JobService;
import com.tech.fate.portal.vo.JobDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年07月14日 16:48
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class JobApiController {

    @Autowired
    JobService jobService;

    @PostMapping("/job/internal/create")
    public ApiResponse createJobByFml(@RequestBody JobRequestFmlDto jobRequestFmlDto) {
        try {
            jobService.createJobByFml(jobRequestFmlDto);
        } catch (Exception e) {
            return ApiResponse.fail("新建失败");
        }

        return ApiResponse.ok("success");
    }

    @PostMapping("/job/{uuid}/approve")
    public ApiResponse approvePendingJob(@PathVariable String uuid) {
        ApiResponse apiResponse;
        try {
            apiResponse = jobService.approvePendingJob(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("通过审批失败");
        }
        return apiResponse;
    }

    @PostMapping("/job/{uuid}/reject")
    public ApiResponse rejectPendingJob(@PathVariable String uuid) {
        ApiResponse apiResponse;
        try {
            apiResponse = jobService.rejectPendingJob(uuid);
        } catch (Exception e) {
            return ApiResponse.fail("拒绝审批失败");
        }
        return apiResponse;
    }

    @PostMapping("/job/internal/{uuid}/response")
    public ApiResponse handleJobResponse(@PathVariable String uuid, @RequestBody JobResponseDto jobResponseDto) {
        ApiResponse apiResponse;
        try {
            apiResponse = jobService.handleJobResponse(uuid, jobResponseDto);
        } catch (Exception e) {
            return ApiResponse.fail("fail");
        }

        return apiResponse;
    }


    @GetMapping("/job/{uuid}")
    public ApiResponse getJobDetailInfo(@PathVariable String uuid, HttpServletRequest request) {
        String userName = TokenUtils.getLoginUserName(request);
        JobDetailVo jobDetailVo;
        try {
            jobDetailVo = jobService.getJobDetailInfo(uuid, userName);
        } catch (Exception e) {
            return ApiResponse.fail("查询失败");
        }
        return ApiResponse.ok("success", jobDetailVo);
    }

    @GetMapping("/job/components")
    public ApiResponse getComponents() {
        String data;
        try {
            data = jobService.getComponents();
        } catch (SQLException e) {
            return ApiResponse.fail("查询组件失败");
        }
        return ApiResponse.ok("success", data);
    }

    @GetMapping("queryJobStatus")
    public ApiResponse queryJobStatusByFate(@RequestParam String jobUuid) {
//        QueryJobFateResponseDto queryJobFateResponseDto;
//        try {
//            queryJobFateResponseDto = jobService.queryJobStatusByFate(jobUuid);
//            if (queryJobFateResponseDto != null && queryJobFateResponseDto.getData().size() > 0) {
//                return ApiResponse.ok(queryJobFateResponseDto.getData().get(0).getFStatus());
//            }
//        } catch (SQLException e) {
//            log.error("查询任务执行结果异常", e);
//        }
        return ApiResponse.fail("查询失败");
    }

    @DeleteMapping("/job/{uuid}")
    public ApiResponse deleteJob(@PathVariable String uuid) {
        log.info("task uuid = {}", uuid);
        JobDto jobDto = new JobDto();
        jobDto.setUuid(uuid);
        jobService.deleteJob(jobDto);
        return ApiResponse.ok("success");
    }
}
