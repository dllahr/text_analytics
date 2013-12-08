
   CREATE SEQUENCE  ARTICLE_ID_SEQ  INCREMENT BY 1 START WITH 166167;
 
   CREATE SEQUENCE  EIGENVALUE_ID_SEQ  INCREMENT BY 1 START WITH 801;

   CREATE SEQUENCE  REGRESSION_MODEL_COEF_ID_SEQ  INCREMENT BY 1 START WITH 308;

   CREATE SEQUENCE  REGRESSION_MODEL_ID_SEQ  INCREMENT BY 1 START WITH 88;

   CREATE SEQUENCE  STEM_ID_SEQ INCREMENT BY 1 START WITH 604128;

--------------------------------------------------------
--  DDL for Table ACTIVITY
--------------------------------------------------------

  CREATE TABLE ACTIVITY 
   (	ID integer, 
	ACTIVITY_DATE timestamp, 
	TRANSACTION varchar(200), 
	DESCRIPTION varchar(200), 
	SYMBOL varchar(100), 
	QTY integer, 
	FILL_PRICE double precision, 
	COMMISSION double precision, 
	NET_AMOUNT double precision
   ) ;
--------------------------------------------------------
--  DDL for Table ARTICLE
--------------------------------------------------------

  CREATE TABLE ARTICLE 
   (	ID integer, 
	ARTICLE_SOURCE_ID integer, 
	PUBLISH_DATE timestamp, 
	DAY_INDEX integer, 
	FILENAME varchar(4000), 
	START_LINE_NUM integer, 
	ADDITIONAL_IDENTIFIER varchar(4000)
   ) ;

   

--------------------------------------------------------
--  DDL for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  CREATE TABLE ARTICLE_PC_VALUE 
   (	ARTICLE_ID integer, 
	EIGENVALUE_ID integer, 
	VALUE double precision
   ) ;
