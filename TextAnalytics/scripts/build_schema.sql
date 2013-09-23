--------------------------------------------------------
--  DDL for Table ACTIVITY
--------------------------------------------------------

  CREATE TABLE ACTIVITY 
   (	ID NUMBER(*,0), 
	ACTIVITY_DATE DATE, 
	TRANSACTION VARCHAR2(200 BYTE), 
	DESCRIPTION VARCHAR2(200 BYTE), 
	SYMBOL VARCHAR2(100 BYTE), 
	QTY NUMBER(*,0), 
	FILL_PRICE NUMBER, 
	COMMISSION NUMBER, 
	NET_AMOUNT NUMBER
   );

--------------------------------------------------------
--  DDL for Table ARTICLE_SOURCE
--------------------------------------------------------
create table article_source (
  id                  integer primary key,
  description         varchar2(4000)
);

--------------------------------------------------------
--  DDL for Table ARTICLE
--------------------------------------------------------
  CREATE TABLE ARTICLE 
   (	ID NUMBER(*,0), 
	ARTICLE_SOURCE_ID NUMBER(*,0), 
	PUBLISH_DATE DATE, 
	DAY_INDEX NUMBER(*,0), 
	FILENAME VARCHAR2(4000 BYTE),
	start_line_number integer,
	additional_identifier varchar2(4000),
	
	foreign key (article_source_id) references article_source(id)
   );

   COMMENT ON COLUMN ARTICLE.DAY_INDEX IS 'integer representing day that data is for';
   
CREATE UNIQUE INDEX ARTICLE_UK1 ON ARTICLE (FILENAME);
	 
create sequence article_id_seq start with 1;

--------------------------------------------------------
--  DDL for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  CREATE TABLE ARTICLE_PC_VALUE 
   (	ARTICLE_ID NUMBER(*,0), 
	EIGENVALUE_ID NUMBER(*,0), 
	VALUE BINARY_FLOAT
   );
--------------------------------------------------------
--  DDL for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  CREATE TABLE ARTICLE_STEM_COUNT 
   (	ARTICLE_ID NUMBER(*,0), 
	STEM_ID NUMBER(*,0), 
	COUNT NUMBER(*,0)
   );

   COMMENT ON TABLE ARTICLE_STEM_COUNT  IS 'represents the number of times each stem occurred in each article.  also known as word vector, sparse format';
--------------------------------------------------------
--  DDL for Table COMPANY
--------------------------------------------------------

  CREATE TABLE COMPANY 
   (	ID NUMBER(*,0), 
	NAME VARCHAR2(100 BYTE), 
	STOCK_SYMBOL VARCHAR2(20 BYTE)
   );
--------------------------------------------------------
--  DDL for Table COMPANY_SCORING_MODEL
--------------------------------------------------------

  CREATE TABLE COMPANY_SCORING_MODEL
   (	COMPANY_ID NUMBER(*,0), 
	SCORING_MODEL_ID NUMBER(*,0)
   );
--------------------------------------------------------
--  DDL for Table EIGENVALUE
--------------------------------------------------------

  CREATE TABLE EIGENVALUE 
   (	ID NUMBER(*,0), 
	SCORING_MODEL_ID NUMBER(*,0), 
	SORT_INDEX NUMBER(*,0), 
	VALUE BINARY_FLOAT
   );

   COMMENT ON COLUMN EIGENVALUE.SORT_INDEX IS 'relative index of an eigenvalue within the set present for a company_id when sorting by value';
   
   create sequence eigenvalue_id_seq start with 1;
--------------------------------------------------------
--  DDL for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  CREATE TABLE EIGENVECTOR_VALUE 
   (	EIGENVALUE_ID NUMBER(*,0), 
	ARTICLE_ID NUMBER(*,0), 
	VALUE BINARY_FLOAT
   );

   COMMENT ON TABLE EIGENVECTOR_VALUE  IS 'if the eigenvectors are the columns of a matrix, the column index is wrt eigenvalues is eigenvalue_id, the row index is article_id';

