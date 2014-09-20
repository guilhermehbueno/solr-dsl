package com.ruler;

import org.testng.Assert;
import org.testng.annotations.Test;

import rule.client.RuleResource;
import rule.client.RulesExecutor6;

import com.solr.dsl.SolrQueryBuilder;
import com.solr.dsl.views.FirstCommandAggregation;

public class RulerTest {
	
	public RuleResource buildDsl(){
		RuleResource dsl = new RuleResource();
		dsl.setResourceName("search.dsl");
		StringBuilder sb = new StringBuilder();
		sb.append("[condition][]Quando o termo buscado for {termo}=$query: SolrQueryBuilder(getQuery() == \"q={termo}\")");
		sb.append("[consequence][]Imprimir a query=System.out.println(\"Passou pelo Drools:\"+$query.getQuery());");
		dsl.setContent(sb.toString());
		return dsl;
	}
	
	public RuleResource buildDslr(){
		RuleResource dslr = new RuleResource();
		dslr.setResourceName("search_rules.dslr");
		
		StringBuilder sb = new StringBuilder();
		sb
			.append("package br.com.search.rules ")
			.append("import com.solr.dsl.SolrQueryBuilder ")
			.append("expander search.dsl ")
			.append("rule \"Imprimir Query_541dd396549c41a04bfd377c\" ")
			.append("when ")
			.append(" Quando o termo buscado for Iphone ")
			.append("then ")
			.append(" Imprimir a query ")
			.append("end");
		dslr.setContent(sb.toString());
		return dslr;
	}
	
	@Test
	public void test() throws Exception{
		//RuleResource dsl = RuleLoader.loadDslFrom("http://localhost:3000/persistent/dsl");
		//RuleResource dslr= RuleLoader.loadRuleFrom("http://localhost:3000/persistent/rule", "com.solr.dsl.SolrQueryBuilder");
		
		RuleResource dsl = buildDsl();
		RuleResource dslr= buildDslr();
		
		System.out.println("######## DSLs ############");
		System.out.println(dsl.getContent());
		
		System.out.println("\n\n######## RULES ############");
		System.out.println(dslr.getResourceName());
		System.out.println(dslr.getContent());
		
		RulesExecutor6 rulesExecutor6 = new RulesExecutor6(dsl, dslr);
		FirstCommandAggregation queryBuilder = SolrQueryBuilder.newQuery("Iphone");
		Assert.assertEquals(queryBuilder.getQuery(), "q=Iphone");
		rulesExecutor6.execute(queryBuilder);
		System.out.println("Resultado:"+queryBuilder.build());
	}
}
