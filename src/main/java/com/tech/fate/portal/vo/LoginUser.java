package com.tech.fate.portal.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 在线用户信息
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
	/**
	 * 登录人账号
	 */
	private String username;

	/**
	 * 登录人密码
	 */
	private String password;


}
