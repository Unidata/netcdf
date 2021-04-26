---
title: NUG - Specifications and Best Practices
last_updated: 2021-04-06
sidebar: nnug_sidebar
permalink: index.html
toc: false
---

## NetCDF
<!-- From netCDF home page: https://www.unidata.ucar.edu/software/netcdf/ --> 
NetCDF (Network Common Data Form) is a set of
machine-independent data formats and software libraries
that support the creation, access, and sharing
of array-oriented scientific data.
It is also a community standard for sharing scientific data.
The Unidata Program Center supports and maintains
netCDF programming interfaces for C, C++, Java, and Fortran.
Programming interfaces are also available
for Python, IDL, MATLAB, R, Ruby, and Perl.

<!-- Look at pulling from "Purpose of netCDF" in NUG/guide.md -->

## NetCDF Users Guide

The NetCDF Users Guide - Abstract Specification
describes the netCDF data models and file formats
independent of the various software implementations.

<!-- From draft text in Roadmap.md -->
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

