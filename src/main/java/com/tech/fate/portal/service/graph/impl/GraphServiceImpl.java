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
package com.tech.fate.portal.service.graph.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.tech.fate.portal.common.FateFlowResult;
import com.tech.fate.portal.common.status.CheckResultStatus;
import com.tech.fate.portal.common.status.JobStatus;
import com.tech.fate.portal.components.Input;
import com.tech.fate.portal.components.OutPut;
import com.tech.fate.portal.components.RunConfRole;
import com.tech.fate.portal.constants.*;
import com.tech.fate.portal.dto.ComponentsDto;
import com.tech.fate.portal.dto.JobDto;
import com.tech.fate.portal.dto.ProjectDataDto;
import com.tech.fate.portal.dto.ProjectParticipantsDto;
import com.tech.fate.portal.mapper.GraphMapper;
import com.tech.fate.portal.service.JobService;
import com.tech.fate.portal.service.LocalDataService;
import com.tech.fate.portal.service.ProjectService;
import com.tech.fate.portal.service.SiteService;
import com.tech.fate.portal.service.graph.GraphService;
import com.tech.fate.portal.service.node.ComponentsService;
import com.tech.fate.portal.util.HttpUtils;
import com.tech.fate.portal.vo.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GraphServiceImpl implements GraphService {


    @Autowired
    private SiteService siteService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ComponentsService componentsService;
    @Autowired
    private LocalDataService localDataService;
    @Autowired
    private JobService jobService;
    @Autowired
    private GraphMapper graphMapper;
    @Value("${fate.job.parameters.common.auto.retries}")
    private Integer autoRetries;
    @Value("${fate.job.parameters.common.computing.partitions}")
    private Integer computingPartitions;
    @Value("${fate.job.parameters.common.task.cores}")
    private Integer taskCores;
    @Value("${fate.job.parameters.common.task.parallelism}")
    private Integer taskParallelism;

    @Override
    public void saveGraphData(GraphData graphData) throws Exception {
        String data = graphData.getGraphDataStr();
        JSONObject graph = JSONUtil.parseObj(data);
        List cells = graph.get(GraphConstants.CELLS, List.class);
        if (cells.size() == 0) {
            throw new Exception("please dispose dag first");
        }
        List<ComponentsInfo> componentsInfoList = buildComponents(graphData, cells);
        List needSettingsNodeList = filterSettingsNodes(componentsInfoList);
        if (checkComponentsSettings(graphData.getProjectUuid(), graphData.getTaskUuid(), needSettingsNodeList.size())) {
            graphData.setStatus(0);
            if (graphMapper.queryGraphData(graphData) != null) {
                graphMapper.updateGraphData(graphData);
                return;
            }
            graphMapper.saveGraphData(graphData);
        } else {
            throw new Exception("please save component settings first");
        }
    }

    @Override
    public GraphData queryGraphData(String projectUuid, String taskUuid) {
        GraphData graphData = new GraphData();
        graphData.setProjectUuid(projectUuid);
        graphData.setTaskUuid(taskUuid);
        graphData.setStatus(CommonConstant.DEL_FLAG_0);
        return graphMapper.queryGraphData(graphData);
    }

    @Override
    public int runGraph(GraphData graphData) {
        try {
            JobDto jobDto = jobService.queryJob(graphData.getTaskUuid());
            if (jobDto != null) {
                if (jobDto.getStatus() == JobStatus.JobRunning.getStatus()) {
                    return CheckResultStatus.JOB_IS_RUNNING.getStatus();
                }
                String jobParams = buildJobParam(graphData);
                log.info("submit jobParams = {}", jobParams);
                String jobSubmitUrl = siteService.getFateFlowAddr() + FateFlowConstants.JOB_SUBMIT;
                String result = HttpUtils.post(jobSubmitUrl, jobParams);
                FateFlowResult submitResult = JSONUtil.toBean(result, FateFlowResult.class);
                if (submitResult.getRetcode() == CommonConstant.FATE_FLOW_OK) {
                    Object data = submitResult.getData();
                    jobDto.setStatus(JobStatus.JobRunning.getStatus());
                    jobDto.setStatusMessage(JobStatus.JobRunning.getStatusMessage());
                    jobDto.setFateJobId(JSONUtil.parseObj(data).getStr(FateFlowConstants.JOB_ID));
                    this.updateJob(jobDto);
                    return CheckResultStatus.SUCCESS.getStatus();
                } else {
                    jobDto.setStatus(JobStatus.UNKNOWN.getStatus());
//                    jobDto.setStatusMessage(submitResult.getRetmsg());
                    this.updateJob(jobDto);
                    return CheckResultStatus.FAILED.getStatus();
                }
            }
        } catch (Exception e) {
            log.error("【run graph】error occured when build job params,[projectId] = {}, [taskId] = {}", graphData.getProjectUuid(), graphData.getTaskUuid(), e);
        }
        return CheckResultStatus.ERROR.getStatus();
    }

    @Override
    public List<ComponentsStatus> taskStatus(QueryComponentsStatus queryComponentsStatus) {
        //调用fate_flow接口查询任务执行结果
        List<ComponentsStatus> componentsStatusList = Lists.newArrayList();
        try {
            List<ComponentsInfo> nodeList = queryComponentsStatus.getNodeList();
            if (!CollectionUtils.isEmpty(nodeList)) {
                CountDownLatch countDownLatch = new CountDownLatch(nodeList.size());
                JobDetailVo jobDetailVo = jobService.getJobDetailInfo(queryComponentsStatus.getTaskUuid(), null);
                List<Integer> partyIds = site();
                if (jobDetailVo != null && !CollectionUtils.isEmpty(partyIds)) {
                    nodeList.forEach(node -> {
                        try {
                            jobService.queryComponentStatus(jobDetailVo.getFateJobId(), partyIds.get(0), JobConstants.GUEST, node, countDownLatch, componentsStatusList);
                        } catch (Exception e) {
                            log.error("query component status error,task_id = {},component = {}", queryComponentsStatus.getTaskUuid(), node.getDslNodeId(), e);
                            ComponentsStatus componentsStatus = new ComponentsStatus();
                            componentsStatus.setDslNodeId(node.getDslNodeId());
                            componentsStatus.setStatus(ComponentStatusConstants.DEFAULT);
                            componentsStatus.setId(node.getId());
                            componentsStatusList.add(componentsStatus);
                        }
                    });
//                nodeList.forEach(node -> jobService.queryComponentStatus(node, countDownLatch, componentsStatusList));
                    countDownLatch.await();
                }
            }
        } catch (Exception e) {
            log.error("query job components status error,projectId = {},JobId = {}", queryComponentsStatus.getProjectUuid(), queryComponentsStatus.getTaskUuid(), e);
        }
        return componentsStatusList;
    }

    private String buildJobParam(GraphData graphData) {
        JSONObject jobParam = new JSONObject();
        String data = graphData.getGraphDataStr();
        JSONObject graph = JSONUtil.parseObj(data);
        List cells = graph.get(GraphConstants.CELLS, List.class);
        List<EdgeInfo> edgeInfoList = buildEdges(graphData, cells);
        List<ComponentsInfo> componentsInfoList = buildComponents(graphData, cells);
        jobParam.set(JobConstants.JOB_DSL, buildJobDslParam(edgeInfoList, componentsInfoList));
        jobParam.set(JobConstants.JOB_CONF, buildJobConfParam(edgeInfoList, componentsInfoList, graphData.getProjectUuid(), graphData.getTaskUuid()));
        return JSONUtil.toJsonStr(jobParam);
    }

    private List buildEdges(GraphData graphData, List cells) {
        List<EdgeInfo> edgeInfoList = Lists.newArrayList();
        cells.forEach(cell -> {
            if (GraphConstants.SHAPE_EDGE.equals(((JSONObject) cell).get(GraphConstants.SHAPE))) {
                EdgeInfo edgeInfo = JSONUtil.toBean(JSONUtil.toJsonStr(cell), EdgeInfo.class);
                edgeInfoList.add(edgeInfo);
            }
        });
        return edgeInfoList;
    }

    private List buildComponents(GraphData graphData, List cells) {
        List<ComponentsInfo> componentsInfoList = Lists.newArrayList();
        cells.forEach(cell -> {
            if (GraphConstants.SHAPE_NODE.equals(((JSONObject) cell).get(GraphConstants.SHAPE))) {
                ComponentsInfo componentsInfo = JSONUtil.toBean(JSONUtil.toJsonStr(cell), ComponentsInfo.class);
                componentsInfoList.add(componentsInfo);
            }
        });
        return componentsInfoList;
    }

    private JSONObject buildJobDslParam(List<EdgeInfo> edgeInfoList, List<ComponentsInfo> componentsInfoList) {
        JSONObject dsl = new JSONObject();
        JSONObject components = new JSONObject();
        componentsInfoList.forEach(componentsInfo -> {
            List<String> inputData = Lists.newArrayList();
            List<String> outputdata = Lists.newArrayList(JobConstants.DSL_DATA);
            List<String> modelData = Lists.newArrayList("model");
            JSONObject component = new JSONObject();
            if (JobConstants.READER.equals(componentsInfo.getData().getNodeId())) {
//                component.set(JobConstants.NEED_DEYLOY, Boolean.TRUE);
//                String reader = ResourceUtil.readUtf8Str("data/dslReader.json");
//                components.set(componentsInfo.getDslNodeId(), JSONUtil.parseObj(reader));
//                inputData.add(componentsInfo.getDslNodeId() + "." + JobConstants.DSL_DATA);
            } else {
                List<EdgeInfo> edgeInfos = edgeInfoList.stream().filter(edgeInfo -> edgeInfo.getTarget().cell.equals(componentsInfo.getId())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(edgeInfos)) {
                    ComponentsInfo matchedComponent = findPreComponent(componentsInfoList, edgeInfos.get(0));
                    if (matchedComponent != null) {
                        inputData.add(matchedComponent.getDslNodeId() + "." + JobConstants.DSL_DATA);
                    }
                }
                Input input = Input.builder().data(Input.InputData.builder().data(inputData).build()).build();
                component.set(JobConstants.DSL_INPUT, input);
            }
            OutPut outPut;
            if (NodeConstants.ContainsModelNodeList.contains(componentsInfo.getData().getNodeId())) {
                outPut = OutPut.builder().data(outputdata).model(modelData).build();
            } else {
                outPut = OutPut.builder().data(outputdata).build();
            }
            component.set(JobConstants.DSL_MODULE, componentsInfo.getData().getNodeModule());
            component.set(JobConstants.DSL_OUTPUT, outPut);
            components.set(componentsInfo.getDslNodeId(), component);
        });
        dsl.set(JobConstants.JOB_COMPONENTS, components);
        log.info("dsl = {}", dsl);
        return dsl;
    }

    private ComponentsInfo findPreComponent(List<ComponentsInfo> componentsInfoList, EdgeInfo edgeInfo) {
        List<ComponentsInfo> componentsInfos = componentsInfoList.stream().filter(componentsInfo -> componentsInfo.getId().equals(edgeInfo.getSource().cell)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(componentsInfos)) {
            return componentsInfos.get(0);
        }
        return null;
    }

    private JSONObject buildJobConfParam(List<EdgeInfo> edgeInfoList, List<ComponentsInfo> componentsInfoList, String projectUuid, String taskUuid) {
        JSONObject runtime_conf = new JSONObject();
        runtime_conf.set(JobConstants.DSL_VERSION, 2);
        RunConfRole runConfRole = new RunConfRole();
        List<Integer> guest = site();
        List<Integer> participantsSite = participantsSite(projectUuid);
        participantsSite.removeAll(guest);
        runConfRole.setHost(participantsSite);
        runConfRole.setGuest(site());
        runConfRole.setArbiter(arbiter(guest, participantsSite, componentsInfoList));
        runtime_conf.set(JobConstants.CONF_ROLE, runConfRole);
        runtime_conf.set(JobConstants.CONF_INITIATOR, buildInitiator());
//        runtime_conf.set(JobConstants.CONF_JOB_PARAMETERS, buildJobParameters());
        runtime_conf.set(JobConstants.CONF_COMPONENT_PARAMETERS, buildComponentParamterCommom(projectUuid, taskUuid));
        log.info("conf = {}", runtime_conf);
        return runtime_conf;
    }

    private List<Integer> site() {
        try {
            return Lists.newArrayList(siteService.querySite().getPartyId());
        } catch (Exception e) {
            log.error("【run graph】query site error when buildJobConfParam in run graph", e);
            throw new RuntimeException();
        }
    }

    private List<Integer> participantsSite(String projectUuid) {
        try {
            List<ProjectParticipantsDto> projectParticipantsDtoList = projectService.queryProjectParticipantList(projectUuid);
            return projectParticipantsDtoList.stream().map(ProjectParticipantsDto::getSitePartyId).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("【run graph】query participantsSite error when buildJobConfParam in run graph", e);
            throw new RuntimeException(e);
        }
    }

    private JSONObject buildInitiator() {
        JSONObject initiator = new JSONObject();
        try {
            initiator.set(JobConstants.CONF_ROLE, JobConstants.GUEST);
            initiator.set(JobConstants.CONF_INITIATOR_PARTY_ID, siteService.querySite().getPartyId());
            return initiator;
        } catch (Exception e) {
            log.error("【run graph】query current site error when buildInitiator in run graph", e);
            throw new RuntimeException(e);
        }
    }

    private JSONObject buildJobParameters() {
        JSONObject jobParameters = new JSONObject();
        String jobParameter = ResourceUtil.readUtf8Str("data/jobParameters.json");
        jobParameter = jobParameter.replace("@" + JobConstants.CONF_JOB_PARAMS_AUTO_RETRIES, String.valueOf(autoRetries))
                .replace("@" + JobConstants.CONF_JOB_PARAMS_COMPUTING_PARTITIONS, String.valueOf(computingPartitions))
                .replace("@" + JobConstants.CONF_JOB_PARAMS_TASK_CORES, String.valueOf(taskCores))
                .replace("@" + JobConstants.CONF_JOB_PARAMS_TASK_PARALLELISM, String.valueOf(taskParallelism));
        return JSONUtil.parseObj(jobParameter);
    }

    private JSONObject buildComponentParamterCommom(String projectUuid, String taaskUuid) {
        JSONObject commom = new JSONObject();
        JSONObject componentParameter = new JSONObject();
        JSONObject role = new JSONObject();
        List<ComponentsParamsSettings> componentsParamsSettings = componentsService.queryAlgorithmComponentsParamsSettings(projectUuid, taaskUuid, null);
        componentsParamsSettings.forEach(componentsParamsSettings1 -> buildComponentParamter(componentParameter, role, componentsParamsSettings1, projectUuid));
        commom.set(JobConstants.CONF_COMPONENT_PARAMETERS_COMMON, componentParameter);
        commom.set(JobConstants.CONF_ROLE, role);
        return commom;
    }

    private void buildComponentParamter(JSONObject commom, JSONObject role, ComponentsParamsSettings componentsParamsSettings, String projectUuid) {
        if (JobConstants.READER.equals(componentsParamsSettings.getNodeId())) {
            buildComponentParamterRole(role, componentsParamsSettings, projectUuid);
        } else {
            buildComponentParamterCommon(commom, componentsParamsSettings);
        }
    }

    private void buildComponentParamterCommon(JSONObject commom, ComponentsParamsSettings componentsParamsSettings) {
        JSONObject paramsSettings = JSONUtil.parseObj(componentsParamsSettings.getParamSettings());
        paramsSettings.forEach(params -> forEachJson(params));
        if (JobConstants.one_hot_encoder.equals(componentsParamsSettings.getNodeId())) {
            paramsSettings.set("transform_col_names", ParamsConstants.transform_col_names);
        }
        commom.set(componentsParamsSettings.getDslNodeId(), paramsSettings);
    }

    private Object transType(Object value) {
        String str = JSONUtil.toJsonStr(value);
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if (str.contains(".")) {
            return Double.parseDouble(str);
        } else if (pattern.matcher(str).matches()) {
            return Integer.parseInt(str);
        } else if ("null".equals(str)) {
            return null;
        }
        return value;
    }

    private void forEachJson(Map.Entry o) {
        if (JSONUtil.isTypeJSONObject(JSONUtil.toJsonStr(o.getValue()))) {
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(o.getValue());
            jsonObject.forEach(json -> {
                json.setValue(transType(json.getValue()));
                o.setValue(json);
            });
        } else {
            o.setValue(transType(o.getValue()));
        }
    }

    private void buildComponentParamterRole(JSONObject role, ComponentsParamsSettings componentsParamsSettings, String projectUuid) {
        JSONObject guest = new JSONObject();
        JSONObject host = new JSONObject();
        Integer hostIndex = 0;
        Integer guestIndex = 0;
        try {
            String reader = ResourceUtil.readUtf8Str("data/confReader.json");
            List<JSONObject> paramSettingList = JSONUtil.toList(componentsParamsSettings.getParamSettings(), JSONObject.class);
            SiteVo siteVo = siteService.querySite();
            for (JSONObject paramSetting : paramSettingList) {
                String siteUuid = null;
                String data_uuid = paramSetting.getStr("data_uuid");
                List<ProjectAssociateDataVo> projectDataDtoList = projectService.projectAssociateDataList(projectUuid);
                Optional<ProjectAssociateDataVo> projectData = projectDataDtoList.stream().filter(projectDataDto -> data_uuid.equals(projectDataDto.getDataUuid())).findFirst();
                if (projectData.isPresent()) {
                    reader = reader.replace("@name", projectData.get().getTableName()).replace("@namespace", projectData.get().getTableNamespace());
                    siteUuid = projectData.get().getProvidingSiteUuid();
                }
                if (siteVo.getUuid().equals(siteUuid)) {
                    guest.set(guestIndex.toString(), buildRole(paramSetting, reader, componentsParamsSettings.getDslNodeId(), true));
                } else {
                    host.set(hostIndex.toString(), buildRole(paramSetting, reader, componentsParamsSettings.getDslNodeId(), false));
                    hostIndex++;
                }
            }
            role.set(JobConstants.HOST, host);
            role.set(JobConstants.GUEST, guest);
        } catch (Exception e) {
            log.error("build component paramter role error,peojectUuid = {}", projectUuid, e);
            throw new RuntimeException(e);
        }
    }

    private JSONObject buildRole(JSONObject paramSettings, String readerFromModule, String dslNodeId, boolean isGuest) {
        JSONObject dataTransform = new JSONObject();
        JSONObject reader = new JSONObject();
        JSONObject role = new JSONObject();
        try {
            if (isGuest) {
                dataTransform.set("output_format", "dense");
            }
            boolean withLabel = 0 == paramSettings.getInt("with_label") ? true : false;
            dataTransform.set("with_label", withLabel);
//            dataTransform.set("with_label", Boolean.FALSE);
//            if (StringUtils.isNotBlank(paramSettings.getStr("label_name"))) {
//                dataTransform.set("label_name", paramSettings.getStr("label_name"));
//                dataTransform.set("label_type", paramSettings.getStr("label_type"));
//                dataTransform.set("with_label", Boolean.TRUE);
//            }
            reader = JSONUtil.parseObj(readerFromModule);
            String index = dslNodeId.split("_")[1];
            role.set("data_transform_" + index, dataTransform);
            role.set(dslNodeId, reader);
            return role;
        } catch (Exception e) {
            log.error("【run graph】 build runtime_conf role error", e);
            throw new RuntimeException(e);
        }
    }

    private void replace(String readerFromModule, ProjectDataDto projectDataDto) {
        readerFromModule = readerFromModule.replace("@name", projectDataDto.getTableName()).replace("@namespace", projectDataDto.getTableNamespace());
    }

    private boolean checkComponentsSettings(String projectUuid, String taskUuid, int componentsNums) {
        List<ComponentsParamsSettings> componentsParamsSettings = componentsService.queryAlgorithmComponentsParamsSettings(projectUuid, taskUuid, null);
        Optional<ComponentsParamsSettings> optionalComponentsParamsSettings = componentsParamsSettings.stream().filter(cps -> StringUtils.isBlank(cps.getParamSettings())).findFirst();
        if (componentsParamsSettings.size() != componentsNums || componentsParamsSettings.size() == 0 || optionalComponentsParamsSettings.isPresent()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void updateJob(JobDto jobDto) {
        jobService.updateJob(jobDto);
    }

    private List filterSettingsNodes(List<ComponentsInfo> componentsInfoList) {
        List<ComponentsDto> componentsDtoList = componentsService.queryAlgorithmComponents();
        List<String> nodeList = componentsDtoList.stream().filter(componentsDto -> componentsDto.getNeedSettings() == 0).map(ComponentsDto::getNodeId).collect(Collectors.toList());
        return componentsInfoList.stream().filter(componentsInfo -> nodeList.contains(componentsInfo.getData().getNodeId())).collect(Collectors.toList());
    }

    private List<Integer> arbiter(List<Integer> guest, List<Integer> host, List<ComponentsInfo> componentsInfoList) {
        List<Integer> arbiter = Lists.newArrayList();
        Random random = new Random();
        int index = random.nextInt(host.size());
        List list = componentsInfoList.stream().filter(componentsInfo -> NodeConstants.arbiter_must_be_host_node.contains(componentsInfo.getData().getNodeId())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(list)) {
            arbiter.add(index == host.size() ? host.get(index - 1) : host.get(index));
        } else {
            arbiter.addAll(guest);
        }
        return arbiter;
    }
}
