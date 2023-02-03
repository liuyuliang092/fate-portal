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
package com.tech.fate.portal.util;

import com.tech.fate.portal.service.SiteService;
import com.tech.fate.portal.vo.SiteVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FateRouteUtils {

    @Autowired
    private SiteService siteService;

    public String getFmlAddr() {
        String fmlAddr = null;
        try {
            SiteVo siteVo = siteService.querySite();
            if (siteVo != null) {
                fmlAddr = siteVo.getFmlManagerEndpoint();
            }
        } catch (Exception e) {
            log.error("get fml address error", e);
        }
        return fmlAddr;
    }

    public String getFateFlowAddr() {
        String fateFlowAddr = null;
        try {
            SiteVo siteVo = siteService.querySite();
            if (siteVo != null) {
                fateFlowAddr = siteVo.getFateFlowHost() + siteVo.getFateFlowHttpPort();
            }
        } catch (Exception e) {
            log.error("get fate flow address error", e);
        }
        return fateFlowAddr;
    }

}
