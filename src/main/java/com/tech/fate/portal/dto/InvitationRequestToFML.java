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
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Data
public class InvitationRequestToFML {

    @JSONField(name = "associated_data")
    private List<AssociatedDataBean> associatedData;
    @JSONField(name = "project_auto_approval_enabled")
    private boolean projectAutoApprovalEnabled;
    @JSONField(name = "project_creation_time")
    private String projectCreationTime;
    @JSONField(name = "project_description")
    private String projectDescription;
    @JSONField(name = "project_manager")
    private String projectManager;
    @JSONField(name = "project_managing_site_name")
    private String projectManagingSiteName;
    @JSONField(name = "project_managing_site_party_id")
    private long projectManagingSitePartyId;
    @JSONField(name = "project_managing_site_uuid")
    private String projectManagingSiteUuid;
    @JSONField(name = "project_name")
    private String projectName;
    @JSONField(name = "project_uuid")
    private String projectUuid;
    @JSONField(name = "site_party_id")
    private long sitePartyId;
    @JSONField(name = "site_uuid")
    private String siteUuid;
    @JSONField(name = "uuid")
    private String uuid;

    @NoArgsConstructor
    @Data
    public static class AssociatedDataBean {
        @JSONField(name = "creation_time")
        private String creationTime;
        @JSONField(name = "data_uuid")
        private String dataUuid;
        @JSONField(name = "description")
        private String description;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "site_name")
        private String siteName;
        @JSONField(name = "site_party_id")
        private long sitePartyId;
        @JSONField(name = "site_uuid")
        private String siteUuid;
        @JSONField(name = "table_name")
        private String tableName;
        @JSONField(name = "table_namespace")
        private String tableNamespace;
        @JSONField(name = "update_time")
        private String updateTime;
    }
}
