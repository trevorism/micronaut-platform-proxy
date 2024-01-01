package com.trevorism.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trevorism.data.FastDatastoreRepository;
import com.trevorism.data.Repository;
import com.trevorism.data.model.filtering.FilterBuilder;
import com.trevorism.data.model.filtering.SimpleFilter;
import com.trevorism.https.AppClientSecureHttpClient;
import com.trevorism.https.SecureHttpClient;
import com.trevorism.model.ApiRegistration;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;

import java.util.List;


@jakarta.inject.Singleton
@Requires(property = "trevorism.platform.enabled", value = "true")
public class PlatformService {

    private final Repository<ApiRegistration> apiRegistrationRepository;
    private String platformUrl;
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();

    @Value("${trevorism.platform.serviceName}")
    private String serviceName;

    public PlatformService() {
        SecureHttpClient secureHttpClient = new AppClientSecureHttpClient();
        apiRegistrationRepository = new FastDatastoreRepository<>(ApiRegistration.class, secureHttpClient);
    }

    public String getPlatformUrl() {
        if(platformUrl == null){
            platformUrl = fetchPlatformUrl();
        }
        return platformUrl;
    }

    private String fetchPlatformUrl() {
        List<ApiRegistration> apiList = apiRegistrationRepository.filter(new FilterBuilder().addFilter(
                new SimpleFilter("name", "=", serviceName),
                new SimpleFilter("active","=",true))
                .build());
        if (apiList == null || apiList.isEmpty()){
            platformUrl = "none";
        }
        return apiList.get(0).getBaseUrl();
    }

    public String toJson(Object o) {
        return gson.toJson(o);
    }
}
