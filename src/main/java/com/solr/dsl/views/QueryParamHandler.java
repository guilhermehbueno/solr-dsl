package com.solr.dsl.views;

public interface QueryParamHandler {
	
	public QueryParamHandler upsert();
	public QueryParamHandler update();
	public QueryParamHandler add();
	public QueryParamHandler remove();

}
