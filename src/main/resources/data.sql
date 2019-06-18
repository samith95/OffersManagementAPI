INSERT INTO product (id, name, description) VALUES
	('2147483650', 'desktop', 'HP Precision'),
	('2147483651', 'laptop', 'Lenovo Carbon'),
	('2147483652', 'tablet', 'Huawei x31y'),
	('2147483653', 'smartphone', 'OnePlus 3T'),
	('2147483654', 'speaker', 'Bose Dolby sorround');
INSERT INTO offer (id, description, price, currency_code, created_on, days_valid_for, status, productid) VALUES
	('100','January Sale', '750','EUR','01/01/2019','30','expired','2147483651'),
	('101','February Sale', '650','GBP','01/02/2019','27','cancelled','2147483651'),
	('102','March Sale', '650','GBP','01/03/2019','27','valid','2147483651'),
	('103','Summer Sale', '550','GBP','01/06/2019','90','valid','2147483651');