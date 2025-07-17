---
title: NetCDF-3 File Format Specifications
last_updated: 2021-04-23
sidebar: nnug_sidebar
toc: false
permalink: nc3_file_formats.html
---

This page will describe all three variants of the `netCDF-3` file format:
* `CDF-1` (aka `netCDF-3` or `classic`) which supports the netCDF Classic Data Model.
* `CDF-2` (aka `64-bit Offset`) which removes some variable and dataset size limitations of the `CDF-1` format.
* `CDF-5` (aka `64-bit Data` or 'pnetcdf') which extends `CDF-2` with support for 64-bit integer and unsigned integer data types.

## BNF Grammar

To present the format more formally, we use a BNF grammar notation.
In this notation:

- Non-terminals (entities defined by grammar rules) are in lower case.
- Terminals (atomic entities in terms of which the format
  specification is written) are in upper case, and are specified
  literally as US-ASCII characters within single-quote characters or are
  described with text between angle brackets (‘\<’ and ‘\>’).
- Optional entities are enclosed between braces (‘[’ and ‘]’).
- A sequence of zero or more occurrences of an entity is denoted by
  ‘[entity ...]’.
- A vertical line character (‘|’) separates alternatives. Alternation
  has lower precedence than concatenation.
- Comments follow ‘//’ characters.
- A single byte that is not a printable character is denoted using a
  hexadecimal number with the notation ‘\\xDD’, where each D is a
  hexadecimal digit.
- A literal single-quote character is denoted by ‘\'’, and a literal
  back-slash character is denoted by ‘\\’.

Following the grammar, a few additional notes are included to specify format characteristics that are impractical to capture in a BNF
grammar, and to note some special cases for implementers.
Comments in the grammar point to the notes and special cases, and help to clarify the intent of elements of the format.

## The Format in Detail {#format_in_detail}

