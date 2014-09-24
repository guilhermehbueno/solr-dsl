package com.solr.dsl.raw;


import java.util.List;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SolrQueryRawExtractorTest {
	
	@Test
	public void shouldExtractSingleQueryParamProperly(){
		String value = SolrQueryRawExtractor.getSingleQueryParamValue("q=teste", "q");
		Assert.assertEquals(value, "teste");
	}
	
	@Test
	public void shouldExtractMultiQueryParamProperly(){
		List<NameValuePair> values = SolrQueryRawExtractor.getMultiQueryParamValue("q=teste&fq=teste:1&fq=category:nome", "fq");
		Assert.assertNotNull(values);
		Assert.assertTrue(values.size()==2);
	}
}
