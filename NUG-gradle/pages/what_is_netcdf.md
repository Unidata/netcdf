---
title: What is NetCDF?
last_updated: 2021-06-28
sidebar: nnug_sidebar
toc: false
permalink: what_is_netcdf.html
---

NetCDF (Network Common Data Form) is a set of data models, machine-independent data formats, and software libraries that support the creation, access, and sharing of array-oriented scientific data.
NetCDF is a widely used community standard for sharing scientific data with a mature ecosystem of FOSS and commercial [software tools](https://www.unidata.ucar.edu/software/netcdf/software.html) that can explore, manipulate, analyze, and visualize netCDF datasets. 

<!-- NOTE:
Text from NUG/netcdf_data_set_components.md#data_model, paragraph 1
-->
A netCDF dataset contains named dimensions, variables, and attributes.
These components (and a few others that will be described later) can be used together to capture the meaning of data and relations among data fields in an array-oriented dataset.

## NetCDF Goals and Design Principles

<!-- NOTE:
Text from [netCDF home page](https://www.unidata.ucar.edu/software/netcdf/) and NUG/guide.md#netcdf_purpose.
[Consider including text from NUG/netcdf_introduction.md#performance ???]
-->
Data in netCDF format is:

* **Self-Describing**: A netCDF dataset includes information defining the data it contains.
* **Portable**: The data in a netCDF dataset can be accessed by computers with different ways of storing integers, characters, and floating-point numbers.
* **Scalable**: Small subsets of large datasets in various formats may be accessed efficiently through netCDF interfaces, even from remote servers.
* **Appendable**: Data may be appended to a properly structured netCDF file without copying the dataset or redefining its structure.
* **Sharable**: One writer and multiple readers may simultaneously access the same netCDF file.
* **Archivable**: Access to all earlier forms of netCDF data will be supported by current and future versions of the netCDF-C and netCDF-Java software.
