package com.eps.module.seeder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.seeder")
public class SeederProperties {

    /**
     * Master switch: Enable or disable all seeders
     * Set to false to completely disable seeding
     */
    private boolean enabled = false;
}
