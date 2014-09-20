package com.solr.dsl.views.facet;

import com.solr.dsl.views.ThirdCommandAggregation;

public interface FacetByPrefix {
	public ThirdCommandAggregation facetByPrefix(String command);
}
