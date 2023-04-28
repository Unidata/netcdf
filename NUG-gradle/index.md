---
title: Introduction
last_updated: 2021-04-06
sidebar: nnug_sidebar
permalink: index.html
toc: false
---

The NetCDF User's Guide (NUG) describes various netCDF concepts and details that may be of interest to all netCDF users, independent of what library/tool they use.
The NUG provides high-level descriptions of netCDF concepts useful to users and creators of netCDF data (e.g., data users, data producers, data managers, and tool developers).
It also provides the details and specifications needed by developers and maintainers of any netCDF implementations (e.g. netCDF-4 based on HDF5 for h5netcdf, ncZarr based on Zarr for netCDF-java).  

The NUG describes and specifies the details of
* the [netCDF data models](data_models.html)
* the [netCDF file formats and encodings](file_formats.html)
* the netCDF [CDL](cdl.html) (Common Data form Language), a text notation for representing the structure and data of a binary netCDF dataset

The NUG also:

* provides a set of [Best Practices](best_practices.html)
* defines a basic set of [attribute conventions](nug_conventions.html) (e.g., units, valid_min, and scale_factor/add_offset)
* provides references to other community conventions (e.g., CF, UGRID) - see current list/page
* How to use netCDF (gallery of tools)

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