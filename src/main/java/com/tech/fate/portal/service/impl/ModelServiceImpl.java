package com.tech.fate.portal.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.tech.fate.portal.dto.ModelDto;
import com.tech.fate.portal.dto.SaveModelRequestDto;
import com.tech.fate.portal.mapper.ModelMapper;
import com.tech.fate.portal.mapper.ProjectMapper;
import com.tech.fate.portal.service.ModelService;
import com.tech.fate.portal.vo.ModelDetailInfoVo;
import com.tech.fate.portal.vo.ModelVo;
import com.tech.fate.portal.vo.ProjectDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;




@Service
@Slf4j
public class ModelServiceImpl implements ModelService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public void handleModelCreation(SaveModelRequestDto saveModelRequestDto) throws SQLException {

//      根据组件名查component_algorithm_type
        ProjectDetailVo projectDetailVo = projectMapper.getProjectDetailInfo(saveModelRequestDto.getProjectUuid());

        ModelDto modelDto = new ModelDto(saveModelRequestDto, projectDetailVo.getProjectName());
        modelMapper.handleModelCreation(modelDto);

    }

    @Override
    public IPage<ModelVo> queryModelPage(IPage<ModelVo> modelVoPage)throws Exception{
        IPage<ModelDto> modelDtoPage=new Page<>(modelVoPage.getCurrent(),modelVoPage.getSize());
        modelDtoPage=modelMapper.queryModelPage(modelDtoPage);
        if(modelDtoPage.getRecords()==null){
            return modelVoPage;
        }
        modelVoPage=modelDtoPage.convert(e->{
            ModelVo modelVo=new ModelVo();
            modelVo.setComponentName(e.getComponentName());
            modelVo.setCreateTime(e.getCreatedAt().toString());
            modelVo.setJobName(e.getJobName());
            modelVo.setJobUuid(e.getJobUuid());
            modelVo.setModelId(e.getFateModelId());
            modelVo.setModelVersion(e.getFateModelVersion());
            modelVo.setName(e.getName());
            modelVo.setPartyId(e.getPartyId());
            modelVo.setProjectName(e.getProjectName());
            modelVo.setProjectUuid(e.getProjectUuid());
            modelVo.setRole(e.getRole());
            modelVo.setUuid(e.getUuid());
            return modelVo;
        });
        return modelVoPage;
    }
    @Override
    public IPage<ModelVo> queryModelPageForProject(IPage<ModelVo> modelVoPage,String projectUuid)throws Exception{
        IPage<ModelDto> modelDtoPage=new Page<>(modelVoPage.getCurrent(),modelVoPage.getSize());
        modelDtoPage=modelMapper.queryModelPageForProject(modelDtoPage,projectUuid);
        if(modelDtoPage.getRecords()==null){
            return modelVoPage;
        }
        modelVoPage=modelDtoPage.convert(e->{
            ModelVo modelVo=new ModelVo();
            modelVo.setComponentName(e.getComponentName());
            modelVo.setCreateTime(e.getCreatedAt().toString());
            modelVo.setJobName(e.getJobName());
            modelVo.setJobUuid(e.getJobUuid());
            modelVo.setModelId(e.getFateModelId());
            modelVo.setModelVersion(e.getFateModelVersion());
            modelVo.setName(e.getName());
            modelVo.setPartyId(e.getPartyId());
            modelVo.setProjectName(e.getProjectName());
            modelVo.setProjectUuid(e.getProjectUuid());
            modelVo.setRole(e.getRole());
            modelVo.setUuid(e.getUuid());
            return modelVo;
        });
        return modelVoPage;
    }
    @Override
    public ModelDetailInfoVo getModelDetailInfo(String uuid)throws Exception{
        ModelDto modelDto=modelMapper.getModelByUuid(uuid);
        if(modelDto==null){
            return null;
        }
        ModelDetailInfoVo modelDetailInfoVo=new ModelDetailInfoVo();
        modelDetailInfoVo.setComponentName(modelDto.getComponentName());
        modelDetailInfoVo.setCreateTime(modelDto.getCreatedAt().toString());
        ModelDetailInfoVo.EvaluationBean evaluationBean=new ModelDetailInfoVo.EvaluationBean();
        JSONObject evaluationJson=JSONUtil.parseObj(modelDto.getEvaluation());
        evaluationBean.setAdditionalProp1(evaluationJson.getStr("additionalProp1"));
        evaluationBean.setAdditionalProp2(evaluationJson.getStr("additionalProp2"));
        evaluationBean.setAdditionalProp3(evaluationJson.getStr("additionalProp3"));
        modelDetailInfoVo.setEvaluation(evaluationBean);
        modelDetailInfoVo.setJobName(modelDto.getJobName());
        modelDetailInfoVo.setJobUuid(modelDto.getJobUuid());
        modelDetailInfoVo.setModelId(modelDto.getFateModelId());
        modelDetailInfoVo.setModelVersion(modelDto.getFateModelVersion());
        modelDetailInfoVo.setName(modelDto.getName());
        modelDetailInfoVo.setPartyId(modelDto.getPartyId());
        modelDetailInfoVo.setProjectName(modelDto.getProjectName());
        modelDetailInfoVo.setProjectUuid(modelDto.getProjectUuid());
        modelDetailInfoVo.setRole(modelDto.getRole());
        modelDetailInfoVo.setUuid(modelDto.getUuid());
        return modelDetailInfoVo;
    }
}
