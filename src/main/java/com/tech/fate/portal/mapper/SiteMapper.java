package com.tech.fate.portal.mapper;

import com.tech.fate.portal.dto.SiteDto;

import java.sql.SQLException;

/**
 * @author Iwi
 * @date 2022.7.15
 */
public interface SiteMapper {
    /**
     * 新增站点
     * @param siteDto
     * @return int
     * @throws SQLException
     */
    int updateSite(SiteDto siteDto) throws SQLException;
    int addSite(SiteDto siteDto)throws SQLException;
    /**
     * 查找站点配置
     * @return SiteDto
     * @throws SQLException
     */
    SiteDto querySite( ) throws SQLException;

}
