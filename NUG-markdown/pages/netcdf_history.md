---
title: NetCDF History - Background and Evolutionn
last_updated: 2021-04-06
sidebar: nnug_sidebar
toc: false
permalink: netcdf_history.html
---
<!-- NOTE:
[//]: # (This text from 'NUG/netcdf_data_set_components.md', "Background ..." section)
-->

The development of the netCDF interface began with a modest goal related to Unidata's needs: to provide a common interface between Unidata applications and real-time meteorological data.
Since Unidata software was intended to run on multiple hardware platforms with access from both C and FORTRAN, achieving Unidata's goals had the potential for providing a package that was useful in a broader context.
By making the package widely available and collaborating with other organizations with similar needs, we hoped to improve the then current situation in which software for scientific data access was only rarely reused by others in the same discipline and almost never reused between disciplines (Fulker, 1988).

Important concepts employed in the netCDF software originated in a paper (Treinish and Gough, 1987) that described data-access software developed at the NASA Goddard National Space Science Data Center (NSSDC).
The interface provided by this software was called the Common Data Format (CDF).
The NASA CDF was originally developed as a platform-specific FORTRAN library to support an abstraction for\ storing arrays.

The NASA CDF package had been used for many different kinds of data in an extensive collection of applications.
It had the virtues of simplicity (only 13 subroutines), independence from storage format, generality, ability to support logical user views of data, and support for generic applications.

Unidata held a workshop on CDF in Boulder in August 1987.
We proposed exploring the possibility of collaborating with NASA to extend the CDF FORTRAN interface, to define a C interface, and to permit the access of data aggregates with a single call, while maintaining compatibility with the existing NASA interface.

Independently, Dave Raymond at the New Mexico Institute of Mining and Technology had developed a package of C software for UNIX that supported sequential access to self-describing array-oriented data and a "pipes and filters" (or "data flow") approach to processing, analyzing, and displaying the data.
This package also used the "Common Data Format" name, later changed to C-Based Analysis and Display System (CANDIS).
Unidata learned of Raymond's work (Raymond, 1988), and incorporated some of his ideas, such as the use of named dimensions and variables with differing shapes in a single data object, into the Unidata netCDF interface.

In early 1988, Glenn Davis of Unidata developed a prototype netCDF package in C that was layered on XDR.
This prototype proved that a single-file, XDR-based implementation of the CDF interface could be achieved at acceptable cost and that the resulting programs could be implemented on both UNIX and VMS systems.
However, it also demonstrated that Yeah providing a small, portable, and NASA CDF-compatible FORTRAN interface with the desired generality was not practical.
NASA's CDF and Unidata's netCDF have since evolved separately, but recent CDF versions share many characteristics with netCDF.

In early 1988, Joe Fahle of SeaSpace, Inc. (a commercial software development firm in San Diego, California), a participant in the 1987 Unidata CDF workshop, independently developed a CDF package in C that extended the NASA CDF interface in several important ways (Fahle, 1989).
Like Raymond's package, the SeaSpace CDF software permitted variables with unrelated shapes to be included in the same data object and permitted a general form of access to multidimensional arrays.
Fahle's implementation was used at SeaSpace as the intermediate form of storage for a variety of steps in their image-processing system.
This interface and format have subsequently evolved into the Terascan data format.

After studying Fahle's interface, we concluded that it solved many of the problems we had identified in trying to stretch the NASA interface to our purposes.
In August 1988, we convened a small workshop to agree on a Unidata netCDF interface, and to resolve remaining open issues.
Attending were Joe Fahle of SeaSpace, Michael Gough of Apple (an author of the NASA CDF software), Angel Li of the University of Miami (who had implemented our prototype netCDF software on VMS and was a potential user), and Unidata systems development staff.
Consensus was reached at the workshop after some further simplifications were discovered.
A document incorporating the results of the workshop into a proposed Unidata netCDF interface specification was distributed widely for comments before Glenn Davis and Russ Rew implemented the first version of the software.
Comparison with other data-access interfaces and experience using netCDF are discussed in Rew and Davis (1990a), Rew and Davis (1990b), Jenter and Signell (1992), and Brown, Folk, Goucher, and Rew (1993).

In October 1991, we announced version 2.0 of the netCDF software distribution.
Slight modifications to the C interface (declaring dimension lengths to be long rather than int) improved the usability of netCDF on inexpensive platforms such as MS-DOS computers, without requiring recompilation on other platforms.
This change to the interface required no changes to the associated file format.

Release of netCDF version 2.3 in June 1993 preserved the same file format but added single call access to records, optimizations for accessing cross-sections involving non-contiguous data, subsampling along specified dimensions (using 'strides'), accessing non-contiguous data (using 'mapped array sections'), improvements to the ncdump and ncgen utilities, and an experimental C++ interface.

In version 2.4, released in February 1996, support was added for new platforms and for the C++ interface, significant optimizations were implemented for supercomputer architectures, and the file format was formally specified in an appendix to the User's Guide.

FAN (File Array Notation), software providing a high-level interface to netCDF data, was made available in May 1996.
The capabilities of the FAN utilities include extracting and manipulating array data from netCDF datasets, printing selected data from netCDF arrays, copying ASCII data into netCDF arrays, and performing various operations (sum, mean, max, min, product, and others) on netCDF arrays.

In 1996 and 1997, Joe Sirott implemented and made available the first implementation of a read-only netCDF interface for Java, Bill Noon made a Python module available for netCDF, and Konrad Hinsen contributed another netCDF interface for Python.

In May 1997, Version 3.3 of netCDF was released.
This included a new type-safe interface for C and Fortran, as well as many other improvements.
A month later, Charlie Zender released version 1.0 of the NCO (netCDF Operators) package, providing command-line utilities for general purpose operations on netCDF data.

Version 3.4 of Unidata's netCDF software, released in March 1998, included initial large file support, performance enhancements, and improved Cray platform support.
Later in 1998, Dan Schmitt provided a Tcl/Tk interface, and Glenn Davis provided version 1.0 of netCDF for Java.

In May 1999, Glenn Davis, who was instrumental in creating and developing netCDF, died in a small plane crash during a thunderstorm.
The memory of Glenn's passions and intellect continue to inspire those of us who worked with him.

In February 2000, an experimental Fortran 90 interface developed by Robert Pincus was released.

John Caron released netCDF for Java, version 2.0 in February 2001.
This version incorporated a new high-performance package for multidimensional arrays, simplified the interface, and included OpenDAP (known previously as DODS) remote access, as well as remote netCDF access via HTTP contributed by Don Denbo.

In March 2001, netCDF 3.5.0 was released.
This release fully integrated the new Fortran 90 interface, enhanced portability, improved the C++ interface, and added a few new tuning functions.

Also in 2001, Takeshi Horinouchi and colleagues made a netCDF interface for Ruby available, as did David Pierce for the R language for statistical computing and graphics.
Charles Denham released WetCDF, an independent implementation of the netCDF interface for Matlab, as well as updates to the popular netCDF Toolbox for Matlab.

In 2002, Unidata and collaborators developed NcML, an XML representation for netCDF data useful for cataloging data holdings, aggregation of data from multiple datasets, augmenting metadata in existing datasets, and support for alternative views of data.
The Java interface currently provides access to netCDF data through NcML.

Additional developments in 2002 included translation of C and Fortran User Guides into Japanese by Masato Shiotani and colleagues, creation of a “Best Practices” guide for writing netCDF files, and provision of an Ada-95 interface by Alexandru Corlan.

In July 2003 a group of researchers at Northwestern University and Argonne National Laboratory (Jianwei Li, Wei-keng Liao, Alok Choudhary, Robert Ross, Rajeev Thakur, William Gropp, and Rob Latham) contributed a new parallel interface for writing and reading netCDF data, tailored for use on high performance platforms with parallel I/O.
The implementation built on the MPI-IO interface, providing portability to many platforms.

In October 2003, Greg Sjaardema contributed support for an alternative format with 64-bit offsets, to provide more complete support for very large files.
These changes, with slight modifications at Unidata, were incorporated into version 3.6.0, released in December, 2004.

<!-- TODO: Include paragraph on development of CDM as combination of netCDF-3, HDF5, and OPeNDAP data models.
OPeNDAP drove much of this by bring these different data models together and trying to do it transparently.
It exposed the need for various mappings. 
-->

In 2004, thanks to a NASA grant, Unidata and NCSA began a collaboration to increase the interoperability of netCDF and HDF5, and bring some advanced HDF5 features to netCDF users.

In February, 2006, release 3.6.1 fixed some minor bugs.

In March, 2007, release 3.6.2 introduced an improved build system that used automake and libtool, and an upgrade to the most recent autoconf release, to support shared libraries and the netcdf-4 builds.
This release also introduced the NetCDF Tutorial and example programs.

The first beta release of netCDF-4.0 was celebrated with a giant party at Unidata in April, 2007.
Over 2000 people danced 'til dawn at the NCAR Mesa Lab, listening to the Flaming Lips and the Denver Gilbert & Sullivan repertory company.
Brittany Spears performed the world-premire of her smash hit "Format me baby, one more time."

In June, 2008, netCDF-4.0 was released.
Version 3.6.3, the same code but with netcdf-4 features turned off, was released at the same time.
The 4.0 release uses HDF5 1.8.1 as the data storage layer for netCDF, and introduces many new features including groups and user-defined types.
The 3.6.3/4.0 releases also introduced handling of UTF8-encoded Unicode names.

NetCDF-4.1.1 was released in April, 2010, provided built-in client support for the DAP protocol for accessing data from remote OPeNDAP servers, full support for the enhanced netCDF-4 data model in the ncgen utility, a new nccopy utility for copying and conversion among netCDF format variants, ability to read some HDF4/HDF5 data archives through the netCDF C or Fortran interfaces, support for parallel I/O on netCDF classic files (CDF-1, 2, and 5) using the PnetCDF library from Argonne/Northwestern, a new nc-config utility to help compile and link programs that use netCDF, inclusion of the UDUNITS library for handling “units” attributes, and inclusion of libcf to assist in creating data compliant with the Climate and Forecast (CF) metadata conventions.

In September, 2010, the NetCDF-Java/CDM (Common Data Model) version 4.2 library was declared stable and made available to users.
This 100%-Java implementation provided a read-write interface to netCDF-3 classic format files, as well as a read-only interface to netCDF-4 enhanced model data and many other formats of scientific data through a common (CDM) interface.
More recent releases support writing netCDF-4 data.
The NetCDF-Java library also implements NcML, which allows you to add metadata to CDM datasets.
A ToolsUI application is also included that provides a graphical user interface to capabilities similar to the C-based ncdump and ncgen utilities, as well as CF-compliance checking and many other features.
