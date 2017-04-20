package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.solr.dsl.views.SmartQuery;

public class SolrQueryBuilderTest {
	
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
			{new QueryBean("q=iphone", SolrQueryBuilder.newQuery("iphone").build())},
			{new QueryBean("q=iphone&fq=name:teste", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").build())},
			{new QueryBean("q=iphone&fq=name:teste&fq=category:categoryName", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").filterBy("category:categoryName").build())},
			{new QueryBean("q=iphone&fq=name:teste&sort=popularity", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").build())},
			{new QueryBean("q=iphone&fq=name:teste&sort=popularity&fl=id,name", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").and().listBy("id,name").build())},
			{new QueryBean("q=iphone&fq=name:teste&sort=popularity&fl=id,name&facet.field=category&facet=true", 
				SolrQueryBuilder.newQuery("iphone")
								.filterBy("name:teste")
								.sortBy("popularity").and()
								.listBy("id,name").and()
								.facetByField("category")
								.build())},
			{new QueryBean("q=iphone&fq=name:teste&sort=popularity&fl=id,name&facet.field=category&facet=true&facet.query=teste", 
				SolrQueryBuilder.newQuery("iphone")
								.filterBy("name:teste")
								.sortBy("popularity").and()
								.listBy("id,name").and()
								.facetByField("category")
								.facetByQuery("teste")
								.build())},
			{new QueryBean("q=iphone&fq=name:teste&bq=skuIds:(12321^500 OR 221300^1000)&sort=popularity&fl=id,name&facet.field=category&facet=true&facet.query=teste", 
				SolrQueryBuilder.newQuery("iphone")
								.filterBy("name:teste")
								.boostBy("skuIds:(12321^500 OR 221300^1000)")
								.sortBy("popularity").and()
								.listBy("id,name").and()
								.facetByField("category")
								.facetByQuery("teste")
								.build())},
			{new QueryBean("q=*:*&fq=Marca_ClassMaster_string:Black\\ \\&\\ Decker", SolrQueryBuilder.fromRawQuery("q=*%3A*&fq=Marca_ClassMaster_string%3ABlack%5C+%5C%26%5C+Decker", SolrQueryBuilder.defaultQueryParser()).build())},
			{new QueryBean("q=*:*&fq=Marca_ClassMaster_string:Black & Decker", SolrQueryBuilder.fromRawQuery("q=*%3A*&fq=Marca_ClassMaster_string%3ABlack+%26+Decker", SolrQueryBuilder.defaultQueryParser()).build())}
		};
	}
	
	@Test(dataProvider="queries")
	public void shouldBuildQuery(QueryBean bean){
		Assert.assertNotNull(bean.getExpectedQuery());
		Assert.assertNotNull(bean.getGeneratedQuery());
		Assert.assertEquals(bean.getGeneratedQuery(), bean.getExpectedQuery());
	}
	
	@Test
	public void shouldBuildQueryWithoutConfigurationFields(){
		String query = SolrQueryBuilder.newQuery("iphone").and().and().build();
		Assert.assertEquals(query, "q=iphone");
	}
	
	@Test
	public void shouldBuildQueryWithoutConfigurationFieldsAndWithFacetQuery(){
		String query = SolrQueryBuilder.newQuery("iphone").and().and().facetByQuery("teste:teste").build();
		Assert.assertEquals(query, "q=iphone&facet.query=teste:teste&facet=true");
	}
	
	@Test(expectedExceptions=UnsupportedOperationException.class)
	public void shouldGoToInit(){
		String build = SolrQueryBuilder.newQuery("iphone").sortBy("popularidade").and().listBy("id,name").goToInit().sortBy("popularity").build();
	}
	
	@Test
	public void shouldToggleFacet(){
	    String query = SolrQueryBuilder.newQuery("iphone").and().and().facetByField("name_text_pt").disableFacet().build();
	    Assert.assertEquals(query, "q=iphone&facet.field=name_text_pt&facet=false");
	}
	
	@Test
	public void shouldOverwriteQueryParam(){
	    String result = SolrQueryBuilder.newQuery("iphone")
		.filterBy("name:teste")
		.sortBy("popularity").and()
		.listBy("id,name").and()
		.facetByField("category")
		.build();
	    System.out.println("Result: " + result);
	}
	
	@Test
	public void shouldReturnQueryValue(){
	    SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery("q=teste&wt=json&indent=true&facet.field=name_text_pt");
	    Assert.assertEquals("teste", smartQuery.info().getQuery());
	}
}