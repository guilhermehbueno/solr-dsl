package com.solr.dsl;

import org.testng.annotations.Test;
import static com.solr.dsl.scaffold.FieldBuilder.field;

public class QueryParamHandlerTest {

	@Test
	public void shouldUpdateParam(){
		SolrQueryBuilder.fromRawQuery("").change().update(field("teste").value("xpto"));
		SolrQueryBuilder.fromRawQuery("").change().upsert(field("teste").value("xpto"));
		SolrQueryBuilder.fromRawQuery("").change().add(field("teste").value("xpto"));
		SolrQueryBuilder.fromRawQuery("").change().remove("teste");
		
		SolrQueryBuilder.fromRawQuery("").info().getQuery();
	}
}
