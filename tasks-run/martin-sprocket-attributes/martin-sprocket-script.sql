SELECT
    id
  , additional_description
  , attributes
  , detail_attributes
  , catalog_number
  , is_visible
FROM
    temp_mro_martin_sprocket;

SELECT
    file_name
  , catalog_number
  , name
  , value
FROM
    temp_scraped_martin_sprocket;

select
    count(*)
from temp_mro_martin_sprocket
where
    unify_catalog_number(catalog_number) in (select distinct unify_catalog_number(catalog_number) from temp_scraped_martin_sprocket)
    and attributes is null
;



/*
    Clean up scraped data.
*/
delete
from temp_scraped_martin_sprocket
where
    name is null
    and value is null;



select
    name
    , count(*)
from temp_scraped_martin_sprocket
where
    value is null
group by name;

set define off;

delete from temp_scraped_martin_sprocket
where
    name in (
        'State/Province',
        'City',
        'Address',
        'Key Way',
        '* Name',
        '* Company',
        '* Postal Code',
        'Contact Phone',
        '* Email',
        'Martin Drawing Library Registration',
        '* I have read and agree to the Terms & Conditions',
        '* Country'
    )
;



select
    name
    , count(*)
from temp_scraped_martin_sprocket
group by name;






insert into atr_raw_attributes(id, catalog_number, is_visible, attributes)
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




SET TIMING ON;
SET DEFINE ON;
ALTER SESSION FORCE PARALLEL QUERY;

select * from atr_preliminary_attributes;
select '"in." measure present in attribute value.' cause, count(*) from atr_preliminary_attributes where upper(attribute_value) like '% IN.';
select '"lbs." measure present in attribute value.' cause, count(*) from atr_preliminary_attributes where upper(attribute_value) like '% LBS.';
select '"max." word present in attribute name.' cause, count(*) from atr_preliminary_attributes where upper(attribute_value) like '% MAX.';
select '"min." word present in attribute name.' cause, count(*) from atr_preliminary_attributes where upper(attribute_value) like '% MIN.';
select 'Attribute value has dash sign (-) between 2 numbers, for example "1-1/2".' cause, count(*) from atr_preliminary_attributes where regexp_like(upper(attribute_value), '[0-9]-[0-9]');
select 'Trade mark sign was found in attribute value.' cause, count(*) from atr_preliminary_attributes where regexp_like(upper(attribute_value), '(©|&#169;|&COPY;|®|&#174;|&REG;|℗|&#8471;|™|&#8482;|&TRADE;|℠|&#8480;)');
select 'Trade mark sign was found in attribute name.' cause, count(*) from atr_preliminary_attributes where regexp_like(upper(attribute_name), '(©|&#169;|&COPY;|®|&#174;|&REG;|℗|&#8471;|™|&#8482;|&TRADE;|℠|&#8480;)');
select 'Catalog number is NULL.' cause, count(*) from atr_preliminary_attributes where catalog_number is null;
select 'Attribute name is NULL.' cause, count(*) from atr_preliminary_attributes where attribute_name is null;
select 'Attribute name starts with low case letter.' cause, count(*) from atr_preliminary_attributes where regexp_like(attribute_name, '^[a-z]');
select 'Every word in attribute names have to start with uppercase letter. `Bore Size` is correct, `Bore size` is not correct.' cause, count(*) from atr_preliminary_attributes where not regexp_like(attribute_name, '^(([A-Z][a-z.\-]+|of|\([a-z-]+\)|\([A-Z-]+\)|at|[A-Z]+|&|per|by|thru|to) *)+$');
select 'Attribute names have not to consist of only uppercase letters. `Bore` is correct, `BORE` is not correct.' cause, count(*) from atr_preliminary_attributes where regexp_like(attribute_name, '[A-Z]{2,}');
select 'Attribute names have not to contain any symbols that out of ASCII. `Bore` is correct, `Bôré` is not correct.' cause, count(*) from atr_preliminary_attributes where regexp_like(attribute_name, '[^ -~]');
select 'Attribute names have not to contain any punctuation marks except dot, dash and brackets. `Bore` is correct, `"Bore"` is not correct.' cause, count(*) from atr_preliminary_attributes where regexp_like(attribute_name, '[``~!@#$%^&*{}_,<>;:?/=+|\[]');
select 'Attribute values have not to contain non ASCII symbols. `Brown` is correct, `Brôwn` is not correct.' cause, count(*) from atr_preliminary_attributes where regexp_like(attribute_value, '[^ -~]');

