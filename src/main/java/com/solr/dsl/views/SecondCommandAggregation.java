package com.solr.dsl.views;

import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.info.QueryInfo;

public interface SecondCommandAggregation extends ListFieldsBy, BuilderToString{
	
	ThirdCommandAggregation and();
	SolrQuery goToInit();
	QueryInfo info();

}