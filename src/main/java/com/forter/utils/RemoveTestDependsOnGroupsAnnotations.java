package com.forter.utils;

import com.google.common.base.Strings;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.regex.Pattern.quote;

/**
 * Test runner options: -listener com.forter.RemoveTestDependsOnAnnotations (already in parent pom.xml so no need to add commandline option)
 * VM Options example: -DremoveDepsClasses=VelocityIT
 */
public class RemoveTestDependsOnGroupsAnnotations implements IAnnotationTransformer {

    private final Set<String> removeDepsClasses;

    public RemoveTestDependsOnGroupsAnnotations() {
        String tests = System.getProperty("removeDepsClasses");
        if (!Strings.isNullOrEmpty(tests)) {
            removeDepsClasses = new HashSet(Arrays.asList(tests.split(quote(","))));
        }
        else {
            removeDepsClasses = new HashSet();
        }
    }
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
                          Method testMethod) {

        if (testMethod != null) {
            testClass = testMethod.getDeclaringClass();
        }
        if (testClass != null &&
                (removeDepsClasses.contains(testClass.getSimpleName()) ||
                 removeDepsClasses.contains("*"))) {
            annotation.setDependsOnGroups(null);
        }
    }
}