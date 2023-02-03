package com.tech.fate.portal.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ModelDetailInfoVo {
    private String componentName;

    private String createTime;

    private EvaluationBean evaluation;

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

    @NoArgsConstructor
    @Data
    public static class EvaluationBean {

        private String additionalProp1;

        private String additionalProp2;

        private String additionalProp3;
    }
}
