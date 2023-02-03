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
 * 功能描述
 *
 * @author: scott
 * @date: 2022年07月26日 10:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryProjectLocalDataDto {

    private String creationTime;

    private String dataId;

    private String description;

    private boolean isLocal;

    private String name;

    private String providingSiteName;

    private long providingSitePartyId;

    private String providingSiteUuid;

    private String updateTime;

    public QueryProjectLocalDataDto(ProjectDataDto pdo) {
        setCreationTime(pdo.getCreationTime());
        setDataId(pdo.getDataUuid());
        setDescription(pdo.getDescription());
        setLocal(true);
        setName(pdo.getName());
        setProvidingSiteName(pdo.getSiteName());
        setProvidingSiteUuid(pdo.getSiteUuid());
        setProvidingSitePartyId(pdo.getSitePartyId());
        setUpdateTime(pdo.getUpdateTime());
    }
}
