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

import java.util.List;

/**
 * 功能描述
 *
 * @author: liuxinyi
 * @date: 2022年08月10日 15:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryJobFateResponseDto {

    private List<ResponseData> data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ResponseData{
        @JSONField(name = "f_apply_resource_time")
        private String fApplyResourceTime;

        @JSONField(name = "f_cancel_signal")
        private boolean fCancelSignal;

        @JSONField(name = "f_cancel_time")
        private String fCancelTime;

        @JSONField(name = "f_cores")
        private int fCores;

        @JSONField(name = "f_create_date")
        private String fCreateDate;

        @JSONField(name = "f_create_time")
        private String fCreateTime;

        @JSONField(name = "f_description")
        private String fDescription;

        @JSONField(name = "f_dsl")
        private String fDsl;

        @JSONField(name = "f_elapsed")
        private int fElapsed;

        @JSONField(name = "f_end_date")
        private String fEndDate;

        @JSONField(name = "f_end_scheduling_updates")
        private int fEndSchedulingUpdates;

        @JSONField(name = "f_end_time")
        private String fEndTime;

        @JSONField(name = "f_engine_name")
        private String fEngineName;

        @JSONField(name = "f_engine_type")
        private String fEngineType;

        @JSONField(name = "f_initiator_party_id")
        private String fInitiatorPartyId;

        @JSONField(name = "f_initiator_role")
        private String fInitiatorRole;

        @JSONField(name = "f_is_initiator")
        private boolean fIsInitiator;

        @JSONField(name = "f_job_id")
        private String fJobId;

        @JSONField(name = "f_memory")
        private int fMemory;

        @JSONField(name = "f_name")
        private String fName;

        @JSONField(name = "f_party_id")
        private String fPartyId;

        @JSONField(name = "f_progress")
        private int fProgress;

        @JSONField(name = "f_ready_signal")
        private boolean fReadySignal;

        @JSONField(name = "f_ready_time")
        private String fReadyTime;

        @JSONField(name = "f_remaining_cores")
        private int fRemainingCores;

        @JSONField(name = "f_remaining_memory")
        private int fRemainingMemory;

        @JSONField(name = "f_rerun_signal")
        private boolean fRerunSignal;

        @JSONField(name = "f_resource_in_use")
        private boolean fResourceInUse;

        @JSONField(name = "f_return_resource_time")
        private String fReturnResourceTime;

        @JSONField(name = "f_role")
        private String fRole;

        @JSONField(name = "f_roles")
        private String fRoles;

        @JSONField(name = "f_runtime_conf")
        private String fRuntimeConf;

        @JSONField(name = "f_runtime_conf_on_party")
        private String fRuntimeConfOnParty;

        @JSONField(name = "f_start_date")
        private String fStartDate;

        @JSONField(name = "f_start_time")
        private String fStartTime;

        @JSONField(name = "f_status")
        private String fStatus;

        @JSONField(name = "f_status_code")
        private String fStatusCode;

        @JSONField(name = "f_tag")
        private String fTag;

        @JSONField(name = "f_train_runtime_conf")
        private String fTrainRuntimeConf;

        @JSONField(name = "f_update_date")
        private String fUpdateDate;

        @JSONField(name = "f_update_time")
        private String fUpdateTime;

        @JSONField(name = "f_user")
        private String fUser;

        @JSONField(name = "f_user_id")
        private String fUserId;
    }
}
