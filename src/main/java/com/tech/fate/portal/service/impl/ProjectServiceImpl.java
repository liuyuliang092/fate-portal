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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tech.fate.portal.common.FmlManagerResp;
import com.tech.fate.portal.common.status.JobStatus;
import com.tech.fate.portal.constants.CommonConstant;
import com.tech.fate.portal.constants.FmlManagerConstants;
import com.tech.fate.portal.constants.SiteConstants;
import com.tech.fate.portal.exception.FatePortalException;
import com.tech.fate.portal.service.JobService;
import com.tech.fate.portal.service.LocalDataService;
import com.tech.fate.portal.service.SiteService;
import com.tech.fate.portal.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.common.FMLException;
import com.tech.fate.portal.dto.*;
import com.tech.fate.portal.mapper.LocalDataMapper;
import com.tech.fate.portal.mapper.ProjectMapper;
import com.tech.fate.portal.service.ProjectService;
import com.tech.fate.portal.util.HttpUtils;
import com.tech.fate.portal.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.tech.fate.portal.common.status.JobParticipantStatus.JobParticipantAccept;
import static com.tech.fate.portal.common.status.JobParticipantStatus.JobParticipantPending;
import static com.tech.fate.portal.common.status.JobStatus.*;
import static com.tech.fate.portal.common.status.ProjectDataStatus.ProjectDataStatusAssociated;
import static com.tech.fate.portal.common.status.ProjectParticipantStatus.*;
import static com.tech.fate.portal.common.status.ProjectStatus.ProjectStatusManaged;
import static com.tech.fate.portal.common.status.ProjectStatus.ProjectStatusPending;
import static com.tech.fate.portal.common.type.ProjectDataType.ProjectDataTypeUnknown;
import static com.tech.fate.portal.common.type.ProjectType.ProjectTypeLocal;
import static com.tech.fate.portal.common.type.ProjectType.ProjectTypeRemote;
import static com.tech.fate.portal.common.type.SiteRoleType.CurrentSite;
import static com.tech.fate.portal.common.type.SiteRoleType.OtherSite;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年07月06日 15:10
 */

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    LocalDataMapper localDataMapper;
    @Autowired
    private SiteService siteService;
    @Autowired
    private LocalDataService localDataService;
    @Lazy
    @Autowired
    private JobService jobService;

    @Override
    public void newProject(BasicProjectInfoDto basicProjectInfoDto, String userName) throws Exception {
        ProjectDto projectDto = new ProjectDto();

        Timestamp t = new Timestamp(System.currentTimeMillis());
        SiteVo siteDto = siteService.querySite();
//        projectDto.setAutoApprovalEnabled(basicProjectInfoDto.isAutoApprovalEnabled());
        projectDto.setDescription(basicProjectInfoDto.getDescription());
        projectDto.setName(basicProjectInfoDto.getName());
        projectDto.setCreatedAt(t);
        projectDto.setDeletedAt(t);
        projectDto.setUpdatedAt(t);
        String projectUuid = UUID.fastUUID().toString().replaceAll("-", "");
        projectDto.setUuid(projectUuid);
        projectDto.setType(ProjectTypeLocal.getType());
        projectDto.setStatus(ProjectStatusManaged.getStatus());
        if (siteDto != null) {
            projectDto.setManagingSiteUuid(siteDto.getUuid());
            projectDto.setManagingSiteName(siteDto.getName());
            projectDto.setManagingSitePartyId(siteDto.getPartyId());
        }
        projectDto.setManager(userName);
        projectMapper.newProject(projectDto);
        ProjectParticipantsDto participantsDto = new ProjectParticipantsDto();
        participantsDto.setCreatedAt(DateUtil.formatDateTime(new Date()));
        participantsDto.setUpdatedAt(DateUtil.formatDateTime(new Date()));
        participantsDto.setDeletedAt(DateUtil.formatDateTime(new Date()));
        participantsDto.setUuid(UUID.fastUUID().toString().replaceAll("-", ""));
        participantsDto.setProjectUuid(projectUuid);
        if (siteDto != null) {
            participantsDto.setSitePartyId(siteDto.getPartyId());
            participantsDto.setSiteName(siteDto.getName());
            participantsDto.setSiteDescription(siteDto.getDescription());
            participantsDto.setSiteUuid(siteDto.getUuid());
        }
        participantsDto.setStatus(joined.getStatus());
        projectMapper.addParticipant(participantsDto);
    }

    @Override
    public QueryOwnProjectsVo queryProjectList(int pageNo, int pageSize) throws Exception {

        SiteVo siteDto = siteService.querySite();
        QueryOwnProjectsVo queryOwnProjectsVo = new QueryOwnProjectsVo();
        if (siteDto != null) {
            String siteUuid = siteDto.getUuid();
            Page<ClosedProjectsDto> pageClosedList = new Page<ClosedProjectsDto>(pageNo, pageSize);
            queryOwnProjectsVo.setClosedProjectsDtoList(queryClosedProjectsList(pageClosedList, siteUuid));
            Page<InvitedProjectsDto> pageInvitedList = new Page<InvitedProjectsDto>(pageNo, pageSize);
            queryOwnProjectsVo.setInvitedProjectsDtoList(queryInvitedProjectsList(pageInvitedList, siteUuid));
            Page<JoinedProjectsDto> pageJoinedList = new Page<JoinedProjectsDto>(pageNo, pageSize);
            queryOwnProjectsVo.setJoinedProjectsDtoList(queryJoinedProjectsList(pageJoinedList, siteUuid));
            Page<JoinedProjectsDto> pageMyList = new Page<JoinedProjectsDto>(pageNo, pageSize);
            queryOwnProjectsVo.setMyProjectsDtoList(queryMyProjectsList(pageMyList, siteUuid));
            Page<JoinedProjectsDto> pageAllList = new Page<JoinedProjectsDto>(pageNo, pageSize);
            queryOwnProjectsVo.setAllProjectsDtoList(queryAllProjectsPageList(pageAllList, siteUuid));
        }
        return queryOwnProjectsVo;
    }

    @Override
    public List<ProjectParticipantsVo> projectParticipantList(String projectUuid) throws Exception {
        List<ProjectParticipantsDto> projectParticipantsDtoList = queryProjectParticipantList(projectUuid);
        List<ProjectParticipantsVo> projectParticipantsVoList = Lists.newArrayList();
        for (ProjectParticipantsDto ppd : projectParticipantsDtoList) {
            ProjectParticipantsVo projectParticipantsVo = new ProjectParticipantsVo();
            projectParticipantsVo.setCreateTime(ppd.getCreatedAt());
            projectParticipantsVo.setDescription(ppd.getSiteDescription());
            projectParticipantsVo.setPartyId(ppd.getSitePartyId());
            projectParticipantsVo.setStatus(ppd.getStatus());
            projectParticipantsVo.setUuid(ppd.getSiteUuid());
            projectParticipantsVo.setName(ppd.getSiteName());
            projectParticipantsVo.setCurrentSite(managedByThisSite(ppd.getSiteUuid()));
            projectParticipantsVo.setRole(managedByThisSite(ppd.getSiteUuid()) ? SiteConstants.CURRENT_SITE : SiteConstants.PARTICIPANT_SITE);
            projectParticipantsVoList.add(projectParticipantsVo);
        }
        return projectParticipantsVoList;
    }

    @Override
    public List<ProjectParticipantsDto> queryProjectParticipantList(String projectUuid) throws Exception {
        List<ProjectParticipantsDto> projectParticipantsDtoList = Lists.newArrayList();
        String url = siteService.getFmlAddr() + String.format(FmlManagerConstants.FML_PROJECT_PARTICIPANT, projectUuid);
        String result = HttpUtils.get(url);
        if (StringUtils.isNotBlank(result)) {
            FmlManagerResp fmlManagerResp = JSONUtil.toBean(result, FmlManagerResp.class);
            cn.hutool.json.JSONObject data = (cn.hutool.json.JSONObject) fmlManagerResp.getData();
            data.forEach(d -> projectParticipantsDtoList.add(JSONObject.parseObject(d.getValue().toString(), ProjectParticipantsDto.class)));
        }
        return projectParticipantsDtoList;
    }

    @Override
    public ProjectDetailVo getProjectDetailInfo(String uuid) throws Exception {
        ProjectDetailVo projectDetailVo = projectMapper.getProjectDetailInfo(uuid);
        projectDetailVo.setManagedByThisSite(managedByThisSite(projectDetailVo.getManagingSiteUuid()));
        projectDetailVo.setRole(managedByThisSite(projectDetailVo.getManagingSiteUuid()) ? SiteConstants.CURRENT_SITE : SiteConstants.PARTICIPANT_SITE);
        return projectDetailVo;
    }

    @Override
    public Page<ProjectJobVo> getProjectJobList(Page<ProjectJobVo> pageList, String uuid) throws Exception {
        List<ProjectJobVo> projectJobVoList = projectMapper.getProjectJobList(uuid);
        CountDownLatch jobsSize = new CountDownLatch(projectJobVoList.size());
        for (ProjectJobVo pjd : projectJobVoList) {
            jobService.queryAndUpdateJobStatusByFate(pjd, jobsSize);
        }
        jobsSize.await();
        pageList.setRecords(projectJobVoList);
        return pageList;
    }

    @Override
    public IPage<ProjectAssociateDataVo> queryProjectDataList(IPage<ProjectAssociateDataVo> projectAssociateDataVoPage, String projectUuid, String participantUuid) throws Exception {
        projectAssociateDataVoPage = projectMapper.getAssociatedDataListForProject(projectAssociateDataVoPage, projectUuid, participantUuid);

        for (ProjectAssociateDataVo data : projectAssociateDataVoPage.getRecords()) {
            if (localDataMapper.queryLocalDataByUuid(data.getDataUuid()) == null) {
                data.setLocal(false);
            } else {
                data.setLocal(true);
            }
        }
        return projectAssociateDataVoPage;
    }

    @Override
    public void invite(String projectUuid, InvitationInfo invitationInfo) throws Exception {
        assert invitationInfo != null;
        String participantUuid = UUID.fastUUID().toString().replaceAll("-", "");

        StringBuilder url = new StringBuilder();
        String fmlUrl = siteService.getFmlAddr();
        url.append(fmlUrl)
                .append(FmlManagerConstants.FML_PROJECT_INVITATION);
        List<ProjectDataDto> projectDataDtoList = projectMapper.queryProjectDataByUuid(projectUuid);
        InvitationRequestToFML invitationRequestToFML = new InvitationRequestToFML();
        List<InvitationRequestToFML.AssociatedDataBean> associatedDataBeanList = Lists.newArrayList();
        for (ProjectDataDto projectDataDto : projectDataDtoList) {
            InvitationRequestToFML.AssociatedDataBean associatedDataBean = new InvitationRequestToFML.AssociatedDataBean();
            associatedDataBean.setCreationTime(DateUtils.formatDateTimeForGo());
            associatedDataBean.setDataUuid(projectDataDto.getDataUuid());
            associatedDataBean.setDescription(projectDataDto.getDescription());
            associatedDataBean.setName(projectDataDto.getName());
            associatedDataBean.setSiteName(projectDataDto.getSiteName());
            associatedDataBean.setSitePartyId(projectDataDto.getSitePartyId());
            associatedDataBean.setSiteUuid(projectDataDto.getSiteUuid());
            associatedDataBean.setTableName(projectDataDto.getTableName());
            associatedDataBean.setTableNamespace(projectDataDto.getTableNamespace());
            associatedDataBean.setUpdateTime(DateUtils.formatDateTimeForGo());
            associatedDataBeanList.add(associatedDataBean);
        }
        invitationRequestToFML.setAssociatedData(associatedDataBeanList);
        ProjectDto projectDto = projectMapper.queryProjectByUuid(projectUuid);
        if (projectDto == null) {
            throw new NullPointerException();
        }
        invitationRequestToFML.setProjectAutoApprovalEnabled(projectDto.isAutoApprovalEnabled());
        invitationRequestToFML.setProjectCreationTime(DateUtils.formatDateTimeForGo());
        invitationRequestToFML.setProjectDescription(projectDto.getDescription());
        invitationRequestToFML.setProjectManager(projectDto.getManager());
        invitationRequestToFML.setProjectManagingSiteName(projectDto.getManagingSiteName());
        invitationRequestToFML.setProjectManagingSitePartyId(projectDto.getManagingSitePartyId());
        invitationRequestToFML.setProjectManagingSiteUuid(projectDto.getManagingSiteUuid());
        invitationRequestToFML.setProjectName(projectDto.getName());
        invitationRequestToFML.setProjectUuid(projectUuid);
        invitationRequestToFML.setSiteUuid(invitationInfo.getUuid());
        invitationRequestToFML.setSitePartyId(invitationInfo.getPartyId());
        invitationRequestToFML.setUuid(participantUuid);
        String result = HttpUtils.post(url.toString(), JSONObject.toJSONString(invitationRequestToFML));
        if (StringUtils.isNotBlank(result)) {
            ApiResponse apiResponse = JSONUtil.toBean(result, ApiResponse.class);
            if (apiResponse.getCode() == CommonConstant.FATE_FLOW_OK) {
                saveParticipant(projectUuid, invitationInfo, participantUuid);
                return;
            }
        }
        throw new FMLException();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public ApiResponse createNewJob(String uuid, JobRequestDto jobRequestDto, String userName) throws Exception {
//        String url = siteService.getFmlAddr() + FmlManagerConstants.FML_JOB_CREATE;
        JobDto jobDto = new JobDto();
        SiteVo siteDto = siteService.querySite();
//        Page<ProjectDataDto> page = new Page<>();
//        List<ProjectDataDto> projectDataDtoList = projectMapper.queryProjectDataDtoList(page, uuid);
//        JobRequestDto.RequestData initiatorData = jobRequestDto.getInitiatorData();
        jobDto.setCreatedAt(DateUtils.formatDateTime());
        jobDto.setDeletedAt(DateUtils.formatDateTime());
        jobDto.setUpdatedAt(DateUtils.formatDateTime());
        jobDto.setName(jobRequestDto.getName());
        jobDto.setDescription(jobRequestDto.getDescription());
        jobDto.setUuid(UUID.fastUUID().toString().replaceAll("-", ""));
        jobDto.setProjectUuid(uuid);
        jobDto.setType(jobRequestDto.getType());
        jobDto.setStatus(NEWJOB.getStatus());
        jobDto.setStatusMessage(NEWJOB.getStatusMessage());
        jobDto.setAlgorithmType(jobRequestDto.getTrainingAlgorithmType());
        jobDto.setAlgorithmComponentName(jobRequestDto.getAlgorithmComponentName());
        jobDto.setAlgorithmConfig(jobRequestDto.getConfJson());
        jobDto.setEvaluateComponentName(jobRequestDto.getEvaluateComponentName());
        jobDto.setModelName(jobRequestDto.getTrainingModelName());
        jobDto.setPredictingModelUuid(jobRequestDto.getPredictingModelUuid());
        assert siteDto != null;
        jobDto.setInitiatingSiteUuid(siteDto.getUuid());
        jobDto.setInitiatingSiteName(siteDto.getName());
        jobDto.setInitiatingSitePartyId(siteDto.getPartyId());
        jobDto.setInitiatingUser(userName);
        jobDto.setInitiatingSite(true);
        jobDto.setConf(jobRequestDto.getConfJson());
        jobDto.setDsl(jobRequestDto.getDslJson());
        jobDto.setRequestJson(JSON.toJSONString(jobRequestDto));
        projectMapper.createNewJob(jobDto);
//        ProjectJobVo projectJobVo = getJobDetailInfo(uuid, jobDto.getUuid());
//        JobParticipantDto jobParticipantInit = createJobParticipantDto(projectJobVo, projectDataDtoList, initiatorData, CurrentSite.getRole());
//        projectMapper.addJobParticipant(jobParticipantInit);
//        List<JobRequestDto.RequestData> otherSiteData = jobRequestDto.getOtherSiteData();
//        if (otherSiteData != null) {
//            for (JobRequestDto.RequestData rd : otherSiteData) {
//                JobParticipantDto jobParticipantRemote = createJobParticipantDto(projectJobVo, projectDataDtoList, rd, OtherSite.getRole());
//                projectMapper.addJobParticipant(jobParticipantRemote);
//            }
//        }
//        JobCreateFMLDto jobCreateFMLDto = new JobCreateFMLDto();
//        jobCreateFMLDto.setConfJson(jobRequestDto.getConfJson());
//        jobCreateFMLDto.setDslJson(jobRequestDto.getDslJson());
//        jobCreateFMLDto.setDescription(jobRequestDto.getDescription());
//        jobCreateFMLDto.setInitiatorData(jobRequestDto.getInitiatorData());
//        jobCreateFMLDto.setName(jobRequestDto.getName());
//        jobCreateFMLDto.setOtherSiteData(jobRequestDto.getOtherSiteData());
//        jobCreateFMLDto.setPredictingModelUuid(jobRequestDto.getPredictingModelUuid());
//        jobCreateFMLDto.setProjectUuid(jobRequestDto.getProjectUuid());
//        jobCreateFMLDto.setTrainingAlgorithmType(jobRequestDto.getTrainingAlgorithmType());
//        jobCreateFMLDto.setTrainingComponentListToDeploy(jobRequestDto.getTrainingComponentListToDeploy());
//        jobCreateFMLDto.setTrainingModelName(jobRequestDto.getTrainingModelName());
//        jobCreateFMLDto.setTrainingValidationEnabled(jobRequestDto.isTrainingValidationEnabled());
//        jobCreateFMLDto.setTrainingValidationPercent(jobRequestDto.getTrainingValidationPercent());
//        jobCreateFMLDto.setType(jobRequestDto.getType());
//        jobCreateFMLDto.setUsername(userName);
//        jobCreateFMLDto.setUuid(jobDto.getUuid());
//        String requestParams = JSON.toJSONString(jobCreateFMLDto);
//        String result = HttpUtils.post(url, requestParams);
//        ApiResponse apiResponse;
//        if (StringUtils.isNotBlank(result)) {
//            apiResponse = JSONUtil.toBean(result, ApiResponse.class);
//            if (apiResponse.getCode() == 1) {
//                apiResponse.setMessage("FML接口执行失败！");
//                return apiResponse;
//            }
//        } else {
//            return ApiResponse.fail("调用FML接口失败！");
//        }
//        apiResponse.setData(projectJobVo);
//        apiResponse.setMessage("success");
        return ApiResponse.ok("success");
    }

    @Override
    public List<ParticipantVo> queryProjectParticipantListByFML(String uuid) throws Exception {
        String fmlUrl = siteService.getFmlAddr();
        if (StringUtils.isBlank(fmlUrl)) {
            throw new FatePortalException("fml_manage host is invalid");
        }
        String url = fmlUrl + FmlManagerConstants.FML_SITE;
        String result = HttpUtils.get(url);
        List<ParticipantVo> participantVoList = new ArrayList<>();
        List<ProjectParticipantFromFMLDto> projectParticipantFromFMLDtoList = null;
        if (StringUtils.isNotBlank(result)) {
            ApiResponse apiResponse = JSONUtil.toBean(result, ApiResponse.class);
            if (apiResponse.getCode() != 0) {
                throw new FMLException();
            }
            String data = JSONUtil.toJsonStr(apiResponse.getData());
            if (StringUtils.isNotBlank(data)) {
                List<ProjectParticipantsDto> projectParticipantsDtoList = queryProjectParticipantList(uuid);
                List<String> alreadyExistsSiteUuid = projectParticipantsDtoList.stream().map(ProjectParticipantsDto::getSiteUuid).collect(Collectors.toList());
                projectParticipantFromFMLDtoList = JSON.parseArray(data, ProjectParticipantFromFMLDto.class);
                for (ProjectParticipantFromFMLDto projectParticipantFromFMLDto : projectParticipantFromFMLDtoList) {
                    if (alreadyExistsSiteUuid.contains(projectParticipantFromFMLDto.getUuid())) {
                        continue;
                    }
                    ParticipantVo participantVo = new ParticipantVo();
                    participantVo.setCreationAt(projectParticipantFromFMLDto.getCreatedAt());
                    participantVo.setSiteDescription(projectParticipantFromFMLDto.getDescription());
                    participantVo.setSiteName(projectParticipantFromFMLDto.getName());
                    participantVo.setSitePartyId(projectParticipantFromFMLDto.getPartyId());
//                    participantVo.setStatus(ProjectParticipantStatusUnknown.getStatus());
                    participantVo.setSiteUuid(projectParticipantFromFMLDto.getUuid());
                    if (projectParticipantFromFMLDto.getUuid().equals(siteService.querySite().getUuid())) {
                        participantVo.setCurrentSite(true);
                        participantVo.setRole(SiteConstants.CURRENT_SITE);
                    } else {
                        participantVo.setCurrentSite(false);
                        participantVo.setRole(SiteConstants.PARTICIPANT_SITE);
                    }
                    participantVoList.add(participantVo);
                }
            }
        } else {
            throw new FMLException();
        }
        return participantVoList;
    }


    @Override
    public ApiResponse associateDataToProject(String uuid, AssociateDataRequestDto request) throws Exception {
        String fmlUrl = siteService.getFmlAddr();
        if (StringUtils.isBlank(fmlUrl)) {
            throw new FatePortalException("fml_manage host is invalid");
        }
        String url = fmlUrl + String.format(FmlManagerConstants.FML_PROJECT_DATA_ASSOCIATE, uuid);
        ProjectDataDto projectDataDto = new ProjectDataDto();
        LocalDataDto localDataDto = localDataMapper.queryLocalDataByUuid(request.getDataId());
        SiteVo siteDto = siteService.querySite();
        projectDataDto.setCreatedAt(DateUtils.formatDateTime());
        projectDataDto.setUpdatedAt(DateUtils.formatDateTime());
        projectDataDto.setDeletedAt(DateUtils.formatDateTime());
        projectDataDto.setName(localDataDto.getName());
        projectDataDto.setDescription(localDataDto.getDescription());
        projectDataDto.setUuid(UUID.fastUUID().toString().replaceAll("-", ""));
        projectDataDto.setProjectUuid(uuid);
        projectDataDto.setDataUuid(localDataDto.getUuid());
        assert siteDto != null;
        projectDataDto.setSiteUuid(siteDto.getUuid());
        projectDataDto.setSiteName(siteDto.getName());
        projectDataDto.setSitePartyId(siteDto.getPartyId());
//        type  0-unknown;1-local;2-remote
        projectDataDto.setType(ProjectDataTypeUnknown.getType());
//        status：Unknown = 0;Dismissed = 1;Associated = 2
        projectDataDto.setStatus(ProjectDataStatusAssociated.getStatus());
        projectDataDto.setTableName(localDataDto.getTableName());
        projectDataDto.setTableNamespace(localDataDto.getTableNamespace());
        projectDataDto.setCreationTime(localDataDto.getCreatedAt());
        projectDataDto.setUpdateTime(localDataDto.getUpdatedAt());

        AssociateDataFMLDto associateDataFMLDto = new AssociateDataFMLDto(projectDataDto);
        String requestParams = JSONObject.toJSONString(associateDataFMLDto);
        String result = HttpUtils.post(url, requestParams);
        if (StringUtils.isNotBlank(result)) {
            ApiResponse apiResponse = JSONUtil.toBean(result, ApiResponse.class);
            if (apiResponse.getCode() == 0) {
                projectMapper.addProjectData(projectDataDto);
                return ApiResponse.ok("success");
            }
        }
        return ApiResponse.fail("failed");
    }

    @Override
    public ApiResponse joinInvitedProject(String uuid) throws Exception {
        String fmlUrl = siteService.getFmlAddr();
        List<ProjectInvitationDto> projectInvitationDtos = projectMapper.projectInvitationList(uuid);
        if (!CollectionUtils.isEmpty(projectInvitationDtos)) {
            String url = fmlUrl + String.format(FmlManagerConstants.FML_PROJECT_INVITATION_ACCEPT, uuid);
            ProjectParticipantsDto projectParticipantsDto = new ProjectParticipantsDto();
            SiteVo siteDto = siteService.querySite();
//        编辑status
            projectParticipantsDto.setCreatedAt(DateUtil.formatDateTime(new Date()));
            projectParticipantsDto.setDeletedAt(DateUtil.formatDateTime(new Date()));
            projectParticipantsDto.setUpdatedAt(DateUtil.formatDateTime(new Date()));
            projectParticipantsDto.setUuid(UUID.fastUUID().toString().replaceAll("-", ""));
            projectParticipantsDto.setProjectUuid(projectInvitationDtos.get(0).getProjectUuid());

            assert siteDto != null;
            projectParticipantsDto.setSiteUuid(siteDto.getUuid());
            projectParticipantsDto.setSitePartyId(siteDto.getPartyId());
            projectParticipantsDto.setSiteName(siteDto.getName());
            projectParticipantsDto.setSiteDescription(siteDto.getDescription());
            projectParticipantsDto.setStatus(joined.getStatus());
            projectMapper.addProjectParticipant(projectParticipantsDto);
            String result = HttpUtils.post(url, "");
            if (StringUtils.isNotBlank(result)) {
                ApiResponse apiResponse = JSONUtil.toBean(result, ApiResponse.class);
                if (apiResponse.getCode() == 0) {
                    projectMapper.updateInvitation(joined.getStatus(), uuid);
                    return ApiResponse.ok("success");
                }
            }
        }
        return ApiResponse.fail("failed！");
    }

    @Override
    public void invitationAcceptResponseFml(String uuid) throws Exception {
        EditProjectInvitationStatusDto epis = new EditProjectInvitationStatusDto();
        epis.setInvitationUuid(uuid);
        epis.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        epis.setStatus(joined.getStatus());
        projectMapper.editProjectParticipantStatus(epis);
    }

    @Override
    public void invitationRejectResponseFml(String uuid) throws Exception {
        EditProjectInvitationStatusDto epis = new EditProjectInvitationStatusDto();
        epis.setInvitationUuid(uuid);
        epis.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        epis.setStatus(rejected.getStatus());
        projectMapper.editProjectParticipantStatus(epis);
    }

    @Override
    public void associateRemoteData(String uuid, List<RemoteDataRequestDto> data) throws SQLException {

        for (RemoteDataRequestDto rd : data) {
            ProjectDataDto projectDataDto = new ProjectDataDto();
            BeanUtils.copyProperties(rd, projectDataDto);
            projectDataDto.setUuid(UUID.fastUUID().toString().replaceAll("-", ""));
            projectMapper.addProjectData(projectDataDto);
        }

    }

    @Override
    public QueryProjectLocalDataVo getProjectLocalData(String uuid, int pageNo, int pageSize) throws Exception {
        SiteVo siteDto = siteService.querySite();

        Page<ProjectDataDto> page1 = new Page<>(pageNo, pageSize);
        List<ProjectDataDto> projectDataDtoList = projectMapper.queryProjectDataDtoList(page1, uuid);
        List<QueryProjectLocalDataDto> projectLocalDataDtoList = new ArrayList<>();

        for (ProjectDataDto pdo : projectDataDtoList) {
            assert siteDto != null;
            if (Objects.equals(pdo.getSiteUuid(), siteDto.getUuid())) {

                QueryProjectLocalDataDto queryProjectLocalDataDto = new QueryProjectLocalDataDto(pdo);
                projectLocalDataDtoList.add(queryProjectLocalDataDto);
            }
        }

        Page<QueryProjectLocalDataDto> page = new Page<>(pageNo, pageSize);
        page.setRecords(projectLocalDataDtoList);
        return new QueryProjectLocalDataVo(page);
    }

    @Override
    public void processInvitation(ProcessInvitationBean processInvitationBean) throws Exception {
        assert processInvitationBean != null;
        ProjectInvitationDto projectInvitationDto = new ProjectInvitationDto();

        projectInvitationDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        projectInvitationDto.setUuid(processInvitationBean.getUuid());
        projectInvitationDto.setStatus(pending.getStatus());
        projectInvitationDto.setProjectUuid(processInvitationBean.getProjectUuid());
        projectInvitationDto.setSiteUuid(processInvitationBean.getSiteUuid());
        projectMapper.addInvitation(projectInvitationDto);
        if (projectMapper.queryProjectByUuid(processInvitationBean.getUuid()) == null) {
            ProjectDto projectDto = new ProjectDto();

            projectDto.setAutoApprovalEnabled(processInvitationBean.isProjectAutoApprovalEnabled());

            projectDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));


            projectDto.setDescription(processInvitationBean.getProjectDescription());
            projectDto.setManager(processInvitationBean.getProjectManager());
            projectDto.setManagingSiteName(processInvitationBean.getProjectManagingSiteName());
            projectDto.setManagingSitePartyId(processInvitationBean.getProjectManagingSitePartyId());
            projectDto.setManagingSiteUuid(processInvitationBean.getProjectManagingSiteUuid());
            projectDto.setName(processInvitationBean.getProjectName());
            projectDto.setUuid(processInvitationBean.getProjectUuid());
            projectDto.setType(ProjectTypeRemote.getType());
            projectDto.setStatus(ProjectStatusPending.getStatus());
            projectMapper.addProject(projectDto);
        }
    }

    @Override
    public List<ProjectDataDto> queryProjectDataList(String projectUuid) {
        return projectMapper.queryProjectDataByUuid(projectUuid);
    }

    @Override
    public List<ProjectAssociateDataVo> projectAssociateDataList(String projectUuid) throws Exception {
        List<ProjectAssociateDataVo> projectAssociateDataVoList = Lists.newArrayList();
        String url = siteService.getFmlAddr() + String.format(FmlManagerConstants.FML_PROJECT_DATA, projectUuid);
        String result = HttpUtils.get(url);
        if (StringUtils.isNotBlank(result)) {
            FmlManagerResp fmlManagerResp = JSONUtil.toBean(result, FmlManagerResp.class);
            cn.hutool.json.JSONObject data = (cn.hutool.json.JSONObject) fmlManagerResp.getData();
            data.forEach(d -> projectAssociateDataVoList.add(JSONObject.parseObject(d.getValue().toString(), ProjectAssociateDataVo.class)));
        }
        for (ProjectAssociateDataVo data : projectAssociateDataVoList) {
            if (localDataMapper.queryLocalDataByUuid(data.getDataUuid()) == null) {
                data.setLocal(false);
            } else {
                data.setLocal(true);
            }
        }
        return projectAssociateDataVoList;
    }

    @Override
    public List<ProjectParticipantAndDataVo> getProjectParticipantAndDataList(String projectUuid) throws Exception {
        List<ProjectParticipantAndDataVo> projectParticipantAndDataVos = Lists.newArrayList();
        List<ProjectParticipantsVo> projectParticipantsVoList = this.projectParticipantList(projectUuid);
        List<ProjectAssociateDataVo> projectAssociateDataList = this.projectAssociateDataList(projectUuid);
        for (ProjectParticipantsVo projectParticipantsVo : projectParticipantsVoList) {
            ProjectParticipantAndDataVo projectParticipantAndDataVo = new ProjectParticipantAndDataVo();
            projectParticipantAndDataVo.setKey(projectParticipantsVo.getUuid());
            projectParticipantAndDataVo.setName(projectParticipantsVo.getName());
            List<ProjectParticipantAndDataVo.Options> list = Lists.newArrayList();
            List<ProjectAssociateDataVo> projectAssociateDatas = projectAssociateDataList.stream().filter(projectAssociateDataVo -> projectAssociateDataVo.getProvidingSiteUuid().equals(projectParticipantsVo.getUuid())).collect(Collectors.toList());
            for (ProjectAssociateDataVo projectAssociateData : projectAssociateDatas) {
                String[] columns = localDataService.getDataColumn(projectParticipantsVo.getUuid());
                List columnList = columns == null ? Lists.newArrayList() : Lists.newArrayList(columns);
                list.add(ProjectParticipantAndDataVo.Options.builder().value(projectAssociateData.getDataUuid()).label(projectAssociateData.getName()).columnList(columnList).build());
                if (columnList.size() > 0 && columnList.get(0) != null) {
                    projectParticipantAndDataVo.setLabelTypeList(Lists.newArrayList("int", "string", "float"));
                }
            }
            BeanUtils.copyProperties(projectAssociateDatas, list);
            projectParticipantAndDataVo.setDataActionOptionsList(list);
            projectParticipantAndDataVos.add(projectParticipantAndDataVo);
        }
        return projectParticipantAndDataVos;
    }


    public JobParticipantDto createJobParticipantDto(ProjectJobVo projectJobVo, List<ProjectDataDto> projectDataDtoList, JobRequestDto.RequestData requestData, String role) {

        ProjectDataDto projectDataDto = new ProjectDataDto();
        for (ProjectDataDto pdd : projectDataDtoList) {
            if (Objects.equals(pdd.getDataUuid(), requestData.getDataUuid())) {
                projectDataDto = pdd;
                break;
            }
        }

        JobParticipantDto jobParticipantDto = new JobParticipantDto();

        jobParticipantDto.setJobUuid(projectJobVo.getUuid());
        jobParticipantDto.setSiteUuid(projectJobVo.getInitiatingSiteUuid());
        jobParticipantDto.setSiteName(projectJobVo.getInitiatingSiteName());
        jobParticipantDto.setSitePartyId(projectJobVo.getInitiatingSitePartyId());
        jobParticipantDto.setSiteRole(role);
        jobParticipantDto.setDataUuid(projectDataDto.getDataUuid());
        jobParticipantDto.setDataName(projectDataDto.getName());
        jobParticipantDto.setDataDescription(projectDataDto.getDescription());
        jobParticipantDto.setDataTableName(projectDataDto.getTableName());
        jobParticipantDto.setDataTableNamespace(projectDataDto.getTableNamespace());
        jobParticipantDto.setDataLabelName(requestData.getLabelName());
//        status取值：1（待审批）、2（审批通过）、3（审批拒绝）
        if (Objects.equals(role, CurrentSite.getRole())) {
            jobParticipantDto.setStatus(JobParticipantAccept.getStatus());
        } else {
            jobParticipantDto.setStatus(JobParticipantPending.getStatus());
        }

        return jobParticipantDto;
    }

    public Page<ClosedProjectsDto> queryClosedProjectsList(Page<ClosedProjectsDto> page, String siteUuid) throws Exception {
        List<ClosedProjectsDto> closedProjectsList = new ArrayList<ClosedProjectsDto>();

        Page<ProjectsVo> page1 = new Page<>(page.getCurrent(), page.getSize());
        List<ProjectsVo> projectsVoList = projectMapper.queryClosedProjectsList(page1, siteUuid);

        for (ProjectsVo pd : projectsVoList) {
            ClosedProjectsDto closedProjectsDto = new ClosedProjectsDto();

            closedProjectsDto.setStatus(pd.getProjectStatus());
            closedProjectsDto.setCreateAt(pd.getCreateDate());
            closedProjectsDto.setDescription(pd.getProjectDescription());
            closedProjectsDto.setManager(pd.getProjectManager());
            closedProjectsDto.setManagingSiteName(pd.getManagingSiteName());
            closedProjectsDto.setManagingSitePartyId(pd.getManagingSitePartyId());
            closedProjectsDto.setName(pd.getProjectName());
            closedProjectsDto.setUuid(pd.getProjectUuid());
            closedProjectsDto.setManagedByThisSite(managedByThisSite(pd.getManagingSiteUuid()));

            closedProjectsList.add(closedProjectsDto);
        }

        page.setRecords(closedProjectsList);
        return page;
    }

    public Page<InvitedProjectsDto> queryInvitedProjectsList(Page<InvitedProjectsDto> page, String siteUuid) throws Exception {
        List<InvitedProjectsDto> invitedProjectsDtoList = new ArrayList<InvitedProjectsDto>();
        Page<ProjectsVo> page1 = new Page<>(page.getCurrent(), page.getSize());
        List<ProjectsVo> projectsVoList = projectMapper.queryInvitedProjectsList(page1, siteUuid, pending.getStatus());
        for (ProjectsVo pd : projectsVoList) {
            InvitedProjectsDto invitedProjectsDto = new InvitedProjectsDto();
            invitedProjectsDto.setCreateAt(pd.getCreateDate());
            invitedProjectsDto.setDescription(pd.getProjectDescription());
            invitedProjectsDto.setManager(pd.getProjectManager());
            invitedProjectsDto.setManagingSiteName(pd.getManagingSiteName());
            invitedProjectsDto.setManagingSitePartyId(pd.getManagingSitePartyId());
            invitedProjectsDto.setName(pd.getProjectName());
            invitedProjectsDto.setUuid(pd.getProjectUuid());
            invitedProjectsDto.setManagedByThisSite(managedByThisSite(pd.getManagingSiteUuid()));

            invitedProjectsDtoList.add(invitedProjectsDto);
        }
        page.setRecords(invitedProjectsDtoList);
        return page;
    }

    public Page<JoinedProjectsDto> queryJoinedProjectsList(Page<JoinedProjectsDto> pageJoinedList, String siteUuid) throws Exception {
        List<JoinedProjectsDto> allProjectsList = allProjectsList(pageJoinedList, siteUuid);
        List<JoinedProjectsDto> joinedProjectsDtoList = allProjectsList.stream().filter(joinedProjectsDto -> !joinedProjectsDto.isManagedByThisSite()).collect(Collectors.toList());
        pageJoinedList.setRecords(joinedProjectsDtoList);
        return pageJoinedList;
    }

    public Page<JoinedProjectsDto> queryMyProjectsList(Page<JoinedProjectsDto> pageJoinedList, String siteUuid) throws Exception {
        List<JoinedProjectsDto> joinedProjectsDtoList = allProjectsList(pageJoinedList, siteUuid);
        List<JoinedProjectsDto> myProjectsDtoList = joinedProjectsDtoList.stream().filter(JoinedProjectsDto::isManagedByThisSite).collect(Collectors.toList());
        pageJoinedList.setRecords(myProjectsDtoList);
        return pageJoinedList;
    }

    public Page<JoinedProjectsDto> queryAllProjectsPageList(Page<JoinedProjectsDto> pageJoinedList, String siteUuid) throws Exception {
        List<JoinedProjectsDto> joinedProjectsDtoList = allProjectsList(pageJoinedList, siteUuid);
        pageJoinedList.setRecords(joinedProjectsDtoList);
        return pageJoinedList;
    }

    public List<JoinedProjectsDto> allProjectsList(Page<JoinedProjectsDto> pageJoinedList, String siteUuid) throws Exception {
        List<JoinedProjectsDto> allProjectsDtoList = Lists.newArrayList();
        Page<ProjectsVo> page1 = new Page<>(pageJoinedList.getCurrent(), pageJoinedList.getSize());
        List<ProjectsVo> projectsVoList = projectMapper.queryJoinedProjectsList(page1, siteUuid);
        for (ProjectsVo pd : projectsVoList) {
            JoinedProjectsDto joinedProjectsDto = new JoinedProjectsDto();
            joinedProjectsDto.setCreationAt(pd.getCreateDate());
            joinedProjectsDto.setDescription(pd.getProjectDescription());
            joinedProjectsDto.setManager(pd.getProjectManager());
            joinedProjectsDto.setManagingSiteName(pd.getManagingSiteName());
            joinedProjectsDto.setManagingSitePartyId(pd.getManagingSitePartyId());
            joinedProjectsDto.setName(pd.getProjectName());
            joinedProjectsDto.setUuid(pd.getProjectUuid());
            joinedProjectsDto.setManagedByThisSite(managedByThisSite(pd.getManagingSiteUuid()));
            joinedProjectsDto.setProjectParticipantsNum(projectMapper.queryProjectParticipantList(pd.getProjectUuid()).size());
            allProjectsDtoList.add(joinedProjectsDto);
        }
        return allProjectsDtoList;
    }

    public boolean managedByThisSite(String siteUuid) throws Exception {
        SiteVo siteDto = siteService.querySite();
        return siteUuid.equals(siteDto.getUuid());
    }

    public ProjectJobVo getJobDetailInfo(String projectUuid, String jobUuid) throws SQLException {
        GetJobDetailInfoDto getJobDetailInfoDto = new GetJobDetailInfoDto();
        getJobDetailInfoDto.setProjectUuid(projectUuid);
        getJobDetailInfoDto.setJobUuid(jobUuid);
        return projectMapper.getJobDetailInfo(getJobDetailInfoDto);
    }

    public void saveParticipant(String projectUuid, InvitationInfo invitationInfo, String uuid) throws SQLException {
        ProjectParticipantsDto projectParticipantsDto = new ProjectParticipantsDto();
        projectParticipantsDto.setCreatedAt(DateUtil.formatDateTime(new Date()));
        projectParticipantsDto.setUpdatedAt(DateUtil.formatDateTime(new Date()));
        projectParticipantsDto.setDeletedAt(DateUtil.formatDateTime(new Date()));
        projectParticipantsDto.setUuid(uuid);
        projectParticipantsDto.setProjectUuid(projectUuid);
        projectParticipantsDto.setSiteName(invitationInfo.getName());
        projectParticipantsDto.setSiteUuid(invitationInfo.getUuid());
        projectParticipantsDto.setSitePartyId(invitationInfo.getPartyId());
        projectParticipantsDto.setSiteDescription(invitationInfo.getDescription());
        projectParticipantsDto.setStatus(pending.getStatus());
        projectMapper.addParticipant(projectParticipantsDto);
    }
}