````
     netcdf_file  = header  data
     header       = magic  numrecs  dim_list  gatt_list  var_list
     magic        = 'C'  'D'  'F'  VERSION
     VERSION      = \x01 |                          // classic format
                    \x02 |                          // 64-bit offset format
                    \x05                            // 64-bit data format (CDF-5)
     numrecs      = NON_NEG | STREAMING             // length of record dimension
     dim_list     = ABSENT | NC_DIMENSION  nelems  [dim ...]
     gatt_list    = att_list                        // global attributes
     att_list     = ABSENT | NC_ATTRIBUTE  nelems  [attr ...]
     var_list     = ABSENT | NC_VARIABLE   nelems  [var ...]
     ABSENT       = ZERO  ZERO |                    // list is not present (CDF-1 and CDF-2)
                    ZERO  ZERO64                    // list is not present (CDF-5)
     ZERO         = \x00 \x00 \x00 \x00                      // 32-bit zero (CDF-1 and CDF-2)
     ZERO64       = \x00 \x00 \x00 \x00 \x00 \x00 \x00 \x00  // 64-bit zero (CDF-5)
     NC_DIMENSION = \x00 \x00 \x00 \x0A             // tag for list of dimensions
     NC_VARIABLE  = \x00 \x00 \x00 \x0B             // tag for list of variables
     NC_ATTRIBUTE = \x00 \x00 \x00 \x0C             // tag for list of attributes
     nelems       = NON_NEG                         // number of elements in following sequence
     dim          = name  dim_length
     name         = nelems  namestring              // Names a dimension, variable, or attribute.
                                                    // Names should match the regular expression
                                                    // ([a-zA-Z0-9_]|{MUTF8})([^\x00-\x1F/\x7F-\xFF]|{MUTF8})*
                                                    // For other constraints, see "Note on names", below.
     namestring   = ID1 [IDN ...] padding
     ID1          = alphanumeric | '_'
     IDN          = alphanumeric | special1 | special2
     alphanumeric = lowercase | uppercase | numeric | MUTF8
     lowercase    = 'a'|'b'|'c'|'d'|'e'|'f'|'g'|'h'|'i'|'j'|'k'|'l'|'m'|
                    'n'|'o'|'p'|'q'|'r'|'s'|'t'|'u'|'v'|'w'|'x'|'y'|'z'
     uppercase    = 'A'|'B'|'C'|'D'|'E'|'F'|'G'|'H'|'I'|'J'|'K'|'L'|'M'|
                    'N'|'O'|'P'|'Q'|'R'|'S'|'T'|'U'|'V'|'W'|'X'|'Y'|'Z'
     numeric      = '0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'
                                  // special1 chars have traditionally been
                                  // permitted in netCDF names.
     special1     = '_'|'.'|'@'|'+'|'-'
                                  // special2 chars are recently permitted in
                                  // names (and require escaping in CDL).
                                  // Note: '/' is not permitted.
     special2     = ' ' | '!' | '"' | '#'  | '$' | '\%' | '&' | '\'' |
                    '(' | ')' | '*' | ','  | ':' | ';' | '<' | '='  |
                    '>' | '?' | '[' | '\\' | ']' | '^' | '`' | '{'  |
                    '|' | '}' | '~'
     MUTF8        = <multibyte UTF-8 encoded, NFC-normalized Unicode character>
     dim_length   = NON_NEG       // If zero, this is the record dimension.
                                  // There can be at most one record dimension.
     attr         = name  nc_type  nelems  [values ...]
     nc_type      = NC_BYTE   |
                    NC_CHAR   |
                    NC_SHORT  |
                    NC_INT    |
                    NC_FLOAT  |
                    NC_DOUBLE |
                    NC_UBYTE  |             // (CDF-5)
                    NC_USHORT |             // (CDF-5)
                    NC_UINT   |             // (CDF-5)
                    NC_INT64  |             // (CDF-5)
                    NC_UINT64               // (CDF-5)
     var          = name  nelems  [dimid ...]  vatt_list  nc_type  vsize  begin
                                  // nelems is the dimensionality (rank) of the
                                  // variable: 0 for scalar, 1 for vector, 2
                                  // for matrix, ...
     dimid        = NON_NEG       // Dimension ID (index into dim_list) for
                                  // variable shape.  We say this is a "record
                                  // variable" if and only if the first
                                  // dimension is the record dimension.
     vatt_list    = att_list      // Variable-specific attributes
     vsize        = NON_NEG       // Variable size.  If not a record variable,
                                  // the amount of space in bytes allocated to
                                  // the variable's data.  If a record variable,
                                  // the amount of space per record.  See "Note
                                  // on vsize", below.
     begin        = OFFSET        // Variable start location.  The offset in
                                  // bytes (seek index) in the file of the
                                  // beginning of data for this variable.
     data         = non_recs  recs
     non_recs     = [vardata ...] // The data for all non-record variables,
                                  // stored contiguously for each variable, in
                                  // the same order the variables occur in the
                                  // header.
     vardata      = [values ...]  // All data for a non-record variable, as a
                                  // block of values of the same type as the
                                  // variable, in row-major order (last
                                  // dimension varying fastest).
     recs         = [record ...]  // The data for all record variables are
                                  // stored interleaved at the end of the
                                  // file.
     record       = [varslab ...] // Each record consists of the n-th slab
                                  // from each record variable, for example
                                  // x[n,...], y[n,...], z[n,...] where the
                                  // first index is the record number, which
                                  // is the unlimited dimension index.
     varslab      = [values ...]  // One record of data for a variable, a
                                  // block of values all of the same type as
                                  // the variable in row-major order (last
                                  // index varying fastest).
     values       = bytes | chars | shorts | ints | floats | doubles |  // for CDF-1, CDF-2, and CDF-5
                    ubytes | ushorts | uints | int64s | uint64s           // for CDF-5 only (or is int64s for CDF-2 as well?)
     string       = nelems  [chars]
     bytes        = [BYTE ...]  padding
     chars        = [CHAR ...]  padding
     shorts       = [SHORT ...]  padding
     ints         = [INT ...]
     floats       = [FLOAT ...]
     doubles      = [DOUBLE ...]
     ubytes      = [UBYTE ...] padding    // for CDF-5 format
     ushorts      = [USHORT ...] padding  // for CDF-5 format
     uints        = [UINT ...]            // for CDF-5 format
     int64s       = [INT64 ...]           // for CDF-5 format
     uint64s      = [UINT64 ...]          // for CDF-5 format
     padding      = <0, 1, 2, or 3 bytes to next 4-byte boundary>
                                  // Header padding uses null (\\x00) bytes.  In
                                  // data, padding uses variable's fill value.
                                  // See "Note on padding", below, for a special
                                  // case.
     NON_NEG      = <non-negative INT> |                     // for CDF-1 and CDF-2 formats
                    <non-negative INT64>                     // for 64-bit data format (CDF-5)
     STREAMING    = \xFF \xFF \xFF \xFF |                    // for CDF-1 and CDF-2 formats
                    \xFF \xFF \xFF \xFF \xFF \xFF \xFF \xFF  // for CDF-5 format
                                                             // Indicates indeterminate record
                                                             // count, allows streaming data
     OFFSET       = <non-negative INT> |    // For classic format (CDF-1) or
                    <non-negative INT64>    // for CDF-2 and CDF-5 formats
     BYTE         = <8-bit byte>            // See "Note on byte data", below.
     CHAR         = <8-bit byte>            // See "Note on char data", below.
     SHORT        = <16-bit signed integer, Bigendian, two's complement>
     INT          = <32-bit signed integer, Bigendian, two's complement>
     INT64        = <64-bit signed integer, Bigendian, two's complement>
     FLOAT        = <32-bit IEEE single-precision float, Bigendian>
     DOUBLE       = <64-bit IEEE double-precision float, Bigendian>
     UBYTE        = <8-bit unsigned byte>                                   // for CDF-5 format
     USHORT       = <16-bit unsigned integer, Bigendian, two's complement>  // for CDF-5 format
     UINT         = <32-bit unsigned integer, Bigendian, two's complement>  // for CDF-5 format
     INT64        = <64-bit signed integer, Bigendian, two's complement>    // for CDF-2 and CDF-5 formats
     UINT64       = <64-bit unsigned integer, Bigendian, two's complement>  // for CDF-5 format

                                              // following type tags are 32-bit integers
     NC_BYTE      = \x00 \x00 \x00 \x01       // 8-bit signed integers
     NC_CHAR      = \x00 \x00 \x00 \x02       // text characters
     NC_SHORT     = \x00 \x00 \x00 \x03       // 16-bit signed integers
     NC_INT       = \x00 \x00 \x00 \x04       // 32-bit signed integers
     NC_FLOAT     = \x00 \x00 \x00 \x05       // IEEE single precision floats
     NC_DOUBLE    = \x00 \x00 \x00 \x06       // IEEE double precision floats
     NC_UBYTE     = \x00 \x00 \x00 \x07       // unsigned 1 byte integer (CDF-5)
     NC_USHORT    = \x00 \x00 \x00 \x08       // unsigned 2-byte integer (CDF-5)
     NC_UINT      = \x00 \x00 \x00 \x09       // unsigned 4-byte integer (CDF-5)
     NC_INT64     = \x00 \x00 \x00 \x0A       // signed 8-byte integer (CDF-5)
     NC_UINT64    = \x00 \x00 \x00 \x0B       // unsigned 8-byte integer (CDF-5)
                                  // Default fill values for each type, may be
                                  // overridden by variable attribute named
                                  // '_FillValue'. See "Note on fill values",
                                  // below.
                                  
     FILL_CHAR    = \x00                      // null byte
     FILL_BYTE    = \x81                      // (signed char) -127
     FILL_SHORT   = \x80 \x01                 // (short) -32767
     FILL_INT     = \x80 \x00 \x00 \x01       // (int) -2147483647
     FILL_FLOAT   = \x7C \xF0 \x00 \x00       // (float) 9.9692099683868690e+36
     FILL_DOUBLE  = \x47 \x9E \x00 \x00 \x00 \x00 \x00 \x00  // (double) 9.9692099683868690e+36
     FILL_UBYTE   = \xFF                                     // (ubyte) 255        -- for CDF-5 format
     FILL_USHORT  = \xFF \xFF                                // (ushort) 65535     -- for CDF-5 format
     FILL_UINT    = \xFF \xFF \xFF \xFF                      // (uint) 4294967295U -- for CDF-5 format
     FILL_INT64   = \x80 \x00 \x00 \x00 \x00 \x00 \x00 \x02  // (long long) -9223372036854775806LL  -- for CDF-5 format
     FILL_UINT64  = \xFF \xFF \xFF \xFF \xFF \xFF \xFF \xFE  // (unsigned long long) 18446744073709551614ULL -- for CDF-5 format
