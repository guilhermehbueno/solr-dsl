package com.solr.dsl.views.impl;

import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.QueryParamHandler;
import com.solr.dsl.views.SmartQuery;

public class QueryParamHandlerImpl implements QueryParamHandler {
	
	private final QueryScaffold scaffold;
	private final SmartQuery solrQuery;
	
	public QueryParamHandlerImpl(QueryScaffold scaffold, SmartQuery solrQuery) {
		super();
		this.scaffold = scaffold;
		this.solrQuery = solrQuery;
	}

	@Override
	public QueryParamHandler upsert(ScaffoldField field) {
		ScaffoldField fieldRecovered = this.scaffold.getByName(field.getName());
		if(fieldRecovered!=null){
			fieldRecovered.setValue(field.getValue());
		}else{
			this.scaffold.add(field);
		}
		return this;
	}

	@Override
	public QueryParamHandler update(ScaffoldField field) {
		ScaffoldField fieldRecovered = this.scaffold.getByName(field.getName());
		if(fieldRecovered!=null){
			fieldRecovered.setValue(field.getValue());
		}
		return this;
	}

	@Override
	public QueryParamHandler add(ScaffoldField field) {
		this.scaffold.add(field);
		return this;
	}

	@Override
	public QueryParamHandler remove(String fieldName) {
		this.scaffold.removeByName(fieldName);
		return this;
	}

	@Override
	public SmartQuery goToInit() {
		return this.solrQuery;
	}

}