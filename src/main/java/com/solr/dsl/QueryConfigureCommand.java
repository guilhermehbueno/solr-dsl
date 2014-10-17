package com.solr.dsl;

import org.apache.commons.lang3.StringUtils;

import com.solr.dsl.SolrQueryBuilder.PrimarySolrQuery;
import com.solr.dsl.scaffold.QueryScaffold;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.SecondCommandAggregation;
import com.solr.dsl.views.SolrQuery;
import com.solr.dsl.views.ThirdCommandAggregation;
import com.solr.dsl.views.build.BuilderToString;
import com.solr.dsl.views.info.QueryInfo;

import static com.solr.dsl.scaffold.FieldBuilder.field;

public class QueryConfigureCommand implements SecondCommandAggregation, ThirdCommandAggregation{
	
	private final PrimarySolrQuery primarySolrQuery;
	private final SecondSolrQuery secondSolrQuery;
	
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
	
	public QueryInfo info() {
		return goToInit().info();
	}

	public ThirdCommandAggregation facetByField(String command) {
		if(command==null){
			return this;
		}
		this.secondSolrQuery.setFacetByField(field("facet.field").value(command));
		return this;
	}

	public ThirdCommandAggregation facetByQuery(String command) {
		if(command==null){
			return this;
		}
		this.secondSolrQuery.setFacetByQuery(field("facet.query").value(command));
		return this;
	}

	public ThirdCommandAggregation facetByPrefix(String command) {
		if(command==null){
			return this;
		}
		this.secondSolrQuery.setFacetByPrefix(field("facet.prexy").value(command));
		return this;
	}

	public SecondCommandAggregation listBy(String fields) {
		if(fields==null){
			return this;
		}
		
		this.secondSolrQuery.setListBy(field("fl").value(fields));
		return this;
	}

	public SolrQuery goToInit() {
		return new SolrQueryBuilder(this.primarySolrQuery, this.secondSolrQuery);
	}
	
	static class SecondSolrQuery implements BuilderToString{
		private final QueryScaffold scaffold;
		
		public SecondSolrQuery(QueryScaffold scaffold) {
			super();
			this.scaffold = scaffold;
		}
		public String getFacetByField() {
			return this.scaffold.getByName("facet.field").getValue();
		}
		public void setFacetByField(ScaffoldField facetByField) {
			if(facetByField==null){
				return;
			}
			scaffold.add(facetByField);
			
		}
		public String getFacetByQuery() {
			return this.scaffold.getByName("facet.query").toString();
		}
		public void setFacetByQuery(ScaffoldField facetByQuery) {
			if(facetByQuery==null){
				return;
			}
			scaffold.add(facetByQuery);
		}
		public String getFacetByPrefix() {
			return this.scaffold.getByName("facet.prefix").getValue();
		}
		public void setFacetByPrefix(ScaffoldField facetByPrefix) {
			if(facetByPrefix==null){
				return;
			}
			scaffold.add(facetByPrefix);
		}
		public String getListBy() {
			return this.scaffold.getByName("fl").toString();
		}
		public void setListBy(ScaffoldField listBy) {
			if(listBy==null){
				return;
			}
			scaffold.add(listBy);
		}
		
		public String build() {
			StringBuilder sb = new StringBuilder();
			
			String facetByField= 	this.scaffold.getByName("facet.field")!=null? this.scaffold.getByName("facet.field").toString(): null;
			String facetByQuery=	this.scaffold.getByName("facet.query")!=null? this.scaffold.getByName("facet.query").toString(): null;
			String facetByPrefix=	this.scaffold.getByName("facet.prefix")!=null? this.scaffold.getByName("facet.prefix").toString(): null;
			String listBy=			this.scaffold.getByName("fl")!=null? this.scaffold.getByName("fl").toString(): null;
			
			if(StringUtils.isNotEmpty(facetByQuery) || StringUtils.isNotEmpty(facetByField) || StringUtils.isNotEmpty(facetByPrefix)){
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