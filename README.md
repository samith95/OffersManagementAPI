# OffersManagementAPI

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

## User stories:
- A merchant can create simple offers.
- A merchant can cancel an offer.
- A merchant can view an offer.

## Future improvements:

### Structural changes:
Depending on the merchant requirements, a builder design patter to create the offers would give the ability to combine various types of catalogue items together, for example, instead of having just a product assigned to the offer, the merchant might want to add an insurance service on the good or some other kind of service. Services as opposed to products are different in terms of some variables, but they do have in common ID, name and a description. Hence, a superclass called CatalogueItem could be implemented in order to have both Product and Service as its child-classes. 

### Functional changes:
Caching should be implemented in order to not retrieve the data from the db or currently memory. This, combined with scaling this application in Kubernetes nodes, will reduce the request response latency.
Metrics should be implemented in order to gain knowledge about which functions are the most used and consequently, make those functions more robust or efficient than they are.

## Quickstart

TODO

## Usage

### List specific offer

**Definition**

`GET /offer/<identifier>`

**Response**

- `200 OK` on success

```json
{
	"id":"unique offer ID",
	"productID":"id of the product on which the offer is based",
	"description":"offer's friendly description",
	"price":"the price of the offer",
	"currencyCode":"currency code of the price e.g. USD",
	"createdOn":"date on which the offer was created",
	"daysValidFor":"days the offer is valid for",
	"status":"defines what the status of the offer is i.e. valid, expired or cancelled"
}
```

- `404 Not Found` if the offer does not exist


### Creating a new offer

**Definition**

`POST /offer`

**Arguments**

- `"description":"string"` offer's friendly description
- `"productID":"string"` id of the product on which the offer is based
- `"price":"string"` price of the offer
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

`PUT /offer/<offer identifier>`

**Arguments**

- `"status":"string"` defines what the status of the offer is i.e. valid, expired or cancelled

**Response**

- `204 No Content` on successfull update
- `404 Not found` in case the required offer does not exist