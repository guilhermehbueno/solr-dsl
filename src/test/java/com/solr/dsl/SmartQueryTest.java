package com.solr.dsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.solr.dsl.scaffold.ScaffoldField;
import com.solr.dsl.views.SmartQuery;

public class SmartQueryTest {

    @Test
    public void shouldBuildQueryToJson() {
	String query = SolrQueryBuilder.newQuery("hello").build().toString();
	Assert.assertEquals(query, "q=hello");

	String buildToJson = SolrQueryBuilder.newQuery("hello").buildToJson();
	System.out.println(buildToJson);

	String result = SolrQueryBuilder
		.fromRawQuery(
			"q=testando&facet.query=teste&fl=name,id&fq=name:teste&unknow=ok&qf=Marca_ClassMaster_string name_text_pt firstName^1&pf=Marca_ClassMaster_string name_text_pt firstName^3&pf2=Marca_ClassMaster_string name_text_pt firstName^2&pf3=Marca_ClassMaster_string name_text_pt firstName^1")
		.buildToJson();
	System.out.println(result);
    }
    
    @Test
    public void shouldChangeQuery(){
	SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery("q=ok");
	Assert.assertEquals(smartQuery.info().getQuery(), "ok");
	smartQuery.change().update(new ScaffoldField("q", "hello", null));
	Assert.assertEquals(smartQuery.info().getQuery(), "hello");
    }

    @Test
    public void shouldPreserveFacetFieldsFromRawQuery() {
	String query = "q=iphone&unack=true&guilherme=bueno&mm=100%&fq=name:teste&facet.field=1&facet=true&facet.field=2";
	SmartQuery smartQuery = SolrQueryBuilder.fromRawQuery(query);
	System.out.println(smartQuery.build());
	Assert.assertEquals(query, smartQuery.build());
	System.out.println(smartQuery.buildToJson());
    }

    @Test
    public void shouldGroupParamsByName() {
	List<ScaffoldField> fields = new ArrayList<>();
	fields.add(new ScaffoldField("facet.field", "1", null));
	fields.add(new ScaffoldField("facet.field", "2", null));
	fields.add(new ScaffoldField("facet.field", "3", null));

	Map<String, List<ScaffoldField>> groupedFields = fields.stream().collect(Collectors.groupingBy(field -> field.getName()));
	Map<String, List<String>> params = new HashMap<>();

	groupedFields.forEach((key, value) -> {
	    List<String> values = value.stream().map(field -> field.getValue()).collect(Collectors.toList());
	    params.put(key, values);
	});

	Map<String, Object> paramsWrapper = new HashMap<>();
	paramsWrapper.put("params", params);

	Gson gson = new Gson();
	String result = gson.toJson(paramsWrapper, HashMap.class);
	System.out.println(result);

	Assert.assertTrue(groupedFields.size() == 1);
	Assert.assertTrue(groupedFields.containsKey("facet.field"));
	Assert.assertEquals(groupedFields.get("facet.field").size(), 3);

    }
}