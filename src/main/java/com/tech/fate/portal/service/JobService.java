package com.tech.fate.portal.service;


import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.dto.JobDto;
import com.tech.fate.portal.dto.JobRequestFmlDto;
import com.tech.fate.portal.dto.JobResponseDto;
import com.tech.fate.portal.dto.QueryJobFateResponseDto;
import com.tech.fate.portal.vo.ComponentsInfo;
import com.tech.fate.portal.vo.ComponentsStatus;
import com.tech.fate.portal.vo.JobDetailVo;
import com.tech.fate.portal.vo.ProjectJobVo;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author: 刘欣怡
 * @date: 2022年07月18日 11:17
 */

public interface JobService {
    /**
     * 新建任务，由fml调用
     *
     * @param jobRequestFmlDto;
     * @throws SQLException;
     */
    void createJobByFml(JobRequestFmlDto jobRequestFmlDto) throws Exception;

    /**
     * 通过任务审批
     *
     * @param jobUuid;
     * @throws SQLException;
     */
    ApiResponse approvePendingJob(String jobUuid) throws Exception;

    /**
     * 接收任务审批的结果回复
     *
     * @param jobUuid,jobResponseDto;
     * @throws SQLException;
     */
    ApiResponse handleJobResponse(String jobUuid, JobResponseDto jobResponseDto) throws Exception;

    /**
     * 获取任务详情信息
     *
     * @param jobUuid,username;
     * @return JobDetailVo
     * @throws SQLException;
     */
    JobDetailVo getJobDetailInfo(String jobUuid, String username) throws Exception;

    /**
     * 拒绝任务审批
     *
     * @param jobUuid;
     * @return ApiResponse;
     * @throws SQLException;
     */
    ApiResponse rejectPendingJob(String jobUuid) throws Exception;

    String getComponents() throws SQLException;

    QueryJobFateResponseDto queryJobStatusByFate(ProjectJobVo pjd);

    void queryComponentStatus(String jobId, Integer partyId, String role, ComponentsInfo componentsInfo, CountDownLatch countDownLatch, List<ComponentsStatus> componentsStatusList) throws Exception;

    void queryComponentStatus(ComponentsInfo componentsInfo, CountDownLatch countDownLatch, List<ComponentsStatus> componentsStatusList);

    void updateJob(JobDto jobDto);

    QueryJobFateResponseDto queryAndUpdateJobStatusByFate(ProjectJobVo pjd, CountDownLatch jobsSize);

    void deleteJob(JobDto jobDto);

    JobDto queryJob(String jobUuid) throws SQLException;
}
