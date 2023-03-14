package com.tech.fate.portal.vo;

import com.tech.fate.portal.dto.LocalDataDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Iwi
 * @date 2022.7.15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalDataVo {

    private String creationTime;

    private String dataId;

    private long featureSize;

    private String name;

    private long sampleSize;

    private int uploadJobStatus;
    public LocalDataVo(LocalDataDto dataDto){
        if(dataDto==null){
            return;
        }
        setCreationTime(dataDto.getCreatedAt());
        setDataId(dataDto.getUuid());
        setFeatureSize(dataDto.getFeatures().split(",").length);
        setName(dataDto.getName());
        setSampleSize(dataDto.getCount());
        setUploadJobStatus(dataDto.getJobStatus());
    }
}
