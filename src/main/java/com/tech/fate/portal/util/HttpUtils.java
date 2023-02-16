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
package com.tech.fate.portal.util;

import cn.hutool.http.ContentType;
import com.tech.fate.portal.common.HttpCommon;
import com.tech.fate.portal.common.HttpRequestCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Slf4j
public class HttpUtils {

    public static String get(String url) {
        String result = null;
        try {
            result = HttpRequestCommon.init(url, HttpCommon.HTTP_GET)
                    .contentType(ContentType.JSON.getValue())
                    .timeout(HttpCommon.TIME_OUT)
                    .execute()
                    .body();
        } catch (Exception e) {
            log.error("http get request error,requestUrl = {}", url, e);
        }
        return result;
    }

    public static String post(String url, String requestParams) {
        String result = null;
        try {
            result = HttpRequestCommon.init(url, HttpCommon.HTTP_POST)
                    .contentType(ContentType.JSON.getValue())
                    .body(requestParams)
                    .timeout(HttpCommon.TIME_OUT)
                    .execute()
                    .body();
        } catch (Exception e) {
            log.error("http post request error,requestUrl = {},requestParams = {}", url, requestParams, e);
        }
        return result;
    }

    public static String put(String url, String requestParams) {
        String result = null;
        try {
            result = HttpRequestCommon.init(url, HttpCommon.HTTP_PUT)
                    .contentType(ContentType.JSON.getValue())
                    .body(requestParams)
                    .timeout(HttpCommon.TIME_OUT)
                    .execute()
                    .body();
        } catch (Exception e) {
            log.error("http post request error,requestUrl = {},requestParams = {}", url, requestParams, e);
        }
        return result;
    }

    public static String uploadFile(String url, String fileName, InputStream inputStream) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // 处理中文文件名称乱码
            multipartEntityBuilder.setMode(HttpMultipartMode.RFC6532);
            multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
            multipartEntityBuilder.addBinaryBody("file", inputStream, org.apache.http.entity.ContentType.MULTIPART_FORM_DATA, fileName);
            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            log.error("file upload error,url = {}", url, e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("CloseableHttpResponse close error,url = {}", url, e);
            }
        }
        return resultString;
    }
}
