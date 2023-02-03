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

public enum JobFateStatus {
//    READY:0
    READY(0),
    WAITING(1),
    RUNNING(2),
    CANCELED(3),
    TIMEOUT(4),
    FAILED(5),
    SUCCESS(6);

    private final int status;

    JobFateStatus(int status) {
        this.status = status;
    }

    public int getStatus(){
        return status;
    }
}
