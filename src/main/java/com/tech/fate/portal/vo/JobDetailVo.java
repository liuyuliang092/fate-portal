package com.tech.fate.portal.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年07月20日 23:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDetailVo {

    @JSONField(name = "algorithm_component_name")
    private String algorithmComponentName;
    @JSONField(name = "conf_json")
    private String confJson;
    @JSONField(name = "creation_time")
    private String creationTime;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "dsl_json")
    private String dslJson;
    @JSONField(name = "evaluate_component_name")
    private String evaluateComponentName;
    @JSONField(name = "fate_job_id")
    private String fateJobId;
    @JSONField(name = "fate_job_status")
    private String fateJobStatus;
    @JSONField(name = "fate_model_name")
    private String fateModelName;
    @JSONField(name = "finish_time")
    private String finishTime;
    @JSONField(name = "initiating_site_name")
    private String initiatingSiteName;
    @JSONField(name = "initiating_site_party_id")
    private long initiatingSitePartyId;
    @JSONField(name = "initiating_site_uuid")
    private String initiatingSiteUuid;
    @JSONField(name = "initiator_data")
    private RequestData initiatorData;
    @JSONField(name = "is_initiator")
    private boolean isInitiator;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "other_site_data")
    private List<RequestData> otherSiteData;
    @JSONField(name = "pending_on_this_site")
    private Boolean pendingOnThisSite;
    @JSONField(name = "predicting_model_uuid")
    private String predictingModelUuid;
    @JSONField(name = "project_uuid")
    private String projectUuid;
    @JSONField(name = "result_info")
    private String resultInfo;
    @JSONField(name = "status")
    private Integer status;
    @JSONField(name = "status_message")
    private String statusMessage;
    @JSONField(name = "status_str")
    private String statusStr;
    @JSONField(name = "training_algorithm_type")
    private String trainingAlgorithmType;
    @JSONField(name = "training_component_list_to_deploy")
    private List<String> trainingComponentListToDeploy;
    @JSONField(name = "training_model_name")
    private String trainingModelName;
    @JSONField(name = "training_validation_enabled")
    private Boolean trainingValidationEnabled;
    @JSONField(name = "training_validation_percent")
    private Integer trainingValidationPercent;
    @JSONField(name = "type")
    private Integer type;
    @JSONField(name = "username")
    private String username;
    @JSONField(name = "uuid")
    private String uuid;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestData {
        @JSONField(name = "data_uuid")
        private String dataUuid;
        @JSONField(name = "description")
        private String description;
        @JSONField(name = "is_local")
        private boolean isLocal;
        @JSONField(name = "label_name")
        private String labelName;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "providing_site_name")
        private String providingSiteName;
        @JSONField(name = "providing_site_party_id")
        private long providingSitePartyId;
        @JSONField(name = "providing_site_uuid")
        private String providingSiteUuid;
        @JSONField(name = "site_status")
        private Integer siteStatus;
        @JSONField(name = "site_status_str")
        private String siteStatusStr;
    }


//    private String algorithmComponentName;
//
//    private String confJson;
//
//    private String description;
//
//    private String dslJson;
//
//    private String evaluateComponentName;
//
//    private String fateJobId;
//
//    private String fateJobStatus;
//
//    private String fateModelName;
//
//    private String finishTime;
//
//    private String initiatingSiteName;
//
//    private long initiatingSitePartyId;
//
//    private String initiatingSiteUuid;
//
//    private RequestData initiatorData;
//
//    private boolean isInitiator;
//
//    private String name;
//
//    private List<RequestData> otherSiteData;
//
//    private boolean pendingOnThisSite;
//
//    private String predictingModelUuid;
//
//    private String projectUuid;
//
//    private String resultInfo;
//
//    private int status;
//
//    private String statusMessage;
//
//    private String statusStr;
//
//    private String trainingAlgorithmType;
//
//    private List<String> trainingComponentListToDeploy;
//
//    private String trainingModelName;
//
//    private boolean trainingValidationEnabled;
//
//    private int trainingValidationPercent;
//
//    private int type;
//
//    private String username;
//
//    private String uuid;
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//
//    public static class RequestData{
//
//        private String dataUuid;
//
//        private String description;
//
//        private String labelName;
//
//        private boolean isLocal;
//
//        private String name;
//
//        private String providingSiteName;
//
//        private long providingSitePartyId;
//
//        private String providingSiteUuid;
//
//        private int siteStatus;
//
//        private String siteStatusStr;
//
//    }
}
