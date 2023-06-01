---
title: NetCDF-4 File Format
last_updated: 2021-04-23
sidebar: nnug_sidebar
toc: false
permalink: nc4_file_format.html
---

The netCDF-4 format implements and expands the netCDF-3 data model by using an enhanced version of HDF5 as the storage layer. Use is made of features that are only available in HDF5 version 1.8 and later.

Using HDF5 as the underlying storage layer, netCDF-4 files remove many of the restrictions for classic and 64-bit offset files.
The richer enhanced model supports user-defined types and data structures, hierarchical scoping of names using groups, additional primitive types including strings, larger variable sizes, and multiple unlimited dimensions.
The underlying HDF5 storage layer also supports per-variable compression, multidimensional tiling, and efficient dynamic schema changes, so that data need not be copied when adding new variables to the file schema.

Creating a netCDF-4/HDF5 file with netCDF-4 results in an HDF5 file.
The features of netCDF-4 are a subset of the features of HDF5, so the resulting file can be used by any existing HDF5 application.

Although every file in netCDF-4 format is an HDF5 file, there are HDF5 files that are not netCDF-4 format files, because the netCDF-4 format intentionally uses a limited subset of the HDF5 data model and file format features.
Some HDF5 features not supported in the netCDF enhanced model and netCDF-4 format include non-hierarchical group structures, HDF5 reference types, multiple links to a data object, user-defined atomic data types, stored property lists, more permissive rules for data object names, the HDF5 date/time type, and attributes associated with user-defined types.

