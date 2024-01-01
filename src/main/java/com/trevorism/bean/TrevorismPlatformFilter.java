package com.trevorism.bean;

import com.trevorism.https.SecureHttpClient;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.FilterChain;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Filter("/**")
@Requires(property = "trevorism.platform.enabled", value = "true")
public class TrevorismPlatformFilter implements HttpServerFilter {

    private static final Logger log = LoggerFactory.getLogger(TrevorismPlatformFilter.class.getName());

    @Inject
    private PlatformService platformService;

    @Inject
    private SecureHttpClient secureHttpClient;

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        String baseUrl = platformService.getPlatformUrl();
        if(!baseUrl.startsWith("https://")){
            log.info("Unable to find platform url, skipping filter");
            return chain.proceed(request);
        }
        if(request.getMethod() == HttpMethod.GET){
            String url = baseUrl + request.getPath();
            log.info("Making GET request to ${url}");
            String json = secureHttpClient.get(url);
            MutableHttpResponse<String> response = HttpResponse.ok(json);
            return Publishers.just(response);
        }
        else if(request.getMethod() == HttpMethod.POST){
            String url = baseUrl + request.getPath();
            log.info("Making POST request to ${url}");
            String json = secureHttpClient.post(url, platformService.toJson(request.getBody().get()));
            MutableHttpResponse<String> response = HttpResponse.ok(json);
            return Publishers.just(response);
        }
        else if(request.getMethod() == HttpMethod.PUT){
            String url = baseUrl + request.getPath();
            log.info("Making PUT request to ${url}");
            String json = secureHttpClient.put(url, platformService.toJson(request.getBody()));
            MutableHttpResponse<String> response = HttpResponse.ok(json);
            return Publishers.just(response);
        }
        else if(request.getMethod() == HttpMethod.DELETE){
            String url = baseUrl + request.getPath();
            log.info("Making DELETE request to ${url}");
            String json = secureHttpClient.delete(url);
            MutableHttpResponse<String> response = HttpResponse.ok(json);
            return Publishers.just(response);
        }

        return chain.proceed(request);
    }

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(HttpRequest<?> request, FilterChain chain) {
        if (chain instanceof ServerFilterChain) {
            return doFilter(request, (ServerFilterChain) chain);
        }
        return chain.proceed(request);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
