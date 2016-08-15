package com.solr.dsl;

import static com.solr.dsl.scaffold.FieldBuilder.field;

import java.util.List;

import org.apache.http.NameValuePair;

import com.solr.dsl.SolrQueryBuilder.PrimarySolrQuery;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.SmartQuery;
import com.solr.dsl.views.ThirdCommandAggregation;
import com.solr.dsl.views.info.QueryInfo;

public class QueryConfigureCommand implements SecondCommandAggregation, ThirdCommandAggregation {

    private final PrimarySolrQuery primarySolrQuery;
    private final SecondSolrQuery secondSolrQuery;

    public QueryConfigureCommand(PrimarySolrQuery primarySolrQuery, SecondSolrQuery secondSolrQuery) {
	super();
	this.primarySolrQuery = primarySolrQuery;
	this.secondSolrQuery = secondSolrQuery;
    }

    @Override
    public String build() {
	StringBuilder sb = new StringBuilder();
	sb.append(this.primarySolrQuery.build());
	sb.append(this.secondSolrQuery.build());
	return sb.toString();
    }

    @Override
    public ThirdCommandAggregation and() {
	return this;
    }

    @Override
    public QueryInfo info() {
	return goToInit().info();
    }
    
    @Override
    public ThirdCommandAggregation facetByField(String command) {
	 this.secondSolrQuery.setFacetByField(field("facet.field").value(command));
	return this;
    }

    @Override
    public ThirdCommandAggregation facetByField(List<NameValuePair> command) {
	if (command == null) {
	    return this;
	}
	
	command.forEach(commandItem -> this.secondSolrQuery.setFacetByField(field("facet.field").value(commandItem.getValue())));
	return this;
    }

    @Override
    public ThirdCommandAggregation facetByQuery(String command) {
	if (command == null) {
	    return this;
	}
	this.secondSolrQuery.setFacetByQuery(field("facet.query").value(command));
	return this;
    }

    @Override
    public ThirdCommandAggregation facetByPrefix(String command) {
	if (command == null) {
	    return this;
	}
	this.secondSolrQuery.setFacetByPrefix(field("facet.prexy").value(command));
	return this;
    }

    @Override
    public SecondCommandAggregation listBy(String fields) {
	if (fields == null) {
	    return this;
	}

	this.secondSolrQuery.setListBy(field("fl").value(fields));
	return this;
    }

    @Override
    public SmartQuery goToInit() {
	return new SolrQueryBuilder(this.primarySolrQuery, this.secondSolrQuery);
    }

    @Override
    public ThirdCommandAggregation enableFacet() {
	this.secondSolrQuery.enableFacet();
	return this;
    }

    @Override
    public ThirdCommandAggregation disableFacet() {
	this.secondSolrQuery.disableFacet();
	return this;
    }

    @Override
    public String buildToJson() {
	// TODO Auto-generated method stub
	return null;
    }
}