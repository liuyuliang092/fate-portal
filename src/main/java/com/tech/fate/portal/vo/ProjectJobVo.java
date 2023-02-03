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
package com.tech.fate.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 项目任务
 *
 * @author: 刘欣怡
 * @date: 2022年07月14日 9:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJobVo {

    private String creationTime;

    private String description;

    private String fateJobId;

    private String fateJobStatus;

    private String fateModelName;

    private String finishTime;

    private String initiatingSiteName;

    private long initiatingSitePartyId;

    private String initiatingSiteUuid;

    private boolean isInitiator;

    private String name;

    private boolean pendingOnThisSite;

    private String projectUuid;

    private int status;

    private String statusStr;

    private int type;

    private String username;

    private String uuid;

}
