package Dlement.util;


import java.util.Random;

// Referenced classes of package com.rongji.dfish.framework:
//            SystemData

public class ClusterIdGetter {

	public static void main(String[] args) {
		System.out.println(ClusterIdGetter.getInstance().getNewId());
	}
	private ClusterIdGetter() {
		lastTime = 0L;
		lastIndex = 0L;
	}

	public static ClusterIdGetter getInstance() {
		if (instance == null)
//			synchronized (this) {
				if (instance == null)
					instance = new ClusterIdGetter();
//			}
		return instance;
	}

	public String getNewId() {
		long currTime = System.currentTimeMillis();
		String timestampStr = Long.toString(currTime, 32);
		StringBuilder idStr = new StringBuilder(timestampStr);
		if (timestampStr.length() != 9)
			fixString(idStr, 9);
		String storeSystemKey = getStoreSystemKey();
		idStr.append(storeSystemKey);
		if (currTime != lastTime) {
			lastTime = currTime;
			lastIndex = 0L;
		} else {
			lastIndex++;
		}
		StringBuilder storeIndex = new StringBuilder();
		storeIndex.append(Long.toString(lastIndex, 32));
		fixString(storeIndex, 4);
		idStr.append(storeIndex.toString());
		return idStr.toString();
	}

	private String getStoreSystemKey() {
		return "";
	}

	private static void fixString(StringBuilder sb, int strLength) {
		if (sb != null && strLength > 0) {
			String oldStr = sb.toString();
			int fixLength = strLength - sb.length();
			sb.setLength(0);
			if (fixLength < 0) {
				sb.append(oldStr.substring(-fixLength));
			} else {
				for (int i = 0; i < fixLength; i++)
					sb.append('0');

				sb.append(oldStr);
			}
		}
	}

	public static String[] parseId(String id) {
		String result[] = null;
		if (id!=null&&!"".equals(id) && id.length() == 16) {
			result = new String[3];
			int split1 = 9;
			int split2 = 12;
			String timestampStr = id.substring(0, split1);
			result[0] = String.valueOf(Long.parseLong(timestampStr, 32));
			String systemKeyStr = id.substring(split1, split2);
			result[1] = String.valueOf(Long.parseLong(systemKeyStr, 32));
			String indexStr = id.substring(split2);
			result[2] = String.valueOf(Long.parseLong(indexStr, 32));
		}
		return result;
	}

	private static final int RADIX = 32;
	private static final int LENGTH_TIMESTAMP = 9;
	private static final int LENGTH_SYSTEM_KEY = 3;
	private static final int LENGTH_INDEX = 4;
	private static final int LENGTH_ID = 16;
	private volatile long lastTime;
	private volatile long lastIndex;
	private static String systemKey = "";
	private static ClusterIdGetter instance;

}