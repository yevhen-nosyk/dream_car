package ua.nure.nosyk;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.nure.nosyk.courseproject.interceptor.AccountStatusInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AccountStatusInterceptor accountStatusInterceptor;

    public WebMvcConfig(
            AccountStatusInterceptor accountStatusInterceptor) {
        this.accountStatusInterceptor = accountStatusInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountStatusInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**",
                        "/webjars/**",
                        "/favicon.ico",
                        "/error"
                );
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false);
    }
}
