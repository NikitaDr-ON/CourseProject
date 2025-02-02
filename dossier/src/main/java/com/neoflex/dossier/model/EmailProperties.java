package com.neoflex.dossier.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("email-properties")
public class EmailProperties {
    private String emailHost;
    private int emailPort;
    private String emailUser;
    private String emailPassword;
    private String emailPropertyForProtocol;
    private String emailProtocol;
    private String emailPropertyForAuth;
    private String isAuthEnable;
    private String emailPropertyForStartTls;
    private String isTlsEnable;
    private String emailPropertyForDebug;
    private String isDebugEnable;

}
