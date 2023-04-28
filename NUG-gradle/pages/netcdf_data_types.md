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

|---
| CDL | netCDF-C | netCDF-Java | Description | Availability
|:-|:-|:-|:-|:-
| char   | NC_CHAR   | CHAR   | 8-bit character                      | Classic, Enhanced, CDF&#x2011;5
| byte   | NC_BYTE   | BYTE   | 8-bit signed integer                 | Classic, Enhanced, CDF&#x2011;5
| short  | NC_SHORT  | SHORT  | 16-bit signed integer                | Classic, Enhanced, CDF&#x2011;5
| int    | NC_INT    | INT    | 32-bit signed integer                | Classic, Enhanced, CDF&#x2011;5
| long   | NC_LONG   | ---    | **Deprecated**: synonymous with int  | ---
| float  | NC_FLOAT  | FLOAT  | IEEE single-precision floating point (32 bits) | Classic, Enhanced, CDF&#x2011;5
| real   | ---       | ---    | **Deprecated**: Synonymous with float | ---
| double | NC_DOUBLE | DOUBLE | IEEE double-precision floating point (64 bits) | Classic, Enhanced, CDF&#x2011;5
| ubyte  | NC_UBYTE  | UBYTE  | Unsigned 8-bit integer               | Enhanced, CDF&#x2011;5
| ushort | NC_USHORT | USHORT | Unsigned 16-bit integer              | Enhanced, CDF&#x2011;5
| uint   | NC_UINT   | UINT   | Unsigned 32-bit integer              | Enhanced, CDF&#x2011;5
| int64  | NC_INT64  | LONG   | 64-bit signed integer                | Enhanced, CDF&#x2011;5
| uint64 | NC_UINT64 | ULONG  | Unsigned 64-bit signed integer       | Enhanced, CDF&#x2011;5
| string | NC_STRING | STRING | Variable-length string of characters | Enhanced

<!-- NOTE:
See netCDF-Java ArrayType lines 19-46
https://github.com/Unidata/netcdf-java/blob/01d8aef292cc7bbcee556657129bc88694613d65/cdm/core/src/main/java/ucar/array/ArrayType.java#L19-L46
-->

<!-- NOTE:
Text from NUG/types.md#external_types
-->

The netCDF atomic data types were chosen to provide a reasonably wide range of trade-offs between data precision and number of bits required for each value.
They are independent from whatever internal data types are supported by a particular machine and language combination.

[//]: # (TODO: This is new text. Better for data model section?)
The netCDF atomic data types provide a variety of integer, floating point, and character data types.
The data types in different programming languages and on different computing platforms will not necessarily align with the netCDF atomic data types.
In those cases conversions will be necessary and are generally handled by the different language libraries.
(More details on conversions in ???section???.)

[//]: # (TODO: Decide where conversion to language specific data types should be discussed.)
[//]: # (      Probably not appropriate for data model section. The following text is from)
[//]: # (      NUG/types.md#external_types. Perhaps need a section on implementations and)
[//]: # (      issues that apply to all implementations. Related to mappings for DAP, Zarr, etc.)

[//]: # (TODO: Also, discussing errors probably doesn't make sense in a data model section.)

Converting from one numeric type to another may result in an error if the target type is not capable of representing the converted value.

Note that mere loss of precision in type conversion does not return an error.
Thus, if you read double precision values into a single-precision floating-point variable, for example, no error results unless the magnitude of the double precision value exceeds the representable range of single-precision floating point numbers on your platform.
Similarly, if you read a large integer into a float incapable of representing all the bits of the integer in its mantissa, this loss of precision will not result in an error.
If you want to avoid such precision loss, check the external types of the variables you access to make sure you use an internal type that has adequate precision.

It is possible to interpret byte data as either signed (-128 to 127) or unsigned (0 to 255).
However, when reading byte data to be converted into other numeric types, it is interpreted as signed.

## Byte data in netCDF
<!-- NOTE: ???
-->
## String data types in netCDF
### `Char` array Strings
### String data type in Enhanced Data Model

[//]: # (TODO: Review various statements about char vs String.)
[//]: # (      The ncgen man page doc says `For netCDF extended, the use of the char type is deprecated in favor of the string type.`)
[//]: # (      This is the only mention of deprecation for `char' type. Not sure deprecated is the right term, maybe discouraged.)

### String character encodings (UTF-8)

# User Defined Data Types

The Enhanced Data Model supports compound types (similar to C-structs), VLEN types (which can be used for ragged arrays), opaque types ( to blobs of )
## Compound Types
## VLEN Types

[//]: # (TODO: Is the VLEN atomic read part of the data model?)

## Opaque Types
## Enum Types

