select
    id
    , 'None' attributes
from mro_attributes
where
    attribute_name in (
        select
            attribute_name
        from group_by_mro_attribute_name_vw
        where
            total_records < 6
    )
group by
    id
;


select
    id
    , LISTAGG(attribute_name, ', ') WITHIN GROUP (ORDER BY attribute_name)
from mro_attributes
where
    attribute_name in (
        select
            attribute_name
        from group_by_mro_attribute_name_vw
        where
            total_records < 6
    )
group by
    id
;