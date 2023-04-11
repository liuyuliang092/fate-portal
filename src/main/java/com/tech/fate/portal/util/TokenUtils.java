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

import cn.hutool.json.JSONUtil;
import com.tech.fate.portal.api.CommonApi;
import com.tech.fate.portal.constants.CacheConstant;
import com.tech.fate.portal.constants.CommonConstant;
import com.tech.fate.portal.constants.RedisKeysConstants;
import com.tech.fate.portal.exception.FatePortalException;
import com.tech.fate.portal.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TokenUtils {


    public static String getTokenByRequest(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (token == null) {
            token = request.getHeader("X-Access-Token");
        }
        return token;
    }

    public static boolean verifyToken(HttpServletRequest request, RedisUtil redisUtil) {
        log.debug(" -- url --" + request.getRequestURL());
        String token = getTokenByRequest(request);
        return TokenUtils.verifyRefreshToken(token, redisUtil);
    }

    public static boolean verifyRefreshToken(String token, RedisUtil redisUtil) {
        if (StringUtils.isBlank(token)) {
            throw new FatePortalException("token不能为空!");
        }
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new FatePortalException("token非法无效!");
        }
//        LoginUser user = TokenUtils.getLoginUser(username, commonApi, redisUtil);
//        if (user == null) {
//            throw new FatePortalException("用户不存在!");
//        }
        if (!jwtTokenRefresh(username, "admin", redisUtil)) {
            throw new FatePortalException(CommonConstant.TOKEN_IS_INVALID_MSG);
        }
        return true;
    }

    private static boolean jwtTokenRefresh(String userName, String passWord, RedisUtil redisUtil) {
        String tokenKey = RedisKeysConstants.BASIC + RedisKeysConstants.TOKEN;
        String cacheToken = JSONUtil.toJsonStr(redisUtil.get(tokenKey));
        if (StringUtils.isNotEmpty(cacheToken)) {
            if (!JwtUtil.verify(cacheToken, userName, passWord)) {
//                String newAuthorization = JwtUtil.sign(userName, passWord);
//                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                redisUtil.expire(tokenKey, JwtUtil.EXPIRE_TIME * 2 / 1000);
            }
            return true;
        }
        return false;
    }

    public static LoginUser getLoginUser(String username, CommonApi commonApi, RedisUtil redisUtil) {
        LoginUser loginUser = null;
        String loginUserKey = CacheConstant.SYS_USERS_CACHE + "::" + username;
        if (redisUtil.hasKey(loginUserKey)) {
            loginUser = (LoginUser) redisUtil.get(loginUserKey);
        } else {
            // 查询用户信息
            loginUser = commonApi.getUserByName(username);
        }
        return loginUser;
    }

    public static String getLoginUserName(HttpServletRequest request) {
        String token = getTokenByRequest(request);
        String userName = null;
        if (StringUtils.isNotBlank(token)) {
            userName = JwtUtil.getUsername(token);
        }
        return userName;
    }
}
