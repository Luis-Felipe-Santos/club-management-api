package dev.clube_api.shared.storage.supabase;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "supabase")
public record SupabaseProperties(
        String url,
        String serviceRoleKey,
        String bucketSocios,
        String bucketDependentes
) {
}