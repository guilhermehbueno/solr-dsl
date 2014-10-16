package com.solr.dsl.raw;


import java.util.List;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.solr.dsl.QueryBean;

public class SolrQueryRawExtractorTest {
	
	@DataProvider(name="queries")
	public Object[][] queriesProvider(){
		return new Object[][] {
				{new QueryBean("iphone", SolrQueryRawExtractor.getSingleQueryParamValue("q=iphone", "q"))},
				{new QueryBean("iphone", SolrQueryRawExtractor.getSingleQueryParamValue("facet.query=iphone", "facet.query"))},
		};
	}
	
	@Test(dataProvider="queries")
	public void shouldExtractSingleQueryParamProperly(QueryBean queryBean){
		Assert.assertEquals(queryBean.getExpectedQuery(), queryBean.getGeneratedQuery());
	}
	
	@Test
	public void shouldExtractMultiQueryParamProperly(){
		List<NameValuePair> values = SolrQueryRawExtractor.getMultiQueryParamValue("q=teste&fq=teste:1&fq=category:nome", "fq");
		Assert.assertNotNull(values);
		Assert.assertTrue(values.size()==2);
	}
}