A complete specification of HDF5 files is beyond the scope of this document.
For more information about HDF5, see the HDF5 web site: [https://hdfgroup.org](https://hdfgroup.org).

The specification that follows is sufficient to allow HDF5 users to create files that will be accessible from netCDF-4.

## Creation Order {#creation_order}

The netCDF API maintains the creation order of objects that are created in the file.
The same is not true in HDF5, which maintains the objects in alphabetical order.
Starting in version 1.8 of HDF5, the ability to maintain creation order was added.
This must be explicitly turned on in the HDF5 data file in several ways.

Each group must have link and attribute creation order set. The following code (from libhdf5/nc4hdf.c)  shows how the netCDF-4 library sets these when creating a group.

````
           /* Create group, with link_creation_order set in the group
            * creation property list. */
           if ((gcpl_id = H5Pcreate(H5P_GROUP_CREATE)) < 0)
              return NC_EHDFERR;
           if (H5Pset_link_creation_order(gcpl_id, H5P_CRT_ORDER_TRACKED|H5P_CRT_ORDER_INDEXED) < 0)
              BAIL(NC_EHDFERR);
           if (H5Pset_attr_creation_order(gcpl_id, H5P_CRT_ORDER_TRACKED|H5P_CRT_ORDER_INDEXED) < 0)
              BAIL(NC_EHDFERR);
           if ((grp->hdf_grpid = H5Gcreate2(grp->parent->hdf_grpid, grp->name,
                                            H5P_DEFAULT, gcpl_id, H5P_DEFAULT)) < 0)
              BAIL(NC_EHDFERR);
           if (H5Pclose(gcpl_id) < 0)
              BAIL(NC_EHDFERR);
````

Each dataset in the HDF5 file must be created with a property list for which the attribute creation order has been set to creation ordering.
The H5Pset_attr_creation_order function is used to set the creation ordering of attributes of a variable.

The following example code (from libsrc4/nc4hdf.c) shows how the creation ordering is turned on by the netCDF library.

````
        /* Turn on creation order tracking. */
        if (H5Pset_attr_creation_order(plistid, H5P_CRT_ORDER_TRACKED|
                                       H5P_CRT_ORDER_INDEXED) < 0)
           BAIL(NC_EHDFERR);
````

## Groups {#groups_spec}

NetCDF-4 groups are the same as HDF5 groups, but groups in a netCDF-4 file must be strictly hierarchical.
In general, HDF5 permits non-hierarchical structuring of groups (for example, a group that is its own grandparent).
These non-hierarchical relationships are not allowed in netCDF-4 files.

In the netCDF API, the global attribute becomes a group-level attribute.
That is, each group may have its own global attributes.

The root group of a file is named “/” in the netCDF API, where names of groups are used.
It should be noted that the netCDF API (like the HDF5 API) makes little use of names, and refers to entities by number.

## Dimensions with HDF5 Dimension Scales {#dims_spec}

Until version 1.8, HDF5 did not have any capability to represent shared dimensions.
With the 1.8 release, HDF5 introduced the dimension scale feature to allow shared dimensions in HDF5 files.

The dimension scale is unfortunately not exactly equivalent to the netCDF shared dimension, and this leads to a number of compromises in the design of netCDF-4.

A netCDF shared dimension consists solely of a length and a name.
An HDF5 dimension scale also includes values for each point along the dimension, information that is (optionally) included in a netCDF coordinate variable.

To handle the case of a netCDF dimension without a coordinate variable, netCDF-4 creates dimension scales of type char, and leaves the contents of the dimension scale empty.
Only the name and length of the scale are significant.
To distinguish this case, netCDF-4 takes advantage of the NAME attribute of the dimension scale  
(Not to be confused with the name of the scale itself.)
In the case of dimensions without coordinate data, the HDF5 dimension scale NAME attribute is set to the string: "This is a netCDF dimension but not a netCDF variable."

In the case where a coordinate variable is defined for a dimension, the HDF5 dimscale matches the type of the netCDF coordinate variable, and contains the coordinate data.

A further difficulty arrises when an n-dimensional coordinate variable is defined, where n is greater than one.
NetCDF allows such coordinate variables, but the HDF5 model does not allow dimension scales to be
attached to other dimension scales, making it impossible to completely represent the multi-dimensional coordinate variables of the netCDF model.

To capture this information, multidimensional coordinate variables have an attribute named `_Netcdf4Coordinates`.
The attribute is an array of H5T_NATIVE_INT, with the netCDF dimension IDs of each of its dimensions.

The _Netcdf4Coordinates attribute is otherwise hidden by the netCDF API.
It does not appear as one of the attributes for the netCDF variable involved, except through the HDF5 API.

## Dimensions without HDF5 Dimension Scales {#dim_spec2}

Starting with the netCDF-4.1 release, netCDF can read HDF5 files which do not use dimension scales.
In this case the netCDF library assigns dimensions to the HDF5 dataset as needed, based on the length of the dimension.

When an HDF5 file is opened, each dataset is examined in turn.
The lengths of all the dimensions involved in the shape of the dataset are determined.
Each new (i.e. previously unencountered) length results in the creation of a phony dimension in the netCDF API.

This will not accurately detect a shared, unlimited dimension in the HDF5 file, if different datasets have different lengths along this dimension (possible in HDF5, but not in netCDF).

Note that this is a read-only capability for the netCDF library.
When the netCDF library writes HDF5 files, they always use a dimension scale for every dimension.

Datasets must have either dimension scales for every dimension, or no dimension scales at all. Partial dimension scales are not, at this time, understood by the netCDF library.

## Dimension and Coordinate Variable Ordering {#dim_spec3}

In order to preserve creation order, the netCDF-4 library writes variables in their creation order.
Since some variables are also dimension scales, their order reflects both the order of the dimensions and the order of the coordinate variables.

However, these may be different. Consider the following code:

````
           /* Create a test file. */
           if (nc_create(FILE_NAME, NC_CLASSIC_MODEL|NC_NETCDF4, &ncid)) ERR;

           /* Define dimensions in order. */
           if (nc_def_dim(ncid, DIM0, NC_UNLIMITED, &dimids[0])) ERR;
           if (nc_def_dim(ncid, DIM1, 4, &dimids[1])) ERR;

           /* Define coordinate variables in a different order. */
           if (nc_def_var(ncid, DIM1, NC_DOUBLE, 1, &dimids[1], &varid[1])) ERR;
           if (nc_def_var(ncid, DIM0, NC_DOUBLE, 1, &dimids[0], &varid[0])) ERR;
````

In this case the order of the coordinate variables will be different from the order of the dimensions.

In practice, this should make little difference in user code, but if the user is writing code that depends on the ordering of dimensions, the netCDF library was updated in version 4.1 to detect this condition, and add the attribute `_Netcdf4Dimid` to the dimension scales in the HDF5 file.
This attribute holds a scalar H5T_NATIVE_INT which is the (zero-based) dimension ID for this dimension.

If this attribute is present on any dimension scale, it must be present on all dimension scales in the file.

## Variables {#vars_spec}

Variables in netCDF-4/HDF5 files exactly correspond to HDF5 datasets.
The data types match naturally between netCDF and HDF5.

In netCDF classic format, the problem of endianness is solved by writing all data in big-endian order.
The HDF5 library allows data to be written as either big or little endian, and automatically reorders the data when it is read, if necessary.

By default, netCDF uses the native types on the machine which writes the data. Users may change the endianness of a variable (before any data are written).
In that case the specified endian type will be used in HDF5 (for example, a H5T_STD_I16LE will be used for NC_SHORT, if little-endian has been specified for that variable.)

- NC_BYTE = H5T_NATIVE_SCHAR
- NC_UBYTE = H5T_NATIVE_UCHAR
- NC_CHAR = H5T_C_S1
- NC_STRING = variable length array of H5T_C_S1
- NC_SHORT = H5T_NATIVE_SHORT
- NC_USHORT = H5T_NATIVE_USHORT
- NC_INT = H5T_NATIVE_INT
- NC_UINT = H5T_NATIVE_UINT
- NC_INT64 = H5T_NATIVE_LLONG
- NC_UINT64 = H5T_NATIVE_ULLONG
- NC_FLOAT = H5T_NATIVE_FLOAT
- NC_DOUBLE = H5T_NATIVE_DOUBLE

The NC_CHAR type represents a single character, and the NC_STRING an array of characters.
This can be confusing because a one-dimensional array of NC_CHAR is used to represent a string (i.e. a scalar NC_STRING).

An odd case may arise in which the user defines a variable with the same name as a dimension, but which is not intended to be the coordinate variable for that dimension.
In this case the string "_nc4_non_coord_" is pre-pended to the name of the HDF5 dataset, and stripped from the name for the netCDF API.

## Attributes {#atts_spec}

Attributes in HDF5 and netCDF-4 correspond very closely.
Each attribute in an HDF5 file is represented as an attribute in the netCDF-4 file, with the exception of the attributes below, which are hidden by the netCDF-4 API.

- _Netcdf4Coordinates An integer array containing the dimension IDs of
  a variable which is a multi-dimensional coordinate variable.
- _nc3_strict When this (scalar, H5T_NATIVE_INT) attribute exists in
  the root group of the HDF5 file, the netCDF API will enforce the
  netCDF classic model on the data file.
- REFERENCE_LIST This attribute is created and maintained by the HDF5
  dimension scale API.
- CLASS This attribute is created and maintained by the HDF5 dimension
  scale API.
- DIMENSION_LIST This attribute is created and maintained by the HDF5
  dimension scale API.
- NAME This attribute is created and maintained by the HDF5 dimension
  scale API.
- _Netcdf4Dimid Holds a scalar H5T_NATIVE_INT that is the (zero-based)
  dimension ID for this dimension, needed when dimensions and
  coordinate variables are defined in different orders.
- _NCProperties Holds provenance information about a file at the time
  it was created. It specifies the versions of the netCDF and HDF5
  libraries used to create the file.

## User-Defined Data Types {#user_defined_spec}

Each user-defined data type in an HDF5 file exactly corresponds to a user-defined data type in the netCDF-4 file.
Only base data types which correspond to netCDF-4 data types may be used.
(For example, no HDF5 reference data types may be used.)

## Compression {#compression_spec}

The HDF5 library provides data compression using the zlib library and the szlib library.
NetCDF-4 only allows users to create data with the zlib library (due to licensing restrictions on the szlib library).  
Since HDF5 supports the transparent reading of the data with either compression filter, the netCDF-4 library can read data compressed with szlib (if the underlying HDF5 library is built to support szlib), but has no way to write data with szlib compression.

With zlib compression (a.k.a. deflation) the user may set a deflation factor from 0 to 9.
In our measurements the zero deflation level does not compress the data, but does incur the performance penalty of compressing the data.
The netCDF API does not allow the user to write a variable with zlib deflation of 0 - when asked to do so, it turns off deflation for the variable instead.
NetCDF can read an HDF5 file with deflation of zero, and correctly report that to the user.

# The NetCDF-4 Classic Model Format {#netcdf_4_classic_spec}

Every classic and 64-bit offset file can be represented as a netCDF-4 file, with no loss of information.
There are some significant benefits to using the simpler netCDF classic model with the netCDF-4 file format.
For example, software that writes or reads classic model data can write or read netCDF-4 classic model format data by recompiling/relinking to a netCDF-4 API library, with no or only trivial changes needed to the program source code.
The netCDF-4\ classic model format supports this usage by enforcing rules on what functions may be called to store data in the file, to make sure its data can be read by older netCDF applications (when relinked to a netCDF-4 library).

Writing data in this format prevents use of enhanced model features such as groups, added primitive types not available in the classic model, and user-defined types. However performance features of the netCDF-4 formats that do not require additional features of the enhanced model, such as per-variable compression and chunking, efficient dynamic schema changes, and larger variable size limits, offer potentially significant performance improvements to readers of data stored in this format, without requiring program changes.

When a file is created via the netCDF API with a CLASSIC_MODEL mode flag, the library creates an attribute (_nc3_strict) in the root group.
This attribute is hidden by the netCDF API, but is read when the file is later opened, and used to ensure that no enhanced model features are written to the file.

# HDF4 SD Format {#hdf4_sd_format}

Starting with version 4.1, the netCDF libraries can read HDF4 SD (Scientific Dataset) files.
Access is limited to those HDF4 files created with the Scientific Dataset API. Access is read-only.

Dataset types are translated between HDF4 and netCDF in a straightforward manner.

- DFNT_CHAR = NC_CHAR
- DFNT_UCHAR, DFNT_UINT8 = NC_UBYTE
- DFNT_INT8 = NC_BYTE
- DFNT_INT16 = NC_SHORT
- DFNT_UINT16 = NC_USHORT
- DFNT_INT32 = NC_INT
- DFNT_UINT32 = NC_UINT
- DFNT_FLOAT32 = NC_FLOAT
- DFNT_FLOAT64 = NC_DOUBLE
