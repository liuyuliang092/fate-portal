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
package com.tech.fate.portal.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tech.fate.portal.common.FateFlowResult;
import com.tech.fate.portal.common.status.JobStatus;
import com.tech.fate.portal.constants.CommonConstant;
import com.tech.fate.portal.constants.ComponentStatusConstants;
import com.tech.fate.portal.constants.FateFlowConstants;
import com.tech.fate.portal.constants.FmlManagerConstants;
import com.tech.fate.portal.service.SiteService;
import com.tech.fate.portal.service.node.ComponentsService;
import com.tech.fate.portal.util.DateUtils;
import com.tech.fate.portal.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.dto.*;
import com.tech.fate.portal.mapper.JobMapper;
import com.tech.fate.portal.mapper.ProjectMapper;
import com.tech.fate.portal.service.JobService;
import com.tech.fate.portal.service.LocalDataService;
import com.tech.fate.portal.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static com.tech.fate.portal.common.status.JobFateStatus.FAILED;
import static com.tech.fate.portal.common.status.JobFateStatus.SUCCESS;
import static com.tech.fate.portal.common.status.JobParticipantStatus.JobParticipantAccept;
import static com.tech.fate.portal.common.status.JobParticipantStatus.JobParticipantReject;
import static com.tech.fate.portal.common.status.JobStatus.*;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年07月14日 16:50
 */

@Service
@Slf4j
public class JobServiceImpl implements JobService {

    @Autowired
    JobMapper jobMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ProjectServiceImpl projectService;
    @Autowired
    LocalDataService localDataService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ComponentsService componentsService;

    @Override
    public void createJobByFml(JobRequestFmlDto jobRequestFmlDto) throws Exception {

        JobDto jobDto = new JobDto();
        SiteVo siteDto = siteService.querySite();
        jobDto.setCreatedAt(DateUtils.formatDateTime());
        jobDto.setUpdatedAt(DateUtils.formatDateTime());
        jobDto.setDeletedAt(DateUtils.formatDateTime());
        jobDto.setName(jobRequestFmlDto.getName());
        jobDto.setDescription(jobRequestFmlDto.getDescription());
        jobDto.setUuid(jobRequestFmlDto.getUuid());
        jobDto.setProjectUuid(jobRequestFmlDto.getProjectUuid());
        jobDto.setType(jobRequestFmlDto.getType());
        jobDto.setStatus(NEWJOB.getStatus());
        jobDto.setStatusMessage(NEWJOB.getStatusMessage());
        jobDto.setAlgorithmType(jobRequestFmlDto.getTrainingAlgorithmType());
        jobDto.setAlgorithmComponentName(jobRequestFmlDto.getAlgorithmComponentName());
        jobDto.setAlgorithmConfig(jobRequestFmlDto.getConfJson());
        jobDto.setEvaluateComponentName(jobRequestFmlDto.getEvaluateComponentName());
        jobDto.setModelName(jobRequestFmlDto.getTrainingModelName());
        jobDto.setPredictingModelUuid(jobRequestFmlDto.getPredictingModelUuid());
        assert siteDto != null;
        jobDto.setInitiatingSiteUuid(siteDto.getUuid());
        jobDto.setInitiatingSiteName(siteDto.getName());
        jobDto.setInitiatingSitePartyId(siteDto.getPartyId());
        jobDto.setInitiatingUser(jobRequestFmlDto.getUserName());
        jobDto.setInitiatingSite(false);
        jobDto.setConf(jobRequestFmlDto.getConfJson());
        jobDto.setDsl(jobRequestFmlDto.getDslJson());
        jobDto.setRequestJson(JSONUtil.toJsonStr(jobRequestFmlDto));
        jobMapper.createNewJob(jobDto);
        Page<ProjectDataDto> page = new Page<>();
        List<ProjectDataDto> projectDataDtoList = projectMapper.queryProjectDataDtoList(page, jobRequestFmlDto.getProjectUuid());
        JobRequestDto.RequestData initiatorData = jobRequestFmlDto.getInitiatorData();
        ProjectJobVo projectJobVo = projectService.getJobDetailInfo(jobRequestFmlDto.getProjectUuid(), jobRequestFmlDto.getUuid());
//        role取0为初始站点，取1为其他站点
        JobParticipantDto jobParticipantInit = projectService.createJobParticipantDto(projectJobVo, projectDataDtoList, initiatorData, "0");
        projectMapper.addJobParticipant(jobParticipantInit);
        if (jobRequestFmlDto.getOtherSiteData() != null) {
            List<JobRequestDto.RequestData> otherSiteData = jobRequestFmlDto.getOtherSiteData();
            for (JobRequestDto.RequestData rd : otherSiteData) {
                JobParticipantDto jobParticipantRemote = projectService.createJobParticipantDto(projectJobVo, projectDataDtoList, rd, "1");
                projectMapper.addJobParticipant(jobParticipantRemote);
            }
        }
    }

