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
 * ModelDto class
 *
 * @author Iwi
 * @date 2022.7.15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {
    private long id;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    private String uuid;

    private String name;

    private String fateModelId;

    private String fateModelVersion;

    private String projectUuid;

    private String projectName;

    private String jobUuid;

    private String jobName;

    private String componentName;

    private int componentAlgorithmType;

    private String role;

    private long partyId;

    private String evaluation;

    public ModelDto(SaveModelRequestDto saveModelRequestDto,String projectName) {

        setCreatedAt(new Timestamp(System.currentTimeMillis()));
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        setDeletedAt(new Timestamp(System.currentTimeMillis()));
        setUuid(saveModelRequestDto.getUuid());
        setName(saveModelRequestDto.getName());
        setFateModelId(saveModelRequestDto.getModelId());
        setFateModelVersion(saveModelRequestDto.getModelVersion());
        setProjectUuid(saveModelRequestDto.getProjectUuid());
        setJobName(saveModelRequestDto.getJobName());
        setJobUuid(saveModelRequestDto.getJobUuid());
        setComponentName(saveModelRequestDto.getComponentName());
        setRole(saveModelRequestDto.getRole());
        setPartyId(saveModelRequestDto.getPartyId());
        setEvaluation(saveModelRequestDto.getEvaluation().toString());
        setProjectName(projectName);
    }
}
