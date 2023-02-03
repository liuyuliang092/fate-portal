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
package com.tech.fate.portal.common;

import lombok.Data;

@Data
public class ApiResponse {

    private Integer code;

    private String message;

    private Object data;

    public static ApiResponse ok() {
        return ok("success", null);
    }

    public static ApiResponse ok(String message) {
        return ok(message, null);
    }

    public static ApiResponse ok(String message, Object data) {
        ApiResponse result = new ApiResponse();
        result.setCode(0);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static ApiResponse fail(String message) {
        return fail(message, null);
    }

    public static ApiResponse fail(String message, Object data) {
        ApiResponse result = new ApiResponse();
        result.setCode(1);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

}
