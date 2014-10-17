package com.solr.dsl.scaffold;

public class FieldBuilder {
	
	public static FieldValue field(String fieldName){
		return new FieldValue(fieldName);
	}
	
	public static FieldValue fieldWithValue(String fieldWithValue){
		//TODO: Implementar tratamento caso o field seja informado juntamente com o valor ex: q=teste
		return new FieldValue(fieldWithValue);
	}
	
	public static class FieldValue{
		private final String fieldName;
		
		private FieldValue(String fieldName){
			this.fieldName = fieldName;
		}
		
		public ScaffoldField value(String value){
			return new ScaffoldField(this.fieldName, value); 
		}
	}
}
