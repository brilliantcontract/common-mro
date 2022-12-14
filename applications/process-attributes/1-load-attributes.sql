/*

    !!! Create view(s).

--create view values_to_add_as_known as
select
    correct_attribute_value
from known_attribute_values
where
    correct_attribute_value NOT IN (select original_attribute_value from known_attribute_values)
group by
    correct_attribute_value;

select
    attribute_value
from preliminary_attributes
where
    attribute_value NOT IN (select original_attribute_value from known_attribute_values)
group by
    attribute_value;
*/


/*
    Load raw attributes.
*/
TRUNCATE TABLE raw_attributes;



/*
    Or load data from scraped .csv file.
*/
INSERT INTO raw_attributes(id, catalog_number, is_visible, attributes)
SELECT
    mro.id id,
    min(mro.catalog_number) catalog_number,
    min(mro.is_visible) is_visible,
    LISTAGG(scraped.name || ':' || replace(scraped.value, ':', '-'), '|') WITHIN GROUP (ORDER BY scraped.name) attributes
FROM
    temp_scraped_martin_sprocket scraped
JOIN temp_mro_martin_sprocket mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
GROUP BY
    mro.id
;


-- You should manually load attributes into "raw_attributes" table.



/*
    Find and add MRO ID (SKU).
*/
DELETE FROM temp_mro_rexnord
WHERE ROWID NOT IN (
    SELECT MIN(ROWID)
    FROM temp_mro_rexnord
    GROUP BY unify_catalog_number(catalog_number)
);

DELETE FROM raw_attributes
WHERE ROWID NOT IN (
    SELECT MIN(ROWID)
    FROM raw_attributes
    GROUP BY unify_catalog_number(catalog_number)
);

merge /*+ PARALLEL(20) */
into raw_attributes b
using temp_mro_rexnord a
on (unify_catalog_number(a.catalog_number) = unify_catalog_number(b.catalog_number))
when matched then update set b.id = a.id;

delete from raw_attributes
where id is null;

delete from raw_attributes
where category != 'linkbelt';





BEGIN
    REFRESH_BASE_AFTER_ATTRIBUTES_LOAD;
    attributes_pkg.replace_unknown_attribute_names_with_known_attribute_names;
    attributes_pkg.replace_unknown_attribute_values_with_known_attribute_values;
    attributes_pkg.move_measure_units_from_attribute_values_to_attribute_names;
    
    commit;
END;
/

UPDATE preliminary_attributes
SET
    attribute_name = attributes_pkg.clean_up_attribute_name(attribute_name)
    , attribute_value = attributes_pkg.clean_up_attribute_value(attribute_value);


