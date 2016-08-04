package com.ruler;

import org.testng.Assert;
import org.testng.annotations.Test;

import rule.client.RuleLoader;
import rule.client.RuleResource;
import rule.client.RulesExecutor6;

import com.solr.dsl.SolrQueryBuilder;
import com.solr.dsl.views.SmartQuery;

public class RulerTest {
	
	@Test(enabled=false)
	public void testFromRuleEditor() throws Exception{
		RuleResource dsl = RuleLoader.loadDslFrom("http://localhost:3000/persistent/dsl");
		RuleResource dslr= RuleLoader.loadRuleFrom("http://localhost:3000/persistent/rule", "com.solr.dsl.SolrQueryBuilder");
		
		System.out.println("######## DSLs ############");
		System.out.println(dsl.getContent());
		
		System.out.println("\n\n######## RULES ############");
		System.out.println(dslr.getResourceName());
		System.out.println(dslr.getContent());
		
		RulesExecutor6 rulesExecutor6 = new RulesExecutor6(dsl, dslr);
		SmartQuery queryBuilder = SolrQueryBuilder.newQuery("Iphone");
		Assert.assertEquals(queryBuilder.info().getQuery(), "q=Iphone");
		rulesExecutor6.execute(queryBuilder);
		System.out.println("Resultado:"+queryBuilder.build());
	}
	
	@Test
	public void test() throws Exception{
		RuleResource dsl = new RuleResource();
		RuleResource dslr= new RuleResource();
		
		dsl.setResourceName("search.dsl");
		dslr.setResourceName("search.dslr");
		
		RulesExecutor6 rulesExecutor6 = new RulesExecutor6(dsl, dslr);
		SmartQuery queryBuilder = SolrQueryBuilder.newQuery("Iphone");
		Assert.assertEquals(queryBuilder.info().getQuery(), "q=Iphone");
		rulesExecutor6.execute(queryBuilder);
		System.out.println("Resultado:"+queryBuilder.build());
	}
}
