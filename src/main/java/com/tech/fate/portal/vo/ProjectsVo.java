package com.tech.fate.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 功能描述
 *
 * @author: 刘欣怡
 * @date: 2022年07月06日 13:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectsVo {

    private String createDate;

    private String projectUuid;

    private String projectName;

    private String projectDescription;

    private short projectStatus;

    private String projectManager;

    private String managingSiteName;

    private long managingSitePartyId;

    private String managingSiteUuid;
}
