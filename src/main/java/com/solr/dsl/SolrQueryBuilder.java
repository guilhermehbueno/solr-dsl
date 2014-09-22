package com.solr.dsl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.solr.dsl.QueryConfigureCommand.SecondSolrQuery;
import com.solr.dsl.views.FirstCommandAggregation;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.info.QueryInfo;

public class SolrQueryBuilder implements FirstCommandAggregation, QueryInfo{
	
	private final PrimarySolrQuery primarySolrQuery = new PrimarySolrQuery();
	private final SecondSolrQuery secondSolrQuery;

	private SolrQueryBuilder(String query) {
		super();
		this.primarySolrQuery.setQuery(query);
		this.secondSolrQuery=null;
	}
	
	SolrQueryBuilder(PrimarySolrQuery primarySolrQuery, SecondSolrQuery secondSolrQuery) {
		super();
		this.primarySolrQuery.setQuery(primarySolrQuery.getQuery());
		this.primarySolrQuery.setSortBy(primarySolrQuery.getSortBy());
		this.primarySolrQuery.addAllFilters(primarySolrQuery.getFilters());
		this.secondSolrQuery=secondSolrQuery;
	}
	
	public PrimarySolrQuery getPrimarySolrQuery() {
		return primarySolrQuery;
	}
	
	public QueryInfo info(){
		return this;
	}
	
	public String getFacetQueries() {
		return this.secondSolrQuery.getFacetByQuery();
	}

	public String getFacetFields() {
		return this.secondSolrQuery.getFacetByField();
	}

	public String getFacetPrefixes() {
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
	
	public static FirstCommandAggregation fromRawQuery(String rawQuery){
		return null;
	}

	public static FirstCommandAggregation newQuery(String query){
		if(query == null){
			return new SolrQueryBuilder("q=*:*");
		}
		return new SolrQueryBuilder("q="+query);
	}

	public FirstCommandAggregation filterBy(String command) {
		this.primarySolrQuery.addFilter("fq="+command);
		return this;
	}

	public FirstCommandAggregation sortBy(String command) {
		this.primarySolrQuery.setSortBy("sort="+command);
		return this;
	}

	public SecondCommandAggregation and() {
		if(this.secondSolrQuery==null){
			return new QueryConfigureCommand(this.primarySolrQuery);
		}else{
			return new QueryConfigureCommand(this.primarySolrQuery, this.secondSolrQuery);
		}
	}

	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.primarySolrQuery.build());
		if(this.secondSolrQuery!=null){
			sb.append(this.secondSolrQuery.build());
		}
		return sb.toString();
	}
	
	static class PrimarySolrQuery implements BuilderToString{
		
		private String query;
		private final List<String> filters = new ArrayList<String>();
		private String sortBy;
		
		public boolean addFilter(String fq) {
			return filters.add(fq);
		}
		
		public boolean addAllFilters(Collection<? extends String> c) {
			return filters.addAll(c);
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
		
		public String build() {
			String fqs = StringUtils.join(this.filters, "&");
			StringBuilder sb = new StringBuilder();
			
			sb.append(query);
			if(StringUtils.isNotEmpty(fqs)){
				sb.append("&").append(fqs);
			}
			
			if(StringUtils.isNotEmpty(sortBy)){
				sb.append("&").append(sortBy);
			}
			return sb.toString();
		}
	}
}