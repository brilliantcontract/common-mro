-- Extract MAIN_IMAGE
select
    mro.id
    , scraped.main_image
from scraped_rexnord scraped
join mro_rexnord mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
where
    mro.main_image is null
    and scraped.main_image is not null
;


-- Extract UPC
select
    mro.id
    , scraped.upc upc_number
from scraped_rexnord scraped
join mro_rexnord mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
where
    mro.upc_number is null
    and scraped.upc is not null
;


-- Extract WEIGHT
select
    mro.id
    , scraped.weight
from scraped_rexnord scraped
join mro_rexnord mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
where
    mro.weight is null
    and scraped.weight is not null
;
