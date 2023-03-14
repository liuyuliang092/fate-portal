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
import com.tech.fate.portal.common.status.CheckResultStatus;
import com.tech.fate.portal.service.graph.GraphService;
import com.tech.fate.portal.vo.GraphData;
import com.tech.fate.portal.vo.QueryComponentsStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class GraphController {

    @Autowired
    private GraphService graphService;

    @PostMapping("/saveGraphData")
    public ApiResponse saveGraphData(@RequestBody GraphData graphData) {
        try {
            graphService.saveGraphData(graphData);
        } catch (Exception e) {
            log.error("save projcet graph data error,graph data = {}", graphData, e);
            return ApiResponse.fail(e.getMessage());
        }
        return ApiResponse.ok("保存成功");
    }

    @GetMapping("/graphData")
    public ApiResponse graphData(@RequestParam String projectUuid, String taskUuid) {
        try {
            GraphData graphData = graphService.queryGraphData(projectUuid, taskUuid);
            if (graphData != null) {
                return ApiResponse.ok("查询成功", graphData);
            }
        } catch (Exception e) {
            log.error("query projcet graph data error,projectUuid = {},taskUuid = {}", projectUuid, taskUuid, e);
        }
        return ApiResponse.fail("未查询到数据");
    }

    @PostMapping("/runGraph")
    public ApiResponse runGraph(@RequestBody GraphData graphData) {
        int code = graphService.runGraph(graphData);
        if (code == CheckResultStatus.SUCCESS.getStatus()) {
            return ApiResponse.ok("启动成功");
            //新增任务流水数据
        } else if (code == CheckResultStatus.JOB_IS_RUNNING.getStatus()) {
            return ApiResponse.ok("任务运行中，请勿重复提交");
        }
        return ApiResponse.fail("启动失败");
    }

    @PostMapping("/componentsStatus")
    public ApiResponse taskStatus(@RequestBody QueryComponentsStatus queryComponentsStatus) {
        return ApiResponse.ok("查询成功", graphService.taskStatus(queryComponentsStatus));
    }
}
