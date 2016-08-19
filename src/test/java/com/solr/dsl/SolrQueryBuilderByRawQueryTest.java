package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.SmartQuery;

public class SolrQueryBuilderByRawQueryTest {
	
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
				{new QueryBean("iphone", SolrQueryBuilder.fromRawQuery("q=iphone").info().getQuery())},
				{new QueryBean("2", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&fq=category:categoryName").info().getFilterQueries().size()+"")},
				{new QueryBean("sort=popularity", SolrQueryBuilder.fromRawQuery("iphone&fq=name:teste&sort=popularity").info().getSortBy())},
				{new QueryBean("fl=id,name", SolrQueryBuilder.fromRawQuery("iphone&fq=name:teste&sort=popularity&fl=id,name").info().getFieldList())},
				{new QueryBean("facet.query=teste", SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category").info().getFacetQueries())}
		};
	}
	
	@Test
	public void shouldGetFacetQueryFromRawQuery(){
		SmartQuery SQB = SolrQueryBuilder.fromRawQuery("q=testando&facet.query=teste&fl=name,id&fq=name:teste");
		String facetQueries = SQB.info().getFacetQueries();
		Assert.assertEquals(facetQueries, "facet.query=teste");
	}
	
	@Test
	public void shouldPreserveUnacknowledgedQueryParams(){
		String query = "q=iphone&unack=true&guilherme=bueno&fq=name:teste";
		String queryResult = SolrQueryBuilder.fromRawQuery(query).build();
		System.out.println(queryResult);
		Assert.assertEquals(queryResult, query);
	}
	
	@Test
	public void shouldAddMMParam(){
	    String query = "q=iphone&unack=true&guilherme=bueno&fq=name:teste";
	    SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery(query);
	    smartQuery.change().upsert(new ScaffoldField("mm", "100%"));
	    String result = smartQuery.build();
	    Assert.assertEquals(result, query+"&mm=100%");
	}

	@Test
	public void shouldGetQueryFromRawQuery(){
		Assert.assertEquals(SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste").info().getQuery(), "iphone");
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