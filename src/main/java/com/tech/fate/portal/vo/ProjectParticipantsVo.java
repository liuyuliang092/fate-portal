package com.tech.fate.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author 刘欣怡
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectParticipantsVo {

    private String createTime;

    private String uuid;

    private Integer partyId;

    private boolean isCurrentSite;

    private String name;

    private String description;

    private int status;

    private String role;

}
