package com.solr.dsl.scaffold;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class QueryScaffold {
	
	private final List<ScaffoldField> fields = new ArrayList<ScaffoldField>();
	
	public boolean add(ScaffoldField field) {
		return fields.add(field);
	}

	public boolean remove(ScaffoldField field) {
		return fields.remove(field);
	}
	
	public ScaffoldField getByName(String name){
		ScaffoldField  result = null;
		for (ScaffoldField field : this.fields) {
			if(field.getName().equalsIgnoreCase(name)){
				result=field;
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
	    List<String> params = fields.stream().map(field -> {
		return field.getName()+"="+field.getValue();
	    }).collect(Collectors.toList());
	    return StringUtils.join(params, "&");
	}
}