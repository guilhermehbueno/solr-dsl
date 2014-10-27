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
import com.solr.dsl.QueryConfigureCommand.SecondSolrQuery;
import com.solr.dsl.raw.SolrQueryRawExtractor;
import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.QueryParamHandler;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.SolrQuery;
import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.impl.QueryParamHandlerImpl;
import com.solr.dsl.views.info.QueryInfo;

public class SolrQueryBuilder implements SolrQuery, QueryInfo {

	private final QueryScaffold scaffold = new QueryScaffold();
	private final SecondSolrQuery secondSolrQuery;
	private final PrimarySolrQuery primarySolrQuery = new PrimarySolrQuery(scaffold);
	private final QueryParamHandler queryParamHandler = new QueryParamHandlerImpl(scaffold, this);
	private static final List<String> recognizedQueryParams = new ArrayList<String>();

	static {
		String [] recoFields = new String[] { 
			"q", "fq", "bf", "fl", "facet.field", "facet.query", "facet.prefix", "bq", "sort" 
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

	public QueryInfo info() {
		return this;
	}
	
	public QueryParamHandler change() {
		return this.queryParamHandler;
	}

	public String getFacetQueries() {
		return this.secondSolrQuery.getFacetByQuery();
	}

	public String getFacetFields() {
		if (this.secondSolrQuery == null) {
			return null;
		}
		return this.secondSolrQuery.getFacetByField();
	}

	public String getFacetPrefixes() {
		if (this.secondSolrQuery == null) {
			return null;
		}
		return this.secondSolrQuery.getFacetByPrefix();
	}

	public String getFieldList() {
		return this.secondSolrQuery.getListBy();
	}

	public List<String> getFilterQueries() {
		List<String> filters = transform(this.primarySolrQuery.getFilters());
		return filters;
	}
	
	private static List<String> transform(List<? extends ScaffoldField> c){
		List<String> fields = Lists.transform(c, new Function<ScaffoldField, String>() {
			public String apply(ScaffoldField field){
				return field.toString();
			}
		});
		return fields;
	}

	public String getQuery() {
		return this.primarySolrQuery.getQuery();
	}

	public String getSortBy() {
		return this.primarySolrQuery.getSortByScaffoldField().toString();
	}

	public static SolrQuery fromRawQuery(String rawQuery) {
		String query = SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery,	"q");
		List<NameValuePair> unacknowledgedQueryParams = SolrQueryRawExtractor.getUnacknowledgedQueryParams(recognizedQueryParams, rawQuery);
		SolrQuery SQB = new SolrQueryBuilder(query, unacknowledgedQueryParams);
		
		List<NameValuePair> paramValues = SolrQueryRawExtractor.getMultiQueryParamValue(rawQuery, "fq");
		for (NameValuePair nameValuePair : paramValues) {
			SQB.filterBy(nameValuePair.getValue());
		}

		SQB.sortBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "sort"))
		   		.boostBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "bq")).and()
				.listBy(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "fl")).and()
				.facetByField(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "facet.field"))
				.facetByQuery(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "facet.query"))
				.facetByPrefix(SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery, "facet.prefix"));
		return SQB;
	}

	public static SolrQuery newQuery(String query) {
		if (query == null) {
			return new SolrQueryBuilder("*:*");
		}
		return new SolrQueryBuilder(query);
	}

	public SolrQuery boostBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		
		this.primarySolrQuery.addBoostQuery(field("bq").value(command));
		return this;
	}

	public SolrQuery filterBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		
		this.primarySolrQuery.addFilter(field("fq").value(command));
		return this;
	}

	public SolrQuery sortBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		
		ScaffoldField sort = field("sort").value(command);
		this.primarySolrQuery.setSortBy(sort);
		return this;
	}

	public SecondCommandAggregation and() {
		return new QueryConfigureCommand(this.primarySolrQuery,
				this.secondSolrQuery);
	}

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
			return unacknowledgeFields.addAll(fields);
		}

		public String getQuery() {
			ScaffoldField field = this.scaffold.getByName("q");
			return field.toString();
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
}