    @Override
    public ApiResponse approvePendingJob(String jobUuid) throws Exception {
        String fmlUrl = siteService.getFmlAddr();
        String url = fmlUrl + String.format(FmlManagerConstants.FML_JOB_RESPONSE, jobUuid);
        SiteVo siteDto = siteService.querySite();
        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setApproved(true);
        assert siteDto != null;
        jobResponseDto.setSiteUuid(siteDto.getUuid());
        EditJobStatusDto editJobStatusDto = new EditJobStatusDto();
        Timestamp t = new Timestamp(System.currentTimeMillis());
        editJobStatusDto.setStatus(JobWaiting.getStatus());
        editJobStatusDto.setJobUuid(jobUuid);
        editJobStatusDto.setStatusMessage("待运行");
        editJobStatusDto.setUpdatedAt(t);
        jobMapper.editJobStatus(editJobStatusDto);
        String requestParams = JSONUtil.toJsonStr(jobResponseDto);
        String result = HttpUtils.post(url, requestParams);
        ApiResponse apiResponse;
        if (StringUtils.isNotBlank(result)) {
            apiResponse = JSONUtil.toBean(result, ApiResponse.class);
            if (apiResponse.getCode() == 1) {
                apiResponse.setMessage("FML接口执行失败！");
                return apiResponse;
            }
        } else {
            return ApiResponse.fail("调用FML接口失败！");
        }
        apiResponse.setMessage("success");
        return apiResponse;
    }

    @Override
    public ApiResponse handleJobResponse(String jobUuid, JobResponseDto jobResponseDto) throws Exception {
        String fmlUrl = siteService.getFmlAddr();
        if (StringUtils.isBlank(fmlUrl)) {
            return ApiResponse.fail("please register fml manage first");
        }
        String url = fmlUrl + FateFlowConstants.JOB_SUBMIT;
        JobDto jobDto = jobMapper.getJobDetailInfo(jobUuid);
        EditParticipantsStatusDto eps = new EditParticipantsStatusDto();
        EditJobStatusDto ejs = new EditJobStatusDto();
        eps.setJobUuid(jobUuid);
        eps.setSiteUuid(jobResponseDto.getSiteUuid());
        ejs.setJobUuid(jobUuid);
        if (isInitiatingSite(jobDto.getInitiatingSiteUuid())) {
            if (jobResponseDto.isApproved()) {
                eps.setStatus(JobParticipantAccept.getStatus());
                jobMapper.editParticipantStatus(eps);
                jobMapper.editApprovedJob(jobUuid);
                JobDto jobDto1 = jobMapper.getJobDetailInfo(jobUuid);
                if (Objects.equals(jobDto1.getStatusMessage(), JobWaiting.getStatusMessage())) {
//                    调用fateflow.submit()
                    FateJobSubmitRequestDto fateJobSubmitRequestDto = new FateJobSubmitRequestDto();
                    fateJobSubmitRequestDto.setDsl(jobDto.getDsl());
                    fateJobSubmitRequestDto.setRuntimeConf(jobDto.getConf());

                    String requestParams = JSONUtil.toJsonStr(fateJobSubmitRequestDto);
                    String result = HttpUtils.post(url, requestParams);
                    ApiResponse apiResponse;
                    if (StringUtils.isNotBlank(result)) {
                        apiResponse = JSONUtil.toBean(result, ApiResponse.class);
                        if (apiResponse.getCode() == 1) {
                            apiResponse.setMessage("Fate接口执行失败！");
                            return apiResponse;
                        } else {
//                           调用submit成功，修改job status为“运行中”
                            ejs.setStatus(JobRunning.getStatus());
                            ejs.setStatusMessage("任务运行中");
                            jobMapper.editJobStatus(ejs);
                        }
                    } else {
                        return ApiResponse.fail("调用Fate接口失败！");
                    }
                    apiResponse.setMessage("success");
                    return apiResponse;
                }
            } else {
                eps.setStatus(JobParticipantReject.getStatus());
                ejs.setStatus(JobFail.getStatus());
                ejs.setStatusMessage("审批拒绝！");
                jobMapper.editParticipantStatus(eps);
                jobMapper.editJobStatus(ejs);
            }
        }
        return ApiResponse.ok("success");
    }

