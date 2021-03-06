package com.example.Memo.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  private static final String HEADER_AUTH = "Authorization";

  @Bean public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
          .consumes(getConsumeContentTypes()) //Request Content-Type
          .produces(getProduceContentTypes()) //Response Content-Type .pathMapping("/")
          .forCodeGeneration(true)
          .genericModelSubstitutes(ResponseEntity.class)
          .ignoredParameterTypes(java.sql.Date.class)
          .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
          .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
          .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
          .securitySchemes(Lists.newArrayList(apiKey()))
          .securityContexts(Lists.newArrayList(securityContext()))
          .useDefaultResponseMessages(false)
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.example.Memo.web.controller"))
          .paths(PathSelectors.any())
          .build()
          .apiInfo(apiInfo());
  }

  private Set<String> getConsumeContentTypes() {
    Set<String> consumes = new HashSet<>();
    consumes.add("application/json;charset=UTF-8");
    return consumes;
  }

  private Set<String> getProduceContentTypes() {
    Set<String> produces = new HashSet<>();
    produces.add("application/json;charset=UTF-8");
    return produces;
  }


  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title("Memo Server API")
            .description("간단한 게시판 서버입니다.")
            .version("1.0.2")
            .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("JWT", HEADER_AUTH, "header");
  }

  private SecurityContext securityContext() {
    return springfox.documentation.spi.service.contexts.SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.any())
            .build();
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
  }
}
