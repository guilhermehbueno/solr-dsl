package com.solr.dsl.raw;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.solr.dsl.scaffold.ScaffoldField;

public class SolrQueryRawExtractor {
	
	public static String getQuery(String queryString){
		return getSingleQueryParamValue(queryString, "q");
	}
	
	public static List<NameValuePair> getFilterQueries(String queryString){
		return getMultiQueryParamValue(queryString, "fq");
	}
	
	public static List<ScaffoldField> getUnacknowledgedQueryParams(final List<String> fields, String queryString){
		List<NameValuePair> parse = URLEncodedUtils.parse(queryString, Charset.defaultCharset());
		return parse.stream().filter( field -> !fields.contains(field.getName())).map(field -> new ScaffoldField(field.getName(), field.getValue())).collect(Collectors.toList());
	}
	
	public static String getSingleQueryParamValue(String queryString, String paramName){
		List<NameValuePair> parse = URLEncodedUtils.parse(queryString, Charset.defaultCharset());
		String result=null;
		for (NameValuePair nameValuePair : parse) {
			if(nameValuePair.getName().equalsIgnoreCase(paramName)){
				result=nameValuePair.getValue();
			}
		}
		return result;
	}
	
	public static List<NameValuePair> getMultiQueryParamValue(String queryString, String paramName){
		List<NameValuePair> parse = URLEncodedUtils.parse(queryString, Charset.defaultCharset());
		List<NameValuePair> results = new ArrayList<NameValuePair>();
		for (NameValuePair nameValuePair : parse) {
			if(nameValuePair.getName().equalsIgnoreCase(paramName)){
				results.add(nameValuePair);
			}
		}
		return results;
	}
}
