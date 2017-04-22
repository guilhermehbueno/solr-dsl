package com.solr.dsl;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.SmartQuery;


public class SolrDslExamples {
    
    @Test
    public void examples(){
	String query = "q=iphone&unack=true&guilherme=bueno&mm=100%25&fq=name:teste&facet.field=1&facet=true&facet.field=2&fq=category:categoryName&sort=popularity&fl=id,name";
	SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery(query);
	
	smartQuery.info().getQuery(); //iphone
	smartQuery.info().getFilterQueries().size(); //2
	smartQuery.info().getFieldList(); //fl=id,name
	smartQuery.info().getFacetFields().size(); //2
	smartQuery.info().getFacetFields().get(0); //facet.field=1
	smartQuery.info().getFacetFieldsStructure().size(); //2
	smartQuery.info().getFacetFieldsStructure().get(0).getValue(); //1
	smartQuery.info().getFieldValue("q"); //iphone
	smartQuery.info().getFieldValue("unack"); // true
	smartQuery.info().getFieldValue("fq"); // BUG: category:categoryName retorna somente o último fq e não uma lista de valores
	
	smartQuery.change().add(new ScaffoldField("teste", "novo-valor")); //Adiciona novo valor
	Assert.assertEquals("novo-valor", smartQuery.info().getFieldValue("teste"));
	smartQuery.change().update(new ScaffoldField("teste", "novo-valor-2"));
	Assert.assertEquals("novo-valor-2", smartQuery.info().getFieldValue("teste"));
	
	Assert.assertEquals("iphone", smartQuery.info().getQuery());
	Assert.assertEquals(2, smartQuery.info().getFilterQueries().size());
	Assert.assertEquals("fl=id,name", smartQuery.info().getFieldList());
	Assert.assertEquals(2, smartQuery.info().getFacetFields().size());
	Assert.assertEquals("facet.field=1", smartQuery.info().getFacetFields().get(0));
	Assert.assertEquals(2, smartQuery.info().getFacetFieldsStructure().size());
	Assert.assertEquals("1", smartQuery.info().getFacetFieldsStructure().get(0).getValue());
	Assert.assertEquals("iphone", smartQuery.info().getFieldValue("q"));
	Assert.assertEquals("true", smartQuery.info().getFieldValue("unack"));
    }
}