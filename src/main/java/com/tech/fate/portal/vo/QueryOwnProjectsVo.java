package com.tech.fate.portal.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tech.fate.portal.dto.ClosedProjectsDto;
import com.tech.fate.portal.dto.InvitedProjectsDto;
import com.tech.fate.portal.dto.JoinedProjectsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryOwnProjectsVo {

    @JSONField(name = "closed_projects")
    private Page<ClosedProjectsDto> closedProjectsDtoList;

    @JSONField(name = "invited_projects")
    private Page<InvitedProjectsDto> invitedProjectsDtoList;

    @JSONField(name = "joined_projects")
    private Page<JoinedProjectsDto> joinedProjectsDtoList;

    private Page<JoinedProjectsDto> allProjectsDtoList;

    private Page<JoinedProjectsDto> myProjectsDtoList;
}
