package com.solr.dsl.learning;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.testng.Assert;
import org.testng.annotations.Test;


public class URLEncodedUtilsLearningTest {
    
    @Test
    public void test(){
	List<NameValuePair> pairs = URLEncodedUtils.parse("q=*%3A*&fq=%7B%21ex%3Dcor%7DMarca_ClassMaster_string%3ABlack+%26+Decker&facet.field=%7B%21ex%3Dcor%7Dspec_fct_Cor", Charset.defaultCharset());
	Assert.assertEquals(pairs.size(), 3);
	
//	List<NameValuePair> pairs = URLEncodedUtils.parse("fq=Marca_ClassMaster_string:Black\\ \\&\\ Decker", Charset.defaultCharset());
//	Assert.assertEquals(pairs.size(), 1);
    }

}
