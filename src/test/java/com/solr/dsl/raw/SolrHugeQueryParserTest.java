package com.solr.dsl.raw;

import java.io.File;
import java.nio.charset.Charset;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Files;
import com.solr.dsl.SolrQueryBuilder;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.SmartQuery;

public class SolrHugeQueryParserTest {
    
    @Test
    public void shouldPreserveMultiFacetFieldParams() throws Exception{
	String content = Files.toString(new File("src/test/resources/huge-query.txt"), Charset.defaultCharset());
	Assert.assertNotNull(content);
	SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery(content);
	Assert.assertNotNull(smartQuery);
	Assert.assertTrue(smartQuery.info().getFacetFields().size()==654);
	System.out.println(smartQuery.build());
    }
    
    @Test
    public void shouldParseHugeQuery() throws Exception{
	String content = Files.toString(new File("src/test/resources/huge-query.txt"), Charset.defaultCharset());
	Assert.assertNotNull(content);
	SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery(content);
	Assert.assertNotNull(smartQuery);
	Assert.assertNotNull(smartQuery.info().getQuery());
	System.out.println(smartQuery.info().getQuery());
	smartQuery.change().add(new ScaffoldField("q", smartQuery.info().getFieldValue("spellcheck.q"), null));
	System.out.println(smartQuery.info().getQuery());
	System.out.println(smartQuery.buildToJson());
    }
    
    
    
    @Test
    public void shouldParseHugeQueryEncoded() throws Exception{
	String content = Files.toString(new File("src/test/resources/encoded-huge-query-pre-prod.txt"), Charset.defaultCharset());
	Assert.assertNotNull(content);
	SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery(content);
	System.out.println("Encoded: " + smartQuery.buildEncoded());
    }
}