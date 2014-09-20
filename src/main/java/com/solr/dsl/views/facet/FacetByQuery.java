package com.solr.dsl.views.facet;

import com.solr.dsl.views.ThirdCommandAggregation;

public interface FacetByQuery {
	public ThirdCommandAggregation facetByQuery(String command);
}
