# The NetCDF User's Guide

# The NetCDF User's Guide {#user_guide}

[TOC]

The project page for the NetCDF User's Guide can be found at [the NetCDF User's Guide Github Page](https://github.com/Unidata/netcdf).

## Table of Contents

- \subpage netcdf_introduction
- \subpage file_structure_and_performance
- \subpage data_type
- \subpage netcdf_data_set_components
- \subpage netcdf_perf_chunking
- \subpage netcdf_utilities_guide
- \subpage best_practices
- \subpage user_defined_formats
- \subpage dap2
- \subpage dap4
- \subpage CDL
- \subpage attribute_conventions
- \subpage file_format_specifications
- \subpage nug_style_guide


## The Purpose of NetCDF {#netcdf_purpose}

The purpose of the Network Common Data Form (netCDF) interface is to allow you to create, access, and share array-oriented data in a form that is self-describing and portable. "Self-describing" means that a dataset includes information defining the data it contains. "Portable" means that the data in a dataset is represented in a form that can be accessed by computers with different ways of storing integers, characters, and floating-point numbers. Using the netCDF interface for creating new datasets makes the data portable. Using the netCDF interface in software for data access, management, analysis, and display can make the software more generally useful.

The netCDF software includes C, Fortran 77, Fortran 90, and C++ interfaces for accessing netCDF data. These libraries are available for many common computing platforms.

The community of netCDF users has contributed ports of the software to additional platforms and interfaces for other programming languages as
well. Source code for netCDF software libraries is freely available to encourage the sharing of both array-oriented data and the software that makes the data useful.

This User's Guide presents the netCDF data model. It explains how the netCDF data model uses dimensions, variables, and attributes to store data.

Reference documentation for UNIX systems, in the form of UNIX 'man'
pages for the C and FORTRAN interfaces is also available at the netCDF
web site (http://www.unidata.ucar.edu/netcdf), and with the netCDF
distribution.

The latest version of this document, and the language specific guides,
can be found at the netCDF web site
(http://www.unidata.ucar.edu/netcdf/docs) along with extensive
additional information about netCDF, including pointers to other
software that works with netCDF data.

Separate documentation of the Java netCDF library can be found at
http://www.unidata.ucar.edu/software/netcdf-java/.
