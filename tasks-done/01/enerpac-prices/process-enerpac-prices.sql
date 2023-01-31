-- Delete records without list price and net price.
select
    *
from scraped_enerpac_prices
where
    (
        list_price is null
        and net_price is null
    )
    or catalog_number is null
;

delete
from scraped_enerpac_prices
where
    (
        list_price is null
        and net_price is null
    )
    or catalog_number is null
;


-- Export list prices
select
    me.id
    , sep.list_price
from scraped_enerpac_prices sep
join mro_enerpac me
    on unify_catalog_number(me.catalog_number) = unify_catalog_number(sep.catalog_number)
where
    sep.list_price is not null
;


-- Export net prices
select
    me.id
    , sep.net_price
from scraped_enerpac_prices sep
join mro_enerpac me
    on unify_catalog_number(me.catalog_number) = unify_catalog_number(sep.catalog_number)
where
    sep.net_price is not null
;