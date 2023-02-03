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

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestCommon {



    public static HttpRequest init(String url, String methodType) throws Exception {
        HttpRequest httpRequest;
        if (HttpCommon.HTTP_GET.equals(methodType)) {
            httpRequest = HttpRequest.get(url).header(Header.USER_AGENT, "Hutool http");
        } else if (HttpCommon.HTTP_POST.equals(methodType)) {
            httpRequest = HttpRequest.post(url).header(Header.USER_AGENT, "Hutool http");
        } else if (HttpCommon.HTTP_PUT.equals(methodType)) {
            httpRequest = HttpRequest.put(url).header(Header.USER_AGENT, "Hutool http");
        } else {
            log.warn("");
            throw new Exception();
        }
        return httpRequest;
    }
}
