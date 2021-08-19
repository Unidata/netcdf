---
title: NUG - Specifications and Best Practices
last_updated: 2021-04-06
sidebar: nnug_sidebar
permalink: index.html
toc: false
---

## NetCDF
<!-- NOTE:
Text mainly from [netCDF home page](https://www.unidata.ucar.edu/software/netcdf/),
self-describing and portable bit from NUG/guide.md#netcdf_purpose
-->

NetCDF (Network Common Data Form) is a set of
machine-independent data formats and interfaces
that support the creation, access, and sharing
of array-oriented scientific data
in a form that is both portable and self-describing.
"Self-describing" means that a dataset includes information defining the data it contains.
"Portable" means that the data in a dataset is represented in a form that can be accessed
by computers with different ways of storing integers, characters, and floating-point numbers.
NetCDF is also a community standard for sharing scientific data.

The Unidata Program Center supports and maintains freely distributed
netCDF programming interfaces for C, C++, Java, and Fortran.
Programming interfaces are also available
for Python, IDL, MATLAB, R, Ruby, and Perl.

Reference documentation for the Unidata maintained implementations
are available at the main Unidata netCDF web page (https://www.unidata.ucar.edu/software/netcdf/).

## NetCDF Users Guide

The "NetCDF Users Guide - Specification & Best Practices"
describes the netCDF data models and file formats
independent of the various software implementations.
It contains information about netCDF that is of interest across specific implementations.

<!-- NOTE:
Text from Roadmap.md
-->

NUG is a document that describes netCDF to any user of netCDF,
independent of which library/tool they use. Including

* Best practices
* Basic attribute conventions (units, valid_min, etc.)
* How to use netCDF (gallery of tools)
* External conventions (e.g., CF, UGRID) - see current list/page

NUG is a document to captures technical details
(maybe in Appendix) needed by developers of an implementation of netCDF

Document to describe netCDF to e.g. program manager, data producer,
data user, data manager

Document with technical details for anyone working
on a netCDF implementation
based on one of the formats
(e.g. netCDF-4 based on HDF5 for h5netcdf,
ncZarr based on Zarr for netCDF-java)
