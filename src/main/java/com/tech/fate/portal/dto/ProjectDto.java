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
 * ProjectDto
 *
 * @author: lxy
 * @date: 2022年07月19日 9:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto extends CommonField {
    private long id;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    private String uuid;

    private String name;

    private String description;

    private boolean autoApprovalEnabled;

    private int type;

    private int status;

    private String manager;

    private String managingSiteName;

    private long managingSitePartyId;

    private String managingSiteUuid;
}
