package com.solr.dsl;


import org.testng.Assert;
import org.testng.annotations.Test;

public class SmartQueryTest {
    
    @Test
    public void shouldBuildQueryToJson(){
	String query = SolrQueryBuilder.newQuery("hello").build().toString();
	Assert.assertEquals(query, "q=hello");
	
	String buildToJson = SolrQueryBuilder.newQuery("hello").buildToJson();
	System.out.println(buildToJson);
	
	String result = SolrQueryBuilder.fromRawQuery("q=testando&facet.query=teste&fl=name,id&fq=name:teste&unknow=ok&qf=Marca_ClassMaster_string name_text_pt firstName^1&pf=Marca_ClassMaster_string name_text_pt firstName^3&pf2=Marca_ClassMaster_string name_text_pt firstName^2&pf3=Marca_ClassMaster_string name_text_pt firstName^1").buildToJson();
	System.out.println(result);
    }
}