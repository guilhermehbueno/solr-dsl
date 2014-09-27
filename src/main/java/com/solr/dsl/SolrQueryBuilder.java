package com.solr.dsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;

import com.solr.dsl.QueryConfigureCommand.SecondSolrQuery;
import com.solr.dsl.raw.SolrQueryRawExtractor;
import com.solr.dsl.views.FirstCommandAggregation;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.info.QueryInfo;

public class SolrQueryBuilder implements FirstCommandAggregation, QueryInfo {

	private final PrimarySolrQuery primarySolrQuery = new PrimarySolrQuery();
	private final SecondSolrQuery secondSolrQuery;
	private static final List<String> recognizedQueryParams = new ArrayList<String>();

	static {
		String [] recoFields = new String[] { 
			"q", "fq", "bf", "fl", "facet.field", "facet.query", "facet.prefix", "bq", "sort" 
		};
		recognizedQueryParams.addAll(Arrays.asList(recoFields));
	}

	private SolrQueryBuilder(String query) {
		super();
		this.primarySolrQuery.setQuery(query);
		this.secondSolrQuery = new SecondSolrQuery();
	}
	
	private SolrQueryBuilder(String query, List<NameValuePair> unacknowledgeFields) {
		super();
		this.primarySolrQuery.setQuery("q="+query);
		this.primarySolrQuery.addAllUnacknowledgeFields(unacknowledgeFields);
		this.secondSolrQuery = new SecondSolrQuery();
	}

	SolrQueryBuilder(PrimarySolrQuery primarySolrQuery, SecondSolrQuery secondSolrQuery) {
		super();
		this.primarySolrQuery.setQuery(primarySolrQuery.getQuery());
		this.primarySolrQuery.setSortBy(primarySolrQuery.getSortBy());
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
		return this.primarySolrQuery.getFilters();
	}

	public String getQuery() {
		return this.primarySolrQuery.getQuery();
	}

	public String getSortBy() {
		return this.primarySolrQuery.getSortBy();
	}

	public static FirstCommandAggregation fromRawQuery(String rawQuery) {
		String query = SolrQueryRawExtractor.getSingleQueryParamValue(rawQuery,	"q");
		List<NameValuePair> unacknowledgedQueryParams = SolrQueryRawExtractor.getUnacknowledgedQueryParams(recognizedQueryParams, rawQuery);
		FirstCommandAggregation SQB = new SolrQueryBuilder(query, unacknowledgedQueryParams);
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

	public static FirstCommandAggregation newQuery(String query) {
		if (query == null) {
			return new SolrQueryBuilder("q=*:*");
		}
		return new SolrQueryBuilder("q=" + query);
	}

	public FirstCommandAggregation boostBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		this.primarySolrQuery.addBoostQuery("bq=" + command);
		return this;
	}

	public FirstCommandAggregation filterBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		this.primarySolrQuery.addFilter("fq=" + command);
		return this;
	}

	public FirstCommandAggregation sortBy(String command) {
		if (StringUtils.isEmpty(command)) {
			return this;
		}
		this.primarySolrQuery.setSortBy("sort=" + command);
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

		private String query;
		private final List<String> filters = new ArrayList<String>();
		private final List<String> boostQuery = new ArrayList<String>();
		private final List<NameValuePair> unacknowledgeFields = new ArrayList<NameValuePair>();
		private String sortBy;

		public boolean addBoostQuery(String e) {
			return boostQuery.add(e);
		}

		public boolean addAllBoostQuery(Collection<? extends String> c) {
			return boostQuery.addAll(c);
		}

		public boolean addFilter(String fq) {
			return filters.add(fq);
		}

		public boolean addAllFilters(Collection<? extends String> c) {
			return filters.addAll(c);
		}
		
		public boolean addAllUnacknowledgeFields(Collection<? extends NameValuePair> fields) {
			return unacknowledgeFields.addAll(fields);
		}

		public String getQuery() {
			return query;
		}

		public void setQuery(String query) {
			this.query = query;
		}

		public String getSortBy() {
			return sortBy;
		}

		public void setSortBy(String sortBy) {
			this.sortBy = sortBy;
		}

		public List<String> getFilters() {
			return filters;
		}
		
		public List<NameValuePair> getUnacknowledgeFields() {
			return unacknowledgeFields;
		}

		public String build() {
			String fqs = StringUtils.join(this.filters, "&");
			String bqs = StringUtils.join(this.boostQuery, "&");
			StringBuilder sb = new StringBuilder();

			sb.append(query);
			if (StringUtils.isNotEmpty(fqs)) {
				sb.append("&").append(fqs);
			}

			if (StringUtils.isNotEmpty(bqs)) {
				sb.append("&").append(bqs);
			}

			if (StringUtils.isNotEmpty(sortBy)) {
				sb.append("&").append(sortBy);
			}
			return sb.toString();
		}
	}
}