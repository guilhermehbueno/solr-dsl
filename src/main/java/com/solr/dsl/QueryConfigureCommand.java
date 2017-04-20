package com.solr.dsl;

import static com.solr.dsl.scaffold.FieldBuilder.field;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.NameValuePair;

import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.SmartQuery;
import com.solr.dsl.views.ThirdCommandAggregation;
import com.solr.dsl.views.info.QueryInfo;

public class QueryConfigureCommand implements SecondCommandAggregation, ThirdCommandAggregation {

    private final QueryScaffold scaffold;

    public QueryConfigureCommand(QueryScaffold scaffold) {
	super();
	this.scaffold = scaffold;
    }

    @Override
    public String build() {
	return scaffold.build();
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
	scaffold.add(field("facet.field").value(command));
	scaffold.change("facet", "true");
	return this;
    }

    @Override
    public ThirdCommandAggregation facetByField(List<NameValuePair> command) {
	if (command == null) {
	    return this;
	}
	
	command.forEach(commandItem -> {
	    scaffold.add(field("facet.field").value(commandItem.getValue()));
	    scaffold.change("facet", "true");  
	});
	return this;
    }

    @Override
    public ThirdCommandAggregation facetByQuery(String command) {
	if (command == null) {
	    return this;
	}
	
	scaffold.add(field("facet.query").value(command));
        scaffold.change("facet", "true");
	return this;
    }

    @Override
    public ThirdCommandAggregation facetByPrefix(String command) {
	if (command == null) {
	    return this;
	}
	
	scaffold.add(field("facet.prexy").value(command));
        scaffold.change("facet", "true");
	return this;
    }

    @Override
    public SecondCommandAggregation listBy(String fields) {
	if (fields == null) {
	    return this;
	}

	ScaffoldField listBy = field("fl").value(fields);
	listBy.setGroup("fl");
        scaffold.add(listBy);
	return this;
    }

    @Override
    @Deprecated
    public SmartQuery goToInit() {
	throw new UnsupportedOperationException("This method cannot be used. Reason: Performance!");
    }

    @Override
    public ThirdCommandAggregation enableFacet() {
	scaffold.change("facet", "true");
	return this;
    }

    @Override
    public ThirdCommandAggregation disableFacet() {
	scaffold.change("facet", "false");
	return this;
    }

    @Override
    public String buildToJson() {
	throw new NotImplementedException("Not implemented.");
    }

    @Override
    public String buildEncoded() {
	return scaffold.buildEncoded();
    }
}