/*
 * @author Constantin Chelban (constantink@saltedge.com)
 * Copyright (c) 2020 Salt Edge.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.saltedge.connector.sdk.config;

import com.saltedge.connector.sdk.tools.KeyTools;
import com.saltedge.connector.sdk.tools.ResourceTools;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;

/**
 * Priora object properties from application.yml
 *
 * Example of application.yml
 * connector:
 *   private_key_name: connector_private_prod.pem
 *   priora:
 *     app_code: spring_connector_example
 *     app_id: xxxxxxxxx
 *     app_secret: xxxxxxxxx
 *     base_url: https://priora.saltedge.com/
 *     public_key_name: priora_public_prod.pem
 */
@Configuration
public class PrioraProperties {

    private PublicKey prioraPublicKey;

    /**
     * Registered Connector code
     * (https://priora.saltedge.com/providers/settings#details)
     */
    @NotBlank
    private String appCode = "not-in-code";

    /**
     * Unique Connector's App ID
     * (https://priora.saltedge.com/providers/settings#details)
     */
    @NotBlank
    private String appId = "not-in-code";

    /**
     * Unique Connector's App Secret
     * (https://priora.saltedge.com/providers/settings#details)
     */
    @NotBlank
    private String appSecret = "not-in-code";

    /**
     * Salt Edge Compliance base url.
     * By default: `https://priora.saltedge.com/`
     */
    @NotBlank
    private String baseUrl = "https://priora.saltedge.com/";

    /**
     * Name of Salt Edge Compliance public key file in PEM format.
     * By default: `priora_public_prod.pem`
     */
    private String publicKeyName = "priora_public_prod.pem";

    /**
     * Salt Edge Compliance public key
     */
    private String publicKey = "not-in-code";

    public URL getPrioraBaseUrl() {
        try {
            return new URL(baseUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PublicKey getPrioraPublicKey() {
        if (prioraPublicKey == null) {
            if (!StringUtils.isEmpty(publicKeyName)) {
                prioraPublicKey = KeyTools.convertPemStringToPublicKey(ResourceTools.readKeyFile(publicKeyName));
            } else {
                prioraPublicKey = KeyTools.convertPemStringToPublicKey(publicKey);
            }
        }
        return prioraPublicKey;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPublicKeyName() {
        return publicKeyName;
    }

    public void setPublicKeyName(String publicKeyName) {
        this.publicKeyName = publicKeyName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }
}
