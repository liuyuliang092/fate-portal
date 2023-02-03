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
 * 任务参与方
 *
 * @author: 刘欣怡
 * @date: 2022年07月15日 10:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobParticipantDto {

    private Timestamp createAt;

    private String uuid;

    private String jobUuid;

    private String siteUuid;

    private String siteName;

    private long sitePartyId;

    private String siteRole;

    private String dataUuid;

    private String dataName;

    private String dataDescription;

    private String dataTableName;

    private String dataTableNamespace;

    private String dataLabelName;

    private int status;
}