select attribute_name from atr_preliminary_attributes where
not regexp_like(attribute_name, '^(([A-Z][a-z.\-]+|of|\([a-z-]+\)|\([A-Z-]+\)|at|[A-Z]+|&|per|by|thru|to) *)+$');








-- Verify that column values of NAME and VALUE don't contain symbol ':' or '|'
select * from preliminary_attributes where name like '%:%' or name like '%|%' or value like '%:%' or value like '%|%';
update preliminary_attributes set name = replace(name, ':', ' '), value = replace(value, ':', ' ');
update preliminary_attributes set name = replace(name, '|', ' '), value = replace(value, '|', ' ');


-- !!! REVERSE STATEMENT !!! 1.2 Attribute names have not to contain measuring unit. `Bore` is correct, `Bore (in)` is not correct.

-- 2.1 Use a space in attribute values between value and measure unit. `4 in` is correct, `4in` is not correct.
select * from preliminary_attributes where regexp_like(value, '\d[A-Za-z]');

-- 2.2 Use in over " for inch measure unit. `4 in` is correct, `4"` is not correct.
select regexp_replace(value, '(\d)"$', '\1 in') new_value, value original_value from preliminary_attributes where regexp_like(value, '\d"$');
update preliminary_attributes set value = regexp_replace(value, '(\d)"$', '\1 in') where regexp_like(value, '\d"$');

select regexp_replace(value, '(\d)in$', '$1 in') new_value, value original_value from preliminary_attributes where regexp_like(value, '\din$');
update preliminary_attributes set value = regexp_replace(value, '(\d)in$', '$1 in') where regexp_like(value, '\din$');

-- 2.3 Use shorthands like in, ft, mm over longhands like inches, foot, millimeters. `4 in` is correct, `4 inches` is not correct.

-- 2.4 Use imperial system convention over metric system convention. `4 in` is correct, `101.6 mm` is not correct.

-- 2.5 Attribute values in metric system have not to be converted to imperial system. `101.6 mm` convert to `4 in`.

-- 2.6 Do not use both metric systems (imperial and metric) at the same time. `1-1/2 in` is correct, `1-1/2" (38.1 mm)` is not correct.
select value, regexp_replace(value, '(.* in).+', '\1') from preliminary_attributes where regexp_like(value, '(.* in).+');
update preliminary_attributes set value = regexp_replace(value, '(.* in).*', '\1') where regexp_like(value, '(.* in).+');

-- 2.7 Do not use special HTML symbols as measure unit. `15°` is correct, `15&deg;` is not correct.
select * from preliminary_attributes where regexp_like(value, '(?:&(?:[A-Za-z_:][\w:.-]*|\#(?:[0-9]+|x[0-9a-fA-F]+));)');

-- 2.9 Use only lowercase letters in measure unit names. `50 ft` is correct, `50 FT` is not correct.

-- 3.1 Use decimal point numbers over fractional numbers. `0.5 in` is correct, `1/2 in` is not correct.

-- 3.2 Attribute values in fractional format have to be converted to decimal point format. `0.5 in` is correct, `1/2 in` is not correct.
select 
    value,
    round(to_number(xmlquery(replace(regexp_replace(value,'( )+','+'),'/',' div ') returning content).getStringVal()), 4) example,
    count(*)
