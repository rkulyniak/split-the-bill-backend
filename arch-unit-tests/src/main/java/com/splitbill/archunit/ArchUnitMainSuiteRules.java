package com.splitbill.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class ArchUnitMainSuiteRules {

    @ArchTest
    public final ArchRule layeredArchitectureRule = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")
            .layer("Entity").definedBy("..entity..")
            .layer("Dto").definedBy("..dto..")
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
            .whereLayer("Entity").mayOnlyBeAccessedByLayers("Repository", "Service")
            .whereLayer("Dto").mayOnlyBeAccessedByLayers("Controller", "Service", "Repository")
            .withOptionalLayers(true);

    @ArchTest
    public final ArchRule controllers = classes()
            .that().areMetaAnnotatedWith("org.springframework.stereotype.Controller")
            .should().resideInAPackage("..controller")
            .andShould().haveSimpleNameEndingWith("Controller");

    @ArchTest
    public final ArchRule controllerHandlers = classes()
            .that().areMetaAnnotatedWith("org.springframework.web.bind.annotation.ControllerAdvice")
            .should().haveSimpleNameEndingWith("Handler")
            .andShould().resideInAPackage("..controller.handler..");

    @ArchTest
    public final ArchRule controllerFilters = classes()
            .that().implement("jakarta.servlet.Filter")
            .should().haveSimpleNameEndingWith("Filter")
            .andShould().resideInAPackage("..controller.filter..")
            .andShould().beAnnotatedWith("org.springframework.stereotype.Component");

    @ArchTest
    public final ArchRule controllerInterceptors = classes()
            .that().implement("org.springframework.web.servlet.HandlerInterceptor")
            .should().haveSimpleNameEndingWith("Interceptor")
            .andShould().resideInAnyPackage("..controller.interceptor..")
            .andShould().beAnnotatedWith("org.springframework.stereotype.Component");

    @ArchTest
    public final ArchRule controllerDtoRequest = classes()
            .that().resideInAPackage("..controller.request..").and().areNotNestedClasses()
            .should().haveSimpleNameEndingWith("Request");

    @ArchTest
    public final ArchRule controllerDtoResponse = classes()
            .that().resideInAPackage("..controller.response..").and().areNotNestedClasses()
            .should().haveSimpleNameEndingWith("Response");

    @ArchTest
    public final ArchRule services = classes()
            .that().resideInAPackage("..service")
            .should().beInterfaces();

    @ArchTest
    public final ArchRule servicesImpl = classes()
            .that().areMetaAnnotatedWith("org.springframework.stereotype.Service")
            .should().resideInAPackage("..service.impl..")
            .andShould().implement(JavaClass.Predicates.resideInAPackage("..service"))
            .andShould().bePackagePrivate();

    @ArchTest
    public final ArchRule serviceDto = classes()
            .that().resideInAPackage("..dto..")
            .should().haveSimpleNameEndingWith("Dto");

    @ArchTest
    public final ArchRule configs = classes()
            .that().areMetaAnnotatedWith("org.springframework.context.annotation.Configuration")
            .and().areNotAnnotatedWith("org.springframework.boot.autoconfigure.SpringBootApplication")
            .and().areNotAnnotatedWith("org.springframework.boot.autoconfigure.AutoConfiguration")
            .should().haveSimpleNameEndingWith("Config")
            .andShould().resideInAPackage("..config..");

    @ArchTest
    public final ArchRule repositories = classes()
            .that().areInterfaces().and().areAssignableTo("org.springframework.data.jpa.repository.JpaRepository")
            .should().haveSimpleNameEndingWith("Repository")
            .andShould().resideInAPackage("..repository");

    @ArchTest
    public final ArchRule repositoryImpl = classes()
            .that().areNotInterfaces().and().areMetaAnnotatedWith("org.springframework.stereotype.Repository")
            .should().haveSimpleNameEndingWith("RepositoryImpl")
            .andShould().resideInAPackage("..repository.impl..")
            .andShould().implement(JavaClass.Predicates.resideInAPackage("..repository"))
            .andShould().bePackagePrivate();

    @ArchTest
    public final ArchRule entities = classes()
            .that().areMetaAnnotatedWith("jakarta.persistence.Entity")
            .should().resideInAPackage("..entity..");

    @ArchTest
    public final ArchRule mappers = classes().that().areMetaAnnotatedWith("org.mapstruct.Mapper")
            .should().haveSimpleNameEndingWith("Mapper")
            .andShould().resideInAnyPackage("..controller.mapper..", "..service.mapper..", "..client.mapper..");

    @ArchTest
    public final ArchRule feignClients = classes()
            .that().areAnnotatedWith("org.springframework.cloud.openfeign.FeignClient")
            .should().haveSimpleNameEndingWith("Client")
            .andShould().resideInAPackage("..client");

    @ArchTest
    public final ArchRule clientDtoRequest = classes()
            .that().resideInAPackage("..client.request..").and().areNotNestedClasses()
            .should().haveSimpleNameEndingWith("Request");

    @ArchTest
    public final ArchRule clientDtoResponse = classes()
            .that().resideInAPackage("..client.response..").and().areNotNestedClasses()
            .should().haveSimpleNameEndingWith("Response");

    @ArchTest
    public final ArchRule utils = classes()
            .that().haveSimpleNameEndingWith("Util")
            .should().haveOnlyPrivateConstructors();

    @ArchTest
    public final ArchRule utilsMethods = methods()
            .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("Util")
            .should().beStatic();

    @ArchTest
    public final ArchRule utilsFields = fields()
            .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("Util")
            .should().beStatic()
            .andShould().beFinal();

    @ArchTest
    public final ArchRule exceptions = classes()
            .that().areAssignableTo(Exception.class)
            .should().haveSimpleNameEndingWith("Exception");

    @ArchTest
    public final ArchRule dtoAreImmutable = classes()
            .that().haveSimpleNameEndingWith("Request").or().haveSimpleNameEndingWith("Response").or().haveSimpleNameEndingWith("Dto")
            .should().haveOnlyFinalFields();

    @ArchTest
    public final ArchRule useValueAnnotationForExternalizedProperties = fields()
            .that().areMetaAnnotatedWith("org.springframework.beans.factory.annotation.Value")
            .should().bePrivate()
            .andShould().beFinal();

    @ArchTest
    public final ArchRule transactionalAnnotationOnServiceMethods = methods().that().areAnnotatedWith("org.springframework.transaction.annotation.Transactional")
            .should().beDeclaredInClassesThat().resideInAPackage("..service.impl..");

    @ArchTest
    private final ArchRule noClassesShouldUseJodatime = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

    @ArchTest
    private final ArchRule noClassesShouldUseJavaUtilLogging = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    private final ArchRule noClassesShouldUseFieldInjection = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    private final ArchRule noClassesShouldAccessStandardStreams = GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    private final ArchRule noClassesShouldThrowGenericExceptions = GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
}
