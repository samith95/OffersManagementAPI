# OffersManagementAPI

### Assumptions:
1. An offer is a proposal to sell a specific product under a time-bounded condition.
2. Offers must have a description, a price, a defined currency, the date they have been created, the lenght of time the offer is valid and a product.
3. An offer can be cancelled within its validity time frame.
4. An offer will expire automatically after its validity time frame.
5. Products are already available to the API and merchant. 
6. Creating, editing, querying and deleting the products is considered out of scope to this project. 
7. Deriving from assumption 4, a merchant must be able to view expired offers which means, offers must be kept after their validity time frame.

### User stories:
- A merchant can create simple offers.
- A merchant can cancel an offer.
- A merchant can view offers.
