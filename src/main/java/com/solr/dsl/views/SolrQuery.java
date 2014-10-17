package com.solr.dsl.views;

import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.info.QueryInfo;

public interface SolrQuery extends FilterBy, SortBy, BoostBy, BuilderToString{
	
	SecondCommandAggregation and();
	QueryInfo info();
	QueryParamHandler change();
}
