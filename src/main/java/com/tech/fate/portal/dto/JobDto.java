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

import com.tech.fate.portal.common.CommonField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 任务
 * @author: 刘欣怡
 * @date: 2022年07月15日 16:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto extends CommonField {

    private long id;

    private String createdAt;

    private String updatedAt;

    private String deletedAt;

    private String name;

    private String description;

    private String uuid;

    private String projectUuid;

    private int type;

    private int status;

    private String statusMessage;

    private String algorithmType;

    private String algorithmComponentName;

    private String evaluateComponentName;

    private String algorithmConfig;

    private String modelName;

    private String predictingModelUuid;

    private String initiatingSiteUuid;

    private String initiatingSiteName;

    private long initiatingSitePartyId;

    private String initiatingUser;

    private boolean isInitiatingSite;

    private String fateJobId;

    private String fateJobStatus;

    private String fateModelId;

    private String fateModelVersion;

    private String finishedAt;

    private String resultJson;

    private String conf;

    private String dsl;

    private String requestJson;

}
