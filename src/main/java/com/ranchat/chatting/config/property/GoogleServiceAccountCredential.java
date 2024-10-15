package com.ranchat.chatting.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "google-apis.service-account-credential")
public record GoogleServiceAccountCredential(
    String projectId,
    String privateKeyId,
    String privateKey,
    String clientEmail,
    String clientId,
    String tokenUri
) {
}
