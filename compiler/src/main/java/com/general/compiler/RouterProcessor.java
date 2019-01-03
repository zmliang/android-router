package com.general.compiler;

import com.general.api.RConfig;
import com.general.api.RParam;
import com.general.api.RRouter;
import com.general.api.RUri;
import com.general.compiler.objs.ParamEntry;
import com.google.auto.service.AutoService;

import com.squareup.javapoet.ClassName;
import com.general.compiler.base.BaseAbstractProcessor;
import com.general.compiler.objs.RouterEntry;
import com.general.compiler.objs.UriEntry;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * Author: zml
 * Date  : 2019/1/3 - 16:46
 **/
@AutoService(Processor.class)
public class RouterProcessor extends BaseAbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypesSet = new HashSet<>();
        supportedTypesSet.add(RConfig.class.getCanonicalName());
        supportedTypesSet.add(RRouter.class.getCanonicalName());
        supportedTypesSet.add(RUri.class.getCanonicalName());
        return supportedTypesSet;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        long start = System.currentTimeMillis();
        logger("[process]annotations: " + Arrays.toString(annotations.toArray()));
        try {
            logger("roundEnv.getRootElements(): " + roundEnv.getRootElements());
            RouterEntry routerEntry = new RouterEntry();

            doPointAnnotation(roundEnv.getElementsAnnotatedWith(RConfig.class), routerEntry);

            for (Element e : roundEnv.getElementsAnnotatedWith(RRouter.class)) {
                doRouterAnnotation(e, routerEntry);
            }

            for (Element e : roundEnv.getElementsAnnotatedWith(RUri.class)) {
                doUriAnnotation(e, routerEntry);
            }

            if (!routerEntry.getUriEntries().isEmpty()) {
                try {
                    logger("RouterMapping generate START...");
                    routerEntry.brewJava().writeTo(filer);
                    logger("RouterMapping generate END...routerEntry: " + routerEntry);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Throwable throwable) {
                    logger("RouterMapping generate FAILED...routerEntry: " + routerEntry);
                    loggerE(throwable);
                }
            }

        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw throwable;
            } else {
                loggerE(throwable);
            }
        } finally {
            logger("[process] tasks: " + (System.currentTimeMillis() - start) + "ms");
        }

        return true;
    }

    private void doPointAnnotation(Set<? extends Element> eles, RouterEntry routerEntry) {
        int size;
        if (null == eles || 0 == (size = eles.size())) {
            return;
        }
        if (size != 1) {
            throw new RuntimeException("More than one Router Point Class Annotated @RPoint.");
        }

        Element configEle = eles.iterator().next();

        String mappingPackage = elementUtils.getPackageOf(configEle).toString();
        if (null != mappingPackage && mappingPackage.length() > 0) {
            routerEntry.routerMappingPackage = mappingPackage;
        }
        RConfig rrConfig = configEle.getAnnotation(RConfig.class);
        String mappingClassName = rrConfig.mappingName();
        if (mappingClassName.length() > 0) {
            routerEntry.routerMappingClassName = mappingClassName;
        }
    }

    private void doUriAnnotation(Element ele, RouterEntry routerEntry) {
        Element classEle = getElementOwnerElement(ele);
        if (null != classEle && !RRouter.class.getCanonicalName().equals(classEle.toString())) {
            List<UriEntry> uriEntries = routerEntry.getUriEntries();

            RUri rrUri = ele.getAnnotation(RUri.class);

            uriEntries.add(parseUriEntry(classEle, rrUri));
        }
    }

    private void doRouterAnnotation(Element ele, RouterEntry routerEntry) {
        Element classEle = getElementOwnerElement(ele);
        List<UriEntry> uriEntries = routerEntry.getUriEntries();

        RRouter rRouter = ele.getAnnotation(RRouter.class);

        for (RUri rrUri : rRouter.value()) {
            uriEntries.add(parseUriEntry(classEle, rrUri));
        }

    }

    private UriEntry parseUriEntry(Element classEle, RUri rrUri) {
        UriEntry uriEntry = new UriEntry();
        uriEntry.setRouterTargetClass(classEle);

        String uriStr = rrUri.uri();
        if (!uriStr.startsWith("~")) {
            URI uri = URI.create(uriStr);
            uriEntry.setScheme(uri.getScheme());
            uriEntry.setHost(uri.getHost());
        } else {
            uriEntry.setUriRegular(uriStr.substring(1));
        }

        List<ParamEntry> paramEntries = uriEntry.getParams();

        for (RParam rrParam : rrUri.params()) {
            ParamEntry paramEntry = new ParamEntry();
            paramEntry.setName(rrParam.name());

            TypeMirror paramTypeMirror = getParamTypeMirror(rrParam);

            if (null == paramTypeMirror) {
                paramEntry.setType(ClassName.get(String.class));
            } else {
                paramEntry.setType(ClassName.get(paramTypeMirror));
            }

            paramEntries.add(paramEntry);
        }
        return uriEntry;
    }

    private static TypeMirror getParamTypeMirror(RParam rrParam) {
        try {
            rrParam.type();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }

}
