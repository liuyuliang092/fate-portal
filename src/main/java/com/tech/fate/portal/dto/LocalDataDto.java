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
 * LocalDataDto class
 *
 * @author Iwi
 * @date 2022.7.15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalDataDto {
    private long id;

    private String createdAt;

    private String updatedAt;

    private String deletedAt;

    private String uuid;

    private String name;

    private String description;

    private String column;

    private String tableName;

    private String tableNamespace;

    private long count;

    private String features;

    private String preview;

    private String idMetaInfo;

    private String jobId;

    private String jobConf;

    private int jobStatus;

    private String jobErrorMsg;

    private String localFilePath;
}
