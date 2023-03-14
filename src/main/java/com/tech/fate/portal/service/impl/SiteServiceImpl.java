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
package com.tech.fate.portal.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.tech.fate.portal.constants.RedisKeysConstants;
import com.tech.fate.portal.util.DateUtils;
import com.tech.fate.portal.util.FateRouteUtils;
import com.tech.fate.portal.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.common.FMLException;
import com.tech.fate.portal.dto.ConnectToFMLBean;
import com.tech.fate.portal.dto.SiteDto;
import com.tech.fate.portal.mapper.SiteMapper;
import com.tech.fate.portal.service.SiteService;
import com.tech.fate.portal.util.HttpUtils;
import com.tech.fate.portal.vo.SiteConnectInfo;
import com.tech.fate.portal.vo.SiteVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

import static com.tech.fate.portal.common.HttpCommon.FML_BASE_URL;

/**
 * @author Iwi
 * @date 2022.7.15
 */
@Service
@Slf4j
public class SiteServiceImpl implements SiteService {
    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${address.fate-flow}")
    private String fateFlowUrl;
    @Value("${address.fml}")
    private String fmlUrl;

    @Override
    public int updateSite(SiteVo siteVo) throws Exception {
        SiteDto siteDto = new SiteDto(siteVo);
        if (siteMapper.querySite() != null) {
            redisUtil.del(RedisKeysConstants.BASIC + RedisKeysConstants.SITE_INFO);
            return siteMapper.updateSite(siteDto);
        } else {
            return siteMapper.addSite(siteDto);
        }
    }

    @Override
    public SiteVo querySite() throws Exception {
        SiteVo siteVo = new SiteVo();
        String siteInfos = JSONUtil.toJsonStr(redisUtil.get(RedisKeysConstants.BASIC + RedisKeysConstants.SITE_INFO));
        if (StringUtils.isNotBlank(siteInfos)) {
            siteVo = JSONUtil.toBean(siteInfos, SiteVo.class);
        } else {
            SiteDto siteDto = siteMapper.querySite();
            if (siteDto == null) {
                return null;
            }
            BeanUtils.copyProperties(siteDto, siteVo);
            siteVo.setCreatedAt(siteDto.getCreatedAt().toString());
            siteVo.setUpdatedAt(siteDto.getUpdatedAt().toString());
            SiteVo.DeletedAtBean deletedAtBean = new SiteVo.DeletedAtBean();
            deletedAtBean.setTime(siteDto.getDeletedAt().toString());
            siteVo.setDeletedAt(deletedAtBean);
            siteVo.setKubeflowConfig(new SiteVo.KubeflowConfigBean());
            siteVo.getKubeflowConfig().setKubeconfig(siteDto.getKubeflowConfig());
            redisUtil.set(RedisKeysConstants.BASIC + RedisKeysConstants.SITE_INFO, siteVo);
        }
        return siteVo;
    }

    @Override
    public ApiResponse registerToFml(SiteConnectInfo siteConnectInfo) throws Exception {
        SiteDto siteDto = siteMapper.querySite();
        if (siteDto != null) {
            String fmlUrl = this.getFmlAddr();
            if (StringUtils.isBlank(fmlUrl)) {
                return ApiResponse.fail("please save site info first");
            }
            StringBuilder url = new StringBuilder();
            url.append(fmlUrl)
                    .append(FML_BASE_URL)
                    .append("/site");
            ConnectToFMLBean param = new ConnectToFMLBean();
            BeanUtils.copyProperties(siteDto, param);
            param.setCreatedAt(DateUtils.formatDateTimeForGo());
            param.setUpdatedAt(DateUtils.formatDateTimeForGo());
            param.setLastConnectedAt(DateUtil.format(new Date(siteDto.getFmlManagerConnectedAt().getTime()), "yyyy-MM-dd'T'HH:mm:ss") + "Z");
            log.info("register fml params = {}", JSONObject.toJSONString(param));
            String result = HttpUtils.post(url.toString(), JSONObject.toJSONString(param));
            if (StringUtils.isNotBlank(result)) {
                ApiResponse apiResponse = JSONUtil.toBean(result, ApiResponse.class);
                if (apiResponse.getCode() != 0) {
                    throw new FMLException();
                }
                siteDto.setFmlManagerConnectedAt(new Timestamp(System.currentTimeMillis()));
                siteMapper.updateSite(siteDto);
            } else {
                throw new FMLException();
            }
            return ApiResponse.ok("site register fml manager success");
        } else {
            return ApiResponse.fail("please save site info first");
        }
    }

    @Override
    public String getFmlAddr() throws Exception {
        String fmlAddr;
        try {
            SiteVo siteVo = this.querySite();
            if (siteVo != null) {
                fmlAddr = siteVo.getFmlManagerEndpoint();
                return fmlAddr;
            }
        } catch (Exception e) {
            log.error("get fml address error", e);
        }
        throw new Exception("get fml address error");
    }

    @Override
    public String getFateFlowAddr() {
        String fateFlowAddr = null;
        try {
            SiteVo siteVo = this.querySite();
            if (siteVo != null) {
                fateFlowAddr = siteVo.getFateFlowHost() + ":" + siteVo.getFateFlowHttpPort();
            }
        } catch (Exception e) {
            log.error("get fate flow address error", e);
        }
        return fateFlowAddr;
    }

    @Override
    public ApiResponse checkFateFlowHealth() throws Exception {
        Socket socket = new Socket();
        boolean isConnected = false;

        try {
            SiteVo siteVo = this.querySite();
            if (siteVo != null) {
                socket.connect(new InetSocketAddress(siteVo.getFateFlowHost().replace("http://", ""), siteVo.getFateFlowHttpPort()), 500);
                isConnected = socket.isConnected();
            } else {
                return ApiResponse.fail("please save site info first");
            }
        } catch (Exception e) {
            log.error("telnet fate-flow server error", e);
            throw new Exception();
        }finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        if (isConnected) {
            return ApiResponse.ok("fate-flow server is valid");
        }
        return ApiResponse.fail("fate-flow server is invalid");
    }
}
