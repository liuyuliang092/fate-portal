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
package com.tech.fate.portal.service.node.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.tech.fate.portal.constants.CommonConstant;
import com.tech.fate.portal.dto.ComponentsDto;
import com.tech.fate.portal.dto.ComponentsParamsDto;
import com.tech.fate.portal.dto.ComponentsParamsSettingsDto;
import com.tech.fate.portal.mapper.ComponentsMapper;
import com.tech.fate.portal.vo.ComponentsBaseInfo;
import com.tech.fate.portal.vo.ComponentsParams;
import com.tech.fate.portal.vo.ComponentsParamsSettings;
import com.tech.fate.portal.vo.ComponentsVo;
import com.tech.fate.portal.service.node.ComponentsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ComponentsServiceImpl implements ComponentsService {

    @Autowired
    private ComponentsMapper componentsMapper;

    @Override
    public List<ComponentsVo> algorithmComponents() {
        List<ComponentsDto> componentsDtoList = this.queryAlgorithmComponents();
        return transform(componentsDtoList);
    }

    @Override
    public ComponentsParams queryAlgorithmComponentsParams(String nodeId) {
        ComponentsParamsDto componentsParamsDto = new ComponentsParamsDto();
        componentsParamsDto.setNodeId(nodeId);
        componentsParamsDto.setStatus(CommonConstant.STATUS_NORMAL);
        ComponentsParams componentsParams = componentsMapper.queryAlgorithmComponentsParams(componentsParamsDto);
        return componentsParams;
    }

    @Override
    public List<ComponentsParamsSettings> queryAlgorithmComponentsParamsSettings(String projectUuid, String taskUuid, String dslNodeId) {
        ComponentsParamsSettingsDto componentsParamsSettingsDto = new ComponentsParamsSettingsDto();
        componentsParamsSettingsDto.setProjectUuid(projectUuid);
        componentsParamsSettingsDto.setTaskUuid(taskUuid);
        componentsParamsSettingsDto.setDslNodeId(dslNodeId);
        List<ComponentsParamsSettings> componentsParamsSettings = componentsMapper.queryAlgorithmComponentsParamsSettings(componentsParamsSettingsDto);
        return componentsParamsSettings;
    }

    @Override
    public void saveAlgorithmComponentsParamsSettings(ComponentsParamsSettings componentsParamsSettings) {
        ComponentsParamsSettingsDto componentsParamsSettingsDto = BeanUtil.copyProperties(componentsParamsSettings, ComponentsParamsSettingsDto.class);
        log.info("ComponentsParamsSettings = {}", componentsParamsSettingsDto);
        if (componentsMapper.queryAlgorithmComponentsParamsSettings(componentsParamsSettingsDto).size() > 0) {
            updateAlgorithmComponentsParamsSettings(componentsParamsSettings);
        } else {
            componentsMapper.saveAlgorithmComponentsParamsSettings(componentsParamsSettingsDto);
        }
    }

    @Override
    public void updateAlgorithmComponentsParamsSettings(ComponentsParamsSettings componentsParamsSettings) {
        componentsMapper.updateAlgorithmComponentsParamsSettings(componentsParamsSettings);
    }

    @Override
    public void deleteAlgorithmComponentsParamsSettings(ComponentsParamsSettings componentsParamsSettings) {
        componentsMapper.deleteAlgorithmComponentsParamsSettings(componentsParamsSettings);
    }

    @Override
    public List<ComponentsDto> queryAlgorithmComponents() {
        return componentsMapper.queryAlgorithmComponents();
    }


    private List<ComponentsVo> transform(List<ComponentsDto> componentsDtoList) {
        List<ComponentsVo> componentsGroupList = BeanUtil.copyToList(componentsDtoList.stream().distinct().collect(Collectors.toList()), ComponentsVo.class);
        Map<String, List<ComponentsDto>> componentsGroupDetails = componentsDtoList.stream().collect(Collectors.groupingBy(ComponentsDto::getGroupId));
        componentsGroupList.forEach(componentsVo -> buildComponentsVo(componentsVo, componentsGroupDetails));
        return componentsGroupList;
    }

    private void buildComponentsVo(ComponentsVo componentsVo, Map<String, List<ComponentsDto>> componentsGroupDetails) {
        componentsVo.setChildren(BeanUtil.copyToList(componentsGroupDetails.get(componentsVo.getGroupId()), ComponentsBaseInfo.class));
        componentsVo.setNodeNum(componentsGroupDetails.get(componentsVo.getGroupId()).size());
    }

//    private List<ComponentsParams> buildTreeByStream(List<ComponentsParams> ComponentsParamsList) {
//        //获取parentId = 0的根节点
//        List<ComponentsParams> list = ComponentsParamsList.stream().filter(item -> "0".equals(item.getParentId())).collect(Collectors.toList());
//        //根据parentId进行分组
//        Map<String, List<ComponentsParams>> map = ComponentsParamsList.stream().collect(Collectors.groupingBy(ComponentsParams::getParentId));
//        recursionFnTree(list, map);
//        return list;
//    }
//
//    private void recursionFnTree(List<ComponentsParams> list, Map<String, List<ComponentsParams>> map) {
//        for (ComponentsParams componentsParams : list) {
//            List<ComponentsParams> childList = map.get(componentsParams.getUuid());
//            componentsParams.setChildParamValue(childList);
//            if (null != childList && 0 < childList.size()) {
//                recursionFnTree(childList, map);
//            }
//        }
//    }

}
