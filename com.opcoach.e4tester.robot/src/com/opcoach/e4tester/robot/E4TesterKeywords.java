/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opcoach.e4tester.robot;

import org.robotframework.javalib.library.AnnotationLibrary;

/**
 *
 * 
 */

public class E4TesterKeywords extends AnnotationLibrary {

	/**
     * The list of keyword patterns for the AnnotationLibrary
     */
    public static final String KEYWORD_PATTERN = "com/opcoach/e4tester/robot/keywords/**/*.class";

    /**
     * The scope of this library is global.
     */
    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

    
    /**
     * Grab keywords
     */
    public E4TesterKeywords() {
        super();
        addKeywordPattern(KEYWORD_PATTERN);
    }

}
