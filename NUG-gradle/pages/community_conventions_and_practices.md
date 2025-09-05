---
title: Community Conventions and Practices
last_updated: 2021-06-28
sidebar: nnug_sidebar
toc: false
permalink: community_conventions_and_practices.html
---

<!-- Text from NUG/netcdf_introduction.md -->
The use of netCDF is not sufficient to make data "self-describing" and meaningful to both humans and machines.
The names of variables and dimensions should be meaningful and conform to any relevant conventions.
Dimensions should have corresponding coordinate variables (See \ref coordinate_variables) where sensible.

Attributes play a vital role in providing ancillary information.
It is important to use all the relevant standard attributes using the relevant conventions.
For a description of reserved attributes (used by the netCDF library) and attribute conventions for generic application software, see \ref attribute_conventions.

A number of groups have defined their own additional conventions and styles for netCDF data.
Descriptions of these conventions, as well as examples incorporating them can be accessed from the netCDF Conventions site, https://www.unidata.ucar.edu/software/netcdf/conventions.html.

These conventions should be used where suitable.
Additional conventions are often needed for local use.
These should be contributed to the above netCDF conventions site if likely to interest other users in similar areas.

<!-- New text -->
Over the years, the netCDF community has developed conventions around how to describe data being stored in netCDF files.
Some of the most general conventions are encoded in the NUG.
While other conventions, more specific to particular scientific domains, have been developed by groups/communities focused on particular domains.

The NUG includes:
* "[Attribute Conventions](nug_conventions.html)"
* "[Best Practices](best_practices.html)"

The "[CF Metadata Conventions for NetCDF](https://cfconventions.org/)" are used widely across the geosciences.


The "[Other Conventions](other_conventions.html)" section discusses and provides references to other community conventions, e.g., CF and UGRID.

