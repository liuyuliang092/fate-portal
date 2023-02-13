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
package com.tech.fate.portal.controller.app;

import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.dto.LoginResultDto;
import com.tech.fate.portal.dto.UserInfoDto;
import com.tech.fate.portal.service.login.LoginService;
import com.tech.fate.portal.util.TokenUtils;
import com.tech.fate.portal.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/auth/login")
    public ApiResponse loginIn(@RequestBody LoginUser loginUser) {
        LoginResultDto resultDto = loginService.loginIn(loginUser);
        if (resultDto.isSuccess()) {
            return ApiResponse.ok("login success", resultDto);
        }
        return ApiResponse.fail("login failed", resultDto);
    }

    @PostMapping("/auth/logout")
    public ApiResponse logOut(HttpServletRequest request) {
        LoginUser loginUser = new LoginUser();
        String userName = TokenUtils.getLoginUserName(request);
        loginUser.setUsername(userName);
        LoginResultDto resultDto = loginService.loginOut(loginUser);
        if (resultDto.isSuccess()) {
            return ApiResponse.ok("logout success");
        }
        return ApiResponse.fail("logout failed");
    }
}
