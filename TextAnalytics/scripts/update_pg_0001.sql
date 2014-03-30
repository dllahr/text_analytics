create table scoring_model_article (
	scoring_model_id		integer primary key,
	article_id_array		integer[],

	foreign key (scoring_model_id) references scoring_model(id)
);