package com.tech.fate.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvitationInfo {

    private String description;

    private String name;

    private Integer partyId;

    private String uuid;
}
