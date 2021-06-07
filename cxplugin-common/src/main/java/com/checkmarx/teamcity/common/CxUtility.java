package com.checkmarx.teamcity.common;


import org.apache.commons.lang3.StringUtils;

import jetbrains.buildServer.serverSide.crypt.EncryptUtil;

public class CxUtility {

	/**
	 * Encrypts the password if not encrypted
	 * @param password
	 * @return
	 */
	public static String encrypt(String password) throws RuntimeException{
		String encPassword = "";
    	if(!EncryptUtil.isScrambled(password)) {
    		encPassword = EncryptUtil.scramble(password);
        } else {
        	encPassword = password;
        }
        return encPassword;
    }
	
	/**
	 * Decrypts the password if encrypted
	 * @param password
	 * @return
	 */
	public static String decrypt(String password) throws RuntimeException {
		String encStr = "";
		if (StringUtils.isNotEmpty(password)) {
			if (EncryptUtil.isScrambled(password)) {
				encStr = EncryptUtil.unscramble(password);
			} else {
				encStr =  password;
			}
		}
		return encStr;
	}
}
