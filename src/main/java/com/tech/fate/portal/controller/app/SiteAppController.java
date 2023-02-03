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

import com.tech.fate.portal.common.FmlManagerResp;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.service.SiteService;
import com.tech.fate.portal.vo.SiteConnectInfo;
import com.tech.fate.portal.vo.SiteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Iwi
 */
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class SiteAppController {
    @Autowired
    private SiteService siteService;

    @GetMapping("/site")
    public ApiResponse querySite() {
        SiteVo siteVo;
        try {
            siteVo = siteService.querySite();
        } catch (Exception e) {
            log.error("site query failed,reason = {} ", e.getMessage());
            return ApiResponse.fail("query failed");
        }
        return ApiResponse.ok("success", siteVo);
    }

    @PutMapping("/site")
    public ApiResponse updateSite(@RequestBody SiteVo siteVo) {
        int count;
        log.info("site info = {}", siteVo);
        try {
            count = siteService.updateSite(siteVo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.fail("add failed");
        }
        return ApiResponse.ok("success");
    }

    @PostMapping("/site/fmlmanager/connect")
    public ApiResponse registerToFML(SiteConnectInfo siteConnectInfo) {
        try {
            ApiResponse apiResponse = siteService.registerToFML(siteConnectInfo);
            return apiResponse;
        } catch (Exception e) {
            log.error("connect to FML failed", e);
            return ApiResponse.fail("connect to FML failed");
        }
    }

    @GetMapping("/status")
    public FmlManagerResp status() {
        return FmlManagerResp.ok("The service is running");
    }

    @GetMapping("/checkFateFlowHealth")
    public ApiResponse checkFateFlowHealth() {
        return siteService.checkFateFlowHealth();
    }
}
