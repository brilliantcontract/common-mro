/*
    Processing attribute values.
*/

select * from group_by_attribute_name_vw
where attribute_name not in (select original_attribute_name from known_attribute_names)
;



update preliminary_attributes
set attribute_value = replace(attribute_value, '-', ' to ')
where  attribute_name = 'Ratio'
;

    
select
    attribute_value
    , count(*) number_of_products
from sorted_preliminary_attributes
where attribute_name = 'Media'
group by attribute_value
order by attribute_value
;

delete from preliminary_attributes
where attribute_value is null;