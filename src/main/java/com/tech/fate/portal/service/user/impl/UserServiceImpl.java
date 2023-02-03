package com.tech.fate.portal.service.user.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.tech.fate.portal.dto.MenuDto;
import com.tech.fate.portal.dto.UserInfoDto;
import com.tech.fate.portal.service.user.UserService;
import com.tech.fate.portal.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserInfoDto getUserInfo(LoginUser loginUser) {
        UserInfoDto userInfoDto = new UserInfoDto();
        //todo 临时写死用户数据，待后续完善用户角色与菜单权限后，根据数据库维护信息进行查询
        String menus = ResourceUtil.readUtf8Str("data/menuList.json");
        return userInfoDto;
    }

    @Override
    public MenuDto getUserMenu(LoginUser loginUser) {
        MenuDto menuDto = new MenuDto();
        //todo 临时写死菜单数据，待后续完善用户角色与菜单权限后，根据数据库维护信息进行查询
        String menus = ResourceUtil.readUtf8Str("data/menuList.json");
        List menuList = JSONUtil.toList(menus, List.class);
        menuDto.setMenuList(menuList);
        log.info("menu = {}", menuDto);
        return menuDto;
    }
}
