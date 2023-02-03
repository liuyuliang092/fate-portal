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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 项目参与方
 *
 * @author: 刘欣怡
 * @date: 2022年07月13日 9:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectParticipantsDto {

    @JSONField(name = "ID")
    private long id;

    @JSONField(name = "CreatedAt")
    private String createdAt;

    @JSONField(name = "UpdatedAt")
    private String updatedAt;

    @JSONField(name = "DeletedAt")
    private String deletedAt;

    @JSONField(name = "uuid")
    private String uuid;

    @JSONField(name = "project_uuid")
    private String projectUuid;

    @JSONField(name = "site_party_id")
    private Integer sitePartyId;

    @JSONField(name = "site_name")
    private String siteName;

    @JSONField(name = "site_description")
    private String siteDescription;

    @JSONField(name = "status")
    private int status;

    @JSONField(name = "site_uuid")
    private String siteUuid;

}
