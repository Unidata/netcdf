---
title: NetCDF User's Guide - Introduction
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

<!-- NOTE:
Text from NUG/netcdf_data_set_components.md#data_model, paragraph 1
-->
A netCDF dataset contains named dimensions, variables, and attributes.
These components can be used together to capture the meaning of data
and relations among data fields in an array-oriented dataset.


The Unidata Program Center supports and maintains freely distributed
netCDF programming interfaces for C, C++, Java, and Fortran.
Programming interfaces are also available
for Python, IDL, MATLAB, R, Ruby, and Perl.

Reference documentation for the Unidata maintained implementations
are available at the main Unidata netCDF web page (https://www.unidata.ucar.edu/software/netcdf/).

## The Purpose of the NetCDF User's Guide

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

## NetCDF Implementations

[//]: # (TODO: Move details about implmentations from top section this page here or vice versa.)

Unidata develops and maintains both the netCDF-C and netCDF-Java libraries as well as both Fortran and C++ wrapper libraries. For details on the Unidata supported libraries, see the library specific documentation pages:
* [netCDF-C User's Guide](https://docs.unidata.ucar.edu/netcdf-c/current/)
* [netCDF-Fortran User's Guide](https://docs.unidata.ucar.edu/netcdf-fortran/current/)
* [netCDF-C++ User's Guide](https://docs.unidata.ucar.edu/netcdf-cxx/current/)
* [netCDF-Java User's Guide](https://docs.unidata.ucar.edu/netcdf-java/current/userguide/)

netcdf4Python
H5netcdf??

## NetCDF Data Models
NetCDF has two main data models.
* The **netCDF Classic Data Model**, which represents a dataset with named variables, dimensions, and attributes.
* The **netCDF Enhanced Data Model**, which adds hierarchical structure, string and unsigned integer types, and user-defined types.

Some file formats and encodings support subsets of the Enhanced Data Model
or minor extensions to the Classic Data Model.
For example, CDF-5 extends the Classic Data Model with the addition of
support for unsigned integer and 64-bit integer data types.

Full details and discussion of the netCDF data models can be found on the ["Data Models" page](data_models.html).

## Text Representations of NetCDF Datasets

### CDL

NetCDF CDL (Common Data form Language) is a text notation for representing the structure and data of a binary netCDF dataset.
CDL can be read (and edited) by a human. It can also be read and produced by machines. For instance, a CDL description can be generated, given a netCDF file, by the `ncdump` utility and a netCDF file can be generated, given a CDL desription, by the `ncgen` utility.

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

### NcML

The NetCDF Markup Language (NcML) is an XML dialect for representing the structure and data of a binary netCDF dataset (similar to CDL). NcML can also be used to modify and aggregate existing datasets into new virtual dataset. This capability is currently supported by the netCDF-Java library and used extensively by the THREDDS Data Server (TDS).

[//]: # (TODO: Decide if NcML should be included in the NUG. And what should be pulled from netCDF-Java.)
A full description of NcML can be found on the ["NcML" page](ncml.html).

## NetCDF File Formats, Encodings, and Mappings

NetCDF has two main file formats:
the `netCDF-3` file format which supports the netCDF Classic Data Model
and the `netCDF-4` file format which supports the netCDF Enhanced Data model.
Along with the `netCDF-3` (aka `CDF-1`) file format, there are also two variants, `CDF-2` and `CDF-5`.

The four file formats are:
* `CDF-1` (aka `netCDF-3` or `classic`) - supports the netCDF Classic Data Model.
* `CDF-2` (aka `64-bit Offset`) - a variant of `CDF-1` that removes some size limitations on variables and datasets, it supports the netCDF Classic Data Model.
* `CDF-5` (aka `64-bit Data` or pnetcdf) - a variant of `CDF-2` which adds support for 64-bit integer and unsigned integer data types. It supports the netCDF Classic Data Model plus the additional integer types.
* `netCDF-4` - supports the netCDF Enhanced Data Model. It is written using the HDF5 file format. It is restricted to a subset of HDF5 while also including additional metadata to represent aspects of the netCDF Enhanced Data Model that do not have a corresponding concept in the HDF5 data model.

See the ["NetCDF-3 File Formats" page](nc3_file_formats.html) for a detailed description and discussion of the `CDF-1`, `CDF-2` and `CDF-5` file formats.

See the ["NetCDF-4 File Format" page](nc4_file_format.html) for a detailed description and discussion of the `netCDF-4` file format.

Both the netCDF-C and netCDF-Java libraries support the Zarr format (and the NCZarr extension). For more details on Zarr/NCZarr support, see the ["NCZarr" page](nczarr.html).

The OPeNDAP Data Access Protocols (DAP-2 and DAP-4) are supported for read-only by both the netCDF-C and netCDF-Java libraries. For details on DAP support, see the ["OPeNDAP" page](dap.html).

### Other File Formats Supported Read-Only by NetCDF-C and -Java Libraries

The netCDF-C library supports HDF-4 SD format files and ... For a full list see the [netCDF-C File Format Support page]().

The netCDF-Java library supports HDF-4, GRIB, BUFR ... For a full list see the [CDM File Types page](https://docs.unidata.ucar.edu/netcdf-java/current/userguide/file_types.html).

## Advice, Guidance, Best Practices (???)
### Best Practices

... Some intro text to Best Practices ...

Link to ["Best Practices" page](best_practices.html).

### Community Conventions

The use of netCDF is not sufficient to make data "self-describing" and meaningful to both humans and machines. ...

[//]: # (TODO: See text in NUG/netcdf_introduction.html#creating_self)

### Performance )???)

[//]: # (TODO: See text in NUG/netcdf_introduction.html#performance)

### Limitations (??? maybe should go in file formats?)

[//]: # (TODO: See text in NUG/netcdf_introduction.html#limitations)

## NetCDF History

