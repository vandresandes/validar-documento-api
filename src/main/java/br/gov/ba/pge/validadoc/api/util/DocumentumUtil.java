package br.gov.ba.pge.validadoc.api.util;

import java.lang.reflect.Array;

public class DocumentumUtil {

	public static String criarDqlBuscarUltimoDocumentoPorId(String rObjectId) {
		return "select+d.r_object_id+from+dm_document+d+where+d.object_name=(select+d2.object_name+from+dm_sysobject+d2+where+d2.r_object_id='".concat(rObjectId).concat("')");
	}

	public static String criarUrlContentsContent(String r_object_id) {
		String objects = "/dctm-rest/repositories/PGE_DEV1/objects/";
		String contentsContent = "/contents/content/";
		return objects.concat(r_object_id).concat(contentsContent);
	}

	public static String getLinkContentMedia(Array links) {
		return null;
	}
}
