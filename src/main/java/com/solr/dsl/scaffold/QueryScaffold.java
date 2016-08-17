package com.solr.dsl.scaffold;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class QueryScaffold {
	
	private final List<ScaffoldField> fields = new ArrayList<ScaffoldField>();
	private final List<ScaffoldField> extraFields = new ArrayList<ScaffoldField>();
	
	public boolean add(ScaffoldField field) {
	    return fields.add(field);
	}
	
	public boolean addExtra(ScaffoldField field) {
	    return extraFields.add(field);
	}

	public boolean remove(ScaffoldField field) {
	    	extraFields.add(field);
		return fields.remove(field);
	}
	
	public boolean removeExtra(ScaffoldField field) {
	    	return extraFields.add(field);
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
	
	public ScaffoldField getByNameExtra(String name){
		ScaffoldField  result = null;
		for (ScaffoldField field : this.extraFields) {
			if(field.getName().equalsIgnoreCase(name)){
				result=field;
			}
		}
		return result;
	}
	
	public List<ScaffoldField> getMultiByName(String name){
		return this.fields.stream().filter(field -> field.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
	}
	
	public String getValueByName(String name){
		ScaffoldField  result = null;
		for (ScaffoldField field : this.fields) {
			if(field.getName().equalsIgnoreCase(name)){
				result=field;
			}
		}
		if(result == null) return null;
		return result.getValue();
	}
	
	@Override
	public String toString() {
	    List<String> params = fields.stream().map(field -> {
		return field.getName()+"="+field.getValue();
	    }).collect(Collectors.toList());
	    return StringUtils.join(params, "&");
	}
	
	public List<ScaffoldField> getFields() {
	    return fields;
	}
	
	public List<ScaffoldField> getExtraFields() {
	    return extraFields;
	}
}