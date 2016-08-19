package com.solr.dsl;

import static com.solr.dsl.scaffold.FieldBuilder.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.solr.dsl.raw.SolrQueryRawExtractor;
import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.scaffold.ScaffoldField.Group;
import com.solr.dsl.views.QueryParamHandler;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.SmartQuery;
import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.impl.QueryParamHandlerImpl;
import com.solr.dsl.views.info.QueryInfo;

public class SolrQueryBuilder implements SmartQuery, QueryInfo {

	private final QueryScaffold scaffold;
	private SecondSolrQuery secondSolrQuery;
	private PrimarySolrQuery primarySolrQuery;
	private final QueryParamHandler queryParamHandler;
	private static final List<String> recognizedQueryParams = new ArrayList<String>();

	static {
		String [] recoFields = new String[] { 
			"q", "fq", "bf", "fl", "facet", "facet.field", "facet.query", "facet.prefix", "bq", "sort" 
		};
		recognizedQueryParams.addAll(Arrays.asList(recoFields));
	}

	private SolrQueryBuilder(String query) {
		super();
		if(query == null){
			query="*:*";
		}
		this.scaffold = new QueryScaffold();
		this.primarySolrQuery = new PrimarySolrQuery(scaffold);
		this.queryParamHandler = new QueryParamHandlerImpl(scaffold, this);
		this.primarySolrQuery.setQuery(field("q").value(query));
		this.secondSolrQuery = new SecondSolrQuery(this.scaffold);
	}
	
	private SolrQueryBuilder(String query, List<ScaffoldField> unacknowledgeFields) {
		this(query);
		this.primarySolrQuery.setQuery(field("q").value(query));
		this.primarySolrQuery.addAllUnacknowledgeFields(unacknowledgeFields);
		this.secondSolrQuery = new SecondSolrQuery(this.scaffold);
	}

	SolrQueryBuilder(PrimarySolrQuery primarySolrQuery, SecondSolrQuery secondSolrQuery){
	    	this.scaffold = primarySolrQuery.getScaffold();
	    	this.queryParamHandler = new QueryParamHandlerImpl(scaffold, this);
		this.primarySolrQuery = primarySolrQuery;
		this.secondSolrQuery = secondSolrQuery;
		this.primarySolrQuery.build();
		this.secondSolrQuery.build();
	}

	public PrimarySolrQuery getPrimarySolrQuery() {
		return primarySolrQuery;
	}

	@Override
	public QueryInfo info() {
		return this;
	}
	
	@Override
	public QueryParamHandler change() {
		return this.queryParamHandler;
	}

	@Override
	public String getFacetQueries() {
		return this.secondSolrQuery.getFacetByQuery();
	}

	@Override
	public List<String> getFacetFields() {
		if (this.secondSolrQuery == null) {
			return null;
		}
		return this.secondSolrQuery.getFacetByField();
	}
	
	@Override
	public List<ScaffoldField> getFacetFieldsStructure() {
		if (this.secondSolrQuery == null) {
			return null;
		}
		return this.secondSolrQuery.getFacetStructureByField();
	}

	@Override
	public String getFacetPrefixes() {
		if (this.secondSolrQuery == null) {
			return null;
		}
		return this.secondSolrQuery.getFacetByPrefix();
	}

	@Override
	public String getFieldList() {
		return this.secondSolrQuery.getListBy();
	}

	@Override
	public List<String> getFilterQueries() {
		List<String> filters = transform(this.primarySolrQuery.getFilters());
		return filters;
	}
	
	private static List<String> transform(List<? extends ScaffoldField> c){
		List<String> fields = Lists.transform(c, new Function<ScaffoldField, String>() {
			@Override
			public String apply(ScaffoldField field){
				return field.toString();
			}
		});
		return fields;
	}

	@Override
	public String getQuery() {
		return this.primarySolrQuery.getQuery();
	}

	@Override
	public String getSortBy() {
		return this.primarySolrQuery.getSortByScaffoldField().toString();
	}

	public static SmartQuery fromRawQuery(String rawQuery) {
		String query = SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery,	"q");
		List<ScaffoldField> unacknowledgedQueryParams = SolrQueryRawExtractor.getUnacknowledgedQueryParams(recognizedQueryParams, rawQuery);
		SmartQuery SQB = new SolrQueryBuilder(query, unacknowledgedQueryParams);
		