--------------------------------------------------------
--  DDL for Table ARTICLE_SOURCE
--------------------------------------------------------

  CREATE TABLE ARTICLE_SOURCE 
   (	ID integer, 
	DESCRIPTION varchar(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  CREATE TABLE ARTICLE_STEM_COUNT 
   (	ARTICLE_ID integer, 
	STEM_ID integer, 
	COUNT integer
   ) ;

   
--------------------------------------------------------
--  DDL for Table COMPANY
--------------------------------------------------------

  CREATE TABLE COMPANY 
   (	ID integer, 
	NAME varchar(100), 
	STOCK_SYMBOL varchar(20)
   ) ;
--------------------------------------------------------
--  DDL for Table EIGENVALUE
--------------------------------------------------------

  CREATE TABLE EIGENVALUE 
   (	ID integer, 
	SCORING_MODEL_ID integer, 
	SORT_INDEX integer, 
	VALUE double precision
   ) ;

 
--------------------------------------------------------
--  DDL for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  CREATE TABLE EIGENVECTOR_VALUE 
   (	EIGENVALUE_ID integer, 
	ARTICLE_ID integer, 
	VALUE double precision
   ) ;

   
--------------------------------------------------------
--  DDL for Table MEAN_STEM_COUNT
--------------------------------------------------------

  CREATE TABLE MEAN_STEM_COUNT 
   (	SCORING_MODEL_ID integer, 
	STEM_ID integer, 
	VALUE double precision
   ) ;
--------------------------------------------------------
--  DDL for Table PRDCTN_MDL_STOCK_SMOOTH_COEF
--------------------------------------------------------

  CREATE TABLE PRDCTN_MDL_STOCK_SMOOTH_COEF 
   (	PREDICTION_MODEL_ID integer, 
	RELATIVE_DAY_INDEX integer, 
	COEF double precision
   ) ;
--------------------------------------------------------
--  DDL for Table PREDICTION_MODEL
--------------------------------------------------------

  CREATE TABLE PREDICTION_MODEL 
   (	ID integer, 
	REGRESSION_MODEL_ID integer, 
	LOWER_THRESHOLD double precision, 
	UPPER_THRESHOLD double precision, 
	PERCENTILE0_VALUE double precision, 
	PERCENTILE25_VALUE double precision, 
	PERCENTILE50_VALUE double precision, 
	PERCENTILE75_VALUE double precision, 
	PERCENTILE100_VALUE double precision
   ) ;
--------------------------------------------------------
--  DDL for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  CREATE TABLE PRINCIPAL_COMPONENT 
   (	EIGENVALUE_ID integer, 
	STEM_ID integer, 
	VALUE double precision
   ) ;
--------------------------------------------------------
--  DDL for Table REGRESSION_MODEL
--------------------------------------------------------

  CREATE TABLE REGRESSION_MODEL 
   (	ID integer, 
	DAY_OFFSET integer, 
	SCORING_MODEL_ID integer, 
	R_EXPRESSION varchar(4000), 
	COMPANY_ID integer
   ) ;
--------------------------------------------------------
--  DDL for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  CREATE TABLE REGRESSION_MODEL_COEF 
   (	ID integer, 
	REGRESSION_MODEL_ID integer, 
	EIGENVALUE_ID integer, 
	COEF double precision
   ) ;
--------------------------------------------------------
--  DDL for Table SCORING_MODEL
--------------------------------------------------------

  CREATE TABLE SCORING_MODEL 
   (	ID integer, 
	NOTES varchar(4000), 
	ARTICLES_NORMALIZED smallint, 
	NO_STOP_WORDS smallint
   ) ;
--------------------------------------------------------
--  DDL for Table SCORING_MODEL_ARTICLE_SOURCE
--------------------------------------------------------

  CREATE TABLE SCORING_MODEL_ARTICLE_SOURCE 
   (	SCORING_MODEL_ID integer, 
	ARTICLE_SOURCE_ID integer
   ) ;
--------------------------------------------------------
--  DDL for Table STEM
--------------------------------------------------------

  CREATE TABLE STEM 
   (	ID integer, 
	TEXT varchar(500), 
	IS_STOP boolean
   ) ;


--------------------------------------------------------
--  DDL for Table STOCK_DATA
--------------------------------------------------------

  CREATE TABLE STOCK_DATA 
   (	COMPANY_ID integer, 
	DAY_TIME timestamp, 
	VOLUME integer, 
	DAY_INDEX integer, 
	OPEN double precision, 
	HIGH double precision, 
	LOW double precision, 
	CLOSE double precision, 
	ADJ_CLOSE double precision
   ) ;



--------------------------------------------------------
--  DDL for View VW_SCRNG_MDL_ARTCL_SRC
--------------------------------------------------------

  CREATE OR REPLACE VIEW VW_SCRNG_MDL_ARTCL_SRC (ID, ARTICLE_SOURCE_ID) AS 
  select distinct sm.id, ar.article_source_id from scoring_model sm
join eigenvalue ev on ev.scoring_model_id = sm.id
join eigenvector_value evv on evv.eigenvalue_id = ev.id
join article ar on ar.id = evv.article_id;
--------------------------------------------------------
--  DDL for Index ARTICLE_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX ARTICLE_COMPANY_INDEX ON ARTICLE (ARTICLE_SOURCE_ID) 
  ;
--------------------------------------------------------
--  DDL for Index STOCK_DATA_DAY_INDEX
--------------------------------------------------------

  CREATE INDEX STOCK_DATA_DAY_INDEX ON STOCK_DATA (COMPANY_ID, DAY_INDEX) 
  ;
--------------------------------------------------------
--  DDL for Index ARTICLE_STEM_COUNT_INDEX
--------------------------------------------------------

  CREATE INDEX ARTICLE_STEM_COUNT_INDEX ON ARTICLE_STEM_COUNT (ARTICLE_ID) 
  ;
--------------------------------------------------------
--  DDL for Index EIGVECT_VALUE_EIGENVALUE_ID
--------------------------------------------------------

  CREATE INDEX EIGVECT_VALUE_EIGENVALUE_ID ON EIGENVECTOR_VALUE (EIGENVALUE_ID) 
  ;
--------------------------------------------------------
--  DDL for Index STOCK_DATA_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX STOCK_DATA_COMPANY_INDEX ON STOCK_DATA (COMPANY_ID) 
  ;

--------------------------------------------------------
--  DDL for Index EIGENVALUE_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX EIGENVALUE_COMPANY_INDEX ON EIGENVALUE (SCORING_MODEL_ID) 
  ;
--------------------------------------------------------
--  DDL for Index PC_EIGVAL_INDEX
--------------------------------------------------------

  CREATE INDEX PC_EIGVAL_INDEX ON PRINCIPAL_COMPONENT (EIGENVALUE_ID) 
  ;
--------------------------------------------------------
--  Constraints for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  ALTER TABLE ARTICLE_PC_VALUE ADD PRIMARY KEY (ARTICLE_ID, EIGENVALUE_ID);
--------------------------------------------------------
--  Constraints for Table STEM
--------------------------------------------------------

  ALTER TABLE STEM ALTER COLUMN IS_STOP SET NOT NULL;
  ALTER TABLE STEM ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table STOCK_DATA
--------------------------------------------------------

  ALTER TABLE STOCK_DATA ADD PRIMARY KEY (COMPANY_ID, DAY_TIME);
--------------------------------------------------------
--  Constraints for Table ARTICLE
--------------------------------------------------------

  ALTER TABLE ARTICLE ALTER COLUMN ARTICLE_SOURCE_ID SET NOT NULL;
  ALTER TABLE ARTICLE ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table SCORING_MODEL
--------------------------------------------------------

  ALTER TABLE SCORING_MODEL ALTER COLUMN NO_STOP_WORDS SET NOT NULL;
  ALTER TABLE SCORING_MODEL ALTER COLUMN ARTICLES_NORMALIZED SET NOT NULL;
  ALTER TABLE SCORING_MODEL ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table COMPANY
--------------------------------------------------------

  ALTER TABLE COMPANY ADD PRIMARY KEY (ID);

--------------------------------------------------------
--  Constraints for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  ALTER TABLE ARTICLE_STEM_COUNT ADD PRIMARY KEY (ARTICLE_ID, STEM_ID);
  ALTER TABLE ARTICLE_STEM_COUNT ALTER COLUMN STEM_ID SET NOT NULL;
  ALTER TABLE ARTICLE_STEM_COUNT ALTER COLUMN ARTICLE_ID SET NOT NULL;
--------------------------------------------------------
--  Constraints for Table ACTIVITY
--------------------------------------------------------

  ALTER TABLE ACTIVITY ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table PRDCTN_MDL_STOCK_SMOOTH_COEF
--------------------------------------------------------

  ALTER TABLE PRDCTN_MDL_STOCK_SMOOTH_COEF ADD PRIMARY KEY (PREDICTION_MODEL_ID, RELATIVE_DAY_INDEX);
  ALTER TABLE PRDCTN_MDL_STOCK_SMOOTH_COEF ALTER COLUMN COEF SET NOT NULL;
--------------------------------------------------------
--  Constraints for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  ALTER TABLE REGRESSION_MODEL_COEF ADD PRIMARY KEY (ID);
  ALTER TABLE REGRESSION_MODEL_COEF ALTER COLUMN REGRESSION_MODEL_ID SET NOT NULL;
--------------------------------------------------------
--  Constraints for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  ALTER TABLE EIGENVECTOR_VALUE ALTER COLUMN ARTICLE_ID SET NOT NULL;
  ALTER TABLE EIGENVECTOR_VALUE ALTER COLUMN EIGENVALUE_ID SET NOT NULL;
  ALTER TABLE EIGENVECTOR_VALUE ADD CONSTRAINT EIGENVECTOR_VALUE_PK PRIMARY KEY (EIGENVALUE_ID, ARTICLE_ID);
--------------------------------------------------------
--  Constraints for Table REGRESSION_MODEL
--------------------------------------------------------

  ALTER TABLE REGRESSION_MODEL ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  ALTER TABLE PRINCIPAL_COMPONENT ADD CONSTRAINT PRINCIPAL_COMPONENT_PK PRIMARY KEY (EIGENVALUE_ID, STEM_ID);
  ALTER TABLE PRINCIPAL_COMPONENT ALTER COLUMN STEM_ID SET NOT NULL;
  ALTER TABLE PRINCIPAL_COMPONENT ALTER COLUMN EIGENVALUE_ID SET NOT NULL;
--------------------------------------------------------
--  Constraints for Table EIGENVALUE
--------------------------------------------------------

  ALTER TABLE EIGENVALUE ALTER COLUMN SCORING_MODEL_ID SET NOT NULL;
  ALTER TABLE EIGENVALUE ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table SCORING_MODEL_ARTICLE_SOURCE
--------------------------------------------------------

  ALTER TABLE SCORING_MODEL_ARTICLE_SOURCE ADD PRIMARY KEY (SCORING_MODEL_ID, ARTICLE_SOURCE_ID);

--------------------------------------------------------
--  Constraints for Table PREDICTION_MODEL
--------------------------------------------------------

  ALTER TABLE PREDICTION_MODEL ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Constraints for Table MEAN_STEM_COUNT
--------------------------------------------------------

  ALTER TABLE MEAN_STEM_COUNT ADD PRIMARY KEY (SCORING_MODEL_ID, STEM_ID);
--------------------------------------------------------
--  Constraints for Table ARTICLE_SOURCE
--------------------------------------------------------

  ALTER TABLE ARTICLE_SOURCE ADD PRIMARY KEY (ID);
--------------------------------------------------------
--  Ref Constraints for Table ARTICLE
--------------------------------------------------------

  ALTER TABLE ARTICLE ADD FOREIGN KEY (ARTICLE_SOURCE_ID)
	  REFERENCES ARTICLE_SOURCE (ID);

--------------------------------------------------------
--  Ref Constraints for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  ALTER TABLE ARTICLE_PC_VALUE ADD FOREIGN KEY (ARTICLE_ID)
	  REFERENCES ARTICLE (ID);
  ALTER TABLE ARTICLE_PC_VALUE ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID);
--------------------------------------------------------
--  Ref Constraints for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  ALTER TABLE ARTICLE_STEM_COUNT ADD FOREIGN KEY (ARTICLE_ID)
	  REFERENCES ARTICLE (ID);
  ALTER TABLE ARTICLE_STEM_COUNT ADD FOREIGN KEY (STEM_ID)
	  REFERENCES STEM (ID);
--------------------------------------------------------
--  Ref Constraints for Table EIGENVALUE
--------------------------------------------------------

  ALTER TABLE EIGENVALUE ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID);
