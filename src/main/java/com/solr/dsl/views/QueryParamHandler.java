package com.solr.dsl.views;

import com.solr.dsl.scaffold.ScaffoldField;

public interface QueryParamHandler {
	
	public QueryParamHandler upsert(ScaffoldField field);
	public QueryParamHandler update(ScaffoldField field);
	public QueryParamHandler add(ScaffoldField field);
	public QueryParamHandler remove(String fieldName);

}
