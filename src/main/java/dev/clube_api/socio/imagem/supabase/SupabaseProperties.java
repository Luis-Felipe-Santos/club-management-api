package dev.clube_api.socio.imagem.supabase;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "supabase")
public record SupabaseProperties(
        String url,
        String serviceRoleKey,
        String bucket
) {
}