--------------------------------------------------------
--  DDL for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  CREATE TABLE PRINCIPAL_COMPONENT 
   (	EIGENVALUE_ID NUMBER(*,0), 
	STEM_ID NUMBER(*,0), 
	VALUE BINARY_FLOAT
   );
--------------------------------------------------------
--  DDL for Table REGRESSION_MODEL
--------------------------------------------------------

  CREATE TABLE REGRESSION_MODEL 
   (	ID NUMBER(*,0), 
	DAY_OFFSET NUMBER(*,0), 
	SCORING_MODEL_ID NUMBER(*,0), 
	R_EXPRESSION VARCHAR2(4000 BYTE)
   );
alter table regression_model add company_id integer;
alter table regression_model add foreign key (company_id) references company(id);

create sequence regression_model_id_seq start with 1 increment by 1;
  
--------------------------------------------------------
--  DDL for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  CREATE TABLE REGRESSION_MODEL_COEF
   (	ID NUMBER(*,0) primary key, 
	REGRESSION_MODEL_ID NUMBER(*,0), 
	EIGENVALUE_ID NUMBER(*,0), 
	COEF NUMBER,
	
	foreign key (regression_model_id) references regression_model(id),
	foreign key (eigenvalue_id) references eigenvalue(id)
   ); 

create sequence regression_model_coef_id_seq start with 1 increment by 1;

--------------------------------------------------------
--  DDL for Table SCORING_MODEL
--------------------------------------------------------

  CREATE TABLE SCORING_MODEL 
   (	ID NUMBER(*,0), 
	NOTES VARCHAR2(4000 BYTE)
   );
   
alter table scoring_model add articles_normalized number(1,0);
alter table scoring_model modify articles_normalized not null;
alter table scoring_model add no_stop_words number(1,0);
alter table scoring_model modify no_stop_words not null;

--------------------------------------------------------
--  DDL for Table STAGING_STOCK_DATA
--------------------------------------------------------

  CREATE TABLE STAGING_STOCK_DATA 
   (	DAY_TIME DATE, 
	OPEN FLOAT(126), 
	HIGH FLOAT(126), 
	LOW FLOAT(126), 
	CLOSE FLOAT(126), 
	VOLUME NUMBER(*,0), 
	ADJ_CLOSE FLOAT(126)
   );
--------------------------------------------------------
--  DDL for Table STEM
--------------------------------------------------------

  CREATE TABLE STEM 
   (	ID NUMBER(*,0), 
	article_source_id NUMBER(*,0), 
	TEXT VARCHAR2(500 BYTE),
	is_stop number(1,0) not null,
	
	foreign key (article_source_id) references article_source(id)
   );

   COMMENT ON TABLE STEM  IS 'contains the set of stems found for articles for each company';
   
   create sequence stem_id_seq start with 1;

--------------------------------------------------------
--  DDL for Table STOCK_DATA
--------------------------------------------------------

  CREATE TABLE STOCK_DATA 
   (	COMPANY_ID NUMBER(*,0), 
	DAY_TIME DATE, 
	OPEN BINARY_FLOAT, 
	HIGH BINARY_FLOAT, 
	LOW BINARY_FLOAT, 
	CLOSE BINARY_FLOAT, 
	VOLUME NUMBER(*,0), 
	ADJ_CLOSE BINARY_FLOAT, 
	DAY_INDEX NUMBER(*,0)
   );

   COMMENT ON COLUMN STOCK_DATA.DAY_INDEX IS 'integer representing day that data is for';

   CREATE INDEX STOCK_DATA_DAY_INDEX ON STOCK_DATA (COMPANY_ID, DAY_INDEX);
   

   
