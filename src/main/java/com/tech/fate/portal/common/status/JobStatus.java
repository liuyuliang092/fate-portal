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
 * @author: 刘欣怡
 */
public enum JobStatus {
    //HOLD=0,用来占位的，实际状态从1开始
    UNKNOWN(0, "异常状态", ""),
    NEWJOB(1, "新建任务", ""),
    JobWaiting(2, "待运行", ""),
    JobRunning(3, "运行中", "running"),
    JobFail(4, "运行失败", "failed"),
    JobSuccess(5, "运行成功", "success");

    private final int status;
    private final String statusMessage;
    private String fateStatus;

    JobStatus(int status, String statusMessage, String fateStatus) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.fateStatus = fateStatus;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public static int getStatusByFateStatus(String fateStatus) {
        for (JobStatus jobStatus : JobStatus.values()) {
            if (jobStatus.fateStatus.equals(fateStatus)) {
                return jobStatus.status;
            }
        }
        return JobStatus.UNKNOWN.status;
    }

    public static String getStatusMessageByStatus(int status) {
        for (JobStatus jobStatus : JobStatus.values()) {
            if (jobStatus.status == status) {
                return jobStatus.statusMessage;
            }
        }
        return JobStatus.UNKNOWN.statusMessage;
    }
}
