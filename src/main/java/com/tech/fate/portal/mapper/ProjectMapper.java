package com.tech.fate.portal.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tech.fate.portal.dto.*;
import com.tech.fate.portal.vo.ProjectAssociateDataVo;
import com.tech.fate.portal.vo.ProjectDetailVo;
import com.tech.fate.portal.vo.ProjectJobVo;
import com.tech.fate.portal.vo.ProjectsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Array
 */
@Mapper
public interface ProjectMapper {

    void newProject(@Param("dto") ProjectDto projectDto) throws SQLException;


    List<ProjectsVo> queryClosedProjectsList(Page<ProjectsVo> page, @Param("projectDto")ProjectDto projectDto) throws SQLException;


    List<ProjectsVo> queryInvitedProjectsList(Page<ProjectsVo> page, @Param("siteUuid") String siteUuid, @Param("status") Integer status) throws SQLException;


    Page<ProjectsVo> queryJoinedProjectsList(Page<ProjectsVo> page, @Param("projectDto") ProjectDto projectDto) throws SQLException;


    List<ProjectParticipantsDto> queryProjectParticipantList(@Param("projectUuid") String projectUuid) throws SQLException;

    ProjectDetailVo getProjectDetailInfo(@Param("uuid") String uuid) throws SQLException;

    List<ProjectJobVo> getProjectJobList(@Param("uuid") String uuid) throws SQLException;


    void createNewJob(JobDto jobDto) throws SQLException;

    ProjectJobVo getJobDetailInfo(GetJobDetailInfoDto getJobDetailInfoDto) throws SQLException;

    void addJobParticipant(JobParticipantDto jobParticipantDto) throws SQLException;

    List<ProjectDataDto> queryProjectDataDtoList(Page<ProjectDataDto> page, @Param("uuid") String uuid) throws SQLException;

    void addProjectParticipant(ProjectParticipantsDto projectParticipantsDto) throws SQLException;

    void editProjectParticipantStatus(EditProjectInvitationStatusDto editProjectInvitationStatusDto) throws SQLException;

    void addProjectData(ProjectDataDto projectDataDto) throws SQLException;

    int addParticipant(ProjectParticipantsDto projectParticipantsDto) throws SQLException;

    IPage<ProjectAssociateDataVo> getAssociatedDataListForProject(IPage<ProjectAssociateDataVo> projectAssociateDataVoPage, @Param("projectUuid") String projectUuid, @Param("participantUuid") String participantUuid) throws SQLException;

    List<ProjectDataDto> queryProjectDataByUuid(@Param("projectUuid") String projectUuid);

    ProjectDto queryProjectByUuid(@Param("projectUuid") String projectUuid) throws SQLException;

    int addProject(ProjectDto projectDto) throws SQLException;

    int addInvitation(ProjectInvitationDto projectInvitationDto) throws SQLException;

    void updateInvitation(@Param("status") Integer status, @Param("uuid") String uuid);

    List<ProjectInvitationDto> projectInvitationList(@Param("uuid") String uuid);

}
