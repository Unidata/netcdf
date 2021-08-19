---
title: NetCDF File Formats
last_updated: 2021-04-23
sidebar: nnug_sidebar
toc: false
permalink: file_formats.html
---

NetCDF has two main file formats: the `netCDF-3` file format which supports the netCDF Classic Data Model and the `netCDF-4` file format which supports the netCDF Enhanced Data model.
The `netCDF-3` file format has two variants.

The four file formats are:
* `CDF-1` (aka `netCDF-3` or `classic`) - supports the netCDF Classic Data Model.
* `CDF-2` (aka `64-bit Offset`) - a variant of `CDF-1` that removes some size limitations on variables and datasets, it supports the netCDF Classic Data Model.
* `CDF-5` (aka `64-bit Data` or pnetcdf) - a variant of `CDF-2` which adds support for 64-bit integer and unsigned integer data types. It supports the netCDF Classic Data Model plus the additional integer types.
* `netCDF-4` - supports the netCDF Enhanced Data Model. It is written using the HDF5 file format. It is restricted to a subset of HDF5 while also including additional metadata to represent aspects of the netCDF Enhanced Data Model that do not have a corresponding concept in the HDF5 data model.

##  NetCDF
