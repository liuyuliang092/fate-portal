package com.tech.fate.portal.vo;

import com.tech.fate.portal.dto.LocalDataDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailedInfoOfDataVo {

    private String creationAt;

    private String dataId;

    private String description;

    private long featureSize;

    private List<String> featuresArray;

    private String filename;

    private IdMetaInfoBean idMetaInfo;

    private String name;

    private String previewArray;

    private long sampleSize;

    private String tableName;

    private String tableNamespace;

    private int uploadJobStatus;

    public DetailedInfoOfDataVo(LocalDataDto localDataDto) {
        if (localDataDto == null) {
            return;
        }
        setCreationAt(localDataDto.getCreatedAt());
        setDataId(localDataDto.getUuid());
        setDescription(localDataDto.getDescription());
        setFeatureSize(localDataDto.getFeatures().split(",").length);
        setFeaturesArray(Arrays.asList(localDataDto.getFeatures().split(",")));
        setFilename(localDataDto.getLocalFilePath());
        setName(localDataDto.getName());
        setPreviewArray(localDataDto.getPreview());
        setSampleSize(localDataDto.getCount());
        setTableName(localDataDto.getTableName());
        setTableNamespace(localDataDto.getTableNamespace());
        setUploadJobStatus(localDataDto.getJobStatus());
    }

    @NoArgsConstructor
    @Data
    public static class IdMetaInfoBean {

        private int idEncryptionType;

        private int idType;
    }
}
