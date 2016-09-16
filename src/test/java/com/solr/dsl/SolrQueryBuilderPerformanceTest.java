package com.solr.dsl;

import java.io.File;
import java.nio.charset.Charset;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Stopwatch;
import com.google.common.io.Files;
import com.solr.dsl.views.SmartQuery;

public class SolrQueryBuilderPerformanceTest {
    
    private String content;

    @BeforeClass
    public void init() throws Exception{
	content = Files.toString(new File("src/test/resources/huge-query.txt"), Charset.defaultCharset());
	
    }
    
    @Test
    public void queryParseTime(){
	int queryCount = 100;
	Stopwatch start = Stopwatch.createStarted();
	for (int i = 0; i < queryCount; i++) {
	    Assert.assertNotNull(content);
	    SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery(content);
	    Assert.assertNotNull(smartQuery);
	    Assert.assertNotNull(smartQuery.info().getQuery());
	    Assert.assertNotNull(smartQuery.build());
	}
	Stopwatch stop = start.stop();
	System.out.println(stop);
    }
}
