select
    mro.id
    , mro.catalog_number
    , scraped.list_price
    , scraped.your_price
    , scraped.label message
from scraped_leeson_prices scraped
join mro_leeson mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.catalog_number)
;

select
    mro.id
    , mro.catalog_number
    , scraped.list_price
    , scraped.your_price
    , scraped.label message
from scraped_marathon_prices scraped
join mro_marathon mro
    on unify_catalog_number(mro.catalog_number) = unify_catalog_number(scraped.part_number)
;
