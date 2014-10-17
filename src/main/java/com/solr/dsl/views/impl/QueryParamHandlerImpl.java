package com.solr.dsl.views.impl;

import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.QueryParamHandler;

public class QueryParamHandlerImpl implements QueryParamHandler {
	
	private final QueryScaffold scaffold;
	
	public QueryParamHandlerImpl(QueryScaffold scaffold) {
		super();
		this.scaffold = scaffold;
	}

	public QueryParamHandler upsert(ScaffoldField field) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueryParamHandler update(ScaffoldField field) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueryParamHandler add(ScaffoldField field) {
		// TODO Auto-generated method stub
		return null;
	}

	public QueryParamHandler remove(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}
}
