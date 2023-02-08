-- Review unknown preliminary attribute names.
SELECT
    *
FROM group_by_attribute_name_vw
WHERE
    attribute_name not in (select original_attribute_name from known_attribute_names)
    OR attribute_name not in (select correct_attribute_name from known_attribute_names)
ORDER BY
    total_records DESC
;



-- Process attribute names and values according to KNOWN_ATTRIBUTE_... tables.
BEGIN
    attributes_pkg.replace_unknown_attribute_names_with_known_attribute_names;
    attributes_pkg.replace_unknown_attribute_values_with_known_attribute_values;
    
    attributes_pkg.move_measure_units_from_attribute_values_to_attribute_names;
    
    COMMIT;
END;
/



-- Clean up attribute names and values.
UPDATE preliminary_attributes
SET
    attribute_name = attributes_pkg.clean_up_attribute_name(attribute_name)
    , attribute_value = attributes_pkg.clean_up_attribute_value(attribute_value);



-- Remove attribute with rare names.
SELECT attribute_name
FROM group_by_attribute_name_vw
WHERE total_records < 2;

DELETE FROM preliminary_attributes
WHERE
    attribute_name IN (
        SELECT attribute_name
        FROM group_by_attribute_name_vw
        WHERE total_records < 2
    );



-- Delete attributes without names or values.
SELECT * FROM preliminary_attributes WHERE attribute_name IS NULL;
DELETE FROM preliminary_attributes WHERE attribute_name IS NULL;

SELECT * FROM preliminary_attributes WHERE attribute_value IS NULL;
DELETE FROM preliminary_attributes WHERE attribute_value IS NULL;
