---
title: NetCDF User's Guide 
last_updated: 2021-04-06
sidebar: nnug_sidebar
permalink: index.html
toc: false
---

## Introduction
### NetCDF

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

### The Purpose of the NetCDF User's Guide

The "NetCDF User's Guide" describes netCDF to any user,
independent of which library/tool they use.
It describes the netCDF data models and file formats
independent of the various software implementations.
It also provides community conventions and best practices.
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

## The Sections of the NUG
### NetCDF Data Models
NetCDF has two main data models.
* The **netCDF Classic Data Model**, which represents a dataset with named variables, dimensions, and attributes.
* The **netCDF Enhanced Data Model**, which adds hierarchical structure, string and unsigned integer types, and user-defined types.

Some file formats and encodings support subsets of the Enhanced Data Model
or minor extensions to the Classic Data Model.
For example, CDF-5 extends the Classic Data Model with the addition of
support for unsigned integer and 64-bit integer data types.

Full details and discussion of the netCDF data models can be found in the NUG "Data Models" page.

#### NetCDF Classic Data Model

A diagram of the Classic Data Model exhibits its simplicity:
{% include image.html file="nc-classic-uml.png" alt="netCDF Classic Data Model UML" caption="" %}

#### NetCDF Enhanced Data Model

A UML diagram of the enhanced netCDF data model
shows (in red) what it adds to the classic netCDF data model:
{% include image.html file="nc-enhanced-uml.png" alt="netCDF Enhanced Data Model UML" caption="" %}

### Text Representations of NetCDF Datasets

#### CDL

NetCDF CDL (Common Data form Language) is a text notation for representing the structure and data of a binary netCDF dataset.
CDL can be read (and edited) by a human. It can also be read and produced by machines. For instance, a CDL description can be generated, given a netCDF file, by the `ncdump` utility and a netCDF file can be generated, given a CDL desription, by the `ncgen` utility.

[//]: # (TODO: Add a simple example of CDL)

A full description of CDL can be found on the ["Common Data Language" page](cdl.html).

#### NcML

[//]: # (TODO: Decide if NcML should be included in the NUG. And what should be pulled from netCDF-Java.)
A full description of NcML can be found on the ...

### NetCDF File Formats, Encodings, and Mappings

NetCDF has two main file formats:
the `netCDF-3` file format which supports the netCDF Classic Data Model
and the `netCDF-4` file format which supports the netCDF Enhanced Data model.
Along with the `netCDF-3` (aka `CDF-1`) file format, there are also two variants, `CDF-2` and `CDF-5`.

The four file formats are:
* `CDF-1` (aka `netCDF-3` or `classic`) - supports the netCDF Classic Data Model.
* `CDF-2` (aka `64-bit Offset`) - a variant of `CDF-1` that removes some size limitations on variables and datasets, it supports the netCDF Classic Data Model.
* `CDF-5` (aka `64-bit Data` or pnetcdf) - a variant of `CDF-2` which adds support for 64-bit integer and unsigned integer data types. It supports the netCDF Classic Data Model plus the additional integer types.
* `netCDF-4` - supports the netCDF Enhanced Data Model. It is written using the HDF5 file format. It is restricted to a subset of HDF5 while also including additional metadata to represent aspects of the netCDF Enhanced Data Model that do not have a corresponding concept in the HDF5 data model.

#### NetCDF-3 File Formats

#### NetCDF-4 File Formats

#### NCZarr Encoding

#### OPeNDAP Data Access Protocol (DAP-2 and DAP-4)

### Best Practices and Community Conventions

### NetCDF History