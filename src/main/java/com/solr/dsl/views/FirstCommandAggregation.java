package com.solr.dsl.views;

import com.solr.dsl.views.build.BuilderToString;

public interface FirstCommandAggregation extends FilterBy, SortBy, BuilderToString{
	
	SecondCommandAggregation and();
	String getQuery();

}
