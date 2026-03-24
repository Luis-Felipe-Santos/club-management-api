package dev.clube_api.shared.storage.supabase;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SupabaseStorageService {

    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate;

    public SupabaseStorageService(SupabaseProperties supabaseProperties, RestTemplate restTemplate) {
        this.supabaseProperties = supabaseProperties;
        this.restTemplate = restTemplate;
    }

    public void uploadArquivoSocio(byte[] arquivo, String path, String contentType) {
        uploadArquivoPorBucket(arquivo, path, contentType, supabaseProperties.bucketSocios());
    }

    public void uploadArquivoDependente(byte[] arquivo, String path, String contentType) {
        uploadArquivoPorBucket(arquivo, path, contentType, supabaseProperties.bucketDependentes());
    }

    public String gerarSignedUrlSocio(String path, int expiresIn) {
        return gerarSignedUrlPorBucket(path, expiresIn, supabaseProperties.bucketSocios());
    }

    public String gerarSignedUrlDependente(String path, int expiresIn) {
        return gerarSignedUrlPorBucket(path, expiresIn, supabaseProperties.bucketDependentes());
    }

    private void uploadArquivoPorBucket(byte[] arquivo, String path, String contentType, String bucket) {
        String uploadUrl = supabaseProperties.url()
                + "/storage/v1/object/"
                + bucket
                + "/"
                + path;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(supabaseProperties.serviceRoleKey());
        headers.set("apikey", supabaseProperties.serviceRoleKey());
        headers.setContentType(MediaType.parseMediaType(
                contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE
        ));

        HttpEntity<byte[]> entity = new HttpEntity<>(arquivo, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                uploadUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao enviar arquivo para o Supabase.");
        }
    }

    private String gerarSignedUrlPorBucket(String path, int expiresIn, String bucket) {
        String url = supabaseProperties.url()
                + "/storage/v1/object/sign/"
                + bucket
                + "/"
                + path;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(supabaseProperties.serviceRoleKey());
        headers.set("apikey", supabaseProperties.serviceRoleKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(
                Map.of("expiresIn", expiresIn),
                headers
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Erro ao gerar signed URL.");
        }

        Object signedPath = response.getBody().get("signedURL");
        if (signedPath == null) {
            signedPath = response.getBody().get("signedUrl");
        }

        if (signedPath == null) {
            throw new RuntimeException("Resposta inválida ao gerar signed URL.");
        }

        return supabaseProperties.url() + "/storage/v1" + signedPath;
    }
}