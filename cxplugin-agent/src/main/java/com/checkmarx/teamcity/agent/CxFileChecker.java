package com.checkmarx.teamcity.agent;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Galn on 28/02/2017.
 */
public abstract class CxFileChecker {

    public static void deleteFile(String folder, String prefix){

        GenericPrefixFilter filter = new GenericPrefixFilter(prefix);
        File dir = new File(folder);

        //list out all the file name with prefix
        String[] list = dir.list(filter);

        if (list==null || list.length == 0) return;

        File fileDelete;

        for (String file : list){
            String temp = folder + File.separator + file;
            fileDelete = new File(temp);
            boolean isDeleted = fileDelete.delete();
        }
    }

    //inner class, generic prefix filter
    public static class GenericPrefixFilter implements FilenameFilter {

        private String prefix;

        public GenericPrefixFilter(String prefix) {
            this.prefix = prefix;
        }

        public boolean accept(File dir, String name) {
            return (name.startsWith(prefix));
        }
    }
}
