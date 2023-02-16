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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentsDto {
    private String id;

    private String groupId;

    private String groupName;

    private String nodeId;

    private String nodeName;

    private String nodeVersion;

    /**
     * 1有效
     * 0无效
     */
    private String status;

    private String nodeLogo;

    private String nodeModule;

    /**
     * 0:need
     * 1:not
     */
    private Integer needSettings;

    @Override
    public boolean equals(Object o) {
        if (this == o)
        { return true;}
        if (o == null || getClass() != o.getClass())
        {return false;}
        ComponentsDto that = (ComponentsDto) o;
        return groupId.equals(that.groupId) && groupName.equals(that.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName);
    }
}