create table mean_stem_count (
  scoring_model_id          integer,
  stem_id                   integer,
  value                     NUMBER,
  primary key (scoring_model_id, stem_id),
  foreign key (scoring_model_id) references scoring_model(id),
  foreign key (stem_id) references stem(id)
);

create table prediction_model (
  id                      integer,
  regression_model_id     integer,
  
  lower_threshold         number,
  upper_threshold         number,
  
  percentile0_value       number,
  percentile25_value      number,
  percentile50_value      number,
  percentile75_value      number,
  percentile100_value     number,

  primary key (id),
  foreign key (regression_model_id) references regression_model(id)
);

create table prdctn_mdl_stock_smooth_coef (
  prediction_model_id                     integer,
  relative_day_index                      integer,
  coef                                    number not null,
  
  primary key (prediction_model_id, relative_day_index)
);
--------------------------------------------------------
--  DDL for Index EIGVECT_VALUE_EIGENVALUE_ID
--------------------------------------------------------

  CREATE INDEX EIGVECT_VALUE_EIGENVALUE_ID ON EIGENVECTOR_VALUE (EIGENVALUE_ID);
--------------------------------------------------------
--  DDL for Index STOCK_DATA_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX STOCK_DATA_COMPANY_INDEX ON STOCK_DATA (COMPANY_ID);
--------------------------------------------------------
--  DDL for Index EIGENVECTOR_VALUE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX EIGENVECTOR_VALUE_PK ON EIGENVECTOR_VALUE (EIGENVALUE_ID, ARTICLE_ID);
--------------------------------------------------------
--  DDL for Index EIGENVALUE_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX EIGENVALUE_COMPANY_INDEX ON EIGENVALUE (SCORING_MODEL_ID);

--------------------------------------------------------
--  DDL for Index PC_EIGVAL_INDEX
--------------------------------------------------------

  CREATE INDEX PC_EIGVAL_INDEX ON PRINCIPAL_COMPONENT (EIGENVALUE_ID);
--------------------------------------------------------
--  DDL for Index ARTICLE_STEM_COUNT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX ARTICLE_STEM_COUNT_PK ON ARTICLE_STEM_COUNT (ARTICLE_ID, STEM_ID);
--------------------------------------------------------
--  DDL for Index ARTICLE_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX ARTICLE_COMPANY_INDEX ON ARTICLE (SCORING_MODEL_ID);
--------------------------------------------------------
--  DDL for Index STOCKPRICE_CHANGE_EIG_IND
--------------------------------------------------------

  CREATE INDEX STOCKPRICE_CHANGE_EIG_IND ON STOCKPRICE_CHANGE (EIGENVALUE_ID);
--------------------------------------------------------
--  DDL for Index ARTICLE_STEM_COUNT_INDEX
--------------------------------------------------------

  CREATE INDEX ARTICLE_STEM_COUNT_INDEX ON ARTICLE_STEM_COUNT (ARTICLE_ID);
--------------------------------------------------------
--  DDL for Index STEM_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX STEM_COMPANY_INDEX ON STEM (SCORING_MODEL_ID);
--------------------------------------------------------
--  Constraints for Table STOCKPRICE_CHANGE_CALC
--------------------------------------------------------

  ALTER TABLE STOCKPRICE_CHANGE_CALC MODIFY (COMPANY_ID NOT NULL ENABLE);
  ALTER TABLE STOCKPRICE_CHANGE_CALC ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  ALTER TABLE ARTICLE_PC_VALUE ADD PRIMARY KEY (ARTICLE_ID, EIGENVALUE_ID);
--------------------------------------------------------
--  Constraints for Table STEM
--------------------------------------------------------

  ALTER TABLE STEM MODIFY (SCORING_MODEL_ID NOT NULL ENABLE);
  ALTER TABLE STEM ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table STOCK_DATA
--------------------------------------------------------

  ALTER TABLE STOCK_DATA ADD PRIMARY KEY (COMPANY_ID, DAY_TIME);
