Simple DSL to build complex queries in Solr

Examples:
<h3>Handling raw queries</h3>
```java
 SQB.fromRawQuery("q=iphone")
 		.info().getQuery(); // "iphone"
 SQB.fromRawQuery("q=iphone&fq=name:teste&fq=category:categoryName")
 		.info().getFilterQueries().size(); // "2"
 SQB.fromRawQuery("q=iphone&fq=name:teste&sort=popularity")
 		.info().getSortBy(); // "popularity"
 SQB.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&fl=id,name")
 		.info().getFieldList(); // "id,name"
 SQB.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.field=category")
 		.info().getFacetFields(); // "category"
 SQB.fromRawQuery("q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category")
 		.info().getFacetQueries(); // "teste"

```

<h3>Preserve unacknowleged query params</h3>		
```java 		
SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&unack=true").build();
// "q=iphone&fq=name:teste&unack=true"
```

<h3>[Future] Handling unacknowleged query params</h3>
```java
SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste&unack=true")
		.upsert(field("unack").value("false"))
		.build();
// "q=iphone&fq=name:teste&unack=false

SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste")
		.upsert(field("facet.custom").value("xpto"))
		.build();
// "q=iphone&fq=name:teste&facet.custom=xpto

SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste")
		.upsert(field("facet.custom").value("xpto"))
		.upsert(field("facet.custom").value("xpto1"))
		.build();
// "q=iphone&fq=name:teste&facet.custom=xpto1

SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste")
		.update(field("facet.custom").value("xpto"))
		.build();
// "q=iphone&fq=name:teste

SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste")
		.add(field("facet.custom").value("xpto"))
		.add(field("facet.custom").value("xpto1"))
		.build();
// "q=iphone&fq=name:teste&facet.custom=xpto&facet.custom=xpto1

SolrQueryBuilder.fromRawQuery("q=iphone&fq=name:teste")
		.add(field("facet.custom").value("xpto"))
		.remove(field("facet.custom"))
		.build();
// "q=iphone&fq=name:teste
```


<h3>Building query from DSL</h3>
```java
SQB.newQuery("iphone")
	.build(); 
//"q=iphone" 

SQB.newQuery("iphone")
					.filterBy("name:teste")
					.build(); 
//"q=iphone&fq=name:teste"

SQB.newQuery("iphone")
					.filterBy("name:teste")
					.filterBy("category:categoryName")
					.build(); 
//"q=iphone&fq=name:teste&fq=category:categoryName"

SQB.newQuery("iphone")
					.filterBy("name:teste")
					.sortBy("popularity")
					.build(); 
//"q=iphone&fq=name:teste&sort=popularity"

SQB.newQuery("iphone")
					.filterBy("name:teste")
					.sortBy("popularity").and()
					.listBy("id,name")
					.build(); 
//"q=iphone&fq=name:teste&sort=popularity&fl=id,name"

SQB.newQuery("iphone")
					.filterBy("name:teste")
					.sortBy("popularity").and()
					.listBy("id,name").and()
					.facetByField("category")
					.build(); 
//"q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.field=category"

SQB.newQuery("iphone")
					.filterBy("name:teste")
					.sortBy("popularity").and()
					.listBy("id,name").and()
					.facetByField("category")
					.facetByQuery("teste")
					.build();
//"q=iphone&fq=name:teste&sort=popularity&facet=true&fl=id,name&facet.query=teste&facet.field=category"
````

<h3>Manipulating previous fields</h3>
```java
SolrQueryBuilder.newQuery("iphone")
					.sortBy("popularidade").and()
					.listBy("id,name")
					.goToInit()
					.sortBy("popularity") //Changing previous configured sortBy
					.build();
//q=iphone&sort=popularity&fl=id,name"

```


Interfaces

SolrQuery || SecondCommandAggregation    || ThirdCommandAggregation
QueryCommandAggregation || ConfigureCommandAggregation || FacetCommandAggregation
