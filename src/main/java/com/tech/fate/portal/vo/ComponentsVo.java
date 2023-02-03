package com.tech.fate.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentsVo {

    private String id;

    private String groupId;

    private String groupName;

    private int nodeNum;

    private List<ComponentsBaseInfo> children;
}


