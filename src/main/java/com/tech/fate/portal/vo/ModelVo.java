package com.tech.fate.portal.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ModelVo {
    private String componentName;

    private String createTime;

    private String jobName;

    private String jobUuid;

    private String modelId;

    private String modelVersion;

    private String name;

    private long partyId;

    private String projectName;

    private String projectUuid;

    private String role;

    private String uuid;
}
