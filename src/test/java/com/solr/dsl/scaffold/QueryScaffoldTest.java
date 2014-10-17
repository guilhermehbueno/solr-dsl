package com.solr.dsl.scaffold;

import static com.solr.dsl.scaffold.FieldBuilder.field;

import org.testng.Assert;
import org.testng.annotations.Test;

public class QueryScaffoldTest {
	
	@Test
	public void shouldBuildQueryScaffold(){
		QueryScaffold scaffold = new QueryScaffold();
		scaffold.add(field("q").value("iphone"));
		
		ScaffoldField query = scaffold.getByName("q");
		Assert.assertNotNull(query);
		Assert.assertEquals(query.getValue(), "iphone");
		
		boolean removed = scaffold.remove(query);
		Assert.assertTrue(removed);
		removed = scaffold.remove(query);
		Assert.assertFalse(removed);
	}
}