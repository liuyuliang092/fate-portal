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

@Mapper
public interface ProjectMapper {
    /**
     * 新建项目
     *
     * @param projectDto;
     * @throws SQLException;
     */
    void newProject(@Param("dto") ProjectDto projectDto) throws SQLException;

    /**
     * 功能：查询已关闭项目
     *
     * @param page;
     * @param siteUuid ;
     * @return List<ProjectsVo>
     * @throws SQLException;
     */
    List<ProjectsVo> queryClosedProjectsList(Page<ProjectsVo> page, @Param("siteUuid") String siteUuid) throws SQLException;

    /**
     * 功能：查询受邀请项目
     *
     * @param page,siteUuid;
     * @return ：List<ProjectsVo>
     * @throws SQLException;
     */
    List<ProjectsVo> queryInvitedProjectsList(Page<ProjectsVo> page, @Param("siteUuid") String siteUuid, @Param("status") Integer status) throws SQLException;

    /**
     * 功能：查询项目参与者列表
     *
     * @param page,siteUuid;
     * @return ：List<ProjectsVo>
     * @throws SQLException;
     */
    Page<ProjectsVo> queryJoinedProjectsList(Page<ProjectsVo> page, @Param("siteUuid") String siteUuid) throws SQLException;

    /**
     * 功能：查询项目参与者列表
     *
     * @param projectUuid;
     * @return ：List<ProjectParticipantsDto>
     * @throws SQLException;
     */
    List<ProjectParticipantsDto> queryProjectParticipantList(String projectUuid) throws SQLException;

    /**
     * 功能：查询项目详情
     *
     * @param uuid;
     * @return ：ProjectDetailVo
     * @throws SQLException;
     */
    ProjectDetailVo getProjectDetailInfo(String uuid) throws SQLException;

    /**
     * 功能：查询项目任务列表
     *
     * @param uuid;
     * @return ：List<ProjectJobVo>
     * @throws SQLException;
     */
    List<ProjectJobVo> getProjectJobList(String uuid) throws SQLException;


    /**
     * 功能：新建任务
     * 参数：项目的uuid，请求报文对象
     * 异常：写入数据库发生异常抛出SQLException
     * 返回值：无
     */
    void createNewJob(JobDto jobDto) throws SQLException;

    /**
     * 功能：查询任务详情
     * 参数：项目的uuid，任务名称
     * 异常：查询数据库发生异常抛出SQLException
     * 返回值：ProjectJobDto
     */
    ProjectJobVo getJobDetailInfo(GetJobDetailInfoDto getJobDetailInfoDto) throws SQLException;

    /**
     * 功能：新增任务参与者
     * 参数：任务参与者数据对象
     * 异常：写入数据库发生异常抛出SQLException
     * 返回值：无
     */
    void addJobParticipant(JobParticipantDto jobParticipantDto) throws SQLException;

    List<ProjectDataDto> queryProjectDataDtoList(Page<ProjectDataDto> page, String uuid) throws SQLException;

    void addProjectParticipant(ProjectParticipantsDto projectParticipantsDto) throws SQLException;

    void editProjectParticipantStatus(EditProjectInvitationStatusDto editProjectInvitationStatusDto) throws SQLException;

    void addProjectData(ProjectDataDto projectDataDto) throws SQLException;

    int addParticipant(ProjectParticipantsDto projectParticipantsDto) throws SQLException;

    IPage<ProjectAssociateDataVo> getAssociatedDataListForProject(IPage<ProjectAssociateDataVo> projectAssociateDataVoPage, @Param("projectUuid") String projectUuid, @Param("participantUuid") String participantUuid) throws SQLException;

    List<ProjectDataDto> queryProjectDataByUuid(String projectUuid);

    ProjectDto queryProjectByUuid(String projectUuid) throws SQLException;

    int addProject(ProjectDto projectDto) throws SQLException;

    int addInvitation(ProjectInvitationDto projectInvitationDto) throws SQLException;

    void updateInvitation(@Param("status") Integer status, @Param("uuid") String uuid);

    List<ProjectInvitationDto> projectInvitationList(@Param("uuid") String uuid);

}