    @Override
    public JobDetailVo getJobDetailInfo(String jobUuid, String username) throws Exception {
        JobDto jobDto = jobMapper.getJobDetailInfo(jobUuid);
        JobDetailVo jobDetailVo = new JobDetailVo();
        String requestJson = jobDto.getRequestJson();
        JSONObject jobRequestJson = new JSONObject(requestJson);
        //开始拼
        jobDetailVo.setAlgorithmComponentName(jobRequestJson.getStr("algorithm_component_name"));
        jobDetailVo.setConfJson(jobRequestJson.getStr("conf_json"));
        jobDetailVo.setDescription(jobRequestJson.getStr("description"));
        jobDetailVo.setDslJson(jobRequestJson.getStr("dsl_json"));
        jobDetailVo.setEvaluateComponentName(jobRequestJson.getStr("evaluate_component_name"));
        jobDetailVo.setFateJobId(jobDto.getFateJobId());
        jobDetailVo.setFateJobStatus(jobDto.getFateJobStatus());
        jobDetailVo.setFateModelName(jobDto.getFateModelId());
        jobDetailVo.setInitiatingSiteName(jobDto.getInitiatingSiteName());
        jobDetailVo.setInitiatingSitePartyId(jobDto.getInitiatingSitePartyId());
        jobDetailVo.setInitiatingSiteUuid(jobDto.getInitiatingSiteUuid());

        JobRequestDto.RequestData ind = jobRequestJson.get("initiatorData", JobRequestDto.RequestData.class);
        JobDetailVo.RequestData initiatorData = getRequestData(ind, jobDto.getProjectUuid());
        jobDetailVo.setInitiatorData(initiatorData);

        jobDetailVo.setInitiator(jobDto.isInitiatingSite());
        jobDetailVo.setName(jobDto.getName());

        JSONArray osd = jobRequestJson.getJSONArray("other_site_data");
        if (osd != null) {
            List<JSONObject> otherSiteDatas = osd.toList(JSONObject.class);
            List<JobDetailVo.RequestData> otherSiteDataVo = new ArrayList<>();
            for (JSONObject jr : otherSiteDatas) {
                JobRequestDto.RequestData data = new JobRequestDto.RequestData(jr.getStr("dataUuid"), jr.getStr("labelName"));
                JobDetailVo.RequestData otherSiteData = getRequestData(data, jobDto.getProjectUuid());
                otherSiteDataVo.add(otherSiteData);
            }
            jobDetailVo.setOtherSiteData(otherSiteDataVo);
        }

        jobDetailVo.setPendingOnThisSite(projectService.managedByThisSite(jobDto.getInitiatingSiteUuid()) && jobDto.getStatus() == 1);

        jobDetailVo.setPredictingModelUuid(jobDto.getPredictingModelUuid());
        jobDetailVo.setProjectUuid(jobDto.getProjectUuid());
        jobDetailVo.setResultInfo(jobDto.getResultJson());
        jobDetailVo.setStatus(jobDto.getStatus());
        jobDetailVo.setStatusMessage(jobDto.getStatusMessage());
        jobDetailVo.setStatusStr(jobDto.getStatusMessage());
        jobDetailVo.setTrainingAlgorithmType(jobDto.getAlgorithmType());

        JSONArray tcl = jobRequestJson.getJSONArray("training_component_list_to_deploy");
        if (tcl != null) {
            List<String> tcltd = tcl.toList(String.class);
            jobDetailVo.setTrainingComponentListToDeploy(tcltd);
        }

        jobDetailVo.setTrainingModelName(jobDto.getModelName());
        jobDetailVo.setTrainingValidationEnabled((Boolean) jobRequestJson.get("training_validation_enabled"));
        jobDetailVo.setTrainingValidationPercent((Integer) jobRequestJson.get("training_validation_percent"));
        jobDetailVo.setType(jobDto.getType());
        jobDetailVo.setUsername(username);
        jobDetailVo.setUuid(jobDto.getUuid());

        return jobDetailVo;
    }

