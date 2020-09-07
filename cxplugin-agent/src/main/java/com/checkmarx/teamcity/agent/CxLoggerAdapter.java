package com.checkmarx.teamcity.agent;


import jetbrains.buildServer.agent.BuildProgressLogger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

/**
 * Created by: Dorg.
 * Date: 14/09/2016.
 */
public class CxLoggerAdapter extends MarkerIgnoringBase {

    private BuildProgressLogger buildLogger;

    public CxLoggerAdapter(BuildProgressLogger log) {
        this.name = "Build Logger";
        this.buildLogger = log;
    }

    public boolean isTraceEnabled() {
        return false;
    }

    public void trace(String s) {
        //Empty implementation - currently not in use .
    }

    public void trace(String s, Object o) {
        //Empty implementation - currently not in use .
    }

    public void trace(String s, Object o, Object o1) {
        //Empty implementation - currently not in use .
    }

    public void trace(String s, Object... objects) {
        //Empty implementation - currently not in use .
    }

    public void trace(String s, Throwable throwable) {
        //Empty implementation - currently not in use .
    }

    private void printHelper(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.format(s, objects);
        buildLogger.message(ft.getMessage());
    }

    public boolean isDebugEnabled() {
        return true;
    }

    public void debug(String s) {
        buildLogger.message(s);
    }

    public void debug(String s, Object o) {
        printHelper(s, o);
    }

    public void debug(String s, Object o, Object o1) {
        printHelper(s, o, o1);
    }

    public void debug(String s, Object... objects) {
        printHelper(s, objects);
    }

    public void debug(String s, Throwable throwable) {
        buildLogger.message(s);
    }

    /****************************************************************/


    public boolean isInfoEnabled() {
        return true;
    }

    public void info(String s) {
        buildLogger.message(s);
    }


    public void info(String s, Object o) {
        printHelper(s, o);
    }

    public void info(String s, Object o, Object o1) {
        printHelper(s, o, o1);
    }

    public void info(String s, Object... objects) {
        printHelper(s, objects);
    }

    public void info(String s, Throwable throwable) {
        buildLogger.message(s);
    }


    public boolean isWarnEnabled() {
        return true;
    }

    public void warn(String s) {
        buildLogger.warning(s);
    }

    public void warn(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        buildLogger.warning(ft.getMessage());
    }

    public void warn(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.format(s, objects);
        buildLogger.warning(ft.getMessage());
    }

    public void warn(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        buildLogger.warning(ft.getMessage());
    }

    public void warn(String s, Throwable throwable) {
        buildLogger.warning(s);
    }

    public boolean isErrorEnabled() {
        return true;
    }

    public void error(String s) {
        buildLogger.error(s);
    }

    public void error(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        buildLogger.error(ft.getMessage());
        buildLogger.exception(ft.getThrowable());
    }

    public void error(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        buildLogger.error(ft.getMessage());
        buildLogger.exception(ft.getThrowable());
    }

    public void error(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.format(s, objects);
        buildLogger.error(ft.getMessage());
        buildLogger.exception(ft.getThrowable());
    }

    public void error(String s, Throwable throwable) {
        buildLogger.error(s);
        buildLogger.exception(throwable);

    }
}
