/*
    Processing attribute names.
*/
select * from group_by_attribute_name_vw
where
    attribute_name not in (select original_attribute_name from known_attribute_names)
    --and upper(attribute_name) like upper('%teeth%')
order by
    attribute_name
;
--select * from known_attribute_names where upper(original_attribute_name) like upper('%teeth%');

select * from group_by_attribute_name_and_value_vw
where
    attribute_name not in (select original_attribute_name from known_attribute_names)
    and upper(attribute_name) like upper('%Hole Diameter%')
;


select *
from mro_group_by_attribute_name_mv
where upper(attribute_name) like upper('%Bearing Type%')
;

select attribute_name, attribute_value
from mro_group_by_attribute_name_and_value_mv
where upper(attribute_name) like upper('%Thread%')
;






/*
    Move measure units from attribute values to attribute names.
*/
BEGIN
    ATTRIBUTES_PKG.MOVE_MEASURE_UNITS_FROM_ATTRIBUTE_VALUES_TO_ATTRIBUTE_NAMES();
END;
/



/*
    Custom updates.
*/
update preliminary_attributes
    set attribute_name = replace(attribute_name, 'AirMax', 'Air Max');

update preliminary_attributes
    set attribute_name = replace(attribute_name, 'WaterMax', 'Water Max');

-- Remove duplicate attributes after attribute names were fixed. There are actually duplicates.
exec attributes_pkg.remove_duplicated_attribute_names;

-- Remove attribute with rare names.
delete from preliminary_attributes
where attribute_name in (
    select attribute_name from group_by_attribute_name_vw
    where
        attribute_name not in (select original_attribute_name from known_attribute_names)
        and total_records < 100
);

delete from preliminary_attributes
where attribute_name is null;



