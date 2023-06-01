---
title: NetCDF Object Names
last_updated: 2021-06-28
sidebar: nnug_sidebar
toc: false
permalink: netcdf_object_names.html
---

# Character Encodings (UTF-8)
# Permitted Characters in NetCDF Names

The names of dimensions, variables and attributes (and, in netCDF-4 files, groups, user-defined types, compound member names, and enumeration symbols) consist of arbitrary sequences of alphanumeric characters, underscore '_', period '.', plus '+', hyphen '-', or at sign '@', but beginning with an alphanumeric character or underscore. However names commencing with underscore are reserved for system use.

Beginning with versions 3.6.3 and 4.0, names may also include UTF-8 encoded Unicode characters as well as other special characters, except for the character '/', which may not appear in a name.

Names that have trailing space characters are also not permitted.

Case is significant in netCDF names.

# Length of NetCDF Object Names

[//]: # (TODO: Separate out the netCDF-C specific material.)
A zero-length name is not allowed.

Names longer than ::NC_MAX_NAME will not be accepted any netCDF define function. An error of ::NC_EMAXNAME will be returned.

All netCDF inquiry functions will return names of maximum size ::NC_MAX_NAME for netCDF files. Since this does not include the terminating NULL, space should be reserved for NC_MAX_NAME + 1 characters.

# Conventions and NetCDF Object Names

Some widely used conventions restrict names to only alphanumeric characters or underscores.

> Note that, when using the DAP2 protocol to access netCDF data, there are \em reserved keywords, the use of which may result in undefined behavior.  See \ref dap2_reserved_keywords for more information.
