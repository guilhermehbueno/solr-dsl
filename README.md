Simple DSL to build complex queries in Solr

Examples:

```java
/**Handling raw queries*/
SBQ.fromRawQuery("q=iphone").info().getQuery(); //iphone
SBQ.fromRawQuery("q=iphone").info().getQuery(); //iphone



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


/*Manipulating previous fields*/
SolrQueryBuilder.newQuery("iphone")
					.sortBy("popularidade").and()
					.listBy("id,name")
					.goToInit()
					.sortBy("popularity") //Changing previous configured sortBy
					.build();
//q=iphone&sort=popularity&fl=id,name"

```


Interfaces

FirstCommandAggregation || SecondCommandAggregation    || ThirdCommandAggregation
QueryCommandAggregation || ConfigureCommandAggregation || FacetCommandAggregation
