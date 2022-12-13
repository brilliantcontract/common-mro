-- Extract MAIN_IMAGE
select
    mro.id
    , scraped.main_image
from scraped_kop_flex scraped
join mro_kop_flex mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
where
    mro.main_image is null
    and scraped.main_image is not null
;


-- Extract UPC
select
    mro.id
    , scraped.upc upc_number
from scraped_kop_flex scraped
join mro_kop_flex mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
where
    mro.upc_number is null
    and scraped.upc is not null
;


-- Extract WEIGHT
select
    mro.id
    , scraped.weight
from scraped_kop_flex scraped
join mro_kop_flex mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
where
    mro.weight is null
    and scraped.weight is not null
;


-- Extract DOCUMENTS
select
    mro.id
    , 'Installation manual' AS name
    , scraped.installation_manual AS url
    , '1' doc_type
from scraped_kop_flex scraped
join mro_kop_flex mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
where
    mro.documents is null
    and scraped.installation_manual is not null
;