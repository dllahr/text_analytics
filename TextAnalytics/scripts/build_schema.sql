--------------------------------------------------------
--  File created - Sunday-May-12-2013   
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Sequence DJI_HIST_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DJI_HIST_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DJI_TEMP_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DJI_TEMP_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 10681 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table ACTIVITY
--------------------------------------------------------

  CREATE TABLE "ACTIVITY" 
   (	"ID" NUMBER(*,0), 
	"ACTIVITY_DATE" DATE, 
	"TRANSACTION" VARCHAR2(200), 
	"DESCRIPTION" VARCHAR2(200), 
	"SYMBOL" VARCHAR2(100), 
	"QTY" NUMBER(*,0), 
	"FILL_PRICE" BINARY_DOUBLE, 
	"COMMISSION" BINARY_DOUBLE, 
	"NET_AMOUNT" BINARY_DOUBLE
   ) ;
--------------------------------------------------------
--  DDL for Table ARTICLE
--------------------------------------------------------

  CREATE TABLE "ARTICLE" 
   (	"ID" NUMBER(*,0), 
	"SCORING_MODEL_ID" NUMBER(*,0), 
	"PUBLISH_DATE" DATE, 
	"DAY_INDEX" NUMBER(*,0), 
	"FILENAME" VARCHAR2(4000)
   ) ;

   COMMENT ON COLUMN "ARTICLE"."DAY_INDEX" IS 'integer representing day that data is for';
--------------------------------------------------------
--  DDL for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  CREATE TABLE "ARTICLE_PC_VALUE" 
   (	"ARTICLE_ID" NUMBER(*,0), 
	"EIGENVALUE_ID" NUMBER(*,0), 
	"VALUE" BINARY_FLOAT
   ) ;
--------------------------------------------------------
--  DDL for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  CREATE TABLE "ARTICLE_STEM_COUNT" 
   (	"ARTICLE_ID" NUMBER(*,0), 
	"STEM_ID" NUMBER(*,0), 
	"COUNT" NUMBER(*,0)
   ) ;

   COMMENT ON TABLE "ARTICLE_STEM_COUNT"  IS 'represents the number of times each stem occurred in each article.  also known as word vector, sparse format';
--------------------------------------------------------
--  DDL for Table COMPANY
--------------------------------------------------------

  CREATE TABLE "COMPANY" 
   (	"ID" NUMBER(*,0), 
	"NAME" VARCHAR2(100), 
	"STOCK_SYMBOL" VARCHAR2(20)
   ) ;
