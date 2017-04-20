package com.solr.dsl.raw;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.solr.dsl.scaffold.ScaffoldField;

public class SolrQueryRawExtractor {
	
	public static String getQuery(List<NameValuePair> pairs){
		return getSingleQueryParamValue("q", pairs);
	}
	
	public static List<NameValuePair> getFilterQueries(List<NameValuePair> pairs){
		return getMultiQueryParamValue("fq", pairs);
	}
	
	@Deprecated
	public static List<NameValuePair> parseQueryString(String queryString) {
	    List<NameValuePair> parse = URLEncodedUtils.parse(queryString, Charset.defaultCharset());
	    return parse;
	}
	
	public static List<ScaffoldField> getUnacknowledgedQueryParams(final List<String> fields,List<NameValuePair> pairs){
		return pairs.stream().filter( field -> !fields.contains(field.getName())).map(field -> new ScaffoldField(field.getName(), field.getValue(), "")).collect(Collectors.toList());
	}
	
	public static String getSingleQueryParamValue(String paramName, List<NameValuePair> pairs){
		String result=null;
		for (NameValuePair nameValuePair : pairs) {
			if(nameValuePair.getName().equalsIgnoreCase(paramName)){
				result=nameValuePair.getValue();
			}
		}
		return result;
	}

	public static List<NameValuePair> getMultiQueryParamValue(String paramName, List<NameValuePair> pairs){
		List<NameValuePair> results = new ArrayList<>();
		for (NameValuePair nameValuePair : pairs) {
			if(nameValuePair.getName().equalsIgnoreCase(paramName)){
				results.add(nameValuePair);
			}
		}
		return results;
	}
}
