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
 * @date: 2022年08月08日 14:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveModelRequestDto {

    private int algorithmType;

    private String componentName;

    private Timestamp createTime;

    private EvaluationClass evaluation;

    private String jobName;

    private String jobUuid;

    private String modelId;

    private String modelVersion;

    private String name;

    private long partyId;

    private String projectUuid;

    private String role;

    private String uuid;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor

    public static class EvaluationClass{

        private String additionalProp1;

        private String additionalProp2;

        private String additionalProp3;
    }
}
