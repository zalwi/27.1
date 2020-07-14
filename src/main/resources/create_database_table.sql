CREATE TABLE transactions (
	id BIGINT PRIMARY KEY auto_increment,
	trans_type VARCHAR(20) NOT NULL,
	trans_descr VARCHAR(500) NOT NULL,
	amount DECIMAL(10, 2),
	date_time DATETIME
);