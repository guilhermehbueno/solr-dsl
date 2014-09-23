package com.solr.dsl.views;

import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.info.QueryInfo;

public interface FirstCommandAggregation extends FilterBy, SortBy, BoostBy, BuilderToString{
	
	SecondCommandAggregation and();
	QueryInfo info();
}
