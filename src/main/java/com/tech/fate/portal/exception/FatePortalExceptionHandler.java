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
package com.tech.fate.portal.exception;

import cn.hutool.core.util.ObjectUtil;
import com.tech.fate.portal.common.Result;
import com.tech.fate.portal.enums.SentinelErrorInfoEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class FatePortalExceptionHandler {

    @ExceptionHandler(FatePortalException.class)
    public Result<?> handleFatePortalException(FatePortalException e) {
        log.error(e.getMessage(), e);
        return Result.error("error");
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        //update-begin---author:zyf ---date:20220411  for：处理Sentinel限流自定义异常
        Throwable throwable = e.getCause();
        SentinelErrorInfoEnum errorInfoEnum = SentinelErrorInfoEnum.getErrorByException(throwable);
        if (ObjectUtil.isNotEmpty(errorInfoEnum)) {
            return Result.error(errorInfoEnum.getError());
        }
        //update-end---author:zyf ---date:20220411  for：处理Sentinel限流自定义异常
        return Result.error("fail");
    }
}
