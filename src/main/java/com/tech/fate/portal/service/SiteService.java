package com.tech.fate.portal.service;


import com.tech.fate.portal.common.ApiResponse;
import com.tech.fate.portal.vo.SiteConnectInfo;
import com.tech.fate.portal.vo.SiteVo;

import java.io.IOException;
import java.sql.SQLException;


/**
 * @author Iwi
 * @date 2022.7.15
 */
public interface SiteService {
    /**
     * 无站点则新增站点,否则为更新站点
     *
     * @param siteVo
     * @return int
     * @throws SQLException
     */
    int updateSite(SiteVo siteVo) throws Exception;

    /**
     * 在本地查找所有站点
     *
     * @return SiteDto
     * @throws SQLException
     */
    SiteVo querySite() throws Exception;

    ApiResponse registerToFml(SiteConnectInfo siteConnectInfo) throws Exception;

    String getFmlAddr() throws Exception;

    String getFateFlowAddr();

    ApiResponse checkFateFlowHealth() throws Exception;
}
