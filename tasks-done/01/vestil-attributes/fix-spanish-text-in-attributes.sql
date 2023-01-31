select
    *
from preliminary_attributes
where
    upper(attribute_value) like '%ESTA%'
;

select
    attribute_name
    , attribute_value
    , count(*)
from preliminary_attributes
where
    attribute_name in ('Legend', 'Sign Subject Matter')
    and regexp_like(attribute_value, ' [a-zA-Z] ')
group by
    attribute_name
    , attribute_value
;


-- Fix detail attributes
select * from (
    SELECT
        id,
        LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) detail_attributes
    FROM preliminary_attributes
    WHERE
        attribute_name NOT IN (
            'Height (in)'
            , 'Width (in)'
            , 'Double-Sided'
            , 'Header'
            , 'Legend'
            , 'Sign Material'
            , 'Sign Shape'
            , 'Sign Background Color'
            , 'Color'
            , 'Overall Length (in)'
            , 'Overall Width (in)'
            , 'Material'
            , 'Overall Height (in)'
            , 'Depth (in)'
            , 'Length (in)'
            , 'Finish'
        )
        AND attribute_value not in (
            select
                attribute_value
            from preliminary_attributes
            where
                attribute_name in ('Legend', 'Sign Subject Matter', 'Features', 'Application')
                and (
                    regexp_like(attribute_value, ' [a-zA-Z] ')
                    or length(attribute_value) > 30
                )
            group by
                attribute_name
                , attribute_value
        )
    GROUP BY id
)
where
    id in (
        select
            id
        from preliminary_attributes
        where
            attribute_name in ('Legend', 'Sign Subject Matter')
            and regexp_like(attribute_value, ' [a-zA-Z] ')
    )
;



-- Fix attributes.
select * from (
    SELECT
        id,
        LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) attributes
    FROM preliminary_attributes
    WHERE
        attribute_name IN (
            'Height (in)'
            , 'Width (in)'
            , 'Double-Sided'
            , 'Header'
            , 'Legend'
            , 'Sign Material'
            , 'Sign Shape'
            , 'Sign Background Color'
            , 'Color'
            , 'Overall Length (in)'
            , 'Overall Width (in)'
            , 'Material'
            , 'Overall Height (in)'
            , 'Depth (in)'
            , 'Length (in)'
            , 'Finish'
        )
        AND attribute_value not in (
            select
                attribute_value
            from preliminary_attributes
            where
                attribute_name in ('Legend', 'Sign Subject Matter', 'Features', 'Application')
                and (
                    regexp_like(attribute_value, ' [a-zA-Z] ')
                    or length(attribute_value) > 30
                )
            group by
                attribute_name
                , attribute_value
        )
    GROUP BY id
)
where
    id in (
        select
            id
        from preliminary_attributes
        where
            attribute_name in ('Legend', 'Sign Subject Matter')
            and regexp_like(attribute_value, ' [a-zA-Z] ')
    )
;


delete from temp_vestil where detail_attributes is null;
select count(*) from temp_vestil;



select * from temp_vestil where id not in
    (
        select
            id
        from preliminary_attributes
        where
            attribute_name in ('Legend', 'Sign Subject Matter')
            and regexp_like(attribute_value, ' [a-zA-Z] ')
    )

union all

select * from (
    SELECT
        id,
        LISTAGG(attribute_name || ':' || attribute_value, '|') WITHIN GROUP (ORDER BY attribute_name) detail_attributes
    FROM preliminary_attributes
    WHERE
        attribute_name NOT IN (
            'Height (in)'
            , 'Width (in)'
            , 'Double-Sided'
            , 'Header'
            , 'Legend'
            , 'Sign Material'
            , 'Sign Shape'
            , 'Sign Background Color'
            , 'Color'
            , 'Overall Length (in)'
            , 'Overall Width (in)'
            , 'Material'
            , 'Overall Height (in)'
            , 'Depth (in)'
            , 'Length (in)'
            , 'Finish'
        )
        AND attribute_value not in (
            select
                attribute_value
            from preliminary_attributes
            where
                attribute_name in ('Legend', 'Sign Subject Matter')
                and regexp_like(attribute_value, ' [a-zA-Z] ')
            group by
                attribute_name
                , attribute_value
        )
    GROUP BY id
)
where
    id in (
        select
            id
        from preliminary_attributes
        where
            attribute_name in ('Legend', 'Sign Subject Matter')
            and regexp_like(attribute_value, ' [a-zA-Z] ')
    )
;