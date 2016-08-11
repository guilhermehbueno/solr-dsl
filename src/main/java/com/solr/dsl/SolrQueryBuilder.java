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
import com.solr.dsl.views.QueryParamHandler;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.SmartQuery;
import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.impl.QueryParamHandlerImpl;
import com.solr.dsl.views.info.QueryInfo;

public class SolrQueryBuilder implements SmartQuery, QueryInfo {

	private final QueryScaffold scaffold = new QueryScaffold();
	private final SecondSolrQuery secondSolrQuery;
	private final PrimarySolrQuery primarySolrQuery = new PrimarySolrQuery(scaffold);
	private final QueryParamHandler queryParamHandler = new QueryParamHandlerImpl(scaffold, this);
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
		this.primarySolrQuery.setQuery(field("q").value(query));
		this.secondSolrQuery = new SecondSolrQuery(this.scaffold);
	}
	
	private SolrQueryBuilder(String query, List<NameValuePair> unacknowledgeFields) {
		super();
		if(query == null){
			query="*:*";
		}
		this.primarySolrQuery.setQuery(field("q").value(query));
		this.primarySolrQuery.addAllUnacknowledgeFields(unacknowledgeFields);
		this.secondSolrQuery = new SecondSolrQuery(this.scaffold);
	}

	SolrQueryBuilder(PrimarySolrQuery primarySolrQuery, SecondSolrQuery secondSolrQuery) {
		super();
		this.scaffold.add(field("q").value(primarySolrQuery.getQuery()));
		this.primarySolrQuery.setQuery(primarySolrQuery.getQueryScaffoldField());
		this.primarySolrQuery.setSortBy(primarySolrQuery.getSortByScaffoldField());
		this.primarySolrQuery.addAllFilters(primarySolrQuery.getFilters());
		this.primarySolrQuery.addAllUnacknowledgeFields(primarySolrQuery.unacknowledgeFields);
		this.secondSolrQuery = secondSolrQuery;
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
	public String getFacetFields() {
		if (this.secondSolrQuery == null) {
			return null;
		}
		return this.secondSolrQuery.getFacetByField();
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
		List<NameValuePair> unacknowledgedQueryParams = SolrQueryRawExtractor.getUnacknowledgedQueryParams(recognizedQueryParams, rawQuery);
		SmartQuery SQB = new SolrQueryBuilder(query, unacknowledgedQueryParams);
		
		List<NameValuePair> paramValues = SolrQueryRawExtractor.getMultiQueryParamValue(rawQuery, "fq");
		for (NameValuePair nameValuePair : paramValues) {
			SQB.filterBy(nameValuePair.getValue());
		}

		//TODO: EXTRAIR FACET=TRUE
		SQB.sortBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "sort"))
		   		.boostBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "bq")).and()
				.listBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "fl")).and()
				.facetByField(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "facet.field"))
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
		StringBuilder sb = new StringBuilder();
		sb.append(this.primarySolrQuery.build());
		if (this.secondSolrQuery != null) {
			sb.append(this.secondSolrQuery.build());
		}
		
		for (NameValuePair pair : this.primarySolrQuery.getUnacknowledgeFields()) {
			if(pair!=null){
				sb.append("&").append(pair.getName()+"="+pair.getValue());
			}
		}
		return sb.toString();
	}

	static class PrimarySolrQuery implements BuilderToString {

		private final QueryScaffold scaffold;
		private final List<ScaffoldField> filters = new ArrayList<ScaffoldField>();
		private final List<ScaffoldField> boostQuery = new ArrayList<ScaffoldField>();
		private final List<NameValuePair> unacknowledgeFields = new ArrayList<NameValuePair>();
		
		public PrimarySolrQuery(QueryScaffold scaffold) {
			super();
			this.scaffold = scaffold;
		}

		public boolean addBoostQuery(ScaffoldField field) {
			return boostQuery.add(field);
		}

		public boolean addAllBoostQuery(Collection<? extends ScaffoldField> c) {
			return boostQuery.addAll(c);
		}

		public boolean addFilter(ScaffoldField fq) {
			return filters.add(fq);
		}

		public boolean addAllFilters(Collection<? extends ScaffoldField> c) {
			return filters.addAll(c);
		}
		
		public boolean addAllUnacknowledgeFields(Collection<? extends NameValuePair> fields) {
		    	fields.forEach(field -> this.scaffold.add(new ScaffoldField(field.getName(), field.getValue())));
			return unacknowledgeFields.addAll(fields);
		}

		public String getQuery() {
			return this.scaffold.getValueByName("q");
		}
		
		public ScaffoldField getQueryScaffoldField() {
			ScaffoldField field = this.scaffold.getByName("q");
			return field;
		}

		public void setQuery(ScaffoldField query) {
			this.scaffold.add(query);
		}

		public ScaffoldField getSortByScaffoldField() {
			ScaffoldField field = this.scaffold.getByName("sort");
			return field;
		}

		public void setSortBy(ScaffoldField sortBy) {
			this.scaffold.add(sortBy);
		}

		public List<ScaffoldField> getFilters() {
			return filters;
		}
		
		public List<NameValuePair> getUnacknowledgeFields() {
			return unacknowledgeFields;
		}
		
		@Override
		public String build() {
			String fqs = StringUtils.join(this.filters, "&");
			String bqs = StringUtils.join(this.boostQuery, "&");
			StringBuilder sb = new StringBuilder();
			
			ScaffoldField query = this.scaffold.getByName("q");
			ScaffoldField sortBy = this.scaffold.getByName("sort");

			sb.append(query);
			if (StringUtils.isNotEmpty(fqs)) {
				sb.append("&").append(fqs);
			}

			if (StringUtils.isNotEmpty(bqs)) {
				sb.append("&").append(bqs);
			}

			if (sortBy!=null && StringUtils.isNotEmpty(sortBy.toString())) {
				sb.append("&").append(sortBy);
			}
			return sb.toString();
		}
	}

	@Override
	public <T> T getFieldValue(String fieldName) {
	    return (T) this.scaffold.getValueByName(fieldName);
	}
}