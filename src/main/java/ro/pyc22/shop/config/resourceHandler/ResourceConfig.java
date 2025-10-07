package ro.pyc22.shop.config.resourceHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Value("${upload.folder}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){

        //TODO to understand
        String normalized = uploadFolder.replace("\\", "/");
        if (!normalized.endsWith("/")) normalized += "/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + normalized) // ex: file:D:/appImages/
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new org.springframework.web.servlet.resource.PathResourceResolver());
    }
}
