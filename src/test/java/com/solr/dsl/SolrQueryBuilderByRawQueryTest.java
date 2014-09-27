package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SolrQueryBuilderByRawQueryTest {
	
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
			{new QueryBean("iphone", SolrQueryBuilder.fromRawQuery("q=iphone").info().getQuery())},
			{new QueryBean("2", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&fq=category:categoryName").info().getFilterQueries().size()+"")},
			{new QueryBean("popularity", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity").info().getSortBy())},
			{new QueryBean("id,name", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&fl=id,name").info().getFieldList())},
			{new QueryBean("category", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.field=category").info().getFacetFields())},
			{new QueryBean("teste", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category").info().getFacetQueries())}
		};
	}

	@Test
	public void shouldGetQueryFromRawQuery(){
		Assert.assertEquals(SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste").info().getQuery(), "iphone");
	}
	
	@Test
	public void shouldGetFilterQueryFromRawQuery(){
		Assert.assertEquals(SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity").info().getSortBy(), "popularity");
	}
	
	@DataProvider(name="queries")
	public void validate(QueryBean bean){
		Assert.assertNotNull(bean.getExpectedQuery());
		Assert.assertNotNull(bean.getGeneratedQuery());
		Assert.assertEquals(bean.getGeneratedQuery(), bean.getExpectedQuery());
	}
	
}
