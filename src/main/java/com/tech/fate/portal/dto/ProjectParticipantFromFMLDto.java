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

@NoArgsConstructor
@Data
public class ProjectParticipantFromFMLDto {
    @JSONField(name = "createdAt")
    private String createdAt;
    @JSONField(name = "deletedAt")
    private DeletedAtBean deletedAt;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "external_host")
    private String externalHost;
    @JSONField(name = "external_port")
    private long externalPort;
    @JSONField(name = "https")
    private boolean https;
    @JSONField(name = "id")
    private long id;
    @JSONField(name = "last_connected_at")
    private String lastConnectedAt;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "party_id")
    private long partyId;
    @JSONField(name = "updatedAt")
    private String updatedAt;
    @JSONField(name = "uuid")
    private String uuid;

    @NoArgsConstructor
    @Data
    public static class DeletedAtBean {
        @JSONField(name = "time")
        private String time;
        @JSONField(name = "valid")
        private boolean valid;
    }
}
