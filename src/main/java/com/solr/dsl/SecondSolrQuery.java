package com.solr.dsl;

import java.util.List;
import java.util.stream.Collectors;

import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.scaffold.ScaffoldField.Group;
import com.solr.dsl.views.ThirdCommandAggregation;
import com.solr.dsl.views.build.BuilderToString;

class SecondSolrQuery implements BuilderToString {
    private final QueryScaffold scaffold;

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
        scaffold.add(facetByField);
        scaffold.change("facet", "true");
    }

    public String getFacetByQuery() {
        return this.scaffold.getByName("facet.query").toString();
    }

    public void setFacetByQuery(ScaffoldField facetByQuery) {
        if (facetByQuery == null) {
    	return;
        }
        scaffold.add(facetByQuery);
        scaffold.change("facet", "true");
    }

    public String getFacetByPrefix() {
	scaffold.change("facet", "true");
        return this.scaffold.getByName("facet.prefix").getValue();
    }

    public void setFacetByPrefix(ScaffoldField facetByPrefix) {
        if (facetByPrefix == null) {
    	return;
        }
        scaffold.add(facetByPrefix);
        scaffold.change("facet", "true");
    }

    public String getListBy() {
        return this.scaffold.getByName("fl").toString();
    }

    public void setListBy(ScaffoldField listBy) {
        if (listBy == null) {
    	return;
        }
        
        listBy.setGroup(new Group("fl"));
        scaffold.add(listBy);
    }

    @Override
    public String build() {
	  return this.scaffold.build();
    }

    public ThirdCommandAggregation enableFacet() {
	scaffold.change("facet", "true");
        return null;
    }

    public ThirdCommandAggregation disableFacet() {
	scaffold.change("facet", "false");
        return null;
    }

    @Override
    public String buildToJson() {
	// TODO Auto-generated method stub
	return null;
    }
}