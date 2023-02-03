package com.tech.fate.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.dto.*;
import com.tech.fate.portal.vo.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @author liuxinyi
 */

public interface ProjectService {
    /**
     * 新建项目
     *
     * @param basicProjectInfoDto;
     * @param userName;
     * @throws SQLException;
     */
    void newProject(BasicProjectInfoDto basicProjectInfoDto, String userName) throws Exception;

    /**
     * 查询项目列表
     *
     * @param pageNo;
     * @param pageSize;
     * @return QueryOwnProjectsDto
     */
    QueryOwnProjectsVo queryProjectList(int pageNo, int pageSize) throws Exception;

    /**
     * 查询项目参与者列表
     *
     * @param page,projectUuid;
     * @return List<ProjectParticipantsDao>
     */
    List<ProjectParticipantsVo> projectParticipantList(String projectUuid) throws Exception;

    List<ProjectParticipantsDto> queryProjectParticipantList(String projectUuid) throws Exception;

    /**
     * 查询项目详情
     *
     * @param uuid;
     * @return ProjectDetailVo
     * @throws SQLException;
     */
    ProjectDetailVo getProjectDetailInfo(String uuid) throws Exception;

    /**
     * 查询项目任务列表
     *
     * @param pageList,uuid;
     * @return List<ProjectJobDto>
     * @throws SQLException;
     */
    Page<ProjectJobVo> getProjectJobList(Page<ProjectJobVo> pageList, String uuid) throws Exception;

    /**
     * 新建任务
     *
     * @param uuid,jobRequestDto,username;
     * @throws SQLException;
     */
    ApiResponse createNewJob(String uuid, JobRequestDto jobRequestDto, String username) throws Exception;

    /**
     * 关联数据到项目
     *
     * @param uuid,request;
     * @throws SQLException;
     */
    ApiResponse associateDataToProject(String uuid, AssociateDataRequestDto request) throws Exception;

    /**
     * 同意邀请，加入受邀请的项目
     *
     * @param uuid;
     * @throws SQLException;
     */
    ApiResponse joinInvitedProject(String uuid) throws Exception;

    /**
     * 接收邀请的回复，由fml调用
     *
     * @param uuid,request;
     * @throws SQLException;
     */
    void invitationAcceptResponseFml(String uuid) throws Exception;

    /**
     * 接收拒绝邀请的回复，由fml调用
     *
     * @param uuid,request;
     * @throws SQLException;
     */
    void invitationRejectResponseFml(String uuid) throws Exception;

    /**
     * 将远端数据关联到项目
     *
     * @param uuid,data;
     * @throws SQLException;
     */
    void associateRemoteData(String uuid, List<RemoteDataRequestDto> data) throws SQLException;

    /**
     * 获取本地数据列表
     *
     * @param uuid,pageNo,pageSize;
     * @return QueryProjectLocalDataVo
     * @throws SQLException;
     */
    QueryProjectLocalDataVo getProjectLocalData(String uuid, int pageNo, int pageSize) throws Exception;

    List<ParticipantVo> queryProjectParticipantListByFML(String uuid) throws Exception;

    IPage<ProjectAssociateDataVo> queryProjectDataList(IPage<ProjectAssociateDataVo> projectAssociateDataVoPage, String projectUuid, String participantUuid) throws Exception;

    void invite(String uuid, InvitationInfo invitationInfo) throws Exception;

    void processInvitation(ProcessInvitationBean processInvitationBean) throws Exception;

    List<ProjectDataDto> queryProjectDataList(String projectUuid);

    List<ProjectAssociateDataVo> projectAssociateDataList(String projectUuid) throws Exception;

    List<ProjectParticipantAndDataVo> getProjectParticipantAndDataList(String projectUuid) throws Exception;
}