from preliminary_attributes
where regexp_like(value, '([1-9][0-9]*\s|)[1-9][0-9]*\/[1-9][0-9]*')
group by value
order by value;
update preliminary_attributes set value = round(to_number(xmlquery(replace(regexp_replace(value,'( )+','+'),'/',' div ') returning content).getStringVal()), 4) where regexp_like(value, '([1-9][0-9]*\s|)[1-9][0-9]*\/[1-9][0-9]*');

-- 3.3 Always use leading zero before decimal point. `0.5 in` is correct, `.5 in` is not correct.
select value, regexp_replace(value, '(([^0-9]|^)\.\d+)', '0\1') examle, count(*) from preliminary_attributes where regexp_like(value, '([^0-9]|^)\.\d+') group by value order by value;
update preliminary_attributes set value = regexp_replace(value, '(([^0-9]|^)\.\d+)', '0\1') where regexp_like(value, '([^0-9]|^)\.\d+');

-- 3.4 Remove trailing zeros. `1.5 in` and `1 in` is correct, `1.500 in` and `1.000 in` is not correct.
select value, regexp_replace(regexp_replace(value, '(^|.*\s)(\d\.\d*?)0+(\s.*|$)', '\1\2\3'), '\. ', ' ') from preliminary_attributes where regexp_like(value, '(^|\s)\d\.\d*0+(\s|$)');
update preliminary_attributes set value = regexp_replace(regexp_replace(value, '(^|.*\s)(\d\.\d*?)0+(\s.*|$)', '\1\2\3'), '\. ', ' ') where regexp_like(value, '(^|\s)\d\.\d*0+(\s|$)');

-- 3.5 Remove trailing dot in numbers
select value, regexp_replace(value, '(^|.*\s)(\d+)\.(\s.*|$)', '\1\2\3') from preliminary_attributes where regexp_like(value, '(^|\s)\d\.(\s|$)');
update preliminary_attributes set value = regexp_replace(value, '(^|.*\s)(\d+)\.(\s.*|$)', '\1\2\3') where regexp_like(value, '(^|\s)\d\.(\s|$)');

-- 4.1 List of values have to be splitted with comma. `Motorized, Quill Input` is correct, `Motorized; Quill Input` is not correct.

-- 4.2 Every item in list of values have to start with upper letter. `Motorized, Quill Input` is correct, `Motorized, quill input` is not correct.

-- 4.3 Attribute values should present only single characteristic. Three separate attribute values `48 in`, `24 in`, `60 in` is correct, one single attribute value`48inx24inx60in` is not correct.

-- 4.4 Attribute values that represent range of values should use dash sign with spaces from both sides. `5 - 8` is correct, `5-8` and `5 to 8` is not correct.

-- 4.5 Attribute values that represent range of values should use measure unit only once at the end. `5 - 8 ft` is correct, `5 ft - 8 ft` is not correct.

-- NOTE Watch out spaces at the start and end of attribute names and values.
select name, value from preliminary_attributes where regexp_like(name, '^\s') or regexp_like(name, '\s$') or regexp_like(value, '^\s') or regexp_like(value, '\s$');
update preliminary_attributes set name = regexp_replace(name, '^\s'), value = regexp_replace(value, '^\s') where regexp_like(name, '^\s') or regexp_like(value, '^\s');
update preliminary_attributes set name = regexp_replace(name, '\s$'), value = regexp_replace(value, '\s$') where regexp_like(name, '\s$') or regexp_like(value, '\s$');

-- NOTE Detect incorrect number formats `3-5/8 in`, should be `3 5/8 in`.
select value, regexp_replace(value, '([0-9./])-([0-9./])', '\1 \2') from preliminary_attributes where regexp_like(value, '([0-9./])-([0-9./])');
update preliminary_attributes set value = regexp_replace(value, '([0-9./])-([0-9./])', '\1 \2') where regexp_like(value, '([0-9./])-([0-9./])');
