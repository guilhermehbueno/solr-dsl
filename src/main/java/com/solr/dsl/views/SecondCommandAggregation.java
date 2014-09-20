package com.solr.dsl.views;

import com.solr.dsl.views.build.BuilderToString;

public interface SecondCommandAggregation extends ListFieldsBy, BuilderToString{
	
	ThirdCommandAggregation and();
	FirstCommandAggregation goToInit();

}