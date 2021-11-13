package com.test.util;

public class UrlConvertUtil {

    private UrlConvertUtil(){

    }

    public static String getRegPath(String path){
        char[] chars = path.toCharArray();

        int len = chars.length;
        StringBuilder stb = new StringBuilder();
        boolean prex = false;
        for(int i=0 ; i < len; i++){
            if(chars[i] == '*'){
                if(prex){
                    stb.append(".*");
                    prex=false;
                }else if(i+1==len){
                    stb.append("[^/]*");
                }else {
                    prex = true;
                    continue;
                }
            }else {
                if(prex){
                    stb.append("[^/]*");
                    prex = false;
                }
                if(chars[i] == '?'){
                    stb.append(".");
                }else {
                    stb.append(chars[i]);
                }
            }
        }
        return stb.toString();
    }

}
