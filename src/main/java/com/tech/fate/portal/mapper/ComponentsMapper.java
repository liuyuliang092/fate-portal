package com.tech.fate.portal.mapper;

import com.tech.fate.portal.dto.ComponentsDto;
import com.tech.fate.portal.dto.ComponentsParamsDto;
import com.tech.fate.portal.dto.ComponentsParamsSettingsDto;
import com.tech.fate.portal.vo.ComponentsParams;
import com.tech.fate.portal.vo.ComponentsParamsSettings;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Array
 */
public interface ComponentsMapper {

    List<ComponentsDto> queryAlgorithmComponents();

    ComponentsParams queryAlgorithmComponentsParams(ComponentsParamsDto componentsParamsDto);

    List<ComponentsParamsSettings> queryAlgorithmComponentsParamsSettings(ComponentsParamsSettingsDto componentsParamsSettingsDto);

    void saveAlgorithmComponentsParamsSettings(ComponentsParamsSettingsDto componentsParamsSettings);

    void updateAlgorithmComponentsParamsSettings(ComponentsParamsSettings componentsParamsSettings);

    void deleteAlgorithmComponentsParamsSettings(ComponentsParamsSettings componentsParamsSettings);
}
