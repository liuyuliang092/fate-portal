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
 * @author 刘欣怡
 */

public enum ProjectStatus {
//
    ProjectStatusManaged(1),
    ProjectStatusPending(2),
    ProjectStatusJoined(3),
    ProjectStatusRejected(4),
    ProjectStatusLeft(5),
    ProjectStatusClosed(6),
    ProjectStatusDismissed(7);

    private final int status;
    ProjectStatus(int status) {
        this.status = status;
    }

    public int getStatus(){
        return status;
    }
}
