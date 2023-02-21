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
package com.tech.fate.portal.vo;

import cn.hutool.json.JSONUtil;
import com.tech.fate.portal.common.CommonField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphData extends CommonField {

    @NotNull
    private String projectUuid;

    @NotNull
    private String taskUuid;

    private Object graphData;

    private Integer status;

    private String graphDataStr;

    public String getGraphDataStr() {
        if (graphData != null) {
            graphDataStr = JSONUtil.toJsonStr(graphData);
        }
        return graphDataStr;
    }

    public Object getGraphData() {
        if (StringUtils.isNotBlank(graphDataStr)) {
            graphData = JSONUtil.parseObj(graphDataStr);
        }
        return graphData;
    }

    public void setGraphData(Object graphData) {
        if (graphData != null) {
            this.graphData = JSONUtil.parseObj(graphData);
        } else {
            this.graphData = graphData;
        }
    }
}
