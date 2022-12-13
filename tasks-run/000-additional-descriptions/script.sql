-- 5263
truncate table tide_descriptions;

--create view tided_descriptions as
select
    id
    , mro.tidy_html_snippet_pkg.tidy(additional_description) additional_description
from tide_descriptions
;
