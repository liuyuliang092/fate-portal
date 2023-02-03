package com.tech.fate.portal.mapper;

import com.tech.fate.portal.dto.ComponentDto;
import com.tech.fate.portal.dto.EditJobStatusDto;
import com.tech.fate.portal.dto.EditParticipantsStatusDto;
import com.tech.fate.portal.dto.JobDto;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author: 刘欣怡
 * @date: 2022年07月18日 11:17
 */
public interface JobMapper {

    /**
     *
     * 新建任务
     */
    void createNewJob(JobDto jobDto) throws SQLException;
    /**
     * 查找所有本地数据基本信息
     * @return JobDto
     */
    JobDto getJobDetailInfo(String jobUuid) throws SQLException;

    /**
     * 编辑参与方状态
     */
    void editParticipantStatus(EditParticipantsStatusDto eps) throws SQLException;
    /**
     * 编辑任务状态
     */
    void editJobStatus(EditJobStatusDto editJobStatusDto) throws SQLException;
    /**
     * 判断任务参与方是否全部审批通过，是则修改状态信息
     */
    void editApprovedJob(String jobUuid) throws SQLException;

    /**
     * 查找所有算法组件信息
     * @return List<ComponentDto>
     */
    List<ComponentDto> getComponents() throws SQLException;

    void updateJob(JobDto jobDto);

    void deleteJob(JobDto jobDto);
}
