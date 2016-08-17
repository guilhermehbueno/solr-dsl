package com.solr.dsl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.ThirdCommandAggregation;
import com.solr.dsl.views.build.BuilderToString;

class SecondSolrQuery implements BuilderToString {
    private final QueryScaffold scaffold;
    private Boolean enabledFacet;

    public SecondSolrQuery(QueryScaffold scaffold) {
        super();
        this.scaffold = scaffold;
    }

    public List<String> getFacetByField() {
        return this.scaffold.getMultiByName("facet.field").stream().map(field -> field.toString()).collect(Collectors.toList());
    }
    
    public List<ScaffoldField> getFacetStructureByField() {
        return this.scaffold.getMultiByName("facet.field");
    }

    public void setFacetByField(ScaffoldField facetByField) {
        if (facetByField == null) {
    	return;
        }
        this.enabledFacet = true;
        scaffold.add(facetByField);

    }

    public String getFacetByQuery() {
	this.enabledFacet = true;
        return this.scaffold.getByName("facet.query").toString();
    }

    public void setFacetByQuery(ScaffoldField facetByQuery) {
        if (facetByQuery == null) {
    	return;
        }
        this.enabledFacet = true;
        scaffold.add(facetByQuery);
    }

    public String getFacetByPrefix() {
	this.enabledFacet = true;
        return this.scaffold.getByName("facet.prefix").getValue();
    }

    public void setFacetByPrefix(ScaffoldField facetByPrefix) {
        if (facetByPrefix == null) {
    	return;
        }
        this.enabledFacet = true;
        scaffold.add(facetByPrefix);
    }

    public String getListBy() {
        return this.scaffold.getByName("fl").toString();
    }

    public void setListBy(ScaffoldField listBy) {
        if (listBy == null) {
    	return;
        }
        scaffold.add(listBy);
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();
        
        List<ScaffoldField> multiByName = this.scaffold.getMultiByName("facet.field");
        List<String> collectMultiByName = multiByName.stream().map(field -> field.toString()).collect(Collectors.toList());
    	String facetByField = null;
    	if(collectMultiByName!= null && collectMultiByName.size()>0){
    	    facetByField = StringUtils.join(collectMultiByName, "&");
    	}
    	
    	
    	String facetByQuery = this.scaffold.getByName("facet.query") != null ? this.scaffold.getByName("facet.query").toString() : null;
    	String facetByPrefix = this.scaffold.getByName("facet.prefix") != null ? this.scaffold.getByName("facet.prefix").toString() : null;
    	String listBy = this.scaffold.getByName("fl") != null ? this.scaffold.getByName("fl").toString() : null;

    	if (StringUtils.isNotEmpty(facetByQuery) || StringUtils.isNotEmpty(facetByField) || StringUtils.isNotEmpty(facetByPrefix)) {
    	    if(enabledFacet!= null && this.enabledFacet == true){
    		sb.append("&").append("facet=true");
    	    }else{
    		sb.append("&").append("facet=false");
    	    }
    	} else {
    	    if (StringUtils.isNotEmpty(facetByQuery) || StringUtils.isNotEmpty(facetByField) || StringUtils.isNotEmpty(facetByPrefix)) {
    		sb.append("&").append("facet=false");
    	    }
    	}
    	if (StringUtils.isNotEmpty(listBy)) {
    	    sb.append("&").append(listBy);
    	}

    	if (StringUtils.isNotEmpty(facetByQuery)) {
    	    sb.append("&").append(facetByQuery);
    	}

    	if (StringUtils.isNotEmpty(facetByField)) {
    	    sb.append("&").append(facetByField);
    	}

    	if (StringUtils.isNotEmpty(facetByPrefix)) {
    	    sb.append("&").append(facetByPrefix);
    	}
    	
        //}
        return sb.toString();
    }

    public ThirdCommandAggregation enableFacet() {
        this.enabledFacet=true;
        return null;
    }

    public ThirdCommandAggregation disableFacet() {
        this.enabledFacet=false;
        return null;
    }

    @Override
    public String buildToJson() {
	// TODO Auto-generated method stub
	return null;
    }
}