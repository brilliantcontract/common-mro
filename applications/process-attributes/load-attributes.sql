-- Empty pages that should be loaded with new data.
TRUNCATE TABLE raw_attributes;
TRUNCATE TABLE raw_mro_attributes;



-- Load attributes from .csv file where each attribute suited on separate row.
--INSERT INTO raw_attributes(id, catalog_number, is_visible, attributes)
SELECT
    mro.id id,
    min(mro.catalog_number) catalog_number,
    min(mro.is_visible) is_visible,
    LISTAGG(scraped.name || ':' || REPLACE(scraped.value, ':', '-'), '|') WITHIN GROUP (ORDER BY scraped.name) attributes
FROM
    temp_BRAND_NAME scraped
JOIN raw_mro_attributes mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
GROUP BY
    mro.id
;



-- Remove duplicate products groupped by CATALOG_NUMBER
DELETE FROM raw_attributes
WHERE ROWID NOT IN (
    SELECT MIN(ROWID)
    FROM raw_attributes
    GROUP BY unify_catalog_number(catalog_number)
);

DELETE FROM raw_mro_attributes
WHERE ROWID NOT IN (
    SELECT MIN(ROWID)
    FROM raw_mro_attributes
    GROUP BY unify_catalog_number(catalog_number)
);



-- Append MRO ID (SKU).
UPDATE raw_attributes SET id = NULL;

MERGE /*+ PARALLEL(20) */
INTO raw_attributes scraped
USING raw_mro_attributes mro
ON (unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number))
WHEN MATCHED THEN UPDATE SET scraped.id = mro.id;

DELETE FROM raw_attributes
WHERE
    id IS NULL
    OR attributes IS NULL
;



-- Fill PRELIMINARY_ATTRIBUTES table.
EXECUTE refresh_base_after_attributes_load;
COMMIT;



-- Merge MRO attributes and detail attributes with preliminary attributes.
INSERT INTO preliminary_attributes(
    id
    , catalog_number
    , brand
    , attribute_name
    , attribute_value
    , origin_attribute_name
    , origin_attribute_value
) SELECT
    id
    , catalog_number
    , brand
    , attribute_name
    , attribute_value
    , attribute_name
    , attribute_value
FROM mro_attributes;

INSERT INTO preliminary_attributes(
    id
    , catalog_number
    , brand
    , attribute_name
    , attribute_value
    , origin_attribute_name
    , origin_attribute_value
) SELECT
    id
    , catalog_number
    , brand
    , detail_attribute_name
    , detail_attribute_value
    , detail_attribute_name
    , detail_attribute_value
FROM mro_detail_attributes;

COMMIT;