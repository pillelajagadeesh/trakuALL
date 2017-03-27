package com.tresbu.trakeye.domain.enumeration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The ColorCode enumeration.
 */
public enum ColorCode {
	CYAN("#00FFFF"), BLACK("#000000"), BLUE("#0000FF"), BLUEVIOLET("#8A2BE2"), BROWN("#A52A2A"), CHARTREUSE("#7FFF00"), 
	 CRIMSON("#DC143C"), YELLOW("#FFFF00"), MAGENTA("#8B008B"), DEEPPINK("#FF1493"),
	 LIGHTCORAL("#F08080");
	 
	String hex;
	ColorCode(String p) {
		hex = p;
	   }
	   public String showHex() {
	      return hex;
	   } 
	   
	   private static final Map<String, ColorCode> BY_CODE_MAP = new LinkedHashMap<>();
	    static {
	        for (ColorCode colorCode : ColorCode.values()) {
	            BY_CODE_MAP.put(colorCode.hex, colorCode);
	        }
	    }

	    public static ColorCode forCode(String code) {
	        return BY_CODE_MAP.get(code);
	    }
	
}
