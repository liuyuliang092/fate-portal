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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * ProjectDataDto class
 *
 * @author 杨毅文
 * @date 2022.07.15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDataDto {

    private long id;

    private String createdAt;

    private String updatedAt;

    private String deletedAt;

    private String name;

    private String description;

    private String uuid;

    private String projectUuid;

    private String dataUuid;

    private String siteUuid;

    private String siteName;

    private long sitePartyId;

    private int type;

    private int status;

    private String tableName;

    private String tableNamespace;

    private String creationTime;

    private String updateTime;

    public ProjectDataDto(RemoteDataRequestDto rd) {
        setId(rd.getId());
        setCreatedAt(rd.getCreateAt());
        setCreationTime(getCreationTime());
        setUpdatedAt(rd.getUpdateAt());
        setUpdateTime(rd.getUpdateTime());
        setDeletedAt(rd.getDeleteAt().getTime());
        setName(rd.getName());
        setDescription(rd.getDescription());
        setUuid(rd.getUuid());
        setProjectUuid(rd.getProjectUuid());
        setDataUuid(rd.getDataUuid());
        setSiteUuid(rd.getSiteUuid());
        setSiteName(rd.getSiteName());
        setSitePartyId(rd.getSitePartyId());
        setType(rd.getType());
        setStatus(rd.getStatus());
        setTableName(rd.getTableName());
        setTableNamespace(rd.getTableNamespace());
    }
}
