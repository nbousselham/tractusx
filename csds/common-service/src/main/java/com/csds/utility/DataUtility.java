package com.csds.utility;

public interface DataUtility {
	
	static Long getNumber(Object[] fieldVals, int index) {
		if (fieldVals.length > index) {
			Object o = fieldVals[index];
			return o == null ? null : Long.parseLong(o.toString());
		}
		return null;
	}

	static String getString(Object[] fieldVals, int index) {
		if (fieldVals.length > index) {
			Object o = fieldVals[index];
			return o == null ? null : o.toString();
		}
		return null;
	}

}
