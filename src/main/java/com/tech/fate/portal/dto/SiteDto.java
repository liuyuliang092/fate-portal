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
package com.tech.fate.portal.dto;

import cn.hutool.core.lang.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import com.tech.fate.portal.vo.SiteVo;

import java.sql.Timestamp;
/**
 * SiteDto class
 *
 * @author 杨毅文
 * @date 2022.07.15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteDto {

    private long id;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    private String uuid;

    private String name;

    private String description;

    private Integer partyId;

    private String externalHost;

    private long externalPort;

    private boolean https;

    private String fmlManagerEndpoint;

    private String fmlManagerServerName;

    private Timestamp fmlManagerConnectedAt;

    private boolean fmlManagerConnected;

    private String fateFlowHost;

    private Integer fateFlowHttpPort;

    private long fateFlowGrpcPort;

    private Timestamp fateFlowConnectedAt;

    private boolean fateFlowConnected;

    private String kubeflowConfig;

    private Timestamp kubeflowConnectedAt;

    private boolean kubeflowConnected;

    public SiteDto(SiteVo siteVo){
        this.id=1;

        this.createdAt=new Timestamp(System.currentTimeMillis());


        this.deletedAt=new Timestamp(System.currentTimeMillis());

        this.description=siteVo.getDescription();
        this.name=siteVo.getName();
        this.partyId=siteVo.getPartyId();
        this.externalHost=siteVo.getExternalHost();
        this.externalPort=siteVo.getExternalPort();
        this.fateFlowConnected=siteVo.isFateFlowConnected();

        this.fateFlowConnectedAt=new Timestamp(System.currentTimeMillis());

        this.fateFlowGrpcPort=siteVo.getFateFlowGrpcPort();
        this.fateFlowHost=siteVo.getFateFlowHost();
        this.fateFlowHttpPort=siteVo.getFateFlowHttpPort();
        this.fmlManagerConnected=siteVo.isFmlManagerConnected();

        this.fmlManagerConnectedAt=new Timestamp(System.currentTimeMillis());


        this.fmlManagerEndpoint=siteVo.getFmlManagerEndpoint();
        this.fmlManagerServerName=siteVo.getFmlManagerServerName();
        this.https=siteVo.isHttps();
        if(siteVo.getKubeflowConfig()!=null){
            this.kubeflowConfig=siteVo.getKubeflowConfig().getKubeconfig();
        }

        this.kubeflowConnected=siteVo.isKubeflowConnected();

        this.kubeflowConnectedAt=new Timestamp(System.currentTimeMillis());

        this.updatedAt=new Timestamp(System.currentTimeMillis());

        if(StringUtils.isNotBlank(siteVo.getUuid())){
            this.uuid=siteVo.getUuid();
        }
        else{
            this.uuid= UUID.fastUUID().toString().replaceAll("-","");
        }

    }
}
