*** Settings ***
Library           com.opcoach.e4tester.robot.E4TesterKeywords

*** Test Cases ***
Test launch application
    com.opcoach.e4tester.robot.E4TesterKeywords.doSomething
    com.opcoach.e4tester.robot.E4TesterKeywords.startApplicationInSeparateThread    org.eclipse.equinox.launcher.Main
    ...    -debug
    ...    -Dosgi.bundles=reference\:file\:org.eclipse.equinox.simpleconfigurator_1.3.400.v20191015-1836.jar@1\:start
org.eclipse.equinox.simpleconfigurator.configUrl=file\:org.eclipse.equinox.simpleconfigurator/bundles.info
    ...    -launcher "C:/Users/jbeaumont/Documents/ALTRAN/git_repository/tas/workspace12/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/eclipse.exe"
    ...    -name Eclipse
    ...    -showsplash 600 
    ...    -product com.opcoach.e4tester.test.eap.product
    ...    -data "C:/Users/jbeaumont/Documents/ALTRAN/git_repository/tas/workspace12/../runtime-E4TesterSampleEAP"
    ...    -configuration "file\\:\\C:/Users/jbeaumont/Documents/ALTRAN/git_repository/tas/workspace12/.metadata/.plugins/org.eclipse.pde.core/E4Tester_ComponentApplication_win32/config.ini"
    ...    -dev "file\\:C\\:/Users/jbeaumont/Documents/ALTRAN/git_repository/tas/workspace12/.metadata/.plugins/org.eclipse.pde.core/E4Tester_ComponentApplication_win32/dev.properties"
    ...    -os win32
    ...    -ws win32
    ...    -arch x86_64
    ...    -nl fr_FR
    ...    -consoleLog
    ...    -console 
    