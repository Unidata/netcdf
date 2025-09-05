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
A dimension may be used to represent a real physical dimension, for example, time, latitude, longitude, or height.
A dimension might also be used to index other quantities, for example station or model-run-number.

A dimension length is either an arbitrary positive integer or unlimited.
Dimensions with an unlimited length are called unlimited dimensions or record dimensions.

## Dimension Scope in a Group Hierarchy
Dimensions are scoped such that they can be seen in all descendant groups.
That is, dimensions can be shared between variables in different groups, if they are defined in a parent group.

## Dimension advice
It is possible (since version 3.1 of netCDF) to use the same dimension more than once in specifying a variable shape.
For example, correlation(instrument, instrument) could be a matrix giving correlations between measurements using different instruments.
But data whose dimensions correspond to those of physical space/time should have a shape comprising different dimensions, even if some of these have the same length.

[//]: # (TODO: Is creation time part of the data model? Does it apply to both netCDF-C and -Java? Mentioned here in Variables section.)

# Variables
Variables are used to store the bulk of the data in a netCDF dataset.
A variable is a multidimensional array whose elements are all of the same type.
A variable has a name, a data type, and a shape given by its list of dimensions.
(All three aspects of a variable must be specified when the variable is created and cannot be changed after creation.)
The number of dimensions is called the rank (a.k.a. dimensionality) of that variable.
A scalar variable has rank 0, a vector has rank 1 and a matrix has rank 2.
A variable may also have associated attributes, which may be added, deleted or changed after the variable is created.

A variables shape and the dimensions that make up that shape define the index space that allows individual elements of the variable to be identified.
Dimensions shared by two or more variables indicates the variables share a grid / coordinate system.

## Variables with Unlimited Dimensions
[//]: # (TODO: Is write/access performance impacts part of the data model?)
[//]: # (TODO: Is relation between dimension ordering and array layout part of data model??)

Unlimited dimensions indicate that data may be efficiently appended to variables along that dimension.
A variable with an unlimited dimension can grow to any length along that dimension.
The unlimited dimension index is like a record number in conventional record-oriented files.
In the netCDF classic data model, a dataset can have at most one unlimited dimension, but need not have any.
If there is an unlimited dimension, it must be the most significant (slowest changing) dimension which means it must be the first dimension in a variables dimension list in CDL and C array declarations.
In the netCDF enhanced data model, a variable may have multiple unlimited dimensions, and there are no restrictions on their order in the list of a variables dimensions.

## Coordinate Variables

It is legal for a variable to have the same name as a dimension.
Such variables have no special meaning to the netCDF library.
However, there is a convention that such variables should be treated in a special way by software using this library.

A variable with the same name as a dimension is called a **coordinate variable**.
It typically defines a physical coordinate corresponding to that dimension.
Current application packages that make use of coordinate variables commonly assume they are numeric vectors and strictly monotonic (all values are different and either increasing or decreasing).

[//]: # (TODO: Harmonize the various coordinate variable sections.)
[//]: # (      E.g. - NUG/bestpractices.md#coordinate_systems section defines `char station\(station, stn_len\)`)
[//]: # (      as a coordinate variable. The above does not include string value coord vars.)

# Attributes
NetCDF attributes are used to store information about the data (ancillary data or metadata), similar in many ways to the information stored in data dictionaries and schema in conventional database systems.
There are two types of attributes.
There are global attributes and variable attributes.
Global attributes provide information about the dataset as a whole, they are identified by the name of the attribute.
Variable attributes provide information about a specific variable and are identified by the name (or ID) of that variable together with the name of the attribute.

In netCDF enhanced data model datasets, there can also be group attributes which provide information about the contents of the group.

An attribute is associated with the dataset, a group, or a variable and has a name, a data type, a length, and a value.
An attribute's value can be either a vector or a scalar.

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

Groups provide a hierarchical structure for organizing data.
A group has a name and may contain one or more named variables, dimensions, attributes, groups, and user-defined types.
Each netCDF dataset contains a top-level root group ("/").

Groups, like directories in a Unix file system, are hierarchically organized, to arbitrary depth.
They can be used to organize large numbers of variables.

