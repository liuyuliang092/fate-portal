package com.tech.fate.portal.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Iwi
 * @date 2022.7.15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAssociateDataVo {


    @JSONField(name = "creation_time")
    private String creationTime;

    @JSONField(name = "data_uuid")
    private String dataUuid;

    @JSONField(name = "description")
    private String description;

    private boolean isLocal;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "site_name")
    private String providingSiteName;

    @JSONField(name = "site_party_id")
    private long providingSitePartyId;

    @JSONField(name = "site_uuid")
    private String providingSiteUuid;

    @JSONField(name = "update_time")
    private String updateTime;

    @JSONField(name = "table_name")
    private String tableName;

    @JSONField(name = "table_namespace")
    private String tableNamespace;
}
