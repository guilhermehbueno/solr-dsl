package com.solr.dsl.views.impl;

import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.QueryParamHandler;
import com.solr.dsl.views.SolrQuery;

public class QueryParamHandlerImpl implements QueryParamHandler {
	
	private final QueryScaffold scaffold;
	private final SolrQuery solrQuery;
	
	public QueryParamHandlerImpl(QueryScaffold scaffold, SolrQuery solrQuery) {
		super();
		this.scaffold = scaffold;
		this.solrQuery = solrQuery;
	}

	public QueryParamHandler upsert(ScaffoldField field) {
		ScaffoldField fieldRecovered = this.scaffold.getByName(field.getName());
		if(fieldRecovered!=null){
			fieldRecovered.setValue(field.getValue());
		}else{
			this.scaffold.add(field);
		}
		return this;
	}

	public QueryParamHandler update(ScaffoldField field) {
		ScaffoldField fieldRecovered = this.scaffold.getByName(field.getName());
		if(fieldRecovered!=null){
			fieldRecovered.setValue(field.getValue());
		}
		return this;
	}

	public QueryParamHandler add(ScaffoldField field) {
		this.scaffold.add(field);
		return this;
	}

	public QueryParamHandler remove(String fieldName) {
		ScaffoldField fieldRecovered = this.scaffold.getByName(fieldName);
		this.scaffold.remove(fieldRecovered);
		return this;
	}

	public SolrQuery goToInit() {
		return this.solrQuery;
	}
}