--------------------------------------------------------
--  Ref Constraints for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  ALTER TABLE EIGENVECTOR_VALUE ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID);
  alter table eigenvector_value add foreign key (article_id)
          references article (id);
--------------------------------------------------------
--  Ref Constraints for Table MEAN_STEM_COUNT
--------------------------------------------------------

  ALTER TABLE MEAN_STEM_COUNT ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID);
  ALTER TABLE MEAN_STEM_COUNT ADD FOREIGN KEY (STEM_ID)
	  REFERENCES STEM (ID);
--------------------------------------------------------
--  Ref Constraints for Table PREDICTION_MODEL
--------------------------------------------------------

  ALTER TABLE PREDICTION_MODEL ADD FOREIGN KEY (REGRESSION_MODEL_ID)
	  REFERENCES REGRESSION_MODEL (ID);
--------------------------------------------------------
--  Ref Constraints for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  ALTER TABLE PRINCIPAL_COMPONENT ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID);
  ALTER TABLE PRINCIPAL_COMPONENT ADD FOREIGN KEY (STEM_ID)
	  REFERENCES STEM (ID);
--------------------------------------------------------
--  Ref Constraints for Table REGRESSION_MODEL
--------------------------------------------------------

  ALTER TABLE REGRESSION_MODEL ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID);
  ALTER TABLE REGRESSION_MODEL ADD FOREIGN KEY (COMPANY_ID)
	  REFERENCES COMPANY (ID);
