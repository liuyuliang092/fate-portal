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

import com.alibaba.fastjson.annotation.JSONField;
import com.tech.fate.portal.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年07月25日 17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociateDataFMLDto {

    @JSONField(name = "creation_time")
    private String creationTime;

    @JSONField(name = "data_uuid")
    private String dataUuid;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "site_uuid")
    private String siteUuid;

    @JSONField(name = "site_name")
    private String siteName;

    @JSONField(name = "site_party_id")
    private long sitePartyId;

    @JSONField(name = "table_name")
    private String tableName;

    @JSONField(name = "table_namespace")
    private String tableNamespace;

    @JSONField(name = "update_time")
    private String updateTime;

    public AssociateDataFMLDto(ProjectDataDto projectDataDto) {
        setCreationTime(DateUtils.formatDateTimeForGo());
        setDataUuid(projectDataDto.getDataUuid());
        setDescription(projectDataDto.getDescription());
        setName(projectDataDto.getName());
        setSiteUuid(projectDataDto.getSiteUuid());
        setSiteName(projectDataDto.getSiteName());
        setSitePartyId(projectDataDto.getSitePartyId());
        setTableName(projectDataDto.getTableName());
        setTableNamespace(projectDataDto.getTableNamespace());
        setUpdateTime(DateUtils.formatDateTimeForGo());
    }
}
