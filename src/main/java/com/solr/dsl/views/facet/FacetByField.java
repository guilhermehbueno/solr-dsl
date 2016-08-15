package com.solr.dsl.views.facet;

import java.util.List;

import org.apache.http.NameValuePair;

import com.solr.dsl.views.ThirdCommandAggregation;

public interface FacetByField {
	public ThirdCommandAggregation facetByField(List<NameValuePair> command);
	public ThirdCommandAggregation facetByField(String command);
}
