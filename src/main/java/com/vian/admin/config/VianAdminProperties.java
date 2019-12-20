package com.vian.admin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin", ignoreUnknownFields = false)
@Getter
public class VianAdminProperties {

}
