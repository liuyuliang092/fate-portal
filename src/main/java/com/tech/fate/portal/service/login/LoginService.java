package com.tech.fate.portal.service.login;

import com.tech.fate.portal.dto.LoginResultDto;
import com.tech.fate.portal.dto.UserInfoDto;
import com.tech.fate.portal.vo.LoginUser;

public interface LoginService {

    LoginResultDto loginIn(LoginUser loginUser);

    LoginResultDto loginOut(LoginUser loginUser);

}
