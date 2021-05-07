---
title: NetCDF Data Models
last_updated: 2021-04-06
sidebar: nnug_sidebar
toc: false
permalink: data_models.html
---

<!--
  Where should we put "Limitations of NetCDF" section in NUG/netcdf_introduction.md ?
  - Is the "2 GiBytes size limit" in CDF-1 part of the data model?
  - Is the "only one unlimited dimension" limitation in CDF-1, 2, and -5 part of the data model?
-->

NetCDF has two main data models. 
* The netCDF Classic Data Model, which represents a dataset with named variables, dimensions, and attributes.
* The netCDF Enhanced Data Model, which adds hierarchical structure, string and unsigned integer types, and user-defined types.  

Some file formats and encodings support subsets of the Enhanced Data Model
or minor extensions to the Classic Data Model.
For example, CDF-5 extends the Classic Data Model with the addition of
support for unsigned integer and 64-bit integer data types.
Whereas ncZarr supports a subset of the Enhanced Data Model
that does nont include support for user-defined types or VLEN dimensions (and ???).

<!-- Do we explain the difference between Enhanced DM and CDM?  -->

<!-- Maybe grab text from Rew & Caron "Developing Conventions for netCDF-4" paper:
https://www.unidata.ucar.edu/software/netcdf/papers/nc4_conventions.html
-->
## NetCDF Classic Data Model
The Classic Data Model represents data sets using named variables, dimensions, and attributes.
A variable is a multidimensional array whose elements are all of the same type.
A variable may also have attributes, which are associated named values.
Each variable has a shape, specified by its dimensions, named axes that have a length.
Variables may share dimensions, indicating a common grid.
One dimension may be of unlimited length, so data may be efficiently appended to variables along that dimension.
Variables and attributes have one of six atomic data types: char, byte, short, int, float, or double.

A diagram of the Classic Data Model exhibits its simplicity:
{% include image.html file="nc-classic-uml.png" alt="netCDF Classic Data Model UML" caption="" %}

### Dimensions
### Attributes
### Variables
### Coordinate Variables
### Data Types
#### `Char` array Strings
##### String character encodings (UTF-8)
### NetCDF Object Names {#object_name}

#### Character Encodings (UTF-8)
#### Permitted Characters in NetCDF Names {#permitted_characters}

The names of dimensions, variables and attributes (and, in netCDF-4 files, groups, user-defined types, compound member names, and enumeration symbols) consist of arbitrary sequences of alphanumeric characters, underscore '_', period '.', plus '+', hyphen '-', or at sign '@', but beginning with an alphanumeric character or underscore. However names commencing with underscore are reserved for system use.

Beginning with versions 3.6.3 and 4.0, names may also include UTF-8 encoded Unicode characters as well as other special characters, except for the character '/', which may not appear in a name.

Names that have trailing space characters are also not permitted.

Case is significant in netCDF names.

#### Name Length {#name_length}

A zero-length name is not allowed.

Names longer than ::NC_MAX_NAME will not be accepted any netCDF define function. An error of ::NC_EMAXNAME will be returned.

All netCDF inquiry functions will return names of maximum size ::NC_MAX_NAME for netCDF files. Since this does not include the terminating NULL, space should be reserved for NC_MAX_NAME + 1 characters.

#### NetCDF Conventions {#netcdf_conventions}

Some widely used conventions restrict names to only alphanumeric characters or underscores.

> Note that, when using the DAP2 protocol to access netCDF data, there are \em reserved keywords, the use of which may result in undefined behavior.  See \ref dap2_reserved_keywords for more information.

## NetCDF Enhanced Data Model

### Dimensions
multiple unlimited
VLEN
### Groups
### New Data Types

#### String
##### String character encodings (UTF-8)


## Supported String Encodings



## Data Types

|---
| DataType | Array subclass | Array.getElementType
|:-|:-|:-
| BYTE | ArrayByte | byte.class
| SHORT | ArrayShort | short.class
| INT | ArrayInt | int.class
| LONG | ArrayLong | long.class
| FLOAT | ArrayFloat | float.class
| DOUBLE | ArrayDouble | double.class
| CHAR | ArrayChar | char.class
| STRING | ArrayObject | String.class
| STRUCTURE | ArrayStructure | StructureData.class
| SEQUENCE | ArraySequence | StructureData.class
| ENUM1 | ArrayByte | byte.class
| ENUM2 | ArrayShort | short.class
| ENUM4 | ArrayInt | int.class
| OPAQUE | ArrayObject | ByteBuffer.class


|---
| Data Type | CDL | netCDF-C | netCDF-Java | Description
|:-|:-|:-|:-|:-
| char   | char   | NC_CHAR   | CHAR | Characters.
| byte   | byte   | NC_BYTE   | BYTE | Eight-bit integers.
| short  | short  | NC_SHORT  | SHORT | 16-bit signed integers.
| int    | int    | NC_INT    | INT  | 32-bit signed integers.
|        | long   | NC_LONG   |  | (Deprecated, synonymous with int)
| float  | float  | NC_FLOAT  | FLOAT | IEEE single-precision floating point (32 bits).
|        | real   |           | |  (?Deprecated? Synonymous with float).
| double | double | NC_DOUBLE | DOUBLE | IEEE double-precision floating point (64 bits).
| ubyte  | ubyte  | NC_UBYTE  | | Unsigned eight-bit integers.
| ushort | ushort | NC_USHORT | | Unsigned 16-bit integers.
| uint   | uint   | NC_UINT   | | Unsigned 32-bit integers.
| int64  | int64  | NC_INT64  | LONG | 64-bit signed integers.
| uint64 | uint64 | NC_UINT64 | | Unsigned 64-bit signed integers.
| string | string | NC_STRING | | Variable-length string of characters

<!--
See netCDF-Java ArrayType ilines 19-46
https://github.com/Unidata/netcdf-java/blob/01d8aef292cc7bbcee556657129bc88694613d65/cdm/core/src/main/java/ucar/array/ArrayType.java#L19-L46
-->
### Atomic Data Types
<!-- Both "atomic" and "primitive" are used to describe data types in the current NUG. -->
### NetCDF Object Names

#### Encoded as UTF-8

#### Characters allowed

