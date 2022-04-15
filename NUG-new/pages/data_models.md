---
title: NetCDF Data Models
last_updated: 2021-04-06
sidebar: nnug_sidebar
toc: false
permalink: data_models.html
---

<!-- NOTE:
Two main sources of text were used for this page:
- NUG/netcdf_data_set_components.md
- "Developing Conventions for netCDF-4\" article by Rew and Caron - https://www.unidata.ucar.edu/software/netcdf/papers/nc4_conventions.html
More details in notes below.
-->
[//]: # (TODO: Review other possible source of content.)
[//]: # (      - Unidata's Common Data Model (CDM) - https://docs.unidata.ucar.edu/netcdf-java/current/userguide/common_data_model_overview.html)
[//]: # (      - CDM NetCDF Mapping - https://docs.unidata.ucar.edu/netcdf-java/current/userguide/cdm_netcdf_mapping.html)


[//]: # (TODO: Decide where should we put "Limitations of NetCDF" section in NUG/netcdf_introduction.md ?)
[//]: # (      - Is the "2 GiBytes size limit" in CDF-1 part of the data model?)
[//]: # (      - Is the "only one unlimited dimension" limitation in CDF-1, 2, and -5 part of the data model?)

NetCDF has two main data models. 
* The netCDF Classic Data Model, which represents a dataset with named variables, dimensions, and attributes.
* The netCDF Enhanced Data Model, which adds hierarchical structure, string and unsigned integer types, and user-defined types.  

Some file formats and encodings support subsets of the Enhanced Data Model
or minor extensions to the Classic Data Model.
For example, CDF-5 extends the Classic Data Model with the addition of
support for unsigned integer and 64-bit integer data types.
Whereas ncZarr supports a subset of the Enhanced Data Model
that does not include support for strings, user-defined types, or VLEN dimensions.

<!-- TODO: Should we use CDL, C, Fortran, Java to clarify some aspects of the data model? (E.g., dimension order, data types, etc.)
If so we should mention something about them here (with pointers to full descriptions).
Or add more details (and pointers) to CDL and libraries in index.md and netcdf_overview.md. -->

<!-- Do we explain the difference between Enhanced DM and CDM?  -->


## NetCDF Classic Data Model
<!-- NOTE:
This text from [Rew & Caron] Section \"NetCDF Data Models\" paragraph 2
-->

The Classic Data Model represents data sets using named variables, dimensions, and attributes.
A variable is a multidimensional array whose elements are all of the same type.
The shape of each variable is specified by a list of dimensions.
Dimensions are named axes that have a length.
Variables may share dimensions, indicating a common grid.
One dimension in a dataset may be of unlimited length, so data may be efficiently appended to variables along that dimension.
A variable may also have attributes, which are associated named values.
Variables and attributes have one of six atomic data types: char, byte, short, int, float, or double.

A diagram of the Classic Data Model exhibits its simplicity:
{% include image.html file="nc-classic-uml.png" alt="netCDF Classic Data Model UML" caption="" %}

## NetCDF Enhanced Data Model
<!-- NOTE:
Text from [Rew & Caron] Section \"NetCDF Data Models\", paragraph 5
and from NUG/netcdf_data_set_components.md#enhanced_nc4_hdf5
-->
The Enhanced Data Model adds groups, a string type, several unsigned integer types, and four kinds of user-defined types (see [below](#user_defined_data_types)).
Groups, like directories in a file system, can be hierarchically organized to arbitrary depth.
Each netCDF file/dataset contains a top-level, unnamed group (aka root group).
Each group may contain one or more named variables, dimensions, attributes, groups, and user-defined types.

A variable is still a multidimensional array whose elements are all of the same type.
Each variable may have attributes, and each variable's shape is specified by its dimensions, which may be shared.
However, in the enhanced data model, one or more dimensions may be of unlimited length, so data may be efficiently appended to variables along any of those dimensions.
Variables and attributes have one of twelve primitive data types or one of four kinds of user-defined types.

Dimensions are scoped such that they can be seen in all descendant groups.
That is, dimensions can be shared between variables in different groups, if they are defined in a parent group.

In netCDF-4 files, the user may also define a type.
For example a compound type may hold information from an array of C structures,
or a variable length type allows the user to read and write arrays of variable length values.

User-defined types are ~~also~~ scoped such that they can be referenced in all other groups.
That means, for example, that variables in different groups can have the same type.

Variables, groups, and types share a namespace.
Within the same group, variables, groups, and types must have unique names.
(That is, a type and variable may not have the same name within the same group,
and similarly for sub-groups of that group.)

A UML diagram of the enhanced netCDF data model
shows (in red) what it adds to the classic netCDF data model:
{% include image.html file="nc-enhanced-uml.png" alt="netCDF Enhanced Data Model UML" caption="" %}

## NetCDF Objects
### Dimensions
<!-- NOTE:
Text from NUG/netcdf_data_set_components.md#dimensions
-->

A netCDF dimension is defined by its name and its length.
NetCDF dimensions are used to specify the shape of a netCDF variable.
Dimensions may be shared between variables.
Shared dimensions indicate a common grid.

A dimension may be used to represent a real physical dimension, for example, time, latitude, longitude, or height.
A dimension might also be used to index other quantities, for example station or model-run-number.

A dimension length is either an arbitrary positive integer or unlimited.
Dimensions with an unlimited length are called unlimited dimensions or record dimensions.
<!-- TODO: Move the rest of this section be in the variable section?? Or is write/access performance impacts part of the data model? But again, perhaps better in the variable section than in the dimension section. -->
Unlimited dimensions indicate that data may be efficiently appended to variables along that dimension.
A variable with an unlimited dimension can grow to any length along that dimension.
The unlimited dimension index is like a record number in conventional record-oriented files.

In the netCDF classic data model, a dataset can have at most one unlimited dimension, but need not have any.
<!-- TODO: Is relation between dimension ordering and array layout part of data model?? -->
If a variable has an unlimited dimension, that dimension must be the most significant (slowest changing) one.
???Thus any unlimited dimension must be the first dimension in a CDL shape and the first dimension in corresponding C array declarations.???

In the netCDF enhanced data model, a dataset may have multiple unlimited dimensions, and there are no restrictions on their order in the list of a variables dimensions.

#### Dimension advice???
It is possible (since version 3.1 of netCDF) to use the same dimension more than once in specifying a variable shape.
For example, correlation(instrument, instrument) could be a matrix giving correlations between measurements using different instruments.
But data whose dimensions correspond to those of physical space/time should have a shape comprising different dimensions, even if some of these have the same length.

### Variables
Variables are used to store the bulk of the data in a netCDF dataset.
A variable is a multidimensional array whose elements are all of the same type.
A variable has a name, a data type, and a shape given by its list of dimensions.
(All three aspects of a variable must be specified when the variable is created and cannot be changed after creation. <!-- ??? Is creation time part of the data model??? -->)
The number of dimensions is called the rank (a.k.a. dimensionality) of that variable.
A scalar variable has rank 0, a vector has rank 1 and a matrix has rank 2.
A variable may also have associated attributes, which may be added, deleted or changed after the variable is created. <!-- ??? Creation time here as well.???> 

A variables shape and the dimensions that make up that shape define the index space that allows individual elements of the variable to be identified.
<!-- TODO: Shared dimensions indicating a shared grid is mentioned in dimensino section. It should probably be moved to the variables section.

Dimensions shared by two or more variables indicates the variables share a grid / coordinate system.
-->

#### Coordinate Variables

It is legal for a variable to have the same name as a dimension.
Such variables have no special meaning to the netCDF library.
However there is a convention that such variables should be treated in a special way by software using this library.

A variable with the same name as a dimension is called a **coordinate variable**.
It typically defines a physical coordinate corresponding to that dimension.
~~The above CDL example includes the coordinate variables lat, lon, level and time, defined as follows:~~

Current application packages that make use of coordinate variables commonly assume they are numeric vectors and strictly monotonic (all values are different and either increasing or decreasing).
<!-- TODO: Current "Best Practices" section defines 'char station(station, stn_len)' as a coordinate variable. The above does not include string value coord vars. Need to harmonize various coord var sections. -->

### Attributes
NetCDF attributes are used to store data about the data (ancillary data or metadata), similar in many ways to the information stored in data dictionaries and schema in conventional database systems.
Most attributes provide information about a specific variable.
These are identified by the name (or ID) of that variable, together with the name of the attribute.

Some attributes provide information about the dataset as a whole and are called global attributes.

In ~~netCDF-4 file~~ netCDF enhanced data model datasets, attributes can also be added at the group level.

An attribute has an associated variable (the null "global variable" for a global or group-level attribute), a name, a data type, a length, and a value.
The current version treats all attributes as vectors; scalar values are treated as single-element vectors.

<!-- TODO: Rewrite next sentence something like: "Follow community conventions for attribute names where possible."
Conventional attribute names should be used where applicable.
New names should be as meaningful as possible. -->

The types permitted for attributes are the same as the netCDF external data types for variables.
Attributes with the same name for different variables should sometimes be of different types.
For example, the attribute valid_max, specifying the maximum valid data value for a variable of type int, should be of type int.
Whereas the attribute valid_max for a variable of type double, should instead be of type double.

Attributes are more dynamic than variables or dimensions; they can be deleted and have their type, length, and values changed after they are created, whereas the netCDF interface provides no way to delete a variable or to change its type or shape.
<!-- TODO: Again, is how/when create delete part of data model.
-->

### Differences between Attributes and Variables
<!-- TODO: Does this section need updating?
-->

In contrast to variables, which are intended for bulk data, attributes are intended for ancillary data, or information about the data.
The total amount of ancillary data associated with a netCDF object, and stored in its attributes, is typically small enough to be memory-resident.
However variables are often too large to entirely fit in memory and must be split into sections for processing.

Another difference between attributes and variables is that variables may be multidimensional.
Attributes are all either scalars (single-valued) or vectors (a single, fixed dimension).

Variables are created with a name, type, and shape before they are assigned data values, so a variable may exist with no values.
The value of an attribute is specified when it is created, unless it is a zero-length attribute.

A variable may have attributes, but an attribute cannot have attributes.
Attributes assigned to variables may have the same units as the variable (for example, valid_range) or have no units (for example, scale_factor).
If you want to store data that requires units different from those of the associated variable, it is better to use a variable than an attribute.
More generally, if data require ancillary data to describe them, are multidimensional, require any of the defined netCDF dimensions to index their values, or require a significant amount of storage, that data should be represented using variables rather than attributes.

### Groups

## NetCDF Data Types

### Atomic Data Types

[//]: # (TODO: Decide whether to use "atomic", "primitive", and "external" for these data types.)
[//]: # (      The current NUG uses these terms somewhat interchangeable.)
[//]: # (      The term "external" is in reference to these types being external to the)
[//]: # (      programming language data types used in each particular library.)
[//]: # (      So, "external" isn't appropriate for data types in the netCDF data model.)

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

Note: Availability - A ==> all data models and file variants; E --> Enhanced data model; 5 --> CDF-5

<!-- NOTE:
See netCDF-Java ArrayType lines 19-46
https://github.com/Unidata/netcdf-java/blob/01d8aef292cc7bbcee556657129bc88694613d65/cdm/core/src/main/java/ucar/array/ArrayType.java#L19-L46
-->

<!-- NOTE:
Text from NUG/types.md#external_types
-->

The netCDF atomic data types were chosen to provide a reasonably wide range of trade-offs between data precision and number of bits required for each value.
They are independent from whatever internal data types are supported by a particular machine and language combination.

<!-- NOTE:
This is new text. Better for data model section?
-->
The netCDF atomic data types provide a variety of integer, floating point, and character data types.
The data types in different programming languages and on different computing platforms will not necessarily align with the netCDF atomic data types.
In those cases conversions will be necessary and are generally handled by the different language libraries.
(More details on conversions in ???section???.)

<!-- NOTE:
Not sure data model section is appropriate for discussion of converting between netCDF data types and language specific data types. The following is from NUG/types.md#external_types. Would a seperate section on conversions (and mapping for DAP, Zarr, etc.) makes sense?
-->
<!-- NOTE:
I don't think talk of errors makes sense in a data model section.
-->
Converting from one numeric type to another may result in an error if the target type is not capable of representing the converted value.

Note that mere loss of precision in type conversion does not return an error.
Thus, if you read double precision values into a single-precision floating-point variable, for example, no error results unless the magnitude of the double precision value exceeds the representable range of single-precision floating point numbers on your platform.
Similarly, if you read a large integer into a float incapable of representing all the bits of the integer in its mantissa, this loss of precision will not result in an error.
If you want to avoid such precision loss, check the external types of the variables you access to make sure you use an internal type that has adequate precision.

It is possible to interpret byte data as either signed (-128 to 127) or unsigned (0 to 255).
However, when reading byte data to be converted into other numeric types, it is interpreted as signed.

#### Byte data in netCDF
<!-- NOTE: ???
-->
#### String data types in netCDF
##### `Char` array Strings
##### String data type in Enhanced Data Model
<!-- TODO: The ncgen man page doc says
"For netCDF extended, the use of the char type is deprecated in favor of the string type."
This is the only mention of deprecation for 'char' type. Not sure deprecated is the right term, maybe discouraged.
-->
##### String character encodings (UTF-8)

### User Defined Data Types

The Enhanced Data Model supports compound types (similar to C-structs), VLEN types (which can be used for ragged arrays), opaque types ( to blobs of )
#### Compound Types
#### VLEN Types

<!-- TODO: Is the VLEN atomic read part of the data model? -->

#### Opaque Types
#### Enum Types

## NetCDF Object Names

### Character Encodings (UTF-8)
### Permitted Characters in NetCDF Names

The names of dimensions, variables and attributes (and, in netCDF-4 files, groups, user-defined types, compound member names, and enumeration symbols) consist of arbitrary sequences of alphanumeric characters, underscore '_', period '.', plus '+', hyphen '-', or at sign '@', but beginning with an alphanumeric character or underscore. However names commencing with underscore are reserved for system use.

Beginning with versions 3.6.3 and 4.0, names may also include UTF-8 encoded Unicode characters as well as other special characters, except for the character '/', which may not appear in a name.

Names that have trailing space characters are also not permitted.

Case is significant in netCDF names.

### Length of NetCDF Object Names

<!-- TODO: Some of this is netCDF-C specific.
-->
A zero-length name is not allowed.

Names longer than ::NC_MAX_NAME will not be accepted any netCDF define function. An error of ::NC_EMAXNAME will be returned.

All netCDF inquiry functions will return names of maximum size ::NC_MAX_NAME for netCDF files. Since this does not include the terminating NULL, space should be reserved for NC_MAX_NAME + 1 characters.

### Conventions and NetCDF Object Names

Some widely used conventions restrict names to only alphanumeric characters or underscores.

> Note that, when using the DAP2 protocol to access netCDF data, there are \em reserved keywords, the use of which may result in undefined behavior.  See \ref dap2_reserved_keywords for more information.
