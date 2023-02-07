from scrapy import Request
import scrapy
import requests
from bs4 import BeautifulSoup
import json

from crawlers.spiders.basespider import BaseSpider, ProxyMixin, LoginMixin


# class EatonComSpider(ProxyMixin, BaseSpider):
class EatonComSpider(BaseSpider):
    name = 'eaton'
    allowed_domains = ['www.eaton.com']
    start_urls = ['https://www.eaton.com/']
    search_product_url = "https://www.eaton.com/us/en-us/" \
                         "site-search.html.searchTerm${catalog_number}.tabs$all.html"


    def parse(self, response, **kwargs):
        items = response.xpath("//a[@class='results-list-submodule__name-link']")
        for item in items:
            sku = item.xpath("./text()").get()
            url = item.xpath("./@href").get()
            if sku == response.meta.get('catalog_number'):
                yield Request(
                    url=url,
                    method="POST",
                    callback=self.parse_item,
                    meta={
                        'id': response.meta.get("id"),
                        'catalog_number': response.meta.get("catalog_number")
                    }
                )

    def parse_item(self, response):
        final_data = {}
        main = response.xpath('/html/body').get()
        soup = BeautifulSoup(main, 'html.parser')
        maindiv = soup.find('div', attrs={'class':'slider-prod-disc'})
        imageTag = maindiv.find('div', attrs={'class':'module-media-gallery__slide-preview'})
        main_image_tag = imageTag.find('img', attrs={'class':'module-media-gallery__image'})
        image = main_image_tag['data-src']
        desc = maindiv.find('div', attrs={'class':'module-product-detail-card__description'})
        desc = ''
        if desc:
            desc = desc.text

        feature_div = soup.find('div', attrs={'class':'product-specifications'})
        specification_div2 = feature_div.find('div', attrs={'id':'1'})

        specifications = feature_div.find('div', attrs={'class':'module-table'})
        

        specifications_key2 = ''
        specifications2 = specification_div2.findAll('div', attrs={'class':'module-table__row'})
        if specifications2:
            for i, specification in enumerate(specifications2):
                i = i+1
                key = specification.find('div', attrs={'class':'module-table__col'})
                if key:
                    key = key.text
                    if key:
                        key = key.strip()
                value = specification.find('div', attrs={'class':'module-table__value'})
                if value:
                    value = value.text
                    if value:
                        value = value.strip()
                
                if key and value:
                    if i == len(specifications2):
                        specifications_key2 += str(key)+":"+str(value)
                    else:
                        specifications_key2 += str(key)+":"+str(value)+" | "

        panel_group = soup.find('div', attrs={'class':'panel-group'})
        if panel_group:
            collapse_1 = panel_group.find('div', attrs={'id':'collapse-1'})
            collapse_2 = panel_group.find('div', attrs={'id':'collapse-4'})

            catalog_download = ''
            if collapse_1:
                resourcelist__downloaditem_1 = collapse_1.findAll('li', attrs={'class':'resource-list__download-item'})
                for i, resourcelist__downloaditem in enumerate(resourcelist__downloaditem_1):
                    i = i+1
                    link = resourcelist__downloaditem.find('a', attrs={'class':'resource-list__title-link'})
                    if link:
                        f_link = link['href']
                        if f_link:
                            final_link = f_link.strip()
                        name = link.text
                        if name:
                            name = name.strip()
                    if name and final_link:
                        if i == len(resourcelist__downloaditem_1):
                            catalog_download += str(name)+":"+str(final_link)
                        else:
                            catalog_download += str(name)+":"+str(final_link)+" | "

            specification_download = ''
            if collapse_2:
                resourcelist__downloaditem_2 = collapse_2.findAll('li', attrs={'class':'resource-list__download-item'})
                for i, resourcelist__downloaditem2 in enumerate(resourcelist__downloaditem_2):
                    i = i+1
                    link2 = resourcelist__downloaditem2.find('a', attrs={'class':'resource-list__title-link'})
                    if link2:
                        f_link2 = link2['href']
                        if f_link2:
                            final_link2 = f_link2.strip()
                        # if f_link2:
                        name2 = link2.text
                        if name2:
                            name2 = name2.strip()
                    
                    if name2 and final_link2:
                        if i == len(resourcelist__downloaditem_1):
                            specification_download += str(name2)+":"+str(final_link2)
                        else:
                            specification_download += str(name2)+":"+str(final_link2)+" | "
        
        final_data['image'] = image
        final_data['description'] = desc.strip()
        final_data['additional_description'] = specifications
        final_data['product_specification'] = specifications_key2
        final_data['catalog_datasheets'] = catalog_download
        final_data['specific_datasheets'] = specification_download
        # print(final_data)
        return final_data
