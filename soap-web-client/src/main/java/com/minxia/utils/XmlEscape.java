package com.minxia.utils;

public class XmlEscape {
	private static String human[] = { "&", "'", "\"", "<", ">" };

	private static String xmls[] = { "&amp;", "&apos;", "&quot;", "&lt;", "&gt;" };

	public static String escapeXml(String in) {
		if(in==null || in.length()==0)
			return null;
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(in);
		int idx = 0;
		for (int ii = 0; ii < human.length; ii++) {
			idx = -1;
			while ((idx = sbuf.toString().indexOf(human[ii], idx + 1)) >= 0) {
				if (ii == 0) {
					boolean already = false;
					String ss = sbuf.substring(idx, idx + Math.min(6, sbuf.length() - idx));
					for (int jj = 0; jj < xmls.length; jj++) {
						if (ss.startsWith(xmls[jj])) {
							already = true;
							break;
						}
					}
					if (!already) {
						sbuf.replace(idx, idx + 1, xmls[ii]);
					}
				} else {
					sbuf.replace(idx, idx + 1, xmls[ii]);
				}
			}
		}
		return sbuf.toString();
	}

	public static String escapeHuman(String in) {
		if(in==null || in.length()==0)
			return null;
		StringBuffer sbuf = new StringBuffer(in);
		int idx = -1;
		for (int ii = 0; ii < xmls.length; ii++) {
			while ((idx = sbuf.toString().indexOf(xmls[ii])) >= 0) {
				sbuf.replace(idx, idx + xmls[ii].length(), human[ii]);
			}
		}
		return sbuf.toString();
	}

	public static String escapeXmlDoc(String xdoc) {
		StringBuffer sbuf = new StringBuffer(xdoc);

		int idx0 = 0;
		int idx1 = 0;

		while ((idx1 = sbuf.toString().indexOf("value=\"", idx0)) >= 0) {
			idx1 += 7;

			idx0 = sbuf.toString().indexOf("\"", idx1);
			if (idx0 > idx1) {
				String ss = sbuf.substring(idx1, idx0);
				sbuf.replace(idx1, idx0, escapeXml(ss));
			}

			idx0 = idx1;
		}

		return sbuf.toString();
	}
}