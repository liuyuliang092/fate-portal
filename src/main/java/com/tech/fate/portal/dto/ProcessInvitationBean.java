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
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessInvitationBean {
    @JsonProperty("project_auto_approval_enabled")
    @JSONField(name = "project_auto_approval_enabled")
    private boolean projectAutoApprovalEnabled;
    @JsonProperty("project_creation_time")
    @JSONField(name = "project_creation_time")
    private String projectCreationTime;
    @JsonProperty("project_description")
    @JSONField(name = "project_description")
    private String projectDescription;
    @JsonProperty("project_manager")
    @JSONField(name = "project_manager")
    private String projectManager;
    @JsonProperty("project_managing_site_name")
    @JSONField(name = "project_managing_site_name")
    private String projectManagingSiteName;
    @JsonProperty("project_managing_site_party_id")
    @JSONField(name = "project_managing_site_party_id")
    private long projectManagingSitePartyId;
    @JsonProperty("project_managing_site_uuid")
    @JSONField(name = "project_managing_site_uuid")
    private String projectManagingSiteUuid;
    @JsonProperty("project_name")
    @JSONField(name = "project_name")
    private String projectName;
    @JsonProperty("project_uuid")
    @JSONField(name = "project_uuid")
    private String projectUuid;
    @JsonProperty("site_party_id")
    @JSONField(name = "site_party_id")
    private long sitePartyId;
    @JsonProperty("site_uuid")
    @JSONField(name = "site_uuid")
    private String siteUuid;
    @JsonProperty("uuid")
    @JSONField(name = "uuid")
    private String uuid;
}
