package com.solr.dsl.encoding;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EncodingUtilsTest {
    
    @DataProvider(name="encode")
    public Object[][] queriesToBeEncoded(){
	return new Object[][]{
	    {"((name_text_pt:Black~))", "%28%28name_text_pt%3ABlack%7E%29%29"},
	    {"Marca_ClassMaster_string name_text_pt^1 name_text_pt_s firstName^1 firstName_s code_string", "Marca_ClassMaster_string%20name_text_pt%5E1%20name_text_pt_s%20firstName%5E1%20firstName_s%20code_string"},
	    {" ","%20"},
	    {"+","%2B"}
	};
    }
    
    @Test(dataProvider="encode")
    public void shouldEncode(String query, String expected){
	String result = EncodingUtils.encode(query);
	Assert.assertEquals(expected, result);
    }
    
    @Test(dataProvider="encode")
    public void shouldDecode(String expected, String query){
	String result = EncodingUtils.decode(query);
	Assert.assertEquals(expected, result);
    }
}