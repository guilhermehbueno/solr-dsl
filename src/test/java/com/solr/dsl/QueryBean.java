package com.solr.dsl;

public class QueryBean {
	private final String expectedQuery;
	private final String generatedQuery;
	
	public QueryBean(String expectedQuery, String generatedQuery) {
		super();
		this.expectedQuery = expectedQuery;
		this.generatedQuery = generatedQuery;
	}
	
	public String getExpectedQuery() {
		return expectedQuery;
	}
	
	public String getGeneratedQuery() {
		return generatedQuery;
	}

	@Override
	public String toString() {
		return "QueryBean [expectedQuery=" + expectedQuery + ", generatedQuery=" + generatedQuery + "]";
	}
}
