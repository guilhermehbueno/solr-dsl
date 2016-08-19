package com.solr.dsl;

import static com.solr.dsl.scaffold.FieldBuilder.field;

import org.testng.Assert;
import org.testng.annotations.Test;

public class QueryParamHandlerTest {

	@Test
	public void shouldUpdateParam(){
		String query = SolrQueryBuilder.fromRawQuery("q=teste").change().upsert(field("q").value("xpto")).upsert(field("q").value("xpto1")).goToInit().build();
		Assert.assertEquals(query, "q=xpto1");
		System.out.println(query);
		
		
		String build = SolrQueryBuilder.fromRawQuery("q=qwerty").change().add(field("q").value("xpto")).goToInit().build();
		System.out.println(build);
		SolrQueryBuilder.fromRawQuery("").change().remove("teste");
		SolrQueryBuilder.fromRawQuery("").info().getQuery();
	}
}
