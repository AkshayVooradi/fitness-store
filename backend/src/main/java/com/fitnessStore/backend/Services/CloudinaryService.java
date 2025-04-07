package com.fitnessStore.backend.Services;

import com.cloudinary.Cloudinary;
import com.fitnessStore.backend.ExceptionHandling.ResourceNotFoundException;
import com.fitnessStore.backend.Repository.CloudinaryImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;

@Service
public class CloudinaryService implements CloudinaryImageUpload {

    @Autowired
    private Cloudinary cloudinary;

    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
             sc.init(null, new TrustManager[]{new X509TrustManager() {
                 public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                 public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                 public X509Certificate[] getAcceptedIssuers() { return null; }
             }}, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map upload(MultipartFile file) {
        try {
            Map data = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
