# An Introduction to NetCDF {#netcdf_introduction}

[TOC]
\tableofcontents

# The netCDF Interface {#netcdf_interface}

[//]: # (TODO: Consider moving some of the text in this section to NUG-new/index.md.)
[//]: # (      Text needs updating - include Java, Python, etc.)

The Network Common Data Form, or netCDF, is an interface to a library of data access functions for storing and retrieving data in the form of arrays.
An array is an n-dimensional (where n is 0, 1, 2, ...) rectangular structure containing items which all have the same data type (e.g., 8-bit character, 32-bit integer).
A scalar (simple single value) is a 0-dimensional array.

NetCDF is an abstraction that supports a view of data as a collection of self-describing, portable objects that can be accessed through a simple interface.
Array values may be accessed directly, without knowing details of how the data are stored.
Auxiliary information about the data, such as what units are used, may be stored with the data.
Generic utilities and application programs can access netCDF datasets and transform, combine, analyze, or display specified fields of the data.
The development of such applications has led to improved accessibility of data and improved re-usability of software for array-oriented data management, analysis, and display.

The netCDF software implements an abstract data type, which means that all operations to access and manipulate data in a netCDF dataset must use only the set of functions provided by the interface.
The representation of the data is hidden from applications that use the interface, so that how the data are stored could be changed without affecting existing programs.
The physical representation of netCDF data is designed to be independent of the computer on which the data were written.

Unidata supports the netCDF interfaces for C (see [NetCDF-C User's Guide](https://www.unidata.ucar.edu/software/netcdf/docs/index.html)), Fortran (see [NetCDF-Fortran User's Guide](https://www.unidata.ucar.edu/netcdf/fortran/docs/)) and C++ (see [NetCDF C++ Interface Guide](https://www.unidata.ucar.edu/netcdf/documentation/historic/cxx4/index.html)).

The netCDF library is supported for various UNIX operating systems.
A MS Windows port is also available.
The software is also ported and tested on a few other operating systems, with assistance from users with access to these systems, before each major release.
Unidata's netCDF software is freely available [via HTTP and FTP](https://www.unidata.ucar.edu/downloads/netcdf/) to encourage its widespread use.

For detailed installation instructions, see \ref getting_and_building_netcdf.

# The netCDF File Format {#netcdf_format}
[//]: # (TODO: Consider moving the text in this section to NUG-new/netcdf_history.md.)

Until version 3.6.0, all versions of netCDF employed only one binary data format, now referred to as netCDF classic format.
NetCDF classic is the default format for all versions of netCDF.

In version 3.6.0 a new binary format was introduced, 64-bit offset format.
Nearly identical to netCDF classic format, it uses 64-bit offsets (hence the name), and allows users to create far larger datasets.
This format is also referred as CDF-2, because it bears the signature string "CDF2" in the file header.
After this extension, the classic file format (i.e. not supporting 64-bit offsets) is now referred as CDF-1.

In version 4.0.0 a third binary format was introduced: the HDF5 format.
Starting with this version, the netCDF library can use HDF5 files as its base format.
(Only HDF5 files created with netCDF-4 can be understood by netCDF-4).

Starting from version 4.4.0, netCDF included the support of CDF-5 format.
In order to allow defining large array variables with more than 4-billion elements, CDF-5 replaces most of the 32-bit integers used to describe metadata in file header with 64-bit integers.
In addition, it supports the following new external data types: NC_UBYTE, NC_USHORT, NC_UINT, NC_INT64, and NC_UINT64.
The CDF-5 format specifications can be found at http://cucis.ece.northwestern.edu/projects/PnetCDF/CDF-5.html.

The classic file formats are now referring to the collection of CDF-1, 2 and 5 formats.
By default, netCDF uses the classic format (CDF-1).
To use the CDF-2, CDF-5, or netCDF-4/HDF5 format, set the appropriate constant in the file mode argument when creating the file.

To achieve network-transparency (machine-independence), netCDF classic formats are implemented in terms of an external representation much like XDR (eXternal Data Representation, see https://www.ietf.org/rfc/rfc1832.txt), a standard for describing and encoding data.
This representation provides encoding of data into machine-independent sequences of bits.
It has been implemented on a wide variety of computers, by assuming only that eight-bit bytes can be encoded and decoded in a consistent way.
The IEEE 754 floating-point standard is used for floating-point data representation.

Descriptions of the overall structure of netCDF classic files are provided later in this manual.
See \ref file_structure_and_performance.

The details of the CDF-1 and CDF-2 formats are described in an appendix.
See \ref netcdf_format.
However, users are discouraged from using the format specification to develop independent low-level software for reading and writing netCDF files, because this could lead to compatibility problems if the format is ever modified.

## How to Select the Format {#select_format}
[//]: # (TODO: Consider moving the text in this section to NUG-new/file_formats.md)
[//]: # (      May need some rewriting.)

With four different base formats, care must be taken in creating data files to choose the correct base format.

The format of a netCDF file is determined at create time.

When opening an existing netCDF file the netCDF library will transparently detect its format and adjust accordingly.
However, netCDF library versions earlier than 3.6.0 cannot read CDF-2 format files, library versions before 4.0 can't read netCDF-4/HDF5
files, and versions before 4.4.0 cannot read CDF-5 files.
NetCDF classic format files (even if created by version 3.6.0 or later) remain compatible with older versions of the netCDF library.

Users are encouraged to use netCDF classic CDF-1 format to distribute data, for maximum portability.

To select CDF-2, CDF-5 or netCDF-4 format files, C programmers should use flag NC_64BIT_OFFSET, NC_64BIT_DATA, or NC_NETCDF4 respectively in function nc_create().

In Fortran, use flag nf_64bit_offset, nf_64bit_data, or nf_format_netcdf4 in function NF_CREATE.
See NF_CREATE.

It is also possible to change the default creation format, to convert a large body of code without changing every create call.
C programmers see nc_set_default_format().
Fortran programs see NF_SET_DEFAULT_FORMAT.

## NetCDF Classic Format (CDF-1) {#classic_format}
[//]: # (TODO: Consider moving the next few sections to NUG-new/file_formats.md )
[//]: # (      Some rewriting probably needed.)

The original netCDF format is identified using four bytes in the file header.
All files in this format have "CDF\001" at the beginning of the file.
In this documentation this format is referred to as CDF-1 format.

NetCDF CDF-1 format is identical to the format used by every previous version of netCDF.
It has maximum portability, and is still the default netCDF format.

## NetCDF 64-bit Offset Format (CDF-2) {#netcdf_64bit_offset_format}

For some users, the various 2 GiB format limitations of the classic format become a problem (see \ref limitations).
For these users, 64-bit offset format is a natural choice.
It greatly eases the size restrictions of netCDF classic files (see \ref limitations).

Files with the 64-bit offsets are identified with a "CDF\002" at the beginning of the file.
In this documentation this format is called CDF-2 format.

Since CDF-2 format was introduced in version 3.6.0, earlier versions of the netCDF library can't read CDF-2 files.

##  NetCDF 64-bit Data Format (CDF-5) {#netcdf_64bit_data_format}

To allow large variables with more than 4-billion array elements, 64-bit data format is developed to support such I/O requests.

Files with the 64-bit data are identified with a "CDF\005" at the beginning of the file.
In this documentation this format is called CDF-5 format.

Since CDF-5 format was introduced in version 4.4.0, earlier versions of the netCDF library can't read CDF-5 files.

## NetCDF-4 Format {#netcdf_4_format}

In version 4.0, netCDF included another new underlying format: HDF5.

NetCDF-4 format files offer new features such as groups, compound types, variable length arrays, new unsigned integer types, parallel I/O access, etc.
None of these new features can be used with classic or 64-bit offset files.

NetCDF-4 files can't be created at all, unless the netCDF configure script is run with `--enable-netcdf-4`.
This also requires version 1.8.0 of HDF5.

For the netCDF-4.0 release, netCDF-4 features are only available from the C and Fortran interfaces.
We plan to bring netCDF-4 features to the CXX API in a future release of netCDF.

NetCDF-4 files can't be read by any version of the netCDF library previous to 4.0 (but they can be read by HDF5, version 1.8.0 or better).

For more discussion of format issues see [The NetCDF Tutorial](https://www.unidata.ucar.edu/software/netcdf/docs/tutorial_8dox.html).

# NetCDF Library Architecture {#architecture}
[//]: # (TODO: Move this section to C-library docs.)

![NetCDF Architecture](images/netcdf_architecture.png "NetCDF Architecture")

The netCDF C-based libraries depend on a core C library and some externally developed libraries.

* NetCDF-Java is an independent implementation, not shown here
* C-based 3rd-party netCDF APIs for other languages include Python, Ruby, Perl, Fortran-2003, MATLAB, IDL, and R
* Libraries that don't support netCDF-4 include Perl and old C++
* 3rd party libraries are optional (HDF5, HDF4, zlib, szlib, PnetCDF, libcurl), depending on what features are needed and how netCDF is configured
* "Apps" in the above means applications, not mobile apps!

# What about Performance? {#performance}
[//]: # (TODO: Decide if keeping this section in NUG-new Where? In intro?)

One of the goals of netCDF is to support efficient access to small subsets of large datasets.
To support this goal, netCDF uses direct access rather than sequential access.
This can be much more efficient when the order in which data is read is different from the order in which it was written, or when it must be read in different orders for different applications.

The amount of overhead for a portable external representation depends on many factors, including the data type, the type of computer, the granularity of data access, and how well the implementation has been tuned to the computer on which it is run.
This overhead is typically small in comparison to the overall resources used by an application.
In any case, the overhead of the external representation layer is usually a reasonable price to pay for portable data access.

Although efficiency of data access has been an important concern in designing and implementing netCDF, it is still possible to use the netCDF interface to access data in inefficient ways: for example, by requesting a slice of data that requires a single value from each record.
Advice on how to use the interface efficiently is provided in \ref file_structure_and_performance.

The use of HDF5 as a data format adds significant overhead in metadata operations, less so in data access operations.
We continue to study the challenge of implementing netCDF-4/HDF5 format without compromising performance.

# Creating Self-Describing Data conforming to Conventions {#creating_self}

<!-- Moved to NUG-new/community_conventions_and_practices.md -->

~~The mere use of netCDF is not sufficient to make data "self-describing" and meaningful to both humans and machines.
The names of variables and dimensions should be meaningful and conform to any relevant conventions.
Dimensions should have corresponding coordinate variables (See \ref coordinate_variables) where sensible.~~

~~Attributes play a vital role in providing ancillary information.
It is important to use all the relevant standard attributes using the relevant conventions.
For a description of reserved attributes (used by the netCDF library) and attribute conventions for generic application software, see \ref attribute_conventions.~~

~~A number of groups have defined their own additional conventions and styles for netCDF data.
Descriptions of these conventions, as well as examples incorporating them can be accessed from the netCDF Conventions site, https://www.unidata.ucar.edu/software/netcdf/conventions.html.~~

~~These conventions should be used where suitable.
Additional conventions are often needed for local use.
These should be contributed to the above netCDF conventions site if likely to interest other users in similar areas.~~

# Limitations of NetCDF {#limitations}
[//]: # (TODO: Decide if/where to move this section. Overview or Best Practices?)

The netCDF classic data model is widely applicable to data that can be organized into a collection of named array variables with named attributes, but there are some important limitations to the model and its implementation in software.
Some of these limitations have been removed or relaxed in netCDF-4 files, but still apply to netCDF classic and netCDF 64-bit offset files.

NetCDF classic CDF-1 and CDF-2 formats offer a limited number of external numeric data types: 8-, 16-, 32-bit integers, or 32- or 64-bit floating-point numbers.
The CDF-5 and netCDF-4 formats add 64-bit integer types and unsigned integer types.

With the netCDF-4/HDF5 format, new unsigned integers (of various sizes), 64-bit integers, and the string type allow improved expression of meaning in scientific data.
The new VLEN (variable length) and COMPOUND types allow users to organize data in new ways.

With the classic CDF-1 file format, there are constraints that limit how a dataset is structured to store more than 2 GiBytes (a GiByte is 2^30 or 1,073,741,824 bytes, as compared to a Gbyte, which is 1,000,000,000 bytes) of data in a single netCDF dataset (see \ref limitations).
This limitation is a result of 32-bit offsets used for storing relative offsets within a classic netCDF format file.
Since one of the goals of netCDF is portable data, and some file systems still can't deal with files larger than 2 GiB, it is best to keep files that must be portable below this limit.
Nevertheless, it is possible to create and access netCDF files larger than 2 GiB on platforms that provide support for such files (see \ref large_file_support).

The CDF-2 format allows large files, and makes it easy to create fixed variables of about 4 GiB, and record variables of about 4 GiB per record (see \ref netcdf_64bit_offset_format).
However, old netCDF applications will not be able to read the 64-bit offset files until they are upgraded to at least version 3.6.0 of netCDF (i.e. the version in which 64-bit offset format was introduced).

With the netCDF-4/HDF5 format, size limitations are further relaxed, and files can be as large as the underlying file system supports.
NetCDF-4/HDF5 files are unreadable to the netCDF library before version 4.0.

Similarly, CDF-5 format uses 64-bit integers to allow users to define large variables.
CDF-5 files are not unreadable to the netCDF library before version 4.4.0.

Another limitation of the classic formats (CDF-1, 2 and 5) is that only one unlimited (changeable) dimension is permitted for each netCDF data set.
Multiple variables can share an unlimited dimension, but then they must all grow together.
Hence the classic netCDF model does not permit variables with several unlimited dimensions or the use of multiple unlimited dimensions in different variables within the same dataset.
Variables that have non-rectangular shapes (for example, ragged arrays) cannot be represented conveniently.

In netCDF-4/HDF5 files, multiple unlimited dimensions are fully supported.
Any variable can be defined with any combination of limited and unlimited dimensions.

The extent to which data can be completely self-describing is limited: there is always some assumed context without which sharing and archiving data would be impractical.
NetCDF permits storing meaningful names for variables, dimensions, and attributes; units of measure in a form that can be used in computations; text strings for attribute values that apply to an entire data set; and simple kinds of coordinate system information.
But for more complex kinds of metadata (for example, the information necessary to provide accurate georeferencing of data on unusual grids or from satellite images), it is often necessary to develop conventions.

Specific additions to the netCDF data model might make some of these conventions unnecessary or allow some forms of metadata to be represented in a uniform and compact way.
For example, adding explicit georeferencing to the netCDF data model would simplify elaborate georeferencing conventions at the cost of complicating the model.
The problem is finding an appropriate trade-off between the richness of the model and its generality (i.e., its ability to encompass many kinds of data).
A data model tailored to capture the shared context among researchers within one discipline may not be appropriate for sharing or combining data from multiple disciplines.

The classic netCDF data model (which is used for classic CDF-1, 2 and 5 format data) does not support nested data structures such as trees, nested arrays, or other recursive structures.
Through use of indirection and conventions it is possible to represent some kinds of nested structures, but the result may fall short of the netCDF goal of self-describing data.

In netCDF-4/HDF5 format files, the introduction of the compound type allows the creation of complex data types, involving any combination of types.
The VLEN type allows efficient storage of ragged arrays, and the introduction of hierarchical groups allows users new ways to organize data.

NetCDF-4 supports parallel read/write access to netCDF-4/HDF5 files, using the underlying HDF5 library and parallel read/write access to classic files using the PnetCDf library.

For more information about HDF5, see the HDF5 web site: https://www.hdfgroup.org/solutions/hdf5/.

For more information about PnetCDF, see their web site: https://parallel-netcdf.github.io/.
