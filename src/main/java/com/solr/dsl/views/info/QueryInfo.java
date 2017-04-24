package com.solr.dsl.views.info;

import java.util.List;

import com.solr.dsl.scaffold.ScaffoldField;

public interface QueryInfo {
    	public <T> T getFieldValue(String fieldName);
	public String getQuery();
	public String getSortBy();
	public String getFieldList();
	public List<String> getFilterQueries();
	public String getFacetQueries();
	public List<String> getFacetFields();
	public List<ScaffoldField> getFacetFieldsStructure();
	public List<ScaffoldField> getFieldsStructure();
	public String getFacetPrefixes();
}
