package com.solr.dsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.solr.dsl.views.SmartQuery;


public class SolrQueryBuilderValidationTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrQueryBuilderValidationTest.class);
    
    @Test
    public void test(){
	SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery("q=black & decker");
	Assert.assertEquals(0, smartQuery.info().getFieldsStructure().stream().filter(field -> {
	    LOGGER.info("Field: {} -> {}", field.getName(), field.getValue());
	    return field.getValue() == null && "null".equalsIgnoreCase(field.getValue());
	}).count());
    }

}
