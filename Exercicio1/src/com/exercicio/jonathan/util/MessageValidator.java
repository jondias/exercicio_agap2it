package com.exercicio.jonathan.util;

import org.json.JSONObject;

public class MessageValidator {

    public static boolean hasValidCommonFields(JSONObject jsonObject) {

	if (jsonObject != null && jsonObject.get(Constant.MESSAGE_TIME) != null && jsonObject.get(Constant.MESSAGE_ORIGIN) != null
		&& jsonObject.get(Constant.MESSAGE_TIME) != null && jsonObject.get(Constant.MESSAGE_ORIGIN) != null) {
	    return Boolean.TRUE;
	} else {
	    return Boolean.FALSE;
	}

    }

    public static boolean hasValidCallFields(JSONObject jsonObject) {

	if (jsonObject != null && jsonObject.get(Constant.MESSAGE_DURATION) != null && jsonObject.get(Constant.MESSAGE_STATUS_CODE) != null
		&& jsonObject.get(Constant.MESSAGE_STATUS_DESCRIPTION) != null) {
	    return Boolean.TRUE;
	} else {
	    return Boolean.FALSE;
	}

    }

    public static boolean hasValidMessageFields(JSONObject jsonObject) {

	if (jsonObject != null && jsonObject.get(Constant.MESSAGE_CONTENT) != null && jsonObject.get(Constant.MESSAGE_STATUS) != null) {
	    return Boolean.TRUE;
	} else {
	    return Boolean.FALSE;
	}

    }

}
