package com.solr.dsl;

import org.testng.annotations.Test;

public class QueryParamHandlerTest {

	@Test
	public void shouldUpdateParam(){
		SolrQueryBuilder.fromRawQuery("").change().update();
		SolrQueryBuilder.fromRawQuery("").change().upsert();
		SolrQueryBuilder.fromRawQuery("").change().add();
		SolrQueryBuilder.fromRawQuery("").change().remove();
		
		SolrQueryBuilder.fromRawQuery("").info().getQuery();
	}
}