--------------------------------------------------------
--  Constraints for Table ARTICLE
--------------------------------------------------------

  ALTER TABLE ARTICLE ADD CONSTRAINT ARTICLE_UK1 UNIQUE (FILENAME);
  ALTER TABLE ARTICLE MODIFY (SCORING_MODEL_ID NOT NULL ENABLE);
  ALTER TABLE ARTICLE ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table SCORING_MODEL
--------------------------------------------------------

  ALTER TABLE SCORING_MODEL ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table COMPANY_SCORING_MODEL
--------------------------------------------------------

  ALTER TABLE COMPANY_SCORING_MODEL ADD PRIMARY KEY (COMPANY_ID, SCORING_MODEL_ID);
--------------------------------------------------------
--  Constraints for Table COMPANY
--------------------------------------------------------

  ALTER TABLE COMPANY ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  ALTER TABLE ARTICLE_STEM_COUNT ADD CONSTRAINT ARTICLE_STEM_COUNT_PK PRIMARY KEY (ARTICLE_ID, STEM_ID);
  ALTER TABLE ARTICLE_STEM_COUNT MODIFY (STEM_ID NOT NULL ENABLE);
  ALTER TABLE ARTICLE_STEM_COUNT MODIFY (ARTICLE_ID NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ACTIVITY
--------------------------------------------------------

  ALTER TABLE ACTIVITY ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  ALTER TABLE REGRESSION_MODEL_COEF ADD PRIMARY KEY (ID);
  ALTER TABLE REGRESSION_MODEL_COEF MODIFY (COEF NOT NULL ENABLE);
  ALTER TABLE REGRESSION_MODEL_COEF MODIFY (REGRESSION_MODEL_ID NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  ALTER TABLE EIGENVECTOR_VALUE MODIFY (ARTICLE_ID NOT NULL ENABLE);
  ALTER TABLE EIGENVECTOR_VALUE MODIFY (EIGENVALUE_ID NOT NULL ENABLE);
  ALTER TABLE EIGENVECTOR_VALUE ADD CONSTRAINT EIGENVECTOR_VALUE_PK PRIMARY KEY (EIGENVALUE_ID, ARTICLE_ID);
--------------------------------------------------------
--  Constraints for Table REGRESSION_MODEL
--------------------------------------------------------

  ALTER TABLE REGRESSION_MODEL ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  ALTER TABLE PRINCIPAL_COMPONENT ADD CONSTRAINT PRINCIPAL_COMPONENT_PK PRIMARY KEY (EIGENVALUE_ID, STEM_ID) DISABLE;
  ALTER TABLE PRINCIPAL_COMPONENT MODIFY (STEM_ID NOT NULL DISABLE);
  ALTER TABLE PRINCIPAL_COMPONENT MODIFY (EIGENVALUE_ID NOT NULL DISABLE);
--------------------------------------------------------
--  Constraints for Table EIGENVALUE
--------------------------------------------------------

  ALTER TABLE EIGENVALUE MODIFY (SCORING_MODEL_ID NOT NULL ENABLE);
  ALTER TABLE EIGENVALUE ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table STOCKPRICE_CHANGE
--------------------------------------------------------

  ALTER TABLE STOCKPRICE_CHANGE MODIFY (CALC_ID NOT NULL ENABLE);
  ALTER TABLE STOCKPRICE_CHANGE ADD PRIMARY KEY (ID);

--------------------------------------------------------
--  Ref Constraints for Table ARTICLE
--------------------------------------------------------

  ALTER TABLE ARTICLE ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  ALTER TABLE ARTICLE_PC_VALUE ADD FOREIGN KEY (ARTICLE_ID)
	  REFERENCES ARTICLE (ID) ENABLE;
  ALTER TABLE ARTICLE_PC_VALUE ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  ALTER TABLE ARTICLE_STEM_COUNT ADD FOREIGN KEY (ARTICLE_ID)
	  REFERENCES ARTICLE (ID) ENABLE;
  ALTER TABLE ARTICLE_STEM_COUNT ADD FOREIGN KEY (STEM_ID)
	  REFERENCES STEM (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table COMPANY_SCORING_MODEL
--------------------------------------------------------

  ALTER TABLE COMPANY_SCORING_MODEL ADD FOREIGN KEY (COMPANY_ID)
	  REFERENCES COMPANY (ID) ENABLE;
  ALTER TABLE COMPANY_SCORING_MODEL ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EIGENVALUE
--------------------------------------------------------

  ALTER TABLE EIGENVALUE ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  ALTER TABLE EIGENVECTOR_VALUE ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID) ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  ALTER TABLE PRINCIPAL_COMPONENT ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID) DISABLE;
  ALTER TABLE PRINCIPAL_COMPONENT ADD FOREIGN KEY (STEM_ID)
	  REFERENCES STEM (ID) DISABLE;
--------------------------------------------------------
--  Ref Constraints for Table REGRESSION_MODEL
--------------------------------------------------------

  ALTER TABLE REGRESSION_MODEL ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  ALTER TABLE REGRESSION_MODEL_COEF ADD FOREIGN KEY (REGRESSION_MODEL_ID)
	  REFERENCES REGRESSION_MODEL (ID) ENABLE;
  ALTER TABLE REGRESSION_MODEL_COEF ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STEM
--------------------------------------------------------

  ALTER TABLE STEM ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STOCKPRICE_CHANGE
--------------------------------------------------------

  ALTER TABLE STOCKPRICE_CHANGE ADD FOREIGN KEY (CALC_ID)
	  REFERENCES STOCKPRICE_CHANGE_CALC (ID) ENABLE;
  ALTER TABLE STOCKPRICE_CHANGE ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STOCKPRICE_CHANGE_CALC
--------------------------------------------------------

  ALTER TABLE STOCKPRICE_CHANGE_CALC ADD FOREIGN KEY (COMPANY_ID)
	  REFERENCES SCORING_MODEL (ID) ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STOCK_DATA
--------------------------------------------------------

  ALTER TABLE STOCK_DATA ADD CONSTRAINT STOCK_DATA_COMPANY_FK FOREIGN KEY (COMPANY_ID)
	  REFERENCES COMPANY (ID) ENABLE;
--------------------------------------------------------
--  DDL for Function TIME_DIFF
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION TIME_DIFF (
DATE_1 IN DATE, DATE_2 IN DATE) RETURN NUMBER IS

NDATE_1   NUMBER;
NDATE_2   NUMBER;
NSECOND_1 NUMBER(5,0);
NSECOND_2 NUMBER(5,0);

BEGIN
  -- Get Julian date number from first date (DATE_1)
  NDATE_1 := TO_NUMBER(TO_CHAR(DATE_1, 'J'));

  -- Get Julian date number from second date (DATE_2)
  NDATE_2 := TO_NUMBER(TO_CHAR(DATE_2, 'J'));

  -- Get seconds since midnight from first date (DATE_1)
  NSECOND_1 := TO_NUMBER(TO_CHAR(DATE_1, 'SSSSS'));

  -- Get seconds since midnight from second date (DATE_2)
  NSECOND_2 := TO_NUMBER(TO_CHAR(DATE_2, 'SSSSS'));

  RETURN (((NDATE_2 - NDATE_1) * 86400)+(NSECOND_2 - NSECOND_1));
END time_diff;

/

create or replace procedure reset_all_sequences(article_id_start integer, 
  stem_id_start integer)
is
begin
  execute immediate 'drop sequence article_id_seq';
  execute immediate 'create sequence article_id_seq start with ' || article_id_start;
  
  execute immediate 'drop sequence stem_id_seq';
  execute immediate 'create sequence stem_id_seq start with ' || stem_id_start;
end;
/