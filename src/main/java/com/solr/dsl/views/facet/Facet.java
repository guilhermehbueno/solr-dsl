package com.solr.dsl.views.facet;

import com.solr.dsl.views.ThirdCommandAggregation;

public interface Facet {
    
    public ThirdCommandAggregation enableFacet();
    public ThirdCommandAggregation disableFacet();

}
