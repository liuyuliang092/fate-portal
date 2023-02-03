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
package com.tech.fate.portal.common.status;

/**
 * @author liuxinyi
 */

public enum ProjectParticipantStatus {
    //    ProjectParticipantStatusUnknown: 0
    unknown(-1, "未知状态"),
    pending(1, "待处理"),
    joined(3, "已加入"),
    rejected(4, "已拒绝"),
    cancel(2, "已取消"),
    create(0, "已创建");
    private final int status;

    private final String message;

    ProjectParticipantStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatusMessage(Integer status) {
        ProjectParticipantStatus[] projectParticipantStatuses = ProjectParticipantStatus.values();
        for (ProjectParticipantStatus p : projectParticipantStatuses) {
            if (p.status == status) {
                return p.message;
            }
        }
        return unknown.getMessage();
    }
}
