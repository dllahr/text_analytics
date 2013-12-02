alter TABLE article_stem_count add CONSTRAINT article_stem_count_stem_id_fkey FOREIGN KEY (stem_id) REFERENCES stem(id);
alter TABLE mean_stem_count add CONSTRAINT mean_stem_count_stem_id_fkey FOREIGN KEY (stem_id) REFERENCES stem(id);
alter TABLE principal_component add CONSTRAINT principal_component_stem_id_fkey FOREIGN KEY (stem_id) REFERENCES stem(id);

