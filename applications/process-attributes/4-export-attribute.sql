/*
    Export attribute.
*/
UPDATE preliminary_attributes
SET
    attribute_name = regexp_replace(attribute_name, ' +', ' '),
    attribute_value = regexp_replace(attribute_value, ' +', ' ');


-- Export attributes
SELECT
    id,
    LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) attributes
FROM preliminary_attributes
WHERE
    attribute_name in (
        'Bearing Type'
        , 'Mounted Clearance'
        , 'Frame Material'
        , 'Shaft Locking'
    )
    AND id IS NOT NULL
GROUP BY id;


-- Export detail attributes
SELECT
    id,
    LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) detail_attributes
FROM preliminary_attributes
WHERE
    attribute_name NOT IN (
        'Bearing Type'
        , 'Mounted Clearance'
        , 'Frame Material'
        , 'Shaft Locking'
    )
GROUP BY id;


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
