package com.rsm.common.util;

public enum MessageProp {
	
	ERR_INS("ERR_INS"),
    
	ERR_DEL("ERR_DEL"),
	
    ERR_SAVE("ERR_SAVE"),
    
    INFO_INS("INFO_INS"),
    
    INFO_DEL("INFO_DEL"),
    
	INFO_SAVE("INFO_SAVE"),
	
	INFO_OK("INFO_OK"),
	
	WRN_LOGIN_PW("WRN_LOGIN_PW"),
	
	WRN_LOGIN_ID("WRN_LOGIN_ID");
	
    private String msg;
    
    private MessageProp(String p_msg) {
        msg = p_msg;
    }
    
    public String getMsg() {
        return msg;
    }
	
}
