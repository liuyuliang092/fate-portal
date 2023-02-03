package com.tech.fate.portal.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 项目详情dto
 *
 * @author: 刘欣怡
 * @date: 2022年07月13日 17:03
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetailVo {

    @JSONField(name = "auto_approval_enabled")
    private boolean autoApprovalEnabled;

    @JSONField(name = "creation_time")
    private String creationTime;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "managed_by_this_site")
    private boolean managedByThisSite;

    @JSONField(name = "manager")
    private String manager;

    @JSONField(name = "managing_site_name")
    private String managingSiteName;

    @JSONField(name = "managing_site_party_id")
    private long managingSitePartyId;

    @JSONField(name = "name")
    private String projectName;

    @JSONField(name = "uuid")
    private String projectUuid;

    private String role;

    private String managingSiteUuid;
}
