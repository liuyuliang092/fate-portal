/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tech.fate.portal.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年07月20日 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestFmlDto {

    @JSONField(name = "algorithm_component_name")
    private String algorithmComponentName;

    @JSONField(name = "conf_json")
    private String confJson;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "dsl_json")
    private String dslJson;

    @JSONField(name = "evaluate_component_name")
    private String evaluateComponentName;

    @JSONField(name = "initiator_data")
    private JobRequestDto.RequestData initiatorData;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "other_site_data")
    private List<JobRequestDto.RequestData> otherSiteData;

    @JSONField(name = "predicting_model_uuid")
    private String predictingModelUuid;

    @JSONField(name = "project_uuid")
    private String projectUuid;

    @JSONField(name = "training_algorithm_type")
    private String trainingAlgorithmType;

    @JSONField(name = "training_component_list_to_deploy")
    private List<String> trainingComponentListToDeploy;

    @JSONField(name = "training_model_name")
    private String trainingModelName;

    @JSONField(name = "training_validation_enabled")
    private boolean trainingValidationEnabled;

    @JSONField(name = "training_validation_percent")
    private int trainingValidationPercent;

    @JSONField(name = "type")
    private int type;

    @JSONField(name = "username")
    private String userName;

    @JSONField(name = "uuid")
    private String uuid;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor

    public static class RequestData{

        @JSONField(name = "data_uuid")
        private String dataUuid;

        @JSONField(name = "label_name")
        private String labelName;
    }
}
