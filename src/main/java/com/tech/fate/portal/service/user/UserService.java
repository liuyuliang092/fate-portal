package com.tech.fate.portal.service.user;

import com.tech.fate.portal.dto.MenuDto;
import com.tech.fate.portal.dto.UserInfoDto;
import com.tech.fate.portal.vo.LoginUser;

public interface UserService {
    UserInfoDto getUserInfo(LoginUser loginUser);

    MenuDto getUserMenu(LoginUser loginUser);
}
