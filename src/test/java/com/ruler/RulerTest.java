package com.ruler;

import org.testng.annotations.Test;

public class RulerTest {
	
	@Test(enabled=false)
	public void testFromRuleEditor() throws Exception{
//		RuleResource dsl = RuleLoader.loadDslFrom("http://localhost:3000/persistent/dsl");
//		RuleResource dslr= RuleLoader.loadRuleFrom("http://localhost:3000/persistent/rule", "com.solr.dsl.SolrQueryBuilder");
//		
//		System.out.println("######## DSLs ############");
//		System.out.println(dsl.getContent());
//		
//		System.out.println("\n\n######## RULES ############");
//		System.out.println(dslr.getResourceName());
//		System.out.println(dslr.getContent());
//		
//		RulesExecutor6 rulesExecutor6 = new RulesExecutor6(dsl, dslr);
//		SolrQuery queryBuilder = SolrQueryBuilder.newQuery("Iphone");
//		Assert.assertEquals(queryBuilder.info().getQuery(), "q=Iphone");
//		rulesExecutor6.execute(queryBuilder);
//		System.out.println("Resultado:"+queryBuilder.build());
	}
	
	@Test(enabled=false)
	public void test() throws Exception{
//		RuleResource dsl = new RuleResource();
//		RuleResource dslr= new RuleResource();
//		
//		dsl.setResourceName("search.dsl");
//		dslr.setResourceName("search.dslr");
//		
//		RulesExecutor6 rulesExecutor6 = new RulesExecutor6(dsl, dslr);
//		SolrQuery queryBuilder = SolrQueryBuilder.newQuery("Iphone");
//		Assert.assertEquals(queryBuilder.info().getQuery(), "q=Iphone");
//		rulesExecutor6.execute(queryBuilder);
//		System.out.println("Resultado:"+queryBuilder.build());
	}
}
