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
[//]: # (      - Unidata's Common Data Model - CDM - https://docs.unidata.ucar.edu/netcdf-java/current/userguide/common_data_model_overview.html)
[//]: # (      - CDM NetCDF Mapping - https://docs.unidata.ucar.edu/netcdf-java/current/userguide/cdm_netcdf_mapping.html)

[//]: # (TODO: Decide where should we put Limitations of NetCDF section in NUG/netcdf_introduction.md)
[//]: # (      - Is the `2 GiBytes size limit` in CDF-1 part of the data model?)
[//]: # (      - Is the `only one unlimited dimension` limitation in CDF-1, 2, and -5 part of the data model?)

NetCDF has two main data models:
* The **[netCDF Classic Data Model](classic_data_model.html)**, which represents a dataset with named variables, dimensions, and attributes.
* The **[netCDF Enhanced Data Model](enhanced_data_model.html)**, which adds hierarchical structure, string and unsigned integer types, and user-defined types.

Further details are available on
* the [netCDF Objects](netcdf_objects.html) (variable, attributes, dimensions, and groups)
* the [netCDF Data Types](netcdf_data_types.html) (including byte, int, float, and user-defined types)
* the [rules for naming netCDF Objects](netcdf_object_names.html) 

Some file formats and encodings support subsets of the Enhanced Data Model or minor extensions to the Classic Data Model.
For example, CDF-5 extends the Classic Data Model with the addition of support for unsigned integer and 64-bit integer data types.
Whereas ncZarr supports a subset of the Enhanced Data Model that does not include support for strings, user-defined types, or VLEN dimensions.

Further details on the various formats and encodings and the data model they support are available in the [netCDF File Formats section](file_formats.md).

[//]: # (TODO: Should we use CDL, C, Fortran, Java to clarify some aspects of the data model?)
[//]: # (      E.g., dimension order, data types, etc. If so we should mention something about)
[//]: # (      them here, with pointers to full descriptions. Or add more details, and pointers, )
[//]: # (      to CDL and libraries in index.md and netcdf_overview.md.)
[//]: # (TODO: Do we explain the difference between Enhanced DM and CDM?)

