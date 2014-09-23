package com.solr.dsl.views.info;

import java.util.List;

public interface QueryInfo {
	public String getQuery();
	public String getSortBy();
	public String getFieldList();
	public List<String> getFilterQueries();
	public String getFacetQueries();
	public String getFacetFields();
	public String getFacetPrefixes();
}
