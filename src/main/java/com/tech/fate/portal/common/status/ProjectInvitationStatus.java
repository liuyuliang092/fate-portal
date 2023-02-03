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

public enum ProjectInvitationStatus {
    //    ProjectInvitationStatusCreated: 0
    ProjectInvitationStatusCreated(0, ""),
    ProjectInvitationStatusSent(1, "已邀请"),
    ProjectInvitationStatusRevoked(2, ""),
    ProjectInvitationStatusAccepted(3, "已接受"),
    ProjectInvitationStatusRejected(4, "已拒绝");

    private final int status;
    private final String message;

    ProjectInvitationStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }
}
