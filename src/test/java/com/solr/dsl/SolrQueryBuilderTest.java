package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SolrQueryBuilderTest {
	
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
			{new QueryBean("q=iphone", SolrQueryBuilder.newQuery("iphone").build())},
			{new QueryBean("q=iphone&fq=name:teste", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").build())},
			{new QueryBean("q=iphone&fq=name:teste&fq=category:categoryName", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").filterBy("category:categoryName").build())},
			{new QueryBean("q=iphone&fq=name:teste&sort=popularity", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").build())},
			{new QueryBean("q=iphone&fq=name:teste&sort=popularity&fl=id,name", SolrQueryBuilder.newQuery("iphone").filterBy("name:teste").sortBy("popularity").and().listBy("id,name").build())},
			{new QueryBean("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.field=category", 
				SolrQueryBuilder.newQuery("iphone")
								.filterBy("name:teste")
								.sortBy("popularity").and()
								.listBy("id,name").and()
								.facetByField("category")
								.build())},
			{new QueryBean("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category", 
				SolrQueryBuilder.newQuery("iphone")
								.filterBy("name:teste")
								.sortBy("popularity").and()
								.listBy("id,name").and()
								.facetByField("category")
								.facetByQuery("teste")
								.build())},
			{new QueryBean("q=iphone&fq=name:teste&bq=skuIds:(12321^500 OR 221300^1000)&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category", 
				SolrQueryBuilder.newQuery("iphone")
								.filterBy("name:teste")
								.boostBy("skuIds:(12321^500 OR 221300^1000)")
								.sortBy("popularity").and()
								.listBy("id,name").and()
								.facetByField("category")
								.facetByQuery("teste")
								.build())}
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
		Assert.assertEquals(query, "q=iphone&facet=true&facet.query=teste:teste");
	}
	
	@Test
	public void shouldGoToInit(){
		String build = SolrQueryBuilder.newQuery("iphone").sortBy("popularidade").and().listBy("id,name").goToInit().sortBy("popularity").build();
		System.out.println(build);
		Assert.assertNotNull(build);
		Assert.assertEquals(build, "q=iphone&sort=popularity&fl=id,name");
	}
	
	@Test
	public void shouldToggleFacet(){
	    String query = SolrQueryBuilder.newQuery("iphone").and().and().facetByField("name_text_pt").disableFacet().build();
	    Assert.assertEquals(query, "q=iphone&facet=false&facet.field=name_text_pt");
	}
	
	
	public void shouldOverwriteQueryParam(){
	    
	}
}
