package com.solr.dsl.raw.parser;

import java.util.List;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.annotations.Test;

public class QueryStringParserTest {

    @Test
    public void testQueryParserWithoutDecode(){
	String query = "q=%28_query_%3A%7B%21multiMaxScore+tie%3D0.0+v%3D%27%28%28code_string%3ABlack%29+OR+%28name_text_pt%3ABlack%29+OR+%28category_string_mv%3ABlack%29%29+OR+%28%28code_string%3A%5C%26%29+OR+%28category_string_mv%3A%5C%26%29%29+OR+%28%28code_string%3ADecker%29+OR+%28name_text_pt%3ADecker%29+OR+%28category_string_mv%3ADecker%29%29+OR+%28%28name_text_pt%3ABlack%7E%29%29+OR+%28%28name_text_pt%3A%5C%26%7E%29%29+OR+%28%28name_text_pt%3ADecker%7E%29%29%27%7D%29";
	List<NameValuePair> pairs = QueryStringParser.queryParserWithoutDecode().apply(query);
	Assert.assertNotNull(pairs);
	Assert.assertEquals(pairs.size(), 1);
    }
    
    @Test
    public void testDefaultQueryParser(){
	String query = "q=%28_query_%3A%7B%21multiMaxScore+tie%3D0.0+v%3D%27%28%28code_string%3ABlack%29+OR+%28name_text_pt%3ABlack%29+OR+%28category_string_mv%3ABlack%29%29+OR+%28%28code_string%3A%5C%26%29+OR+%28category_string_mv%3A%5C%26%29%29+OR+%28%28code_string%3ADecker%29+OR+%28name_text_pt%3ADecker%29+OR+%28category_string_mv%3ADecker%29%29+OR+%28%28name_text_pt%3ABlack%7E%29%29+OR+%28%28name_text_pt%3A%5C%26%7E%29%29+OR+%28%28name_text_pt%3ADecker%7E%29%29%27%7D%29";
	List<NameValuePair> pairs = QueryStringParser.defaultQueryParser().apply(query);
	Assert.assertNotNull(pairs);
	Assert.assertEquals(pairs.size(), 1);
	System.out.println(pairs.get(0));
    }
}