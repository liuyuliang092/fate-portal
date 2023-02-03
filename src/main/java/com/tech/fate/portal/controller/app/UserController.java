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
import com.tech.fate.portal.dto.MenuDto;
import com.tech.fate.portal.dto.UserInfoDto;
import com.tech.fate.portal.service.user.UserService;
import com.tech.fate.portal.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/info")
    public ApiResponse getUserInfo(LoginUser loginUser) {
        UserInfoDto userInfoDto = userService.getUserInfo(loginUser);
        log.info("user info = {}", userInfoDto);
        return ApiResponse.ok("success", userInfoDto);
    }

    @GetMapping("/user/nav")
    public ApiResponse getUserMebuList(LoginUser loginUser) {
        MenuDto menuDto = userService.getUserMenu(loginUser);
        return ApiResponse.ok("success", menuDto);
    }
}
