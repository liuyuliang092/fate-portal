package com.tech.fate.portal.mapper;

import com.tech.fate.portal.dto.SiteDto;

import java.sql.SQLException;

/**
 * @author Iwi
 * @date 2022.7.15
 */
public interface SiteMapper {

    int updateSite(SiteDto siteDto) throws SQLException;
    int addSite(SiteDto siteDto)throws SQLException;
    SiteDto querySite( ) throws SQLException;

}
