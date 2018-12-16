package com.easycode.gencode.service.ftlmethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleCollection;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

//$(valueMatch("{'a':'b'})
public class FiltMatch implements TemplateMethodModelEx {

	 

	public FiltMatch() {
	}

	public Object exec(List args) throws TemplateModelException {

		if (args.size() <= 1) {
			throw  new TemplateModelException("参数个数异常！");
		} else {
			List<Object> resultList = new ArrayList<Object>();
			Object t = args.get(0);
			if (t instanceof SimpleSequence) {
				SimpleSequence seq = (SimpleSequence) t;
				//TemplateModelIterator tit = coll.g
				for (int i=0;i<seq.size();i++) {
					TemplateModel m = seq.get(i);
					if (m instanceof SimpleHash) {
						SimpleHash shash = (SimpleHash) m;
						for (int j=1;j<args.size();j++) {
							JSONObject o = JSONObject.fromObject(String.valueOf(args.get(j)));
							Iterator it = o.keys();
							while (it.hasNext()) {
								String key = (String) it.next();
								String value = String.valueOf(o.get(key));
								String propValue = String.valueOf(shash
										.get(key));
								if ( propValue.matches(value)) {
									resultList.add(m);
									break;
								}

							}
		 
						}

					} else {

						String propValue = String.valueOf(m);
						for (int j=1;j<args.size();j++) {
							 
							if (propValue.matches(String.valueOf(args.get(j)))) {
								resultList.add(m);
								break;
							}
 
						}

					}
				}
			}
			//return new SimpleCollection((List) resultList);
			return new SimpleSequence((List) resultList);
		}
	}
}