--------------------------------------------------------
--  DDL for Table COMPANY_SCORING_MODEL
--------------------------------------------------------

  CREATE TABLE "COMPANY_SCORING_MODEL" 
   (	"COMPANY_ID" NUMBER(*,0), 
	"SCORING_MODEL_ID" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table DJI_CORR
--------------------------------------------------------

  CREATE TABLE "DJI_CORR" 
   (	"DAY_OFFSET" NUMBER(*,0), 
	"EIGENVALUE_ID" NUMBER(*,0), 
	"CORRELATION" FLOAT(126), 
	"COUNT" NUMBER(*,0), 
	"AVE_CORR" BINARY_DOUBLE
   ) ;
--------------------------------------------------------
--  DDL for Table DJI_FRAC_CHANGE
--------------------------------------------------------

  CREATE TABLE "DJI_FRAC_CHANGE" 
   (	"FRAC_CHANGE" BINARY_FLOAT, 
	"ADJ_CLOSE" BINARY_FLOAT, 
	"DAY_INDEX" NUMBER(*,0), 
	"DAY_TIME" DATE, 
	"VOLUME" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table DJI_FRAC_CHANGE_HIST
--------------------------------------------------------

  CREATE TABLE "DJI_FRAC_CHANGE_HIST" 
   (	"LOWER_LIMIT" BINARY_FLOAT, 
	"UPPER_LIMIT" BINARY_FLOAT, 
	"NUM" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table DJI_TEMP
--------------------------------------------------------

  CREATE TABLE "DJI_TEMP" 
   (	"IND" NUMBER(*,0), 
	"ADJ_CLOSE" BINARY_FLOAT, 
	"DAY_INDEX" NUMBER(*,0), 
	"DAY_TIME" DATE, 
	"VOLUME" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table EIGENVALUE
--------------------------------------------------------

  CREATE TABLE "EIGENVALUE" 
   (	"ID" NUMBER(*,0), 
	"SCORING_MODEL_ID" NUMBER(*,0), 
	"SORT_INDEX" NUMBER(*,0), 
	"VALUE" BINARY_FLOAT
   ) ;

   COMMENT ON COLUMN "EIGENVALUE"."SORT_INDEX" IS 'relative index of an eigenvalue within the set present for a company_id when sorting (ascending) by value';
--------------------------------------------------------
--  DDL for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  CREATE TABLE "EIGENVECTOR_VALUE" 
   (	"EIGENVALUE_ID" NUMBER(*,0), 
	"ARTICLE_ID" NUMBER(*,0), 
	"VALUE" BINARY_FLOAT
   ) ;

   COMMENT ON TABLE "EIGENVECTOR_VALUE"  IS 'if the eigenvectors are the columns of a matrix, the column index is wrt eigenvalues is eigenvalue_id, the row index is article_id';
--------------------------------------------------------
--  DDL for Table PREDICTION
--------------------------------------------------------

  CREATE TABLE "PREDICTION" 
   (	"ARTICLE_ID" NUMBER(*,0), 
	"PREDICTION_MODEL_ID" NUMBER(*,0), 
	"STOCK_PRICE" BINARY_DOUBLE, 
	"DAY_TIME" DATE
   ) ;
--------------------------------------------------------
--  DDL for Table PREDICTION_MODEL
--------------------------------------------------------

  CREATE TABLE "PREDICTION_MODEL" 
   (	"ID" NUMBER(*,0), 
	"STOCKPRICE_CHANGE_ID" NUMBER(*,0), 
	"THRESHOLD_LOWER" BINARY_DOUBLE, 
	"THRESHOLD_UPPER" BINARY_DOUBLE
   ) ;
--------------------------------------------------------
--  DDL for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  CREATE TABLE "PRINCIPAL_COMPONENT" 
   (	"EIGENVALUE_ID" NUMBER(*,0), 
	"STEM_ID" NUMBER(*,0), 
	"VALUE" BINARY_FLOAT
   ) ;
--------------------------------------------------------
--  DDL for Table REGRESSION_MODEL
--------------------------------------------------------

  CREATE TABLE "REGRESSION_MODEL" 
   (	"ID" NUMBER(*,0), 
	"DAY_OFFSET" NUMBER(*,0), 
	"SCORING_MODEL_ID" NUMBER(*,0), 
	"R_EXPRESSION" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  CREATE TABLE "REGRESSION_MODEL_COEF" 
   (	"ID" NUMBER(*,0), 
	"REGRESSION_MODEL_ID" NUMBER(*,0), 
	"EIGENVALUE_ID" NUMBER(*,0), 
	"COEF" BINARY_DOUBLE
   ) ;
--------------------------------------------------------
--  DDL for Table SCORING_MODEL
--------------------------------------------------------

  CREATE TABLE "SCORING_MODEL" 
   (	"ID" NUMBER(*,0), 
	"NOTES" VARCHAR2(4000)
   ) ;
--------------------------------------------------------
--  DDL for Table STAGING_STOCK_DATA
--------------------------------------------------------

  CREATE TABLE "STAGING_STOCK_DATA" 
   (	"DAY_TIME" DATE, 
	"OPEN" FLOAT(126), 
	"HIGH" FLOAT(126), 
	"LOW" FLOAT(126), 
	"CLOSE" FLOAT(126), 
	"VOLUME" NUMBER(*,0), 
	"ADJ_CLOSE" FLOAT(126)
   ) ;
--------------------------------------------------------
--  DDL for Table STEM
--------------------------------------------------------

  CREATE TABLE "STEM" 
   (	"ID" NUMBER(*,0), 
	"SCORING_MODEL_ID" NUMBER(*,0), 
	"TEXT" VARCHAR2(500)
   ) ;

   COMMENT ON TABLE "STEM"  IS 'contains the set of stems found for articles for each company';
--------------------------------------------------------
--  DDL for Table STOCKPRICE_CHANGE
--------------------------------------------------------

  CREATE TABLE "STOCKPRICE_CHANGE" 
   (	"CALC_ID" NUMBER(*,0), 
	"EIGENVALUE_ID" NUMBER(*,0), 
	"DAY_OFFSET" NUMBER(*,0), 
	"AVERAGE" FLOAT(126), 
	"FWHM" FLOAT(126), 
	"ID" NUMBER(*,0), 
	"USE_FOR_PREDICTION" CHAR(1)
   ) ;

   COMMENT ON COLUMN "STOCKPRICE_CHANGE"."FWHM" IS 'full width at half max for histogram of statistics calculation';
--------------------------------------------------------
--  DDL for Table STOCKPRICE_CHANGE_CALC
--------------------------------------------------------

  CREATE TABLE "STOCKPRICE_CHANGE_CALC" 
   (	"ID" NUMBER(*,0), 
	"COMPANY_ID" NUMBER(*,0), 
	"LOWER_STOCK_DAY_IND" NUMBER(*,0), 
	"UPPER_STOCK_DAY_IND" NUMBER(*,0), 
	"NUM_SAMPLE" NUMBER(*,0), 
	"HIST_RANGE_LOWER" BINARY_DOUBLE, 
	"HIST_RANGE_UPPER" BINARY_DOUBLE, 
	"NUM_BINS" NUMBER(*,0), 
	"PCT_THRESHOLD_LOWER" BINARY_DOUBLE, 
	"PCT_THRESHOLD_UPPER" BINARY_DOUBLE
   ) ;

   COMMENT ON COLUMN "STOCKPRICE_CHANGE_CALC"."NUM_SAMPLE" IS 'number of samples used in the calculation';
   COMMENT ON COLUMN "STOCKPRICE_CHANGE_CALC"."HIST_RANGE_LOWER" IS 'lower limit used when creating the histogram for the stockprice change calculation';
   COMMENT ON COLUMN "STOCKPRICE_CHANGE_CALC"."HIST_RANGE_UPPER" IS 'upper limit used when creating the histogram for the stockprice change calculation';
   COMMENT ON COLUMN "STOCKPRICE_CHANGE_CALC"."NUM_BINS" IS 'number of bins used in creating the histogram for the stockprice change calculation';
--------------------------------------------------------
--  DDL for Table STOCK_DATA
--------------------------------------------------------

  CREATE TABLE "STOCK_DATA" 
   (	"COMPANY_ID" NUMBER(*,0), 
	"DAY_TIME" DATE, 
	"OPEN" BINARY_FLOAT, 
	"HIGH" BINARY_FLOAT, 
	"LOW" BINARY_FLOAT, 
	"CLOSE" BINARY_FLOAT, 
	"VOLUME" NUMBER(*,0), 
	"ADJ_CLOSE" BINARY_FLOAT, 
	"DAY_INDEX" NUMBER(*,0)
   ) ;

   COMMENT ON COLUMN "STOCK_DATA"."DAY_INDEX" IS 'integer representing day that data is for';

--------------------------------------------------------
--  DDL for Index STOCK_CALC_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX "STOCK_CALC_COMPANY_INDEX" ON "STOCKPRICE_CHANGE_CALC" ("COMPANY_ID") 
  ;
--------------------------------------------------------
--  DDL for Index STOCK_DATA_DAY_INDEX
--------------------------------------------------------

  CREATE INDEX "STOCK_DATA_DAY_INDEX" ON "STOCK_DATA" ("COMPANY_ID", "DAY_INDEX") 
  ;
--------------------------------------------------------
--  DDL for Index EIGVECT_VALUE_EIGENVALUE_ID
--------------------------------------------------------

  CREATE INDEX "EIGVECT_VALUE_EIGENVALUE_ID" ON "EIGENVECTOR_VALUE" ("EIGENVALUE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index STOCK_DATA_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX "STOCK_DATA_COMPANY_INDEX" ON "STOCK_DATA" ("COMPANY_ID") 
  ;
--------------------------------------------------------
--  DDL for Index EIGENVECTOR_VALUE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "EIGENVECTOR_VALUE_PK" ON "EIGENVECTOR_VALUE" ("EIGENVALUE_ID", "ARTICLE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index PRINCIPAL_COMPONENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PRINCIPAL_COMPONENT_PK" ON "PRINCIPAL_COMPONENT" ("EIGENVALUE_ID", "STEM_ID") 
  ;
--------------------------------------------------------
--  DDL for Index EIGENVALUE_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX "EIGENVALUE_COMPANY_INDEX" ON "EIGENVALUE" ("SCORING_MODEL_ID") 
  ;
--------------------------------------------------------
--  DDL for Index ARTICLE_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "ARTICLE_UK1" ON "ARTICLE" ("FILENAME") 
  ;
--------------------------------------------------------
--  DDL for Index PC_EIGVAL_INDEX
--------------------------------------------------------

  CREATE INDEX "PC_EIGVAL_INDEX" ON "PRINCIPAL_COMPONENT" ("EIGENVALUE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index ARTICLE_STEM_COUNT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ARTICLE_STEM_COUNT_PK" ON "ARTICLE_STEM_COUNT" ("ARTICLE_ID", "STEM_ID") 
  ;
--------------------------------------------------------
--  DDL for Index ARTICLE_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX "ARTICLE_COMPANY_INDEX" ON "ARTICLE" ("SCORING_MODEL_ID") 
  ;
--------------------------------------------------------
--  DDL for Index STOCKPRICE_CHANGE_EIG_IND
--------------------------------------------------------

  CREATE INDEX "STOCKPRICE_CHANGE_EIG_IND" ON "STOCKPRICE_CHANGE" ("EIGENVALUE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index ARTICLE_STEM_COUNT_INDEX
--------------------------------------------------------

  CREATE INDEX "ARTICLE_STEM_COUNT_INDEX" ON "ARTICLE_STEM_COUNT" ("ARTICLE_ID") 
  ;
--------------------------------------------------------
--  DDL for Index STEM_COMPANY_INDEX
--------------------------------------------------------

  CREATE INDEX "STEM_COMPANY_INDEX" ON "STEM" ("SCORING_MODEL_ID") 
  ;
--------------------------------------------------------
--  DDL for Index DJI_CORR_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "DJI_CORR_PK" ON "DJI_CORR" ("DAY_OFFSET", "EIGENVALUE_ID") 
  ;
--------------------------------------------------------
--  Constraints for Table STOCKPRICE_CHANGE_CALC
--------------------------------------------------------

  ALTER TABLE "STOCKPRICE_CHANGE_CALC" MODIFY ("COMPANY_ID" NOT NULL ENABLE);
  ALTER TABLE "STOCKPRICE_CHANGE_CALC" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  ALTER TABLE "ARTICLE_PC_VALUE" ADD PRIMARY KEY ("ARTICLE_ID", "EIGENVALUE_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table STEM
--------------------------------------------------------

  ALTER TABLE "STEM" MODIFY ("SCORING_MODEL_ID" NOT NULL ENABLE);
  ALTER TABLE "STEM" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table STOCK_DATA
--------------------------------------------------------

  ALTER TABLE "STOCK_DATA" ADD PRIMARY KEY ("COMPANY_ID", "DAY_TIME") ENABLE;
--------------------------------------------------------
--  Constraints for Table ARTICLE
--------------------------------------------------------

  ALTER TABLE "ARTICLE" ADD CONSTRAINT "ARTICLE_UK1" UNIQUE ("FILENAME") ENABLE;
  ALTER TABLE "ARTICLE" MODIFY ("SCORING_MODEL_ID" NOT NULL ENABLE);
  ALTER TABLE "ARTICLE" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table SCORING_MODEL
--------------------------------------------------------

  ALTER TABLE "SCORING_MODEL" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table COMPANY_SCORING_MODEL
--------------------------------------------------------

  ALTER TABLE "COMPANY_SCORING_MODEL" ADD PRIMARY KEY ("COMPANY_ID", "SCORING_MODEL_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table COMPANY
--------------------------------------------------------

  ALTER TABLE "COMPANY" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  ALTER TABLE "ARTICLE_STEM_COUNT" ADD CONSTRAINT "ARTICLE_STEM_COUNT_PK" PRIMARY KEY ("ARTICLE_ID", "STEM_ID") ENABLE;
  ALTER TABLE "ARTICLE_STEM_COUNT" MODIFY ("STEM_ID" NOT NULL ENABLE);
  ALTER TABLE "ARTICLE_STEM_COUNT" MODIFY ("ARTICLE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ACTIVITY
--------------------------------------------------------

  ALTER TABLE "ACTIVITY" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  ALTER TABLE "REGRESSION_MODEL_COEF" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "REGRESSION_MODEL_COEF" MODIFY ("COEF" NOT NULL ENABLE);
  ALTER TABLE "REGRESSION_MODEL_COEF" MODIFY ("REGRESSION_MODEL_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  ALTER TABLE "EIGENVECTOR_VALUE" MODIFY ("ARTICLE_ID" NOT NULL ENABLE);
  ALTER TABLE "EIGENVECTOR_VALUE" MODIFY ("EIGENVALUE_ID" NOT NULL ENABLE);
  ALTER TABLE "EIGENVECTOR_VALUE" ADD CONSTRAINT "EIGENVECTOR_VALUE_PK" PRIMARY KEY ("EIGENVALUE_ID", "ARTICLE_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table REGRESSION_MODEL
--------------------------------------------------------

  ALTER TABLE "REGRESSION_MODEL" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  ALTER TABLE "PRINCIPAL_COMPONENT" ADD CONSTRAINT "PRINCIPAL_COMPONENT_PK" PRIMARY KEY ("EIGENVALUE_ID", "STEM_ID") ENABLE;
  ALTER TABLE "PRINCIPAL_COMPONENT" MODIFY ("STEM_ID" NOT NULL ENABLE);
  ALTER TABLE "PRINCIPAL_COMPONENT" MODIFY ("EIGENVALUE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EIGENVALUE
--------------------------------------------------------

  ALTER TABLE "EIGENVALUE" MODIFY ("SCORING_MODEL_ID" NOT NULL ENABLE);
  ALTER TABLE "EIGENVALUE" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table STOCKPRICE_CHANGE
--------------------------------------------------------

  ALTER TABLE "STOCKPRICE_CHANGE" MODIFY ("CALC_ID" NOT NULL ENABLE);
  ALTER TABLE "STOCKPRICE_CHANGE" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table DJI_FRAC_CHANGE
--------------------------------------------------------

  ALTER TABLE "DJI_FRAC_CHANGE" ADD PRIMARY KEY ("DAY_INDEX") ENABLE;
--------------------------------------------------------
--  Constraints for Table PREDICTION
--------------------------------------------------------

  ALTER TABLE "PREDICTION" ADD PRIMARY KEY ("ARTICLE_ID", "PREDICTION_MODEL_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table PREDICTION_MODEL
--------------------------------------------------------

  ALTER TABLE "PREDICTION_MODEL" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table DJI_CORR
--------------------------------------------------------

  ALTER TABLE "DJI_CORR" ADD CONSTRAINT "DJI_CORR_PK" PRIMARY KEY ("DAY_OFFSET", "EIGENVALUE_ID") ENABLE;
  ALTER TABLE "DJI_CORR" MODIFY ("EIGENVALUE_ID" NOT NULL ENABLE);
  ALTER TABLE "DJI_CORR" MODIFY ("DAY_OFFSET" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table ARTICLE
--------------------------------------------------------

  ALTER TABLE "ARTICLE" ADD FOREIGN KEY ("SCORING_MODEL_ID")
	  REFERENCES "SCORING_MODEL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ARTICLE_PC_VALUE
--------------------------------------------------------

  ALTER TABLE "ARTICLE_PC_VALUE" ADD FOREIGN KEY ("ARTICLE_ID")
	  REFERENCES "ARTICLE" ("ID") ENABLE;
  ALTER TABLE "ARTICLE_PC_VALUE" ADD FOREIGN KEY ("EIGENVALUE_ID")
	  REFERENCES "EIGENVALUE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ARTICLE_STEM_COUNT
--------------------------------------------------------

  ALTER TABLE "ARTICLE_STEM_COUNT" ADD FOREIGN KEY ("ARTICLE_ID")
	  REFERENCES "ARTICLE" ("ID") ENABLE;
  ALTER TABLE "ARTICLE_STEM_COUNT" ADD FOREIGN KEY ("STEM_ID")
	  REFERENCES "STEM" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table COMPANY_SCORING_MODEL
--------------------------------------------------------

  ALTER TABLE "COMPANY_SCORING_MODEL" ADD FOREIGN KEY ("COMPANY_ID")
	  REFERENCES "COMPANY" ("ID") ENABLE;
  ALTER TABLE "COMPANY_SCORING_MODEL" ADD FOREIGN KEY ("SCORING_MODEL_ID")
	  REFERENCES "SCORING_MODEL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EIGENVALUE
--------------------------------------------------------

  ALTER TABLE "EIGENVALUE" ADD FOREIGN KEY ("SCORING_MODEL_ID")
	  REFERENCES "SCORING_MODEL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table EIGENVECTOR_VALUE
--------------------------------------------------------

  ALTER TABLE "EIGENVECTOR_VALUE" ADD FOREIGN KEY ("EIGENVALUE_ID")
	  REFERENCES "EIGENVALUE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PREDICTION
--------------------------------------------------------

  ALTER TABLE "PREDICTION" ADD FOREIGN KEY ("ARTICLE_ID")
	  REFERENCES "ARTICLE" ("ID") ENABLE;
  ALTER TABLE "PREDICTION" ADD FOREIGN KEY ("PREDICTION_MODEL_ID")
	  REFERENCES "PREDICTION_MODEL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PREDICTION_MODEL
--------------------------------------------------------

  ALTER TABLE "PREDICTION_MODEL" ADD FOREIGN KEY ("STOCKPRICE_CHANGE_ID")
	  REFERENCES "STOCKPRICE_CHANGE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PRINCIPAL_COMPONENT
--------------------------------------------------------

  ALTER TABLE "PRINCIPAL_COMPONENT" ADD FOREIGN KEY ("EIGENVALUE_ID")
	  REFERENCES "EIGENVALUE" ("ID") ENABLE;
  ALTER TABLE "PRINCIPAL_COMPONENT" ADD FOREIGN KEY ("STEM_ID")
	  REFERENCES "STEM" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REGRESSION_MODEL
--------------------------------------------------------

  ALTER TABLE "REGRESSION_MODEL" ADD FOREIGN KEY ("SCORING_MODEL_ID")
	  REFERENCES "SCORING_MODEL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REGRESSION_MODEL_COEF
--------------------------------------------------------

  ALTER TABLE "REGRESSION_MODEL_COEF" ADD FOREIGN KEY ("REGRESSION_MODEL_ID")
	  REFERENCES "REGRESSION_MODEL" ("ID") ENABLE;
  ALTER TABLE "REGRESSION_MODEL_COEF" ADD FOREIGN KEY ("EIGENVALUE_ID")
	  REFERENCES "EIGENVALUE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STEM
--------------------------------------------------------

  ALTER TABLE "STEM" ADD FOREIGN KEY ("SCORING_MODEL_ID")
	  REFERENCES "SCORING_MODEL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STOCKPRICE_CHANGE
--------------------------------------------------------

  ALTER TABLE "STOCKPRICE_CHANGE" ADD FOREIGN KEY ("CALC_ID")
	  REFERENCES "STOCKPRICE_CHANGE_CALC" ("ID") ENABLE;
  ALTER TABLE "STOCKPRICE_CHANGE" ADD FOREIGN KEY ("EIGENVALUE_ID")
	  REFERENCES "EIGENVALUE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STOCKPRICE_CHANGE_CALC
--------------------------------------------------------

  ALTER TABLE "STOCKPRICE_CHANGE_CALC" ADD FOREIGN KEY ("COMPANY_ID")
	  REFERENCES "SCORING_MODEL" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STOCK_DATA
--------------------------------------------------------

  ALTER TABLE "STOCK_DATA" ADD CONSTRAINT "STOCK_DATA_COMPANY_FK" FOREIGN KEY ("COMPANY_ID")
	  REFERENCES "COMPANY" ("ID") ENABLE;


--------------------------------------------------------
--  DDL for Function TIME_DIFF
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "TIME_DIFF" (
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

