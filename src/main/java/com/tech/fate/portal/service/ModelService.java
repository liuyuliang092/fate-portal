package com.tech.fate.portal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tech.fate.portal.dto.SaveModelRequestDto;
import com.tech.fate.portal.vo.ModelDetailInfoVo;
import com.tech.fate.portal.vo.ModelVo;

import java.sql.SQLException;

/**
 * @author 86178
 */
public interface ModelService {
    void handleModelCreation(SaveModelRequestDto saveModelRequestDto) throws SQLException;

    IPage<ModelVo> queryModelPage(IPage<ModelVo> modelVoPage)throws Exception;

    IPage<ModelVo> queryModelPageForProject(IPage<ModelVo> modelVoPage,String projectUuid)throws Exception;

    ModelDetailInfoVo getModelDetailInfo(String uuid)throws Exception;
}
