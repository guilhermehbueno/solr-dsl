package com.solr.dsl.views.info;

import java.util.List;

public interface QueryInfo {
    	public <T> T getFieldValue(String fieldName);
	public String getQuery();
	public String getSortBy();
	public String getFieldList();
	public List<String> getFilterQueries();
	public String getFacetQueries();
	public List<String> getFacetFields();
	public String getFacetPrefixes();
}