		List<NameValuePair> paramValues = SolrQueryRawExtractor.getMultiQueryParamValue(rawQuery, "fq");
		for (NameValuePair nameValuePair : paramValues) {
			SQB.filterBy(nameValuePair.getValue());
		}

		//TODO: EXTRAIR FACET=TRUE
		SQB.sortBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "sort"))
		   		.boostBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "bq")).and()
				.listBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "fl")).and()
				.facetByField(SolrQueryRawExtractor.getMultiQueryParamValue(rawQuery, "facet.field"))
				.facetByQuery(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "facet.query"))
				.facetByPrefix(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "facet.prefix"));
		return SQB;
	}

	public static SmartQuery newQuery(String query) {
		if (query == null) {
			return new SolrQueryBuilder("*:*");
		}
		return new SolrQueryBuilder(query);
	}

	@Override
	public SmartQuery boostBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		
		this.primarySolrQuery.addBoostQuery(field("bq").value(command));
		return this;
	}

	@Override
	public SmartQuery filterBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		
		this.primarySolrQuery.addFilter(field("fq").value(command));
		return this;
	}

	@Override
	public SmartQuery sortBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		
		ScaffoldField sort = field("sort").value(command);
		this.primarySolrQuery.setSortBy(sort);
		return this;
	}

	@Override
	public SecondCommandAggregation and() {
		return new QueryConfigureCommand(this.primarySolrQuery,
				this.secondSolrQuery);
	}

	@Override
	public String build() {
		return this.scaffold.build();
	}

	static class PrimarySolrQuery implements BuilderToString {

		private QueryScaffold scaffold;
		
		public QueryScaffold getScaffold() {
		    return scaffold;
		}
		
		public PrimarySolrQuery(QueryScaffold scaffold) {
			super();
			this.scaffold = scaffold;
		}
		
		public void setScaffold(QueryScaffold scaffold) {
		    this.scaffold = scaffold;
		}

		public boolean addBoostQuery(ScaffoldField field) {
		    	field.setGroup(new Group("bq"));
			return scaffold.add(field);
		}

		public boolean addAllBoostQuery(Collection<? extends ScaffoldField> c) {
			return scaffold.addAll(new Group("bq"), c);
		}

		public boolean addFilter(ScaffoldField fq) {
		    	fq.setGroup(new Group("fq"));
			return scaffold.add(fq);
		}

		public boolean addAllFilters(Collection<? extends ScaffoldField> c) {
			return scaffold.addAll(new Group("fq"), c);
		}
		
		public boolean addAllUnacknowledgeFields(Collection<? extends ScaffoldField> fields) {
		    	Group group = new Group("unack");
		    	fields.forEach(field -> this.scaffold.add(new ScaffoldField(field.getName(), field.getValue(), group)));
			return true;
		}

		public String getQuery() {
			return this.scaffold.getValueByName("q");
		}
		
		public void setQuery(ScaffoldField query) {
		    	Group group = new Group("query");
		    	query.setGroup(group);
			this.scaffold.change(query);
		}

		public ScaffoldField getSortByScaffoldField() {
			ScaffoldField field = this.scaffold.getByName("sort");
			return field;
		}

		public void setSortBy(ScaffoldField sortBy) {
		    	if(this.scaffold.hasField("sort")){
		    	    this.scaffold.change("sort", sortBy.getValue());
		    	}else{
        		    Group group = new Group("sort");
        		    sortBy.setGroup(group);
        		    this.scaffold.add(sortBy);
		    	}
		}

		public List<ScaffoldField> getFilters() {
			return this.scaffold.getByGroupName("fq");
		}
		
		public List<ScaffoldField> getUnacknowledgeFields() {
			return this.scaffold.getByGroupName("unack");
		}
		
		List<ScaffoldField> getBoostQuery(){
		    return this.scaffold.getByGroupName("bq");
		}
		
		@Override
		public String build() {
			return scaffold.build();
		}

		@Override
		public String buildToJson() {
		    return this.scaffold.buildToJson();
		}
	}

	@Override
	public <T> T getFieldValue(String fieldName) {
	    return (T) this.scaffold.getValueByName(fieldName);
	}

	@Override
	public String buildToJson() {
	    return this.primarySolrQuery.buildToJson();
	}
}