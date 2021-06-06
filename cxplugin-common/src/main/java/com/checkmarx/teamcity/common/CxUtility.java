package com.checkmarx.teamcity.common;


import org.apache.commons.lang3.StringUtils;

import jetbrains.buildServer.serverSide.crypt.EncryptUtil;

public class CxUtility {

	/**
	 * Encrypts the password if not encrypted
	 * @param password
	 * @return
	 */
	public static String encrypt(String password) {
    	if(!EncryptUtil.isScrambled(password)) {
            try {
                password = EncryptUtil.scramble(password);
            } catch (RuntimeException e) {
                password = "";
            }
        }
        return password;
    }
	
	/**
	 * Decrypts the password if encrypted
	 * @param password
	 * @return
	 */
	public static String decrypt(String password) {
        String encStr;
        if(StringUtils.isNotEmpty(password)) {
        if (EncryptUtil.isScrambled(password)) {
            try {
                encStr = EncryptUtil.unscramble(password);
            } catch (RuntimeException e) {
                encStr = "";
            }
            return encStr;
        } else {
            return password;
        }
        }
        else
        	return "";
    }
}
