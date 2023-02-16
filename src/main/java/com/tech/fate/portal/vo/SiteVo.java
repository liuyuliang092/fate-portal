package com.tech.fate.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


/**
 * @author Iwi
 * @date 2022.7.15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteVo {



    private String createdAt;

    @Nullable
    private DeletedAtBean deletedAt;

    private String description;

    private String externalHost;

    private long externalPort;

    private boolean fateFlowConnected;

    private String fateFlowConnectedAt;

    private long fateFlowGrpcPort;

    private String fateFlowHost;

    private Integer fateFlowHttpPort;

    private boolean fmlManagerConnected;

    private String fmlManagerConnectedAt;

    private String fmlManagerEndpoint;

    private String fmlManagerServerName;

    private boolean https;

    private long id;

    private KubeflowConfigBean kubeflowConfig;

    private boolean kubeflowConnected;

    private String kubeflowConnectedAt;

    private String name;

    private Integer partyId;

    private String updatedAt;

    private String uuid;

    @NoArgsConstructor
    @Data
    public static class DeletedAtBean {

        private String time;

        private boolean valid;
    }

    @NoArgsConstructor
    @Data
    public static class KubeflowConfigBean {

        private String kubeconfig;

        private String minioAccessKey;

        private String minioEndpoint;

        private String minioRegion;

        private String minioSecretKey;

        private boolean minioSslEnabled;
    }
}
