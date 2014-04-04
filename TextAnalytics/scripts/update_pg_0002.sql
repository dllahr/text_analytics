alter table regression_model add model_type varchar(20);

update regression_model set model_type='Price';

alter table regression_model add check (model_type in ('Price', 'Volatility'));
