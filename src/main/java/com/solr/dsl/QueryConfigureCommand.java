package com.solr.dsl;

import org.apache.commons.lang3.StringUtils;

import com.solr.dsl.SolrQueryBuilder.PrimarySolrQuery;
import com.solr.dsl.views.FirstCommandAggregation;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.ThirdCommandAggregation;
import com.solr.dsl.views.build.BuilderToString;

public class QueryConfigureCommand implements SecondCommandAggregation, ThirdCommandAggregation{
	
	private final PrimarySolrQuery primarySolrQuery;
	private final SecondSolrQuery secondSolrQuery;
	
	public QueryConfigureCommand(PrimarySolrQuery primarySolrQuery) {
		super();
		this.primarySolrQuery=primarySolrQuery;
		this.secondSolrQuery = new SecondSolrQuery();
	}
	
	public QueryConfigureCommand(PrimarySolrQuery primarySolrQuery, SecondSolrQuery secondSolrQuery) {
		super();
		this.primarySolrQuery = primarySolrQuery;
		this.secondSolrQuery = secondSolrQuery;
	}

	public String build() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.primarySolrQuery.build());
		sb.append(this.secondSolrQuery.build());
		return sb.toString();
	}

	public ThirdCommandAggregation and() {
		return this;
	}

	public ThirdCommandAggregation facetByField(String command) {
		this.secondSolrQuery.setEnabledFacet(true);
		this.secondSolrQuery.setFacetByField("facet.field="+command);
		return this;
	}

	public ThirdCommandAggregation facetByQuery(String command) {
		this.secondSolrQuery.setEnabledFacet(true);
		this.secondSolrQuery.setFacetByQuery("facet.query="+command);
		return this;
	}

	public ThirdCommandAggregation facetByPrefix(String command) {
		this.secondSolrQuery.setEnabledFacet(true);
		this.secondSolrQuery.setFacetByPrefix("facet.prefix="+command);
		return this;
	}

	public SecondCommandAggregation listBy(String fields) {
		this.secondSolrQuery.setListBy("fl="+fields);
		return this;
	}

	public FirstCommandAggregation goToInit() {
		return new SolrQueryBuilder(this.primarySolrQuery, this.secondSolrQuery);
	}
	
	static class SecondSolrQuery implements BuilderToString{
		private String facetByField;
		private String facetByQuery;
		private String facetByPrefix;
		private String listBy;
		private boolean enabledFacet=false;
		
		public String getFacetByField() {
			return facetByField;
		}
		public void setFacetByField(String facetByField) {
			this.facetByField = facetByField;
		}
		public String getFacetByQuery() {
			return facetByQuery;
		}
		public void setFacetByQuery(String facetByQuery) {
			this.facetByQuery = facetByQuery;
		}
		public String getFacetByPrefix() {
			return facetByPrefix;
		}
		public void setFacetByPrefix(String facetByPrefix) {
			this.facetByPrefix = facetByPrefix;
		}
		public String getListBy() {
			return listBy;
		}
		public void setListBy(String listBy) {
			this.listBy = listBy;
		}
		public boolean isEnabledFacet() {
			return enabledFacet;
		}
		public void setEnabledFacet(boolean enabledFacet) {
			this.enabledFacet = enabledFacet;
		}
		
		public String build() {
			StringBuilder sb = new StringBuilder();
			//sb.append(this.primarySolrQuery.build());
			if(this.enabledFacet){
				sb.append("&").append("facet=true");
			}
			if(StringUtils.isNotEmpty(listBy)){
				sb.append("&").append(listBy);
			}
			
			if(StringUtils.isNotEmpty(facetByQuery)){
				sb.append("&").append(facetByQuery);
			}
			
			if(StringUtils.isNotEmpty(facetByField)){
				sb.append("&").append(facetByField);
			}
			
			if(StringUtils.isNotEmpty(facetByPrefix)){
				sb.append("&").append(facetByPrefix);
			}
			return sb.toString();
		}
	}
}
