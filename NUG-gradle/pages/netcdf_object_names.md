---
title: NetCDF Object Names
last_updated: 2021-06-28
sidebar: nnug_sidebar
toc: false
permalink: netcdf_object_names.html
---

Dimensions, variables and attributes (and, in the netCDF enhanced data model, groups, user-defined types, compound members, and enumeration symbols) are all named netCDF objects.
NetCDF object names consist of an arbitrary sequence of UTF-8 encoded Unicode characters with several restrictions.

# Restrictions
The solidus (or slash) character '/' are not allowed in netCDF object names as it is reserved to indicate the group hierarchical path structure in fully qualified names.
Unicode control (Cc) characters are not allowed in netCDF object names.
Trailing space characters are also not permitted in netCDF object names.
Names commencing with underscore ("_") are reserved for library/implementation use.

Before versions 3.6.3 and 4.0 of the netCDF-C library (June 2008), netCDF object names were restricted to ASCII alphanumeric characters, underscore '_', period '.', plus '+', hyphen '-', or at sign '@', but beginning with an alphanumeric character or underscore.

Case is significant in netCDF names.

# Handling of NetCDF Object Names
All netCDF object names should be Unicode normalized to NFC normal form.
All netCDF libraries/implementations must ensure NFC normal form is used for name comparison operations.

# Length of NetCDF Object Names

[//]: # (TODO: Separate out the netCDF-C specific material.)
A zero-length name is not allowed.
The maximum length of a netCDF object name is library/implementation dependent.
For instance, the netCDF-C library does not allow names longer than `NC_MAX_NAME`.

# Conventions and NetCDF Object Names

Some widely used conventions restrict names to only alphanumeric characters or underscores.
> Note that, when using the DAP2 protocol to access netCDF data, there are \em reserved keywords, the use of which may result in undefined behavior.  See \ref dap2_reserved_keywords for more information.
