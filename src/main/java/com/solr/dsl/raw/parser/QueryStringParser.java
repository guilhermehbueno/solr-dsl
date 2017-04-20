package com.solr.dsl.raw.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.solr.dsl.encoding.EncodingUtils;

public class QueryStringParser {
    
    public static java.util.function.Function<String, List<NameValuePair>> queryParserWithoutDecode() {
	return query -> {
	    String[] params = query.split("&");
	    List<NameValuePair> pairs = Arrays.asList(params).stream().map(param -> {
		String[] val = param.split("=");
		if (val.length > 1)
		    return new BasicNameValuePair(val[0], val[1]);
		return new BasicNameValuePair(val[0], null);
	    }).collect(Collectors.toList());
	    return pairs;
	};
    }
    
    public static java.util.function.Function<String, List<NameValuePair>> defaultQueryParser() {
   	return query -> {
   	    String[] params = query.split("&");
   	    List<NameValuePair> pairs = Arrays.asList(params).stream().map(param -> {
   		String[] val = param.split("=");
   		if (val.length > 1)
   		    return new BasicNameValuePair(EncodingUtils.decode(val[0]), EncodingUtils.decode(val[1]));
   		return new BasicNameValuePair(val[0], null);
   	    }).collect(Collectors.toList());
   	    return pairs;
   	};
       }

}
