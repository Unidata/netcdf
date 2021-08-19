# Application for registration of Media Types

## Type name:
application

## Subtype name:
netcdf -- Standards tree (no prefix)

## Required parameters:
N/A

## Optional parameters:
N/A

Or include a "version" parameter?
- Supported values: "3" and "4"
- Or supported values: "CDF-1", "CDF-2", "CDF-5", and "netCDF-4"

## Encoding considerations:
binary

## Security considerations:
(See https://www.rfc-editor.org/rfc/rfc6838.html#section-4.6)

  - no provision for directives that institute actions on a recipient's files or other resources
    - Need to review HDF5-UDF: https://hdf5-udf.readthedocs.io/en/latest/
      - HDF5-UDF is an application (?) that supports writing and reading executable code in HDF5 files. Not a function of the file format. So perhaps mention of it is not needed here.
  - no provision for directives that lead to disclosure of information
  - highlight opportunity for use of compression, highlight that the application layer: netCDF handles decompression on data access
  - no need for services for security assurance

## Interoperability considerations:

There are two main versions of the netCDF file format, netCDF-3 and netCDF-4, with three minor versions of netCDF-3 and several minor versions(?) of netCDF-4. Current implementations (C and Java libraries) support (automatically identify and handle) all variations.

Byte ordering is always big endian in netCDF-3 data files. In netCDF-4/HDF5 files, the byte ordering is determined at write time (usually the native form of the writing machine) and stored in the data file.

(Details on byte order are given in the specifications. So perhaps not needed here.)

## Published specification:
(See https://www.rfc-editor.org/rfc/rfc6838.html#section-4.10)

The netCDF file formats are specified in the NetCDF User's Guide (NUG). See the NetCDF File Formats section.

Both NASA and the Open Geospatial Consortium (OGC) have approved/accepted the netCDF standard. See
- the NASA netCDF Classic page: https://earthdata.nasa.gov/esdis/eso/standards-and-references/netcdf-classic
- the NASA netCDF-4 page: https://earthdata.nasa.gov/esdis/eso/standards-and-references/netcdf-4hdf5-file-format
- the OGC netCDF standards page: https://www.ogc.org/standards/netcdf

Used by CMIP - reference?
NOAA use for GOES data, reference?
Also WMO efforts?

Other references:
- G. Davis and R. Rew, "Data Management: NetCDF: an Interface for Scientific Data Access" in IEEE Computer Graphics and Applications, vol. 10, no. 04, pp. 76-82, 1990. https://doi.org/10.1109/38.56302

## Applications that use this media type:
- scientific data sharing
- multidimensional arrays of numerical data

## Fragment identifier considerations:
  - no fragment identifier considerations

## Additional information:
- Deprecated alias names for this type:
  - application/x-netcdf
- Magic number(s):
  - ‘C’, ‘D’, ‘F’, ‘\x01’ at start of file - classic format (CDF-1)
  - ‘C’, ‘D’, ‘F’, ‘\x02’ at start of file - 64-bit offset format (CDF-2)
  - ‘C’, ‘D’, ‘F’, ‘\x05’ at start of file - 64-bit data format (CDF-5)
  - ‘\211’, ‘H’, ‘D’, ‘F’, ‘\r’, ‘\n’, ‘\032’, ‘\n’
    at start of file (?) - netCDF-4/HDF5
- File extension(s): .nc, .cdf, .nc3, .nc4
- Macintosh file type code(s):

## Person & email address to contact for further information:

## Intended usage:
Common

## Restrictions on usage:
no restrictions on usage

## Author:

## Change controller:

## Provisional registration? (standards tree only):
(See https://www.rfc-editor.org/rfc/rfc6838.html#section-5.2.1)

NOTE: Given registration/standardization might take time, a request can be made to place a media type name on a provisional registry so it can be used for testing and development. See provisional registry: https://www.iana.org/assignments/provisional-standard-media-types/provisional-standard-media-types.xhtml
  
## Other Information or Comments
(Any other information that the author deems interesting may be added below this line.)
