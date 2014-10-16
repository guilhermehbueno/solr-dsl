package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.solr.dsl.views.SolrQuery;

public class SolrQueryBuilderByRawQueryTest {
	
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
				{new QueryBean("q=iphone", SolrQueryBuilder.fromRawQuery("q=iphone").info().getQuery())},
				{new QueryBean("2", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&fq=category:categoryName").info().getFilterQueries().size()+"")},
				{new QueryBean("sort=popularity", SolrQueryBuilder.fromRawQuery("iphone&fq=name:teste&sort=popularity").info().getSortBy())},
				{new QueryBean("fl=id,name", SolrQueryBuilder.fromRawQuery("iphone&fq=name:teste&sort=popularity&fl=id,name").info().getFieldList())},
				{new QueryBean("facet.query=teste", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category").info().getFacetQueries())}
		};
	}
	
	@Test
	public void shouldGetFacetQueryFromRawQuery(){
		SolrQuery SQB = SolrQueryBuilder.fromRawQuery("q=testando&facet.query=teste&fl=name,id&fq=name:teste");
		String facetQueries = SQB.info().getFacetQueries();
		Assert.assertEquals(facetQueries, "facet.query=teste");
	}
	
	@Test
	public void shouldPreserveUnacknowledgedQueryParams(){
		String query = "q=iphone&fq=name:teste&unack=true&guilherme=bueno";
		String queryResult = SolrQueryBuilder.fromRawQuery(query).build();
		System.out.println(queryResult);
		Assert.assertEquals(queryResult, query);
	}

	@Test
	public void shouldGetQueryFromRawQuery(){
		Assert.assertEquals(SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste").info().getQuery(), "q=iphone");
	}
	
	@Test
	public void shouldGetFilterQueryFromRawQuery(){
		Assert.assertEquals(SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity").info().getSortBy(), "sort=popularity");
	}
	
	@Test(dataProvider="queries")
	public void validate(QueryBean bean){
		Assert.assertNotNull(bean.getExpectedQuery());
		Assert.assertNotNull(bean.getGeneratedQuery());
		Assert.assertEquals(bean.getExpectedQuery(), bean.getGeneratedQuery());
	}
}