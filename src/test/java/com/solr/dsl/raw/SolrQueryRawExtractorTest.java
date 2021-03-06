package com.solr.dsl.raw;


import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.solr.dsl.QueryBean;
import com.solr.dsl.scaffold.ScaffoldField;

public class SolrQueryRawExtractorTest {
	
    
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
				{new QueryBean("iphone", SolrQueryRawExtractor.getSingleQueryParamValue("q", SolrQueryRawExtractor.parseQueryString("q=iphone")))},
				{new QueryBean("iphone", SolrQueryRawExtractor.getSingleQueryParamValue("facet.query", SolrQueryRawExtractor.parseQueryString("facet.query=iphone")))},
		};
	}
	
	@Test(dataProvider="queries")
	public void shouldExtractSingleQueryParamProperly(QueryBean queryBean){
		Assert.assertEquals(queryBean.getExpectedQuery(), queryBean.getGeneratedQuery());
	}
	
	@Test
	public void shouldExtractUnacknowledgeQueryParams(){
	    String query = "unknow=abc&q=teste";
	    List<String> fields = Arrays.asList("q");
	    List<ScaffoldField> params = SolrQueryRawExtractor.getUnacknowledgedQueryParams(fields, SolrQueryRawExtractor.parseQueryString(query));
	    Assert.assertNotNull(params);
	    Assert.assertTrue(params.size()==1);
	    Assert.assertEquals(params.get(0).getName(), "unknow");
	    Assert.assertEquals(params.get(0).getValue(), "abc");
	}
	
	@Test
	public void shouldExtractMultiQueryParamProperly(){
		List<NameValuePair> values = SolrQueryRawExtractor.getMultiQueryParamValue("fq", SolrQueryRawExtractor.parseQueryString("q=teste&fq=teste:1&fq=category:nome"));
		Assert.assertNotNull(values);
		Assert.assertTrue(values.size()==2);
	}
}