package com.easycode.gencode.core.ftlmethod;

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
public class FiltNotMatch implements TemplateMethodModelEx {

	HashMap<String, Object> root = new HashMap<String, Object>();

	public FiltNotMatch() {
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
						//初始每一个值为未匹配上，只要有一个条件满足匹配，则这个值不会添加到返回列表中。
						boolean onecheck = false;
						for (int j=1;j<args.size();j++) {
							JSONObject o = JSONObject.fromObject(String.valueOf(args.get(j)));
							Iterator it = o.keys();
							
							boolean oneConditionMatch = true;
							while (it.hasNext()) {
								String key = (String) it.next();
								String value = String.valueOf(o.get(key));
								String propValue = String.valueOf(shash
										.get(key));
								//只要有一个属性未匹配是哪个，表示这个json条件未匹配上，满足条件，继续下一个值判断。所有值都不满足条件，才作为不满足匹配条件。
								if(!propValue.matches(value)) 
								{
									oneConditionMatch = false;
									break;
								}
							}
							//只要有一个匹配上了，则改变onecheck值，跳出循环，继续下一个值判断
							if (oneConditionMatch) {
								onecheck = true;
								break;
								 
							}
						}

						if (!onecheck) {
							resultList.add(shash);
						}
					} else {

						String propValue = String.valueOf(m);
						boolean onecheck = false;
						for (int j=1;j<args.size();j++) {
							 
							if (propValue.matches(String.valueOf(args.get(j)))) {
								 
								onecheck = true;
								break;
							}
 
						}
						if (!onecheck) {
							resultList.add(m);
							 
						}

					}
				}
			}

			/*
			 * for(Map prop:propArry) { for(Object s:args) { JSONObject o =
			 * JSONObject.fromObject(s); Iterator it = o.keys(); boolean
			 * onecheck = true; while(it.hasNext()) { String key =
			 * (String)it.next(); String value = String.valueOf(o.get(key));
			 * String propValue = String.valueOf(prop.get(key)); if(propValue ==
			 * null || !propValue.matches(value)) { onecheck = false; break; }
			 * 
			 * } if(onecheck) { resultList.add(prop); break; } }
			 * 
			 * }
			 */
			return new SimpleSequence((List) resultList);
			//return new SimpleCollection((List) resultList);
		}
	}
}
