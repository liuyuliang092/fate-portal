package com.tech.fate.portal.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tech.fate.portal.dto.QueryProjectLocalDataDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年07月26日 10:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryProjectLocalDataVo {

    private Page<QueryProjectLocalDataDto> projectLocalDataDtoList;
}
