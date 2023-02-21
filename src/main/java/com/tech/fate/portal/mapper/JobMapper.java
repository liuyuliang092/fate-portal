package com.tech.fate.portal.mapper;

import com.tech.fate.portal.dto.ComponentDto;
import com.tech.fate.portal.dto.EditJobStatusDto;
import com.tech.fate.portal.dto.EditParticipantsStatusDto;
import com.tech.fate.portal.dto.JobDto;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Array
 */
public interface JobMapper {

    void createNewJob(JobDto jobDto) throws SQLException;

    JobDto getJobDetailInfo(@Param("jobUuid") String jobUuid) throws SQLException;


    void editParticipantStatus(EditParticipantsStatusDto eps) throws SQLException;

    void editJobStatus(EditJobStatusDto editJobStatusDto) throws SQLException;

    void editApprovedJob(@Param("jobUuid") String jobUuid) throws SQLException;

    List<ComponentDto> getComponents() throws SQLException;

    void updateJob(JobDto jobDto);

    void deleteJob(JobDto jobDto);
}
