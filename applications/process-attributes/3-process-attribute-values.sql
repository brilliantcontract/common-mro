select * from group_by_attribute_name_vw
where attribute_name not in (select original_attribute_name from known_attribute_names)
;



delete from preliminary_attributes
where attribute_value is null;


-- Normalize spaces.
UPDATE preliminary_attributes
SET
    attribute_name = trim(regexp_replace(attribute_name, ' +', ' ')),
    attribute_value = trim(regexp_replace(attribute_value, ' +', ' '));
