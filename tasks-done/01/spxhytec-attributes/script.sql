/*
    Add ID fields to all "scraped" tables.
*/
ALTER TABLE scraped_spx_attributes ADD (ID NUMBER );
ALTER TABLE scraped_spx_detail_attributes ADD (ID NUMBER );
ALTER TABLE scraped_spx_related_parts ADD (ID NUMBER );
ALTER TABLE scraped_spx_related_parts ADD (RELATED_ID NUMBER );


update scraped_spx_attributes t1
set id = (
    select min(id)
    from mro_spx t2
    where
        unify_catalog_number(replace(t2.catalog_number, 'SPX', '')) = unify_catalog_number(t1.catalog_number)
);

select count(*) from scraped_spx_attributes where id is null;
delete from scraped_spx_attributes where id is null;


update scraped_spx_detail_attributes t1
set id = (
    select min(id)
    from mro_spx t2
    where
        unify_catalog_number(replace(t2.catalog_number, 'SPX', '')) = unify_catalog_number(t1.catalog_number)
);

select count(*) from scraped_spx_detail_attributes where id is null;
delete from scraped_spx_detail_attributes where id is null;


update scraped_spx_related_parts t1
set id = (
    select min(id)
    from mro_spx t2
    where
        unify_catalog_number(replace(t2.catalog_number, 'SPX', '')) = unify_catalog_number(t1.catalog_number)
);

select count(*) from scraped_spx_related_parts where id is null;
delete from scraped_spx_related_parts where id is null;


update scraped_spx_related_parts t1
set related_id = (
    select min(id)
    from mro_spx t2
    where
        unify_catalog_number(replace(t2.catalog_number, 'SPX', '')) = unify_catalog_number(t1.related_catalog_number)
);

select count(*) from scraped_spx_related_parts where related_id is null;
delete from scraped_spx_related_parts where related_id is null;



/*
    Export to .csv files.
*/
select * from scraped_spx_related_parts;

select
    id
    , listagg(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) attributes
from scraped_spx_attributes
group by id;

select
    id
    , LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) detail_attributes
from scraped_spx_detail_attributes
group by id;