package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.solr.dsl.raw.parser.QueryStringParser;
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
			{new QueryBean("q=*:*&fq=Marca_ClassMaster_string:Black\\ \\&\\ Decker", SolrQueryBuilder.fromRawQuery("q=*%3A*&fq=Marca_ClassMaster_string%3ABlack%5C+%5C%26%5C+Decker", QueryStringParser.defaultQueryParser()).build())},
			{new QueryBean("q=*:*&fq=Marca_ClassMaster_string:Black & Decker", SolrQueryBuilder.fromRawQuery("q=*%3A*&fq=Marca_ClassMaster_string%3ABlack+%26+Decker").build())},
			{new QueryBean("q=*:*&fq={!ex=cor}Marca_ClassMaster_string:Black & Decker", SolrQueryBuilder.fromRawQuery("q=*%3A*&fq=%7B%21ex%3Dcor%7DMarca_ClassMaster_string%3ABlack+%26+Decker").build())}
		};
	}
	
	@Test(dataProvider="queries")
	public void shouldBuildQuery(QueryBean bean){
		Assert.assertNotNull(bean.getExpectedQuery());
		Assert.assertNotNull(bean.getGeneratedQuery());
		Assert.assertEquals(bean.getGeneratedQuery(), bean.getExpectedQuery());
	}
	
	@Test
	public void shouldBuildEncodedQuery(){
	    //String rawQuery = "q="+EncodingUtils.encode("black & decker");
	    String rawQuery = "q=black%20%26%20decker";
	    SmartQuery smart = SolrQueryBuilder.fromRawQuery(rawQuery);
	    String build = smart.build();
	    String buildEncoded = smart.buildEncoded();
	    Assert.assertNotEquals(build, buildEncoded);
	    Assert.assertEquals(rawQuery, buildEncoded);
	}
	
	@Test
	public void shouldParseQueryProperly(){
	    SmartQuery squery = SolrQueryBuilder.fromRawQuery("q=%28_query_%3A%7B%21multiMaxScore+tie%3D0.0+v%3D%27%28%28code_string%3AMini%29+OR+%28name_text_pt%3AMini%29+OR+%28category_string_mv%3AMini%29%29+OR+%28%28code_string%3AProcessador%29+OR+%28name_text_pt%3AProcessador%29+OR+%28category_string_mv%3AProcessador%29%29+OR+%28%28code_string%3Ade%29+OR+%28category_string_mv%3Ade%29%29+OR+%28%28code_string%3Aalimentos%29+OR+%28name_text_pt%3Aalimentos%29+OR+%28category_string_mv%3Aalimentos%29%29+OR+%28%28name_text_pt%3AMini%7E%29%29+OR+%28%28name_text_pt%3AProcessador%7E%29%29+OR+%28%28name_text_pt%3Ade%7E%29%29+OR+%28%28name_text_pt%3Aalimentos%7E%29%29%27%7D%29");
	    Assert.assertEquals(
		"(_query_:{!multiMaxScore tie=0.0 v='((code_string:Mini) OR (name_text_pt:Mini) OR (category_string_mv:Mini)) OR ((code_string:Processador) OR (name_text_pt:Processador) OR (category_string_mv:Processador)) OR ((code_string:de) OR (category_string_mv:de)) OR ((code_string:alimentos) OR (name_text_pt:alimentos) OR (category_string_mv:alimentos)) OR ((name_text_pt:Mini~)) OR ((name_text_pt:Processador~)) OR ((name_text_pt:de~)) OR ((name_text_pt:alimentos~))'})",
		squery.info().getFieldValue("q")+""
	    );
	    
	    squery = SolrQueryBuilder.fromRawQuery("q=(_query_:{!multiMaxScore+tie%3D0.0+v%3D'((code_string:black)+OR+(name_text_pt:black)+OR+(category_string_mv:black))+OR+((code_string:\\%26)+OR+(category_string_mv:\\%26))+OR+((code_string:decker)+OR+(name_text_pt:decker)+OR+(category_string_mv:decker))+OR+((name_text_pt:black~))+OR+((name_text_pt:\\%26~))+OR+((name_text_pt:decker~))'");
	    Assert.assertEquals(
		"(_query_:{!multiMaxScore tie=0.0 v='((code_string:black) OR (name_text_pt:black) OR (category_string_mv:black)) OR ((code_string:\\&) OR (category_string_mv:\\&)) OR ((code_string:decker) OR (name_text_pt:decker) OR (category_string_mv:decker)) OR ((name_text_pt:black~)) OR ((name_text_pt:\\&~)) OR ((name_text_pt:decker~))'", 
		squery.info().getFieldValue("q")+""
            );
	    
	    squery = SolrQueryBuilder.fromRawQuery("q=%28_query_%3A%7B%21multiMaxScore+tie%3D0.0+v%3D%27%28%28code_string%3ABlack%29+OR+%28name_text_pt%3ABlack%29+OR+%28category_string_mv%3ABlack%29%29+OR+%28%28code_string%3A%5C%26%29+OR+%28category_string_mv%3A%5C%26%29%29+OR+%28%28code_string%3ADecker%29+OR+%28name_text_pt%3ADecker%29+OR+%28category_string_mv%3ADecker%29%29+OR+%28%28name_text_pt%3ABlack%7E%29%29+OR+%28%28name_text_pt%3A%5C%26%7E%29%29+OR+%28%28name_text_pt%3ADecker%7E%29%29%27%7D%29");
	    Assert.assertEquals(
		"(_query_:{!multiMaxScore tie=0.0 v='((code_string:Black) OR (name_text_pt:Black) OR (category_string_mv:Black)) OR ((code_string:\\&) OR (category_string_mv:\\&)) OR ((code_string:Decker) OR (name_text_pt:Decker) OR (category_string_mv:Decker)) OR ((name_text_pt:Black~)) OR ((name_text_pt:\\&~)) OR ((name_text_pt:Decker~))'})", 
		squery.info().getFieldValue("q")+""
	    );
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