````

## Differences between CDF-1, CDF-2, and CDF-5

CDF-2, the 64-bit offset format, differs from the classic format only in the VERSION byte, ‘\x02’ instead of ‘\x01’, and the OFFSET entity, a 64-bit instead of a 32-bit offset from the beginning of the file.
This small format change permits much larger files, but there are still some practical size restrictions.
Each fixed-size variable and the data for one record's worth of each record variable are still limited in size to a little less that 4 GiB.
The rationale for this limitation is to permit aggregate access to all the data in a netCDF variable (or a record's worth of data) on 32-bit platforms.

CDF-5, the 64-bit data format, differs from the CDF-2 format in that
* the magic number version byte (`VERSION`) byte is ‘\x05’ instead of ‘\x02’
* it adds support for five new integer data types (a 64-bit integer and four unsigned integer data types)
* the following are non-negative 64-bit integers instead of non-negative 32-bit integers (`NON_NEG`):
  * the size of a dimension (`dim_length` and `numrecs`)
  * the number of elements (`nelems`) in a list of dimensions, variables, or attributes
  * the number of bytes (`nelems`) in the name of a dimension, variable, or attribute
  * the size (bytes) of a variable (`vsize`), see "Note on vsize" below
  * the offset (`OFFSET`) to the start (`begin`) of a variable
  * a dimension ID (`dimid`)
* empty lists (`ABSENT`) are represented as a 32-bit integer zero followed by a 64-bit integer zero instead of two 32-bit integer zeros

## Notes

**Note on vsize**: This number is the product of the dimension lengths (omitting the record dimension) and the number of bytes per value (determined from the type), increased to the next multiple of 4, for each variable.
If a record variable, this is the amount of space per record (except that, for backward compatibility, it always includes padding to the next multiple of 4 bytes, even in the exceptional case noted below under “Note on padding”).
The netCDF “record size” is calculated as the sum of the vsize's of all the record variables.

The vsize field is actually redundant, because its value may be computed from other information in the header.
The 32-bit vsize field is not large enough to contain the size of variables that require more than 2^32 - 4 bytes, so 2^32 - 1 is used in the vsize field for such variables.

**Note on names**: Earlier versions of the netCDF C-library reference implementation enforced a more restricted set of characters in creating new names, but permitted reading names containing arbitrary bytes.
This specification extends the permitted characters in names to include multi-byte UTF-8 encoded Unicode and additional printing characters from the US-ASCII alphabet.
The first character of a name must be alphanumeric, a multi-byte UTF-8 character, or '_' (reserved for special names with meaning to implementations, such as the “_FillValue” attribute). Subsequent characters may also include printing special characters, except for '/' which is not allowed in names.
Names that have trailing space characters are also not permitted.

Implementations of the netCDF classic and 64-bit offset format must ensure that names are normalized according to Unicode NFC normalization rules during encoding as UTF-8 for storing in the file header.
This is necessary to ensure that gratuitous differences in the representation of Unicode names do not cause anomalies in comparing files and querying data objects by name.

**Note on streaming data**: The largest possible record count, 2^32 - 1, is reserved to indicate an indeterminate number of records. This means that the number of records in the file must be determined by other means, such as reading them or computing the current number of records from the file length and other information in the header. It also means that the numrecs field in the header will not be updated as records are added to the file. [This feature is not yet implemented].

**Note on padding**: In the special case when there is only one record variable and it is of type character, byte, or short, no padding is
used between record slabs, so records after the first record do not necessarily start on four-byte boundaries.
However, as noted above under “Note on vsize”, the vsize field is computed to include padding to the next multiple of 4 bytes.
In this case, readers should ignore vsize and assume no padding. Writers should store vsize as if padding were included.

**Note on byte data**: It is possible to interpret byte data as either signed (-128 to 127) or unsigned (0 to 255). When reading byte data through an interface that converts it into another numeric type, the default interpretation is signed. There are various attribute conventions for specifying whether bytes represent signed or unsigned data, but no standard convention has been established. The variable attribute “_Unsigned” is reserved for this purpose in future implementations.

**Note on char data**: Although the characters used in netCDF names must be encoded as UTF-8, character data may use other encodings.
The variable attribute “_Encoding” is reserved for this purpose in future implementations.

**Note on fill values**: Because data variables may be created before their values are written, and because values need not be written sequentially in a netCDF file, default “fill values” are defined for each type, for initializing data values before they are explicitly written.
This makes it possible to detect reading values that were never written.
The variable attribute “_FillValue”, if present, overrides the default fill value for a variable. If _FillValue is defined then it should be scalar and of the same type as the variable.

Fill values are not required, however, because netCDF libraries have traditionally supported a “no fill” mode when writing, omitting the initialization of variable values with fill values.
This makes the creation of large files faster, but also eliminates the possibility of detecting the inadvertent reading of values that haven't been written.

# Notes on Computing File Offsets {#computing_offsets}

The offset (position within the file) of a specified data value in a CDF-1, CDF-2, or CDF-5 data file is completely determined by the variable start location (the offset in the begin field), the external type of the variable (the nc_type field), and the dimension indices (one for each of the variable's dimensions) of the value desired.

The external size in bytes of one data value for each possible netCDF type, denoted `extsize` below, is:

- NC_BYTE 1
- NC_CHAR 1
- NC_SHORT 2
- NC_INT 4
- NC_FLOAT 4
- NC_DOUBLE 8
- NC_UBYTE	1     // For CDF-5
- NC_USHORT	2
- NC_UINT	4
- NC_INT64	8
- NC_UINT64	8

The record size, denoted by `recsize` below, is the sum of the `vsize` fields of record variables (variables that use the unlimited dimension), using the actual value determined by dimension sizes and variable type in case the `vsize` field is too small for the variable size.

To compute the offset of a value relative to the beginning of a variable, it is helpful to precompute a “product vector” from the dimension lengths.
From the products of the dimension lengths for the variable from right to left, skipping the leftmost (record) dimension for record variables, and storing the results as the product vector for each variable.

For example:

````
Non-record variable:

  dimension lengths: [ 5 3 2 7]
  product vector:    [210 42 14 7]

Record variable:

  dimension lengths: [0 2 9 4]
  product vector:    [0 72 36 4]
````

At this point, the leftmost product, when rounded up to the next multiple of 4, is the variable size, `vsize`, in the grammar above.
For example, in the non-record variable above, the value of the `vsize` field is 212 (210 rounded up to a multiple of 4).
For the record variable, the value of `vsize` is just 72, since this is already a multiple of 4.

Let 'coord' be the array of coordinates (dimension indices, zero-based) of the desired data value.
Then the offset of the value from the beginning of the file is just the file offset of the first data value of the desired variable (its `begin` field) added to the inner product of the coord and product vectors times the size, in bytes, of each datum for the variable.
Finally, if the variable is a record variable, the product of the record number, 'coord[0]', and the record size, `recsize`, is added to yield the final offset value.

A special case: Where there is exactly one record variable, we drop the requirement that each record be four-byte aligned, so in this case there is no record padding.

## Examples {#offset_examples}

By using the grammar above, we can derive the smallest valid netCDF file, having no dimensions, no variables, no attributes, and hence, no data.
A CDL representation of the empty netCDF file is

```
netcdf empty { }
```

The `ncgen` utility (which comes with the netCDF-C library) can be used to create a netCDF binary file in CDF-1 format as follows:

```
ncgen -1 -o empty_CDF-1.nc
```

For a CDF-2 file, use

```
ncgen -6 -o empty_CDF-2.nc
```

For a CDF-5 file, use

```
ncgen -5 -o empty_CDF-5.nc
```

The size of the empty CDF-1 and CDF-2 files are 32 bytes.
If the file is in CDF-5 format the size is 48 bytes.
The files begins with a four-byte "magic number" that identifies it as a netCDF version 1, 2, or 5 file.
The "magic number" consists of ‘C’, ‘D’, ‘F’, and a byte representing the version (‘\x01’, ‘\x02’, or ‘\x05’).
For a CDF-1 and CDF-2 file, seven 32-bit integer zeros follow the magic number and represent the number of records (zero), an empty list of dimensions, an empty list of global attributes, and an empty list of variables.
The layout for CDF-5 is similar but the number of records is given by a 64-bit integer zero and each empty list is represented by a 32-bit integer zero followed by a 64-bit integer zero. 

Below is an (edited) dump of the file produced using the Unix command

```
od -c empty.nc
```

Each 16-byte portion of the file is displayed with 2 lines.
The first line displays the bytes as characters.
The second line (added by human) presents the interpretation of the bytes in terms of netCDF components and values.

Here it is for a CDF-1 file (a CDF-2 file would be the same except for the version of the magic number):
```
0000000    C   D   F 001  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0
          [magic number]  [ 0 records  ]  [  0 dims (ABSENT=ZERO ZERO) ]
          
0000020   \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0
          [  0 global atts  (ABSENT)   ]  [  0 variables    (ABSENT)   ]
0000040
```

Here is the dump for a CDF-5 file:
```
0000000    C   D   F 005  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0
          [magic number]  [    0 records               ]  [ 0 dimensions ...
           
0000020   \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0
      ...   (ABSENT=ZERO ZERO64)       ]  [ 0 global atts (ABSENT=ZERO   ...
      
0000040   \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0
      ...   ZERO64)    ]  [ 0 variables (ABSENT=ZERO ZERO64)           ]
0000060

```

As a less trivial example, consider the CDL

````
     netcdf tiny {
     dimensions:
             dim = 5;
     variables:
             short vx(dim);
     data:
             vx = 3, 1, 4, 1, 5 ;
     }
````

The dump of the tiny dataset written as a CDF-1 file (92 bytes):
```
0000000    C   D   F 001  \0  \0  \0  \0  \0  \0  \0  \n  \0  \0  \0 001
          [magic number]  [ numrecs = 0]  [NC_DIMENSION]  [ 1 dimension]
0000020   \0  \0  \0 003   d   i   m  \0  \0  \0  \0 005  \0  \0  \0  \0
          [3 characters]  [ name="dim" ]  [dim_length=5]  [0 global atts ...
0000040   \0  \0  \0  \0  \0  \0  \0  \v  \0  \0  \0 001  \0  \0  \0 002
      ...   ABSENT     ]  [NC_VARIABLE ]  [ 1 variable ]  [  2 char      ...
0000060    v   x  \0  \0  \0  \0  \0 001  \0  \0  \0  \0  \0  \0  \0  \0
      ... name = "vx"  ]  [1 dimension ]  [ with ID 0  ]  [ 0 attributes ...
0000100   \0  \0  \0  \0  \0  \0  \0 003  \0  \0  \0  \f  \0  \0  \0   P
      ...   ABSENT     ]  [type NC_SHORT] [size 12 bytes] [ offset: 80 ]
0000120   \0 003  \0 001  \0 004  \0 001  \0 005 200 001  
          [  3 ]  [  1 ]  [  4 ]  [  1 ]  [  5 ] [fill ]              
0000134
```
Note: offset to values of "vx" var is 80 (size of offset value is four bytes)

written as a CDF-2 file (96 bytes):
```
0000000    C   D   F 002  \0  \0  \0  \0  \0  \0  \0  \n  \0  \0  \0 001
          [magic number]  [ numrecs = 0]  [NC_DIMENSION]  [ 1 dimension]
0000020   \0  \0  \0 003   d   i   m  \0  \0  \0  \0 005  \0  \0  \0  \0
          [3 characters]  [ name="dim" ]  [dim_length=5]  [0 global atts ...
0000040   \0  \0  \0  \0  \0  \0  \0  \v  \0  \0  \0 001  \0  \0  \0 002
      ...   ABSENT     ]  [NC_VARIABLE ]  [ 1 variable ]  [  2 char      ...
0000060    v   x  \0  \0  \0  \0  \0 001  \0  \0  \0  \0  \0  \0  \0  \0
      ... name = "vx"  ]  [1 dimension ]  [ with ID 0  ]  [ 0 attributes ...
0000100   \0  \0  \0  \0  \0  \0  \0 003  \0  \0  \0  \f  \0  \0  \0  \0
      ...   ABSENT     ]  [type NC_SHORT] [size 12 bytes] [  offset:     ...
0000120   \0  \0  \0   T  \0 003  \0 001  \0 004  \0 001  \0 005 200 001
      ...      84      ]  [  3 ]  [  1 ]  [  4 ]  [  1 ]  [  5 ] [fill ]
0000140
```
Note: offset to values of "vx" var is 84 (size of offset value is eight bytes) 

written as a CDF-5 file (140 bytes):
```
0000000    C   D   F 005  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \n
          [magic number]  [    numrecs = 0 records     ]  [NC_DIMENSION]
0000020   \0  \0  \0  \0  \0  \0  \0 001  \0  \0  \0  \0  \0  \0  \0 003
          [  nelems = 1 dimension      ]  [ dimension name has 3 chars ]
0000040    d   i   m  \0  \0  \0  \0  \0  \0  \0  \0 005  \0  \0  \0  \0
          [ name="dim" ]  [       dim_length = 5       ]  [ 0 global att ...
0000060   \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \v  \0  \0  \0  \0
      ...    (ABSENT = ZERO ZERO64)    ]  [NC_VARIABLE ]  [  nelems =    ... 
0000100   \0  \0  \0 001  \0  \0  \0  \0  \0  \0  \0 002   v   x  \0  \0
      ...  1 variable  ]  [ variable name has 2 chars  ]  [ name = "vx"]
0000120   \0  \0  \0  \0  \0  \0  \0 001  \0  \0  \0  \0  \0  \0  \0  \0
          [ var has 1 dimension        ]  [ first dimension has ID 0   ]
0000140   \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0  \0 003
          [ var has 0 atts (ABSENT = ZERO ZERO64)      ]  [type=NC_SHORT]
0000160   \0  \0  \0  \0  \0  \0  \0  \f  \0  \0  \0  \0  \0  \0  \0 200
          [ var size, vsize = 12 bytes ]  [var's file offset, begin=128]
0000200   \0 003  \0 001  \0 004  \0 001  \0 005 200 001                
0000214   [  3 ]  [  1 ]  [  4 ]  [  1 ]  [  5 ] [fill ]
```
