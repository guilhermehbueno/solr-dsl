package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SolrQueryBuilderByRawQueryTest {
	
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
			{new QueryBean("iphone", SolrQueryBuilder.newQuery("iphone").info().getQuery())},
			{new QueryBean("2", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").filterBy("category:categoryName").info().getFilterQueries().size()+"")},
			{new QueryBean("popularity", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").info().getSortBy())},
			{new QueryBean("id,name", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").and().listBy("id,name").info().getFieldList())},
			{new QueryBean("category", SolrQueryBuilder.newQuery("iphone")
													.filterBy("name:teste").and().and()
													.facetByField("category")
													.build())},
			{new QueryBean("teste", SolrQueryBuilder.newQuery("iphone")
													.sortBy("popularity").and()
													.listBy("id,name").and()
													.facetByQuery("teste")
													.build())}
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
