package com.solr.dsl.views;

import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.facet.Facet;
import com.solr.dsl.views.facet.FacetByField;
import com.solr.dsl.views.facet.FacetByPrefix;
import com.solr.dsl.views.facet.FacetByQuery;
import com.solr.dsl.views.info.QueryInfo;

public interface ThirdCommandAggregation extends FacetByField, FacetByQuery, FacetByPrefix, Facet, BuilderToString{
	QueryInfo info();
}
