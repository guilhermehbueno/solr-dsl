package com.solr.dsl.scaffold;

public final class ScaffoldField {
    
	private String name;
	private String value;
	private String group;
	
	public ScaffoldField(String name, String value, String groupName) {
		super();
		this.name = name;
		this.value = value;
		this.group = groupName;
	}
	
	public String getGroup() {
	    return this.group;
	}

	public void setGroup(String group) {
	    this.group = group;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isNameValuePair(){
	    return this.value.contains(":");
	}
	
	public NameValuePair getNameValuePair(){
	    return new NameValuePair(this);
	}
	
	@Override
	public String toString() {
		return this.name+"="+this.value;
	}
	
	public static class NameValuePair{
	    
	    private final ScaffoldField field;
	    private String name;
	    private String value;
	    
	    public NameValuePair(ScaffoldField field) {
		super();
		this.field = field;
		String[] values = this.field.getValue().split(":");
		this.name = values[0];
		this.value = values[1];
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	        field.setValue(this.name+":"+this.value);
	    }

	    public String getValue() {
	        return value;
	    }

	    public void setValue(String value) {
	        this.value = value;
	        field.setValue(this.name+":"+this.value);
	    }
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScaffoldField other = (ScaffoldField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}