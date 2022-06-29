---
title: Introduction
last_updated: 2021-04-06
sidebar: nnug_sidebar
permalink: index.html
toc: false
---

The NetCDF User's Guide (NUG) describes various netCDF concepts and details of interest to all netCDF users.
It describes the [netCDF data models](data_models.html) and [netCDF file formats and encodings](file_formats.html).
It also presents a set of [best practices](best_practices.html) and available community conventions.

## Use of CDL in this Document

Throughout the NUG, we will use the [CDL (netCDF Comman Data Language) text notation](cdl.html)
to present the content and structure of netCDF datasets for all examples. A simple example is shown here

````
netcdf minimal_example {   // very simple example of CDL notation
    dimensions:
        lon = 3 ;
        lat = 8 ;
    variables:
        float lon(lon)
        float lat(lat)
        float rh(lon, lat) ;
    // global attributes
    :title = "Simple example" ;

    data: // data for lon, lat, and rh not shown for brevity
}
````

A full description of CDL, along with discussion and more detailed examples,
can be found on the ["Common Data Language" page](cdl.html).