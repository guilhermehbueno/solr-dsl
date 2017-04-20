package com.solr.dsl.encoding;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EncodingUtilsTest {
    
    @DataProvider(name="encode")
    public Object[][] queriesToBeEncoded(){
	return new Object[][]{
	    {"((name_text_pt:Black~))", "%28%28name_text_pt%3ABlack%7E%29%29"}
	};
    }
    
    @Test(dataProvider="encode")
    public void shouldEncode(String query, String expected){
	String result = EncodingUtils.encode(query);
	Assert.assertEquals(expected, result);
    }
}