    @Override
    public ApiResponse rejectPendingJob(String jobUuid) throws Exception {
        String fmlUrl = siteService.getFmlAddr();
        if (StringUtils.isBlank(fmlUrl)) {
            return ApiResponse.fail("please register fml manage first");
        }
        String url = fmlUrl + String.format(FmlManagerConstants.FML_JOB_RESPONSE, jobUuid);
        SiteVo siteDto = siteService.querySite();
        JobResponseDto jobResponseDto = new JobResponseDto();
        jobResponseDto.setApproved(false);
        assert siteDto != null;
        jobResponseDto.setSiteUuid(siteDto.getUuid());

        EditJobStatusDto editJobStatusDto = new EditJobStatusDto();
        Timestamp t = new Timestamp(System.currentTimeMillis());
        editJobStatusDto.setStatus(JobFail.getStatus());
        editJobStatusDto.setJobUuid(jobUuid);
        editJobStatusDto.setStatusMessage("审批拒绝");
        editJobStatusDto.setUpdatedAt(t);
        jobMapper.editJobStatus(editJobStatusDto);

        String requestParams = JSONUtil.toJsonStr(jobResponseDto);
        String result = HttpUtils.post(url, requestParams);
        ApiResponse apiResponse;
        if (StringUtils.isNotBlank(result)) {
            apiResponse = JSONUtil.toBean(result, ApiResponse.class);
            if (apiResponse.getCode() == 1) {
                apiResponse.setMessage("FML接口执行失败！");
                return apiResponse;
            }
        } else {
            return ApiResponse.fail("调用FML接口失败！");
        }
        apiResponse.setMessage("success");
        return apiResponse;
    }

    @Override
    public String getComponents() throws SQLException {
        List<ComponentDto> componentDtoList = jobMapper.getComponents();
        return JSON.toJSONString(componentDtoList);
    }

