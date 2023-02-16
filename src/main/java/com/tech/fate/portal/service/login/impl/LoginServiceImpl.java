package com.tech.fate.portal.service.login.impl;

import com.tech.fate.portal.dto.LoginResultDto;
import com.tech.fate.portal.dto.UserInfoDto;
import com.tech.fate.portal.service.login.LoginService;
import com.tech.fate.portal.util.JwtUtil;
import com.tech.fate.portal.util.RedisUtil;
import com.tech.fate.portal.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Value("${login.name}")
    private String userName;
    @Value("${login.password}")
    private String password;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public LoginResultDto loginIn(LoginUser loginUser) {
        LoginResultDto loginResultDto = new LoginResultDto();
        String md5pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        if (loginUser != null && userName.equals(loginUser.getUsername()) && md5pwd.equals(loginUser.getPassword())) {
            loginResultDto.setSuccess(Boolean.TRUE);
            String token = JwtUtil.sign(loginUser.getUsername(), loginUser.getPassword());
            loginResultDto.setToken(token);
            loginResultDto.setMessage("login success");
        } else {
            loginResultDto.setMessage("login failed,the username or password is incorrect");
        }
        return loginResultDto;
    }

    @Override
    public LoginResultDto loginOut(LoginUser loginUser) {
        LoginResultDto loginResultDto = new LoginResultDto();
        //todo 后续清理缓存的登录信息时使用
        loginResultDto.setSuccess(Boolean.TRUE);
        return loginResultDto;
    }
}
