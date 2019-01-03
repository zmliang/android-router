package com.general.compiler.util;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Author: zml
 * Date  : 2019/1/3
 **/
public class GlobalEnvironment {
    private static ProcessingEnvironment processingEnv;

    public static void init(ProcessingEnvironment processingEnv){
        GlobalEnvironment.processingEnv = processingEnv;
    }
    public static ProcessingEnvironment getProcessingEnv() {
        return processingEnv;
    }
}
