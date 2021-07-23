---
title: NetCDF File Formats
last_updated: 2021-04-23
sidebar: nnug_sidebar
toc: false
permalink: file_formats.html
---

NetCDF has two main file formats and several variants
* CDF-1 (aka netCDF-3 or classic) file format
  * Supports the netCDF Classic Data Model
* CDF-2 (aka 64-bit Offset) file format - a variant of CDF-1 that removes some size limitations on variables and datasets
  * Supports the netCDF Classic Data Model
* CDF-5 (aka 64-bit Data or ?pnetcdf) file format - a variant of CDF-2 which adds support for 64-bit and unsigned integer data types
  * Supports the netCDF Classic Data Model with additional integer data types
* netCDF-4 (HDF5 based) file format
  * Supports the netCDF Enhanced Data Model
  * Written using the HDF5 file format.
    It is restricted to a subset of HDF5 while also including additional metadata
    to represent aspects of the netCDF Enhanced Data Model
    that do not have a corresponding concept in the HDF5 data model.

##  NetCDF
