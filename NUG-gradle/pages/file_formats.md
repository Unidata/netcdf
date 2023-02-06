---
title: NetCDF File Formats, Encodings, and Mappings
last_updated: 2021-04-23
sidebar: nnug_sidebar
toc: false
permalink: file_formats.html
---

NetCDF has two main file formats:
the `netCDF-3` file format which supports the netCDF Classic Data Model
and the `netCDF-4` file format which supports the netCDF Enhanced Data model.
Along with the `netCDF-3` (aka `CDF-1`) file format, there are also two variants, `CDF-2` and `CDF-5`.

The four file formats are:
* `CDF-1` (aka `netCDF-3` or `classic`) - supports the netCDF Classic Data Model.
* `CDF-2` (aka `64-bit Offset`) - a variant of `CDF-1` that removes some size limitations on variables and datasets, it supports the netCDF Classic Data Model.
* `CDF-5` (aka `64-bit Data` or pnetcdf) - a variant of `CDF-2` which adds support for 64-bit integer and unsigned integer data types.
   It supports the netCDF Classic Data Model plus the additional integer types.
* `netCDF-4` - supports the netCDF Enhanced Data Model. 
   It is written using the HDF5 file format.
   It is restricted to a subset of HDF5 while also including additional metadata to represent aspects of the netCDF Enhanced Data Model that do not have a corresponding concept in the HDF5 data model.

See the ["NetCDF-3 File Formats" page](nc3_file_formats.html) for a detailed description and discussion of the `CDF-1`, `CDF-2` and `CDF-5` file formats.

See the ["NetCDF-4 File Format" page](nc4_file_format.html) for a detailed description and discussion of the `netCDF-4` file format.

Both the netCDF-C and netCDF-Java libraries support the Zarr format (and the NCZarr extension). For more details on Zarr/NCZarr support, see the ["NCZarr" page](nczarr.html).

The OPeNDAP Data Access Protocols (DAP-2 and DAP-4) are supported for read-only by both the netCDF-C and netCDF-Java libraries. For details on DAP support, see the ["OPeNDAP" page](dap.html).

### Other File Formats Supported Read-Only by NetCDF-C and -Java Libraries

The netCDF-C library supports HDF-4 SD format files and ... For a full list see the [netCDF-C File Format Support page]().

The netCDF-Java library supports HDF-4, GRIB, BUFR ... For a full list see the [CDM File Types page](https://docs.unidata.ucar.edu/netcdf-java/current/userguide/file_types.html).

