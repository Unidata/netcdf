---
title: What is NetCDF?
last_updated: 2021-06-28
sidebar: nnug_sidebar
toc: false
permalink: what_is_netcdf.html
---

<!-- NOTE:
Text mainly from [netCDF home page](https://www.unidata.ucar.edu/software/netcdf/),
self-describing and portable bit from NUG/guide.md#netcdf_purpose
-->

NetCDF (Network Common Data Form) is a set of machine-independent data formats and interfaces
that support the creation, access, and sharing of array-oriented scientific data
in a form that is both portable and self-describing.
"Self-describing" means that a dataset includes information defining the data it contains.
"Portable" means that the data in a dataset is represented in a form that can be accessed
by computers with different ways of storing integers, characters, and floating-point numbers.
NetCDF is also a community standard for sharing scientific data.

<!-- NOTE:
Text from NUG/netcdf_data_set_components.md#data_model, paragraph 1
-->
A netCDF dataset contains named dimensions, variables, and attributes.
These components can be used together to capture the meaning of data
and relations among data fields in an array-oriented dataset.

The Unidata Program Center supports and maintains freely distributed
netCDF programming interfaces for C, C++, Java, and Fortran.
Programming interfaces are also available for Python, IDL, MATLAB, R, Ruby, and Perl.

Reference documentation for the Unidata maintained implementations
are available at the main Unidata netCDF web page (https://www.unidata.ucar.edu/software/netcdf/).

## NetCDF Goals and Design Principles

[[Use TEXT From NUG/netcdf_introduction.md#performance ???]]

## NetCDF Limitations

[[Use TEXT From NUG/netcdf_introduction.md#limitations ???]]
