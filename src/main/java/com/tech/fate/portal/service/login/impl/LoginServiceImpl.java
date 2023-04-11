package com.tech.fate.portal.service.login.impl;

import com.tech.fate.portal.constants.CommonConstant;
import com.tech.fate.portal.constants.RedisKeysConstants;
import com.tech.fate.portal.dto.LoginResultDto;
import com.tech.fate.portal.service.login.LoginService;
import com.tech.fate.portal.util.JwtUtil;
import com.tech.fate.portal.util.RedisUtil;
import com.tech.fate.portal.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author NO.95
 */
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
        String loginName = loginUser.getUsername();
        String lockedUserKey = RedisKeysConstants.BASIC + RedisKeysConstants.LOCKRD_USER;
        String loginErrorCountKey = RedisKeysConstants.BASIC + RedisKeysConstants.LOGIN_ERROR_COUNT;
        String tokenKey = RedisKeysConstants.BASIC + RedisKeysConstants.TOKEN;
        if (redisUtil.get(lockedUserKey) != null && StringUtils.isNotBlank(redisUtil.get(lockedUserKey).toString())) {
            long accountUnlockLeftTime = redisUtil.getExpire(lockedUserKey);
            loginResultDto.setMessage("The current account is locked, the remaining time : " + formatTime((int) accountUnlockLeftTime));
            return loginResultDto;
        }
        if (userName.equals(loginName) && md5pwd.equals(loginUser.getPassword())) {
            loginResultDto.setSuccess(Boolean.TRUE);
            String token = JwtUtil.sign(loginName, loginUser.getPassword());
            redisUtil.set(tokenKey, token, 30 * 60);
            redisUtil.del(loginErrorCountKey);
            redisUtil.del(lockedUserKey);
            loginResultDto.setToken(token);
            loginResultDto.setMessage("login success");
        } else {
            redisUtil.incr(loginErrorCountKey, 1);
            int loginFailTimes = (Integer) redisUtil.get(loginErrorCountKey);
            if (loginFailTimes >= CommonConstant.LOGIN_FAILED_LIMITED) {
                loginResultDto.setMessage("If the password is incorrect for 5 consecutive times, the current account will be locked for half an hour");
                redisUtil.set(lockedUserKey, loginName, 30 * 60);
                redisUtil.del(loginErrorCountKey);
            } else {
                loginResultDto.setMessage("login failed,the username or password is incorrect,remaining times:" + (CommonConstant.LOGIN_FAILED_LIMITED - loginFailTimes));
            }
        }
        return loginResultDto;
    }

    @Override
    public LoginResultDto loginOut(LoginUser loginUser) {
        LoginResultDto loginResultDto = new LoginResultDto();
        //todo 后续清理缓存的登录信息时使用
        String tokenKey = RedisKeysConstants.BASIC + RedisKeysConstants.TOKEN;
        redisUtil.del(tokenKey);
        loginResultDto.setSuccess(Boolean.TRUE);
        return loginResultDto;
    }

    private static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d分%02d秒", minutes, remainingSeconds);
    }
}


