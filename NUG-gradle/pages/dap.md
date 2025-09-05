---
title: OPeNDAP - DAP-2 and DAP-4
last_updated: 2022-04-14
sidebar: nnug_sidebar
toc: false
permalink: dap.html
---

##  OPeNDAP Data Access Protocols (DAP2 and DAP4)

The OPeNDAP Data Access Protocol (DAP) was designed in the late 1990's to provide Web API-based access to remote scientific datasets over the web.
The DAP and netCDF data models are very similar, both in terms of being composed of `attributes` and n-D array `variables` and in terms of their support for requesting subsets of the data.
This similarity has lead to both the netCDF-C and netCDF-Java libraries supporting both DAP version 2 (DAP2) and DAP version 4 (DAP4).
Various mappings are required to ensure the differences in data models do not lead to loss of information ...

To access a DAP dataset through one of the netCDF libraries, instead of the path and name of a local file, a DAP dataset is identified by an HTTPS (or HTTP) URL.

