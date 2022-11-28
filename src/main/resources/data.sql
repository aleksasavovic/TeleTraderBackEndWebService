insert into users(username,password,cash) values
('a','1',1000),
('s','1',5000),
('d','1',750);

insert into stock(id,price,name) values
(0,5,'spf500'),
(1,10,'fortune100'),
(2,15,'bitcoin'),
(3,20,'nasdaq'),
(4,25,'etherium'),
(5,30,'microsoft'),
(6,35,'apple'),
(7,40,'google');

insert into user_stock(user_username,stock_id,amount) values
('a',0,200),
('a',1,200),
('a',2,200),
('s',1,500),
('d',1,200),
('d',2,400),
('s',4,100),
('a',4,100),
('d',4,100);

insert into limit_order(id, amount_to_trade, price_to_order_at, order_type,user_username, stock_id) values
(0,1,1,0,'s',1),
(1,2,2,1,'d',1),
(2,2,3,0,'a',1),
(3,2,4,1,'s',1),
(4,10,5,1,'a',1),
(5,2,4,1,'d',1),
(6,2,3,1,'a',1),
(7,2,2,1,'s',1),
(8,10,1,1,'a',1),
(9,2,1,1,'d',1),
(10,2,2,1,'a',1),
(11,2,3,1,'s',1),
(12,10,4,1,'a',1),
(13,2,5,1,'d',1),
(14,2,4,1,'a',1),
(15,2,3,1,'s',1),
(16,10,2,1,'a',1),
(17,2,1,1,'d',1),
(18,2,2,1,'a',1),
(19,2,3,1,'s',1),
(20,10,4,1,'a',1),
(21,2,5,1,'d',1),
(22,2,4,1,'a',1),
(23,2,3,1,'s',1);