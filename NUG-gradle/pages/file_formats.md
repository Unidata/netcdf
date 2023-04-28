---
title: NetCDF File Formats, Encodings, and Mappings
last_updated: 2021-04-23
sidebar: nnug_sidebar
toc: false
permalink: file_formats.html
---

NetCDF has two main file formats:
* the `netCDF-3` file format which supports the netCDF Classic Data Model and
* the `netCDF-4` file format which supports the netCDF Enhanced Data model.

The `netCDF-3` file format has two variants for a total of four file formats:
* `CDF-1` (aka `netCDF-3` or `classic`) - supports the netCDF Classic Data Model.
* `CDF-2` (aka `64-bit Offset`) - a variant of `CDF-1` that removes some size limitations on variables and datasets, it supports the netCDF Classic Data Model.
* `CDF-5` (aka `64-bit Data` or pnetcdf) - a variant of `CDF-2` which adds support for 64-bit integer and unsigned integer data types.
  It supports the netCDF Classic Data Model plus the additional integer types.
* `netCDF-4` - supports the netCDF Enhanced Data Model.
  It is written using the HDF5 file format.
  It is restricted to a subset of HDF5 while also including additional metadata to represent aspects of the netCDF Enhanced Data Model that do not have a corresponding concept in the HDF5 data model.

For a detailed description and discussion of the `CDF-1`, `CDF-2`, and `CDF-5` file formats, see the ["NetCDF-3 File Formats" page](nc3_file_formats.html).
For a detailed description and discussion of the `netCDF-4` file format, see the  ["NetCDF-4 File Format" page](nc4_file_format.html).

# Experimental Support for Zarr and NCZarr

Both the netCDF-C and netCDF-Java libaries provide experimental support for the `Zarr` and `NCZarr` format.
(The netCDF-Java library is currently limited to read-only access.)
* The `Zarr` format maps to the netCDF Enhanced Data Model with a few limitations.
  The limitations are due to differences between the Zarr and netCDF data models.
* `NCZarr` datasets are `Zarr` datasets with a metadata convention to capture netCDF data model information not supported by the Zarr data model.

For a detailed description and discussion of netCDF support for `Zarr` and `NCZarr`, see the  ["NCZarr" page](nczarr.html).

# Read-Only Support for OPeNDAP 

NetCDF supports read-only access to the OPeNDAP Data Access Protocols (`DAP-2` and `DAP-4`).
Both `DAP-2` and `DAP-4` are mapped to the netCDF Enhanced Data Model.

For a detailed description and discussion of netCDF support for OPeNDAP, see the ["OPeNDAP" page](dap.html).

# Other File Formats Supported Read-Only by NetCDF-C and -Java Libraries

The netCDF-C library supports HDF-4 SD format files and ... For a full list see the [netCDF-C File Format Support page]().

The netCDF-Java library supports HDF-4, GRIB, BUFR ... For a full list see the [CDM File Types page](https://docs.unidata.ucar.edu/netcdf-java/current/userguide/file_types.html).

