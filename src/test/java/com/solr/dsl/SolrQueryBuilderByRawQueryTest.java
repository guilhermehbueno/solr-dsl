package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SolrQueryBuilderByRawQueryTest {
	
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
				{new QueryBean("q=iphone", SolrQueryBuilder.newQuery("iphone").info().getQuery())},
				{new QueryBean("2", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&fq=category:categoryName").info().getFilterQueries().size()+"")},
				{new QueryBean("sort=popularity", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").info().getSortBy())},
				{new QueryBean("fl=id,name", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").and().listBy("id,name").info().getFieldList())},
				{new QueryBean("teste", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category").info().getFacetQueries())}
		};
	}
	
	
	@Test
	public void shouldPreserveUnacknowledgedQueryParams(){
		String query = "q=iphone&fq=name:teste";
		String queryResult = SolrQueryBuilder.fromRawQuery(query).build();
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