--------------------------------------------------------
--  Ref Constraints for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  ALTER TABLE REGRESSION_MODEL_COEF ADD FOREIGN KEY (REGRESSION_MODEL_ID)
	  REFERENCES REGRESSION_MODEL (ID);
  ALTER TABLE REGRESSION_MODEL_COEF ADD FOREIGN KEY (EIGENVALUE_ID)
	  REFERENCES EIGENVALUE (ID);
--------------------------------------------------------
--  Ref Constraints for Table SCORING_MODEL_ARTICLE_SOURCE
--------------------------------------------------------

  ALTER TABLE SCORING_MODEL_ARTICLE_SOURCE ADD FOREIGN KEY (SCORING_MODEL_ID)
	  REFERENCES SCORING_MODEL (ID);
  ALTER TABLE SCORING_MODEL_ARTICLE_SOURCE ADD FOREIGN KEY (ARTICLE_SOURCE_ID)
	  REFERENCES ARTICLE_SOURCE (ID);
--------------------------------------------------------
--  Ref Constraints for Table STOCK_DATA
--------------------------------------------------------

  ALTER TABLE STOCK_DATA ADD CONSTRAINT STOCK_DATA_COMPANY_FK FOREIGN KEY (COMPANY_ID)
	  REFERENCES COMPANY (ID);

