package com.tech.fate.portal.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ParticipantVo {
    @JSONField(name = "creation_time")
    private String creationAt;
    @JSONField(name = "description")
    private String siteDescription;

    private boolean isCurrentSite;
    @JSONField(name = "name")
    private String siteName;

    private long sitePartyId;

    private int status;
    @JSONField(name = "uuid")
    private String siteUuid;

    private String role;
}
