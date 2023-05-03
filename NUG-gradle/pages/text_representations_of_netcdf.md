---
title: Text Representations of netCDF
last_updated: 2021-06-28
sidebar: nnug_sidebar
toc: false
permalink: text_representation_of_netcdf.html
---


There are two text notations (CDL and NcML) commonly used for representing the structure and data of a binary netCDF dataset.
Text representations of netCDF datasets are useful because they can be read (and edited) by humans, they can also be read and produced by machines.

* NetCDF CDL (Common Data Language) is a text notation for representing the structure and data of a binary netCDF dataset.
  CDL is widely used in documentation about netCDF datasets and when discussing or experimenting with features of netCDF datasets.
  Detailed specifications for CDL, including its grammar, are on the [NetCDF CDL page](cdl.html) .
* The NetCDF Markup Language (NcML) is an XML dialect for representing the structure and data of a binary netCDF dataset.
  It was developed as part of the netCDF-Java and TDS (THREDDS Data Server) projects and has been used to support on-the-fly modification and aggregation of netCDF datasets.
  Detailed specifications for NcML are available on the [NetCDF Markup Language (NCML) page](ncml.html).
