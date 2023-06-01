---
title: NetCDF Objects
last_updated: 2021-06-28
sidebar: nnug_sidebar
toc: false
permalink: netcdf_objects.html
---

# Dimensions
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

[//]: # (TODO: Consider moving the rest of this section into the variable section.)
[//]: # (      Or is write/access performance impacts part of the data model?)
[//]: # (      But again, perhaps better in the variable section than in the dimension section.)

Unlimited dimensions indicate that data may be efficiently appended to variables along that dimension.
A variable with an unlimited dimension can grow to any length along that dimension.
The unlimited dimension index is like a record number in conventional record-oriented files.

In the netCDF classic data model, a dataset can have at most one unlimited dimension, but need not have any.

[//]: # (TODO: Is relation between dimension ordering and array layout part of data model??)

If a variable has an unlimited dimension, that dimension must be the most significant (slowest changing) one.
???Thus any unlimited dimension must be the first dimension in a CDL shape and the first dimension in corresponding C array declarations.???

In the netCDF enhanced data model, a dataset may have multiple unlimited dimensions, and there are no restrictions on their order in the list of a variables dimensions.

## Dimension advice???
It is possible (since version 3.1 of netCDF) to use the same dimension more than once in specifying a variable shape.
For example, correlation(instrument, instrument) could be a matrix giving correlations between measurements using different instruments.
But data whose dimensions correspond to those of physical space/time should have a shape comprising different dimensions, even if some of these have the same length.

# Variables
Variables are used to store the bulk of the data in a netCDF dataset.
A variable is a multidimensional array whose elements are all of the same type.
A variable has a name, a data type, and a shape given by its list of dimensions.
(All three aspects of a variable must be specified when the variable is created and cannot be changed after creation. <!-- ??? Is creation time part of the data model??? -->)
The number of dimensions is called the rank (a.k.a. dimensionality) of that variable.
A scalar variable has rank 0, a vector has rank 1 and a matrix has rank 2.
A variable may also have associated attributes, which may be added, deleted or changed after the variable is created. <!-- ??? Creation time here as well.???>

A variables shape and the dimensions that make up that shape define the index space that allows individual elements of the variable to be identified.

[//]: # (TODO: Consider moving mention of shared dimensions indicating a shared grid to dimension section.)

Dimensions shared by two or more variables indicates the variables share a grid / coordinate system.

## Coordinate Variables

It is legal for a variable to have the same name as a dimension.
Such variables have no special meaning to the netCDF library.
However there is a convention that such variables should be treated in a special way by software using this library.

A variable with the same name as a dimension is called a **coordinate variable**.
It typically defines a physical coordinate corresponding to that dimension.
~~The above CDL example includes the coordinate variables lat, lon, level and time, defined as follows:~~

Current application packages that make use of coordinate variables commonly assume they are numeric vectors and strictly monotonic (all values are different and either increasing or decreasing).

[//]: # (TODO: Harmonize the various coordinate variable sections.)
[//]: # (      E.g. - NUG/bestpractices.md#coordinate_systems section defines `char station\(station, stn_len\)`)
[//]: # (      as a coordinate variable. The above does not include string value coord vars.)

# Attributes
NetCDF attributes are used to store data about the data (ancillary data or metadata), similar in many ways to the information stored in data dictionaries and schema in conventional database systems.
Most attributes provide information about a specific variable.
These are identified by the name (or ID) of that variable, together with the name of the attribute.

Some attributes provide information about the dataset as a whole and are called global attributes.

In ~~netCDF-4 file~~ netCDF enhanced data model datasets, attributes can also be added at the group level.

An attribute has an associated variable (the null "global variable" for a global or group-level attribute), a name, a data type, a length, and a value.
The current version treats all attributes as vectors; scalar values are treated as single-element vectors.

[//]: # (TODO: Consider rewriting sentence recommending meaningful/conventional attribute names.)
[//]: # (      Something like: `Follow community conventions for attribute names where possible.`)
Conventional attribute names should be used where applicable.
New names should be as meaningful as possible.

The types permitted for attributes are the same as the netCDF external data types for variables.
Attributes with the same name for different variables should sometimes be of different types.
For example, the attribute valid_max, specifying the maximum valid data value for a variable of type int, should be of type int.
Whereas the attribute valid_max for a variable of type double, should instead be of type double.

[//]: # (TODO: Decide how much about creating and/or deleting netCDF objects is part of data model.)
Attributes are more dynamic than variables or dimensions; they can be deleted and have their type, length, and values changed after they are created, whereas the netCDF interface provides no way to delete a variable or to change its type or shape.

# Differences between Attributes and Variables

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

# Groups