    @Async
    @Override
    public QueryJobFateResponseDto queryAndUpdateJobStatusByFate(ProjectJobVo pjd, CountDownLatch jobsSize) {
        QueryJobFateResponseDto queryJobFateResponseDto = null;
        try {
            log.info("jobid = {}", StringUtils.isNotBlank(pjd.getFateJobId()));
            log.info("status = {} ,s = {}", pjd.getStatus(),JobRunning.getStatus());
            if (StringUtils.isNotBlank(pjd.getFateJobId()) && pjd.getStatus() == JobRunning.getStatus()) {
                queryJobFateResponseDto = this.queryJobStatusByFate(pjd);
                if (queryJobFateResponseDto != null && queryJobFateResponseDto.getData().size() > 0) {
                    String fateJobSatus = queryJobFateResponseDto.getData().get(0).getFStatus();
                    pjd.setStatus(JobStatus.getStatusByFateStatus(fateJobSatus));
                    JobDto jobDto = new JobDto();
                    jobDto.setStatus(JobStatus.getStatusByFateStatus(fateJobSatus));
                    jobDto.setStatusMessage(JobStatus.getStatusMessageByStatus(jobDto.getStatus()));
                    pjd.setStatusStr(JobStatus.getStatusMessageByStatus(jobDto.getStatus()));
                    jobDto.setUuid(pjd.getUuid());
                    this.updateJob(jobDto);
                }
            }
            pjd.setPendingOnThisSite(projectService.managedByThisSite(pjd.getInitiatingSiteUuid()) && pjd.getStatus() == 1);
        } catch (Exception e) {
            log.error("query job execute result from fate by fate_job_id error,message = ", e);
        } finally {
            jobsSize.countDown();
        }
        return queryJobFateResponseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteJob(JobDto jobDto) {
        jobMapper.deleteJob(jobDto);
        ComponentsParamsSettings componentsParamsSettings = new ComponentsParamsSettings();
        componentsParamsSettings.setTaskUuid(jobDto.getUuid());
        componentsService.deleteAlgorithmComponentsParamsSettings(componentsParamsSettings);
    }

    @Override
    public JobDto queryJob(String jobUuid) throws SQLException {
        return jobMapper.getJobDetailInfo(jobUuid);
    }

    @Async
    @Override
    public void queryComponentStatus(String jobId, Integer partyId, String role, ComponentsInfo componentsInfo, CountDownLatch countDownLatch, List<ComponentsStatus> componentsStatusList) throws Exception {
        ComponentsStatus componentsStatus = new ComponentsStatus();
        componentsStatus.setDslNodeId(componentsInfo.getDslNodeId());
        componentsStatus.setStatus(ComponentStatusConstants.DEFAULT);
        componentsStatus.setId(componentsInfo.getId());
        try {
            String fateFlowUrl = siteService.getFateFlowAddr();
            String jobTaskQueryUrl = fateFlowUrl + FateFlowConstants.JOB_TASK_QUERY;
            JSONObject params = new JSONObject();
            params.set("job_id", jobId);
            params.set("role", role);
            params.set("party_id", partyId);
            params.set("component_name", componentsInfo.getDslNodeId());
            String result = HttpUtils.post(jobTaskQueryUrl, JSONUtil.toJsonStr(params));
            if (StringUtils.isNotBlank(result)) {
                FateFlowResult fateFlowResult = JSONUtil.toBean(result, FateFlowResult.class);
                if (fateFlowResult.getRetcode() == CommonConstant.FATE_FLOW_OK) {
                    Object data = fateFlowResult.getData();
                    JSONArray dataArray = JSONUtil.parseArray(JSONUtil.toJsonStr(data));
                    String status = dataArray.size() > 0 ? ((JSONObject) dataArray.get(0)).getStr("f_status") : ComponentStatusConstants.DEFAULT;
                    if (ComponentStatusConstants.FATE_FLOW_WAITING.equals(status)) {
                        componentsStatus.setStatus(ComponentStatusConstants.DEFAULT);
                    } else {
                        componentsStatus.setStatus(status);
                    }
                    add(componentsStatusList, componentsStatus);
                }
            }
        } catch (Exception e) {
            log.error("query component status error,jobId = {},partyId = {},role = {},component = {}", jobId, partyId, role, componentsInfo);
            throw new Exception(e);
        } finally {
            countDownLatch.countDown();
        }
    }

    @Override
    @Async
    public void queryComponentStatus(ComponentsInfo componentsInfo, CountDownLatch countDownLatch, List<ComponentsStatus> componentsStatusList) {
        ComponentsStatus componentsStatus = new ComponentsStatus();
        componentsStatus.setDslNodeId(componentsInfo.getDslNodeId());
        componentsStatus.setStatus(ComponentStatusConstants.RUNNING);
        componentsStatus.setId(componentsInfo.getId());
        add(componentsStatusList, componentsStatus);
        countDownLatch.countDown();
    }

    @Override
    public void updateJob(JobDto jobDto) {
        jobMapper.updateJob(jobDto);
    }

    @Override
    public QueryJobFateResponseDto queryJobStatusByFate(ProjectJobVo pjd) {
        QueryJobFateResponseDto queryJobFateResponseDto = null;
        String url = siteService.getFateFlowAddr() + FateFlowConstants.JOB_QUERY;
        JSONObject requestParams = new JSONObject();
        requestParams.putOnce(FateFlowConstants.JOB_ID, pjd.getFateJobId());
        String result = HttpUtils.post(url, JSONUtil.toJsonStr(requestParams));
        FateFlowResult fateFlowResult;
        if (StringUtils.isNotBlank(result)) {
            fateFlowResult = JSONUtil.toBean(result, FateFlowResult.class);
            if (fateFlowResult.getRetcode() == CommonConstant.FATE_FLOW_OK) {
                queryJobFateResponseDto = com.alibaba.fastjson.JSONObject.parseObject(result, QueryJobFateResponseDto.class);
            }
        }
        return queryJobFateResponseDto;
    }

    private synchronized void add(List<ComponentsStatus> componentsStatusList, ComponentsStatus componentsStatus) {
        componentsStatusList.add(componentsStatus);
    }

    public boolean isInitiatingSite(String siteUuid) throws Exception {
        SiteVo siteDto = siteService.querySite();
        assert siteDto != null;
        return Objects.equals(siteDto.getUuid(), siteUuid);
    }

    private JobDetailVo.RequestData getRequestData(JobRequestDto.RequestData data, String projectUuid) throws Exception {
        Page<ProjectDataDto> page = new Page<>();
        List<ProjectDataDto> projectDataDtoList = projectMapper.queryProjectDataDtoList(page, projectUuid);

        JobDetailVo.RequestData rd = new JobDetailVo.RequestData();
        String dataUuid = data.getDataUuid();
        String labelName = data.getLabelName();
        ProjectDataDto projectDataDto = new ProjectDataDto();
        for (ProjectDataDto pdd : projectDataDtoList) {
            if (Objects.equals(pdd.getDataUuid(), dataUuid)) {
                projectDataDto = pdd;
                break;
            }
        }
        DetailedInfoOfDataVo detailedInfoOfDataVo = localDataService.getDataRecordsDetailedInfo(dataUuid);

        rd.setDataUuid(dataUuid);
        rd.setDescription(projectDataDto.getDescription());
        rd.setLabelName(labelName);
        rd.setLocal(isInitiatingSite(projectDataDto.getSiteUuid()));
        rd.setName(detailedInfoOfDataVo.getName());
        rd.setProvidingSiteName(projectDataDto.getSiteName());
        rd.setProvidingSitePartyId(projectDataDto.getSitePartyId());
        rd.setProvidingSiteUuid(projectDataDto.getSiteUuid());

        return rd;
    }
}
