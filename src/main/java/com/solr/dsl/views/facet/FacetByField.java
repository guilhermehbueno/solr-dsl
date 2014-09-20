package com.solr.dsl.views.facet;

import com.solr.dsl.views.ThirdCommandAggregation;

public interface FacetByField {
	public ThirdCommandAggregation facetByField(String command);
}
