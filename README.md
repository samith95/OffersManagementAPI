# OffersManagementAPI

OffersManagementAPI is a simple Spring Boot Java RESTful API designed to offer merchant a convenient way of storing and keep record of their offers. Offers are managed through requests to the API.

Please read the assumptions made in the "Assumptions" section. 

To set up the application and to run the tests, please follow the "How to run" section.

## User stories:
- A merchant can create simple offers.
- A merchant can cancel an offer.
- A merchant can view an offer.


## Assumptions:
1. An offer is a proposal to sell a specific product under a time-bounded condition.
2. Offers must have a description, a price, a defined currency, the date they have been created, the length of time the offer is valid and a product.
3. An offer can be cancelled within its validity time frame.
4. An offer will expire automatically after its validity time frame.
5. Products are already available to the API and merchant. 
6. Creating, editing, querying and deleting the products is considered out of scope to this project. 
7. Deriving from assumption 4, a merchant must be able to view expired offers which means, offers must be kept after their validity time frame.
8. An offer can have three statuses: 

    | State | Description | 
    | ------ | ------ |
    | Valid | the offer is within its validity time frame |
    | Cancelled | the offer has been cancelled during its validity time frame |
    | Expired | the offer is beyond its validity time frame |
    
9. A product has a name and description and for simplicity, all products are stored in a json file in the resources directory in "src/main/java/model/resources".
10. The starting date of the offer is equal to the creation date.
11. Once cancelled or expired, an offer cannot be valid again.
12. An offer is valid for a finite number of days e.g. 3, meaning the offer will be valid for 3 days from the creation date.

## How to run

### Prerequisites: 
- JDK 1.8
- Maven 3.3.9

### Build and run the api: 

1. Clone this repository 
2. Make sure you are using JDK 1.8 and Maven 3.3.9 or above.
3. You can build the project and run the tests by running ```mvn clean package``` in cloned "OffersMangementAPI/" project folder.
4. Once successfully built, you can run the service by one of these two methods:
```
        java -jar -Dspring.profiles.active=test target/OffersManagementAPI-0.2.0.jar
or
        mvn spring-boot:run -Drun.arguments="spring.profiles.active=test"
```

Once the application runs you should see something like this:

```
2019-06-20 07:14:10.105  INFO 8440 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-06-20 07:14:10.112  INFO 8440 --- [           main] api.Application                          : Started Application in 6.246 seconds (JVM running for 10.88)
```

The API will run on ```http://localhost:8080/``` the usage is described in the "Usage" section of this README.

## How to use

### Prerequisites: 
- Postman (https://www.getpostman.com/) 

### Use with Postman

1. Run API using the instructions given in the "How to run" section.
2. Open Postman.
3. Click on File -> Import -> Import File -> Choose Files.
4. Navigate into the cloned project repository.
5. Select file "OffersManagementAPI.postman_collection" in "OffersManagementAPI".
6. Press Open.
7. On the right hand side of Postman you could now have a collection called "OffersManagementAPI" with 3 requests:
	- POST request to create a new offer (the body is already initialised to a mock offer).
	- GET request to retrieve a created or existing offer.
	- PUT request to cancel an existing offer.

### Database existing objects:

In order to facilitate the testing of all the api functionalities, some values have already been inserted in the database.

#### Products:

| ID | Name | Description |
| ------ | ------ | ------ |
| 2147483650 | desktop | HP Precision |
| 2147483651 | laptop | Lenovo Carbon |
| 2147483652 | tablet | Huawei x31y |
| 2147483653 | smartphone | OnePlus 3T |
| 2147483654 | speaker | Bose Dolby sorround |

#### Offers:

| ID | Description | Price | Currency code | Created on | Days valid for | status | ProductID |
| ------ | ------ | ------ | ------ | ------ | ------ | ------ | ------ |
| 100 | January Sale | 750 | EUR | 01/01/2019 | 30 | expired | 2147483651 |
| 101 | February Sale | 650 | GBP | 01/02/2019 | 27 | cancelled | 2147483651 |
| 102 | March Sale | 650 | GBP | 01/03/2019 | 27 | valid | 2147483651 |
| 103 | Summer Sale | 550 | GBP | 01/06/2019 | 90 | valid | 2147483651 |

Offer 102 is valid as no retrieval has been done on it, hence when retrieved, it should return an expired offer.

## Documentation

Javadocs can be found in "OffersMangementAPI\doc" directory.

## Usage

### List specific offer

**Definition**

`GET /offer/<identifier>`

**Response**

- `200 OK` on success

```json
{
	"id":"unique offer ID",
	"description":"offer's friendly description",
	"price":"the price of the offer",
	"currencyCode":"currency code of the price e.g. USD",
	"createdOn":"date on which the offer was created",
	"daysValidFor":"days the offer is valid for",
	"productID":"id of the product on which the offer is based",
	"status":"defines what the status of the offer is i.e. valid, expired or cancelled"
}
```

- `404 Not Found` if the offer does not exist


### Creating a new offer

**Definition**

`POST /offer`

**Arguments**

- `"description":"string"` offer's friendly description
- `"productID":"number"` id of the product on which the offer is based
- `"price":"number"` price of the offer
- `"currencyCode":"string"` three letter currency code of the price e.g. USD
- `"daysValidFor":"number"` days the offer is valid for

**Response**

- `201 Created` on success

```json
{
	"id":"unique offer ID"
}
```

### Cancelling an offer

**Definition**

`PUT /offer/cancel/<offer identifier>`

**Response**

- `204 No Content` on successfull update
- `404 Not found` in case the required offer does not exist

## Future improvements:

### Structural changes:
- Depending on the merchant requirements, a builder design patter to create the offers would give the ability to combine various types of catalogue items together, for example, instead of having just a product assigned to the offer, the merchant might want to add an insurance service on the good or some other kind of service. Services as opposed to products are different in terms of some variables, but they do have in common ID, name and a description. Hence, a superclass called CatalogueItem could be implemented in order to have both Product and Service as its child-classes. 

### Functional changes:
- Caching should be implemented in order to not retrieve the data from the db or currently memory. This, combined with scaling this application in Kubernetes nodes, will reduce the request response latency.
Metrics should be implemented in order to gain knowledge about which functions are the most used and consequently, make those functions more robust or efficient than they are.
- Add context so that requests thread can be stopped when issue occurs rather than passing the issue back until the handler function. e.g. if an offer is not present in db, an empty offer is sent back to the calling function which will have to check whether the returned obj is empty and so on until the empty object is received by the handler which will decide the error code and what to send back. By using context we should be able to terminate the request's thread and return error code appropriately.
- Dockerize the project so that it can be brought up and teared down easily, it can also be used in an orchestration such as Kubernetes to enhance scalability.

### Documentation changes:
- Implement swagger for endpoint documentation, this will enable an easy to read, comprehendible and directly testable API
