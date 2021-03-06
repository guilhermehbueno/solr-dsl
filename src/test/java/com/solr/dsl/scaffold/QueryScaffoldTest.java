package com.solr.dsl.scaffold;

import static com.solr.dsl.scaffold.FieldBuilder.field;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class QueryScaffoldTest {
    
    	@Test
    	public void shouldGetMultiFieldByName(){
    	    QueryScaffold scaffold = new QueryScaffold();
    	    scaffold.add(new ScaffoldField("facet.field", "1", null));
    	    scaffold.add(new ScaffoldField("facet.field", "2", null));
    	    scaffold.add(new ScaffoldField("facet.field", "3", null));
    	    
    	    List<ScaffoldField> multiByName = scaffold.getMultiByName("facet.field");
    	    Assert.assertEquals(3, multiByName.size());
    	    System.out.println(scaffold.toString());
    	}
	
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
	
	@Test
	public void shouldBuildQueryScaffoldToString(){
	    QueryScaffold queryScaffold = new QueryScaffold();
	    queryScaffold.add(field("q").value("iphone"));
	    queryScaffold.add(field("fl").value("id,name"));
	    queryScaffold.add(new ScaffoldField("facet.field", "1", null));
	    queryScaffold.add(new ScaffoldField("facet.field", "2", null));
	    queryScaffold.add(new ScaffoldField("facet.field", "3", null));
	    String query = queryScaffold.toString();
	    Assert.assertEquals(query, "q=iphone&fl=id,name&facet.field=1&facet.field=2&facet.field=3");
	    System.out.println(queryScaffold.buildToJson());
	}
}