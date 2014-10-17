package com.solr.dsl.scaffold;

import java.util.ArrayList;
import java.util.List;

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
}