---
title: NetCDF Data Types
last_updated: 2021-06-28
sidebar: nnug_sidebar
toc: false
permalink: netcdf_data_types.html
---

# Atomic Data Types

[//]: # (TODO: Decide whether to use `atomic`, `primitive`, and `external` for these data types.)
[//]: # (      The current NUG uses these terms somewhat interchangeable.)
[//]: # (      The term `external` is in reference to these types being external to the)
[//]: # (      programming language data types used in each particular library.)
[//]: # (      So, `external` isn't appropriate for data types in the netCDF data model.)

[//]: # (TODO: Add DAP2, DAP4, Zarr, and NCZarr to the availability column as appropriate.) 

|---
| CDL | netCDF-C | netCDF-Java | Description | Availability
|:-|:-|:-|:-|:-
| char   | NC_CHAR   | CHAR   | 8-bit character                      | Classic, CDF-5, Enhanced
| byte   | NC_BYTE   | BYTE   | 8-bit signed integer                 | Classic, CDF-5, Enhanced
| short  | NC_SHORT  | SHORT  | 16-bit signed integer                | Classic, CDF-5, Enhanced
| int    | NC_INT    | INT    | 32-bit signed integer                | Classic, CDF-5, Enhanced
| long   | NC_LONG   | ---    | **Deprecated**: synonymous with int  | ---
| float  | NC_FLOAT  | FLOAT  | IEEE single-precision floating point (32 bits) | Classic, CDF-5, Enhanced
| real   | ---       | ---    | **Deprecated**: Synonymous with float | ---
| double | NC_DOUBLE | DOUBLE | IEEE double-precision floating point (64 bits) | Classic, CDF-5, Enhanced
| ubyte  | NC_UBYTE  | UBYTE  | Unsigned 8-bit integer               | CDF-5, Enhanced
| ushort | NC_USHORT | USHORT | Unsigned 16-bit integer              | CDF-5, Enhanced
| uint   | NC_UINT   | UINT   | Unsigned 32-bit integer              | CDF-5, Enhanced
| int64  | NC_INT64  | LONG   | 64-bit signed integer                | CDF-5, Enhanced
| uint64 | NC_UINT64 | ULONG  | Unsigned 64-bit signed integer       | CDF-5, Enhanced
| string | NC_STRING | STRING | Variable-length string of characters | Enhanced

<!-- NOTE:
See netCDF-Java ArrayType lines 19-46
https://github.com/Unidata/netcdf-java/blob/01d8aef292cc7bbcee556657129bc88694613d65/cdm/core/src/main/java/ucar/array/ArrayType.java#L19-L46
-->

<!-- NOTE:
Text from NUG/types.md#external_types
-->

The netCDF atomic data types provide a variety of integer, floating point, and character data types.
The data types in different programming languages and on different computing platforms will not necessarily align with the netCDF atomic data types.
In those cases conversions will be necessary and are generally handled by the different language libraries.

## Conversion Advice ???
[//]: # (TODO: Decide where conversion to language specific data types should be discussed.)
[//]: # (      Probably not appropriate for data model section. The following text is from)
[//]: # (      NUG/types.md#external_types. Perhaps need a section on implementations and)
[//]: # (      issues that apply to all implementations. Related to mappings for DAP, Zarr, etc.)

[//]: # (TODO: Also, discussing errors probably doesn't make sense in a data model section.)
[//]: # (TODO: Perhaps all of this, conversions and errors, should be left for implementation docs.)

Converting from one numeric type to another may result in an error if the target type is not capable of representing the converted value.

Note that mere loss of precision in type conversion does not return an error.
Thus, if you read double precision values into a single-precision floating-point variable, for example, no error results unless the magnitude of the double precision value exceeds the representable range of single-precision floating point numbers on your platform.
Similarly, if you read a large integer into a float incapable of representing all the bits of the integer in its mantissa, this loss of precision will not result in an error.
If you want to avoid such precision loss, check the external types of the variables you access to make sure you use an internal type that has adequate precision.

## Byte data in netCDF
Because the netCDF classic data model only has the byte type (`byte`), byte data in the classic data model can be interpreted as either signed (-128 to 127) or unsigned (0 to 255).
Several conventions have developed over the years for dealing with this situation (see Best Practices "Unsigned Data" [section](best_practices.html#unsigned-data)).

Both the enhanced data model and the CDF-5 file format support unsigned bytes (`ubyte`).
So the issue can be avoided by using either of them.

## String data types in netCDF
[//]: # (TODO: "_Encoding" variable attribute is described in nc-3 spec. Should it be included in data model?)
[//]: # (      Info on string/char and encodings was in nc-3 file format spec, CDL page, and Best Practices.)
Text strings can be stored as fixed-length `char` arrays or as variable-length `string` values.
While netCDF object names must be encoded as UTF-8, variables and attributes storing text strings may use other encodings.
The variable attribute “_Encoding” is reserved for this purpose in future implementations (though this is not a widely used convention).
If an "_Encoding" variable attribute is not provided, the default is to assume UTF-8 encoding.

[//]: # (TODO: Should attributes vs variables be discussed? In terms of atts only scalar or vector, I guess.)
Because attributes are either scalar or vector,

[//]: # (TODO: Is there need to indicate when `char` array might be intended as an array of single characters rather than as a string?)

# User Defined Data Types {#user_defined_data_types}

The Enhanced Data Model supports compound types (similar to C-structs), VLEN types (which can be used for ragged arrays), opaque types ( to blobs of bytes), and enumerations.

Types may be nested in complex ways.
For example, a compound type containing an array of VLEN types, each containing variable length arrays of some other compound type, etc.
Users are cautioned to keep types simple.
Reading data of complex types can be challenging, especially perhaps for Fortran users.

Types cannot have attributes (but variables of the type may have attributes).

## Scope of User Defined Types in a Group Hierarchy
Types may be defined in any group in the data file, but they are always available globally in the file.

## Compound Types
The compound type can be used to define a data structure which contains an arbitrary collection of other data types, including other compound types.
A compound type is defined by its name and the set of named fields in the compound type where each named field is defined by its name and data type.

[//]: # (TODO: NetCDF-C nc_inq_compound returns the size of the data type. How does that work with VLEN???)

## VLEN Types

[//]: # (TODO: Is the VLEN atomic read part of the data model? Or only something for the HDF5 file format?)
[//]: # (      NetCDF-Java handles it as an iterator, I think. But does that matter? Is VLEN only available in HDF5?)

The VLEN type can be used to store variable length arrays of a known base type, providing support for ragged arrays.
A VLEN type is defined by its name and it's base type.
A VLEN base type may be of any type, including user-defined types.
Access to the values of a VLEN variable is atomic.
The number of values contained by a VLEN variable is determined when that variable is written and discovered when the variable is read.
If a field in a compound type is of type VLEN, the size of that field in a variable is determined/discovered when that component is written/read.

## Opaque Types
[//]: # (TODO: Why use an opaque type instead of a byte array? Just a named abstraction shared by variables of that type.)
The opaque type can be used to store a collection/sequence of bytes where the number of bytes is known.
An opaque type is defined by its name and its size in bytes.

## Enum Types
[//]: # (TODO: Text from 2011 workshop page Using Enums - https://www.unidata.ucar.edu/software/netcdf/workshops/2011/groups-types/Enum.html )
The enumerated type can be used to associate integer constant values with names/labels.
An enumerated type is defined by its name, its base type (any integer type), and by its member value/name pairs.
The integer member values need not be consecutive.
The underlying base integer type is what is stored in the netCDF dataset.