package dev.clube_api.config;

import dev.clube_api.shared.storage.supabase.SupabaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SupabaseProperties.class)
public class SupabaseConfig {
}