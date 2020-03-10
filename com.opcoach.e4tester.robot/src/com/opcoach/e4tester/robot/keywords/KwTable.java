/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opcoach.e4tester.robot.keywords;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.opcoach.e4tester.core.E4TestCase;

/**
 *
 * 
 */
@RobotKeywords
public class KwTable {

	   
    @RobotKeyword
    public void doSomething()  {
       System.out.println("Bonjour");
       //try {
       //   E4TestCase.globalSetup();
       //} 
       //catch (Exception e) {
    	   // TODO Auto-generated catch block
       //   e.printStackTrace();
       //}
    }

}
