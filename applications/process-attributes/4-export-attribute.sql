/*
    Export attribute.
*/
UPDATE preliminary_attributes
SET
    attribute_name = trim(regexp_replace(attribute_name, ' +', ' ')),
    attribute_value = trim(regexp_replace(attribute_value, ' +', ' '));


-- Export attributes
SELECT
    id,
    LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) attributes
FROM preliminary_attributes
WHERE
    attribute_name in (
        SELECT
            attribute_name
        FROM accepted_attributes_by_mro
    )
    AND id IS NOT NULL
GROUP BY
    id
HAVING
    LENGTH(LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name)) > 0
;


-- Export detail attributes
SELECT
    id,
    LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) detail_attributes
FROM preliminary_attributes
WHERE
    attribute_name NOT IN (
        SELECT
            attribute_name
        FROM accepted_attributes_by_mro
    )
GROUP BY
    id
HAVING
    LENGTH(LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name)) > 0
;


SELECT
    id,
    attribute_name,
    COUNT(*)
FROM preliminary_attributes
GROUP BY
    id
    , attribute_name
HAVING
    COUNT(*) > 1
ORDER BY
    COUNT(*) desc;
