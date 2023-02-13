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
package com.tech.fate.portal.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.common.Result;
import com.tech.fate.portal.constants.GlobalConstants;
import com.tech.fate.portal.util.JwtUtil;
import com.tech.fate.portal.util.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author Marco Polo
 **/
@Component
@WebFilter(urlPatterns = {"/api/**"}, filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {

    @Value("${login.password}")
    private String password;


    private final List<String> excludeUrl = Lists.newArrayList("/api/auth/login","/api/auth/logout");
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String md5pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String path = httpServletRequest.getRequestURI();
        if(excludeUrl.contains(path)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String token = TokenUtils.getTokenByRequest(httpServletRequest);
        if (StringUtils.isNotBlank(token)) {
            String userName = TokenUtils.getLoginUserName(httpServletRequest);
            if (JwtUtil.verify(token, userName, md5pwd)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        ApiResponse result = ApiResponse.unauthorized("unauthorized");
        httpResponse.getWriter().write(mapper.writeValueAsString(result));
        return;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
