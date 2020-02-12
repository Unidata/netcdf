# The NetCDF User's Guide

## The NetCDF User's Guide {#user_guide}

[TOC]

The project page for the NetCDF User's Guide can be found at [the NetCDF User's Guide Github Page](https://github.com/Unidata/netcdf).

## Table of Contents

- \subpage netcdf_introduction
- \subpage file_structure_and_performance
- \subpage data_type
- \subpage netcdf_data_set_components
- \subpage netcdf_perf_chunking
- \subpage netcdf_utilities_guide
- \subpage BestPractices
- \subpage user_defined_formats
- \subpage dap2
- \subpage dap4
- \subpage CDL
- \subpage attribute_conventions
- \subpage file_format_specifications


## The Purpose of NetCDF {#netcdf_purpose}

The purpose of the Network Common Data Form (netCDF) interface is to allow you to create, access, and share array-oriented data in a form that is self-describing and portable. "Self-describing" means that a dataset includes information defining the data it contains. "Portable" means that the data in a dataset is represented in a form that can be accessed by computers with different ways of storing integers, characters, and floating-point numbers. Using the netCDF interface for creating new datasets makes the data portable. Using the netCDF interface in software for data access, management, analysis, and display can make the software more generally useful.

The netCDF software includes C, Fortran 77, Fortran 90, and C++ interfaces for accessing netCDF data. These libraries are available for many common computing platforms.

The community of netCDF users has contributed ports of the software to additional platforms and interfaces for other programming languages as
well. Source code for netCDF software libraries is freely available to encourage the sharing of both array-oriented data and the software that makes the data useful.

This User's Guide presents the netCDF data model. It explains how the netCDF data model uses dimensions, variables, and attributes to store data.

Reference documentation for UNIX systems, in the form of UNIX 'man'
pages for the C and FORTRAN interfaces is also available at the netCDF
web site (http://www.unidata.ucar.edu/netcdf), and with the netCDF
distribution.

The latest version of this document, and the language specific guides,
can be found at the netCDF web site
(http://www.unidata.ucar.edu/netcdf/docs) along with extensive
additional information about netCDF, including pointers to other
software that works with netCDF data.

Separate documentation of the Java netCDF library can be found at
http://www.unidata.ucar.edu/software/netcdf-java/.




\page netcdf_perf_chunking Improving Performance with Chunking

\tableofcontents

\section chunk_cache The Chunk Cache

When data are first read or written to a netCDF-4/HDF5 variable, the
HDF5 library opens a cache for that variable. The default size of that
cache is 16 MB (settable with the –with-chunk-cache-size at netCDF
build time).

For good performance your chunk cache must be larger than one chunk of
your data - preferably that it be large enough to hold multiple chunks
of data.

In addition, when a file is opened (or a variable created in an open
file), the netCDF-4 library checks to make sure the default chunk
cache size will work for that variable. The cache will be large enough
to hold N chunks, up to a maximum size of M bytes. (Both N and M are
settable at configure time with the –with-default-chunks-in-cache and
the –with-max-default-cache-size options to the configure
script. Currently they are set to 10 and 64 MB.)

To change the default chunk cache size, use the set_chunk_cache
function before opening the file with nc_set_chunk_cache(). Fortran 77
programmers see NF_SET_CHUNK_CACHE()). Fortran 90 programmers use the
optional cache_size, cache_nelems, and cache_preemption parameters to
nf90_open/nf90_create to change the chunk size before opening the
file.

To change the per-variable cache size, use the set_var_chunk_cache
function at any time on an open file. C programmers see
nc_set_var_chunk_cache(), Fortran 77 programmers see
NF_SET_VAR_CHUNK_CACHE().

\section default_chunking_4_1 The Default Chunking Scheme

Unfortunately, there are no general-purpose chunking defaults that are
optimal for all uses. Different patterns of access lead to different
chunk shapes and sizes for optimum access. Optimizing for a single
specific pattern of access can degrade performance for other access
patterns.  By creating or rewriting datasets using appropriate
chunking, it is sometimes possible to support efficient access for
multiple patterns of access.

If you don't know or can't anticipate what access patterns will be most common, or you want to store a variable in a way that will support reasonable access along any of its dimensions, you can use the library's default chunking strategy.

The size and shape of chunks for each individual variable are determined at creation time by the size of each variable element and by the shape of the variable, specified by the ordered list of its dimensions and the lengths of each dimension, with special rules for unlimited dimensions.

The best default chunk size would be as large as possible without exceeding the size of a physical disk access. However, block sizes differ for different file systems and platforms, and in particular may be different when the data is first written and later read. Currently the netCDF default chunk size is 4MiB, which is reasonable for filesystems on high-performance computing platforms. A different default may be specified at configuration time when building the library from source, for example 4KiB for filesystems with small physical block sizes.

The current default chunking strategy of the netCDF library is to balance access time along any of a variable's dimensions, by using chunk shapes similar to the shape of the entire variable but small enough that the resulting chunk size is less than or equal to the default chunk size. This differs from an earlier default chunking strategy that always used one for the length of a chunk along any unlimited dimension, and otherwise divided up the number of chunks along fixed dimensions to keep chunk sizes less than or equal to the default chunk size.

A pragmatic exception to the default strategy is used for variables that only have a single unlimited dimension, for example time series with only a time dimension. In that case, in order to avoid chunks much larger than needed when there are only a small number of records, the chunk sizes for such variables are limited to 4KiB. This may be overridden by explicitly setting the chunk shapes for such variables.

\section chunking_parallel_io Chunking and Parallel I/O

When files are opened for read/write parallel I/O access, the chunk
cache is not used. Therefore it is important to open parallel files
with read only access when possible, to achieve the best performance.

\section bm_file A Utility to Help Benchmark Results: bm_file

The bm_file utility may be used to copy files, from one netCDF format
to another, changing chunking, filter, parallel I/O, and other
parameters. This program may be used for benchmarking netCDF
performance for user data files with a range of choices, allowing data
producers to pick settings that best serve their user base.

NetCDF must have been configured with –enable-benchmarks at build time
for the bm_file program to be built. When built with
–enable-benchmarks, netCDF will include tests (run with “make check”)
that will run the bm_file program on sample data files.

Since data files and their access patterns vary from case to case,
these benchmark tests are intended to suggest further use of the
bm_file program for users.

Here's an example of a call to bm_file:

\code
     ./bm_file -d -f 3 -o  tst_elena_out.nc -c 0:-1:0:1024:256:256 tst_elena_int_3D.nc
\endcode

Generally a range of settings must be tested. This is best done with a
shell script, which calls bf_file repeatedly, to create output like
this:

<pre>
     *** Running benchmarking program bm_file for simple shorts test files, 1D to 6D...
     input format, output_format, input size, output size, meta read time, meta write time, data read time, data write time, enddianness, metadata reread time, data reread time, read rate, write rate, reread rate, deflate, shuffle, chunksize[0], chunksize[1], chunksize[2], chunksize[3]
     1, 4, 200092, 207283, 1613, 1054, 409, 312, 0, 1208, 1551, 488.998, 641.026, 128.949, 0, 0, 100000, 0, 0, 0
     1, 4, 199824, 208093, 1545, 1293, 397, 284, 0, 1382, 1563, 503.053, 703.211, 127.775, 0, 0, 316, 316, 0, 0
     1, 4, 194804, 204260, 1562, 1611, 390, 10704, 0, 1627, 2578, 499.159, 18.1868, 75.5128, 0, 0, 46, 46, 46, 0
     1, 4, 167196, 177744, 1531, 1888, 330, 265, 0, 12888, 1301, 506.188, 630.347, 128.395, 0, 0, 17, 17, 17, 17
     1, 4, 200172, 211821, 1509, 2065, 422, 308, 0, 1979, 1550, 473.934, 649.351, 129.032, 0, 0, 10, 10, 10, 10
     1, 4, 93504, 106272, 1496, 2467, 191, 214, 0, 32208, 809, 488.544, 436.037, 115.342, 0, 0, 6, 6, 6, 6
     *** SUCCESS!!!
</pre>

Such tables are suitable for import into spreadsheets, for easy
graphing of results.

Several test scripts are run during the “make check” of the netCDF
build, in the nc_test4 directory. The following example may be found
in nc_test4/run_bm_elena.sh.

<pre>
     #!/bin/sh

     # This shell runs some benchmarks that Elena ran as described here:
     # http://hdfeos.org/workshops/ws06/presentations/Pourmal/HDF5_IO_Perf.pdf

     # $Id: netcdf.texi,v 1.82 2010/05/15 20:43:13 dmh Exp $

     set -e
     echo ""

     echo "*** Testing the benchmarking program bm_file for simple float file, no compression..."
     ./bm_file -h -d -f 3 -o  tst_elena_out.nc -c 0:-1:0:1024:16:256 tst_elena_int_3D.nc
     ./bm_file -d -f 3 -o  tst_elena_out.nc -c 0:-1:0:1024:256:256 tst_elena_int_3D.nc
     ./bm_file -d -f 3 -o  tst_elena_out.nc -c 0:-1:0:512:64:256 tst_elena_int_3D.nc
     ./bm_file -d -f 3 -o  tst_elena_out.nc -c 0:-1:0:512:256:256 tst_elena_int_3D.nc
     ./bm_file -d -f 3 -o  tst_elena_out.nc -c 0:-1:0:256:64:256 tst_elena_int_3D.nc
     ./bm_file -d -f 3 -o  tst_elena_out.nc -c 0:-1:0:256:256:256 tst_elena_int_3D.nc
     echo '*** SUCCESS!!!'

     exit 0
</pre>

The reading that bm_file does can be tailored to match the expected
access pattern.

The bm_file program is controlled with command line options.

<pre>
     ./bm_file
     bm_file -v [-s N]|[-t V:S:S:S -u V:C:C:C -r V:I:I:I] -o file_out -f N -h -c V:C:C,V:C:C:C -d -m -p -i -e 1|2 file
       [-v]        Verbose
       [-o file]   Output file name
       [-f N]      Output format (1 - classic, 2 - 64-bit offset, 3 - netCDF-4, 4 - netCDF4/CLASSIC)
       [-h]        Print output header
       [-c V:Z:S:C:C:C[,V:Z:S:C:C:C, etc.]] Deflate, shuffle, and chunking parameters for vars
       [-t V:S:S:S[,V:S:S:S, etc.]] Starts for reads/writes
       [-u V:C:C:C[,V:C:C:C, etc.]] Counts for reads/writes
       [-r V:I:I:I[,V:I:I:I, etc.]] Incs for reads/writes
       [-d]        Doublecheck output by rereading each value
       [-m]        Do compare of each data value during doublecheck (slow for large files!)
       [-p]        Use parallel I/O
       [-s N]      Denom of fraction of slowest varying dimension read.
       [-i]        Use MPIIO (only relevant for parallel builds).
       [-e 1|2]    Set the endianness of output (1=little 2=big).
       file        Name of netCDF file
</pre>

\page netcdf_utilities_guide NetCDF Utilities

\tableofcontents

\section cdl_guide CDL Guide

\subsection cdl_syntax CDL Syntax

Below is an example of CDL, describing a netCDF classic format file with several
named dimensions (lat, lon, time), variables (z, t, p, rh, lat, lon,
time), variable attributes (units, _FillValue, valid_range), and some
data.

\code
     netcdf foo {    // example netCDF specification in CDL

     dimensions:
     lat = 10, lon = 5, time = unlimited;

     variables:
       int     lat(lat), lon(lon), time(time);
       float   z(time,lat,lon), t(time,lat,lon);
       double  p(time,lat,lon);
       int     rh(time,lat,lon);

       lat:units = "degrees_north";
       lon:units = "degrees_east";
       time:units = "seconds";
       z:units = "meters";
       z:valid_range = 0., 5000.;
       p:_FillValue = -9999.;
       rh:_FillValue = -1;

     data:
       lat   = 0, 10, 20, 30, 40, 50, 60, 70, 80, 90;
       lon   = -140, -118, -96, -84, -52;
     }
\endcode

All CDL statements are terminated by a semicolon. Spaces, tabs, and
newlines can be used freely for readability. Comments may follow the
double slash characters '//' on any line.

A CDL description for a classic model file consists of three optional
parts: dimensions, variables, and data. The variable part may contain
variable declarations and attribute assignments. For the enhanced
model supported by netCDF-4, a CDL description may also include
groups, subgroups, and user-defined types.

A dimension is used to define the shape of one or more of the
multidimensional variables described by the CDL description. A
dimension has a name and a length. At most one dimension in a classic
CDL description can have the unlimited length, which means a variable
using this dimension can grow to any length (like a record number in a
file). Any number of dimensions can be declared of unlimited length in
CDL for an enhanced model file.

A variable represents a multidimensional array of values of the same
type. A variable has a name, a data type, and a shape described by its
list of dimensions. Each variable may also have associated attributes
(see below) as well as data values. The name, data type, and shape of
a variable are specified by its declaration in the variable section of
a CDL description. A variable may have the same name as a dimension;
by convention such a variable contains coordinates of the dimension it
names.

An attribute contains information about a variable or about the whole
netCDF dataset or containing group. Attributes may be used to specify
such properties as units, special values, maximum and minimum valid
values, and packing parameters. Attribute information is represented
by single values or one-dimensional arrays of values. For example,
“units” might be an attribute represented by a string such as
“celsius”. An attribute has an associated variable, a name, a data
type, a length, and a value. In contrast to variables that are
intended for data, attributes are intended for ancillary data or
metadata (data about data).

In CDL, an attribute is designated by a variable and attribute name,
separated by a colon (':'). It is possible to assign global attributes
to the netCDF dataset as a whole by omitting the variable name and
beginning the attribute name with a colon (':'). The data type of an
attribute in CDL, if not explicitly specified, is derived from the
type of the value assigned to it. In the netCDF-4
enhanced model, attributes may be declared to be of user-defined type,
like variables.

The length of an attribute is the number of data values assigned to
it.  Multiple values are assigned to non-character attributes by
separating the values with commas (',').  All values assigned to an
attribute must be of the same type.  In the classic data model,
character arrays are used for textual information.  The length of a
character attribute is the number of bytes, and an array of
character values can be represented in string notation.  In the
enhanced data model of netCDF-4, variable-length strings are available
as a primitive type, and the length of a string attribute is the
number of string values assigned to it.

In CDL, just as for netCDF, the names of dimensions, variables and
attributes (and, in netCDF-4 files, groups, user-defined types,
compound member names, and enumeration symbols) consist of arbitrary
sequences of alphanumeric characters, underscore '_', period '.', plus
'+', hyphen '-', or at sign '@', but beginning with a letter or
underscore. However names commencing with underscore are reserved for
system use. Case is significant in netCDF names. A zero-length name is
not allowed. Some widely used conventions restrict names to only
alphanumeric characters or underscores. Names that have trailing space
characters are also not permitted.

Beginning with versions 3.6.3 and 4.0, names may also include UTF-8
encoded Unicode characters as well as other special characters, except
for the character '/', which may not appear in a name (because it is
reserved for path names of nested groups). In CDL, most special
characters are escaped with a backslash '\' character, but that
character is not actually part of the netCDF name. The special
characters that do not need to be escaped in CDL names are underscore
'_', period '.', plus '+', hyphen '-', or at sign '@'.  The formal
specification of CDL name syntax is provided in the classic format
specification (see \ref classic_format).  Note that by using
special characters in names, you may make your data not compliant with
conventions that have more stringent requirements on valid names for
netCDF components, for example the CF Conventions.

The names for the primitive data types are reserved words in CDL, so
names of variables, dimensions, and attributes must not be primitive
type names.

The optional data section of a CDL description is where netCDF
variables may be initialized. The syntax of an initialization is
simple:

\code
     variable = value_1, value_2, ... ;
\endcode

The comma-delimited list of constants may be separated by spaces,
tabs, and newlines. For multidimensional arrays, the last dimension
varies fastest. Thus, row-order rather than column order is used for
matrices. If fewer values are supplied than are needed to fill a
variable, it is extended with the fill value. The types of constants
need not match the type declared for a variable; coercions are done to
convert integers to floating point, for example. All meaningful type
conversions among numeric primitive types are supported.

A special notation for fill values is supported: the ‘_’ character
designates a fill value for variables.

\subsection cdl_data_types CDL Data Types

The CDL primitive data types for the classic model are:
- char - Characters.
- byte - Eight-bit integers.
- short - 16-bit signed integers.
- int - 32-bit signed integers.
- long - (Deprecated, synonymous with int)
- float - IEEE single-precision floating point (32 bits).
- real - (Synonymous with float).
- double - IEEE double-precision floating point (64 bits).

NetCDF-4 supports the additional primitive types:
- ubyte - Unsigned eight-bit integers.
- ushort - Unsigned 16-bit integers.
- uint - Unsigned 32-bit integers.
- int64 - 64-bit signed integers.
- uint64 - Unsigned 64-bit signed integers.
- string - Variable-length string of characters

Except for the added numeric data-types byte and ubyte, CDL supports
the same numeric primitive
data types as C. For backward compatibility, in declarations primitive
type names may be specified in either upper or lower case.

The byte type differs from the char type in that it is intended for
numeric data, and the zero byte has no special significance, as it may
for character data. In the classic data model, byte data could be
interpreted as either signed (-128 to 127) or unsigned (0 to
255). When reading byte data in a way that converts it into another
numeric type, the default interpretation is signed.  The netCDF-4
enhanced data model added an unsigned byte type.

The short type holds values between -32768 and
32767. The ushort type holds values between 0 and 65536. The int type
can hold values between -2147483648 and 2147483647. The uint type
holds values between 0 and 4294967296. The int64 type can hold values
between -9223372036854775808 and 9223372036854775807. The uint64 type
can hold values between 0 and 18446744073709551616.

The float type can hold values between about -3.4+38 and 3.4+38, with
external representation as 32-bit IEEE normalized single-precision
floating-point numbers. The double type can hold values between about
-1.7+308 and 1.7+308, with external representation as 64-bit IEEE
standard normalized double-precision, floating-point numbers. The
string type holds variable length strings.

A netCDF-4 string is a variable length array of Unicode
<http://unicode.org/> characters. When reading/writing a String to a
netCDF file or other external representation, the characters are UTF-8
encoded <http://en.wikipedia.org/wiki/UTF-8> (note that ASCII is a
subset of UTF-8). Libraries may use different internal
representations, for example the Java library uses UTF-16 encoding.
Note especially that Microsoft Windows does not support UTF-8
encoding, only ASCII and UTF-16. So using netcdf on Windows may
cause some problems with respect to objects like file paths.

The netCDF char type contains uninterpreted characters, one character
per byte.  Typically these contain 7-bit ASCII characters, but the
character encoding is application specific. For this reason,
applications writing data using the enhanced data model are encouraged
to use the netCDF-4 string data type in preference to the char data
type.  Applications writing string data using the char data type are
encouraged to add the special variable attribute "_Encoding" with a
value that the netCDF libraries recognize. Currently those valid
values are "UTF-8" or "ASCII", case insensitive.

\subsection cdl_constants CDL Notation for Data Constants

This section describes the CDL notation for constants.

Attributes are initialized in the variables section of a CDL
description by providing a list of constants that determines the
attribute's length and type (if primitive and not explicitly
declared). CDL defines a syntax for constant values that permits
distinguishing among different netCDF primitive types. The syntax for
CDL constants is similar to C syntax, with type suffixes appended to
bytes, shorts, and floats to distinguish them from ints and doubles.

A byte constant is represented by an integer constant with a 'b' (or
'B') appended.  In the old netCDF-2 API, byte constants could also be
represented using single characters or standard C character escape
sequences such as 'a' or '\n'.  This is still supported for backward
compatibility, but deprecated to make the distinction clear between
the numeric byte type and the textual char type.  Example byte
constants include:

\code
     0b      // a zero byte
     -1b     // -1 as an 8-bit byte
     255b    // also -1 as a signed 8-bit byte
\endcode

Character constants are enclosed in double quotes. A character array
may be represented as a string enclosed in double quotes. Multiple CDL
strings are concatenated into a single array of characters, permitting
long character arrays to appear on multiple lines. To support multiple
variable-length textual values, a conventional delimiter such as ','
or blank may be used, but interpretation of any such convention for a
delimiter must be implemented in software above the netCDF library
layer. The usual escape conventions for C strings are honored. For
example:

\code
     "a"            // ASCII 'a'
     "Two\nlines\n" // a 10-character string with two embedded newlines
     "a bell:\007"  // a character array containing an ASCII bell
     "ab","cde"     // the same as "abcde"
\endcode

The form of a short constant is an integer constant with an 's' or 'S'
appended. If a short constant begins with '0', it is interpreted as
octal. When it begins with '0x', it is interpreted as a hexadecimal
constant. For example:

\code
     2s      // a short 2
     0123s   // octal
     0x7ffs  // hexadecimal
\endcode

The form of an int constant is an ordinary integer constant. If an int
constant begins with '0', it is interpreted as octal. When it begins
with '0x', it is interpreted as a hexadecimal constant. Examples of
valid int constants include:

\code
     -2
     0123            // octal
     0x7ff           // hexadecimal
     1234567890L     // deprecated, uses old long suffix
\endcode

The float type is appropriate for representing data with about seven
significant digits of precision. The form of a float constant is the
same as a C floating-point constant with an 'f' or 'F' appended. A
decimal point is required in a CDL float to distinguish it from an
integer. For example, the following are all acceptable float
constants:

\code
     -2.0f
     3.14159265358979f       // will be truncated to less precision
     1.f
     .1f
\endcode

The double type is appropriate for representing floating-point data
with about 16 significant digits of precision. The form of a double
constant is the same as a C floating-point constant. An optional 'd'
or 'D' may be appended. A decimal point is required in a CDL double to
distinguish it from an integer. For example, the following are all
acceptable double constants:

\code
     -2.0
     3.141592653589793
     1.0e-20
     1.d
\endcode

Unsigned integer constants can be created by appending the character
'U' or 'u' between the constant and any trailing size specifier.  Thus
one could say 10U, 100us, 100000ul, or 1000000ull, for example.

Constants for the variable-length string type, available as a
primitive type in the netCDF-4 enhanced data model are, like character
constants, represented using double quotes. This represents a
potential ambiguity since a multi-character string may also indicate a
dimensioned character value. Disambiguation usually occurs by context,
but care should be taken to specify the string type to ensure the
proper choice.  For example, these two CDL specifications of global
attributes have different types:

\code
   :att1 = "abcd", "efg" ;       // a char attribute of length 7
   string :att2 = "abcd", efg" ; // a string attribute of length 2
\endcode

Opaque constants are represented as sequences of hexadecimal digits
preceded by 0X or 0x: 0xaa34ffff, for example.  These constants can
still be used as integer constants and will be either truncated or
extended as necessary.

The ncgen man-page reference has more details about CDL representation
of constants of user-defined types.

\section ncdump_guide ncdump

Convert NetCDF file to text form (CDL)

\subsection  ncdump_SYNOPSIS ncdump synopsis

\code
ncdump   [-chistxw]  [-v  var1,...]  [-b lang]  [-f lang]
         [-l  len]  [-n  name]  [-p n[,n]]  [-g  grp1,...]  file


ncdump    -k file
\endcode

\subsection ncdump_DESCRIPTION ncdump description

The \b ncdump utility generates a text representation of a specified
netCDF file on standard output, optionally excluding some or all of
the variable data in the output.  The text representation is in a form
called CDL (network Common Data form Language) that can be viewed,
edited, or serve as input to \b ncgen, a companion program that can
generate a binary netCDF file from a CDL file.  Hence \b ncgen and \b
ncdump can be used as inverses to transform the data representation
between binary and text representations.  See \b ncgen documentation
for a description of CDL and netCDF representations.

\b ncdump may also be used to determine what kind of netCDF file
is used (which variant of the netCDF file format) with the -k
option.

If DAP support was enabled when \b ncdump was built, the file name may
specify a DAP URL. This allows \b ncdump to access data sources from
DAP servers, including data in other formats than netCDF.  When used
with DAP URLs, \b ncdump shows the translation from the DAP data
model to the netCDF data model.

\b ncdump may also be used as a simple browser for netCDF data files,
to display the dimension names and lengths; variable names, types, and
shapes; attribute names and values; and optionally, the values of data
for all variables or selected variables in a netCDF file.  For
netCDF-4 files, groups and user-defined types are also included in \b
ncdump output.

\b ncdump uses '_' to represent data values that are equal to the
'_FillValue' attribute for a variable, intended to represent
data that has not yet been written.  If a variable has no
'_FillValue' attribute, the default fill value for the variable
type is used unless the variable is of byte type.

\b ncdump defines a default display format used for each type of
netCDF data, but this can be changed if a 'C_format' attribute
is defined for a netCDF variable.  In this case, \b ncdump will
use the 'C_format' attribute to format each value.  For
example, if floating-point data for the netCDF variable 'Z' is
known to be accurate to only three significant digits, it would
be appropriate to use the variable attribute

\code
    Z:C_format = "%.3g"
\endcode

\subsection ncdump_OPTIONS ncdump options

@par -c
Show the values of \e coordinate \e variables (1D variables with the
same names as dimensions) as well as the declarations of all
dimensions, variables, attribute values, groups, and user-defined
types.  Data values of non-coordinate variables are not included in
the output.  This is usually the most suitable option to use for a
brief look at the structure and contents of a netCDF file.

@par -h
Show only the header information in the output, that is, output only
the declarations for the netCDF dimensions, variables, attributes,
groups, and user-defined types of the input file, but no data values
for any variables. The output is identical to using the '-c' option
except that the values of coordinate variables are not included. (At
most one of '-c' or '-h' options may be present.)

@par -v \a var1,...

@par
The output will include data values for the specified variables, in
addition to the declarations of all dimensions, variables, and
attributes. One or more variables must be specified by name in the
comma-delimited list following this option. The list must be a single
argument to the command, hence cannot contain unescaped blanks or
other white space characters. The named variables must be valid netCDF
variables in the input-file. A variable within a group in a netCDF-4
file may be specified with an absolute path name, such as
'/GroupA/GroupA2/var'.  Use of a relative path name such as 'var' or
'grp/var' specifies all matching variable names in the file.  The
default, without this option and in the absence of the '-c' or '-h'
options, is to include data values for \e all variables in the output.

@par -b [c|f]
A brief annotation in the form of a CDL comment (text beginning with
the characters '//') will be included in the data section of the
output for each 'row' of data, to help identify data values for
multidimensional variables. If lang begins with 'C' or 'c', then C
language conventions will be used (zero-based indices, last dimension
varying fastest). If lang begins with 'F' or 'f', then FORTRAN
language conventions will be used (one-based indices, first dimension
varying fastest). In either case, the data will be presented in the
same order; only the annotations will differ. This option may be
useful for browsing through large volumes of multidimensional data.

@par -f [c|f]
Full annotations in the form of trailing CDL comments (text beginning
with the characters '//') for every data value (except individual
characters in character arrays) will be included in the data
section. If lang begins with 'C' or 'c', then C language conventions
will be used. If lang begins with 'F' or 'f', then FORTRAN language
conventions will be used. In either case, the data will be presented
in the same order; only the annotations will differ. This option may
be useful for piping data into other filters, since each data value
appears on a separate line, fully identified. (At most one of '-b' or
'-f' options may be present.)

@par -l \e length

@par
Changes the default maximum line length (80) used in formatting lists
of non-character data values.

@par -n \e name

@par
CDL requires a name for a netCDF file, for use by 'ncgen -b' in
generating a default netCDF file name. By default, \b ncdump
constructs this name from the last component of the file name of
the input netCDF file by stripping off any extension it has. Use
the '-n' option to specify a different name. Although the output
file name used by 'ncgen -b' can be specified, it may be wise to
have \b ncdump change the default name to avoid inadvertently
overwriting a valuable netCDF file when using \b ncdump, editing the
resulting CDL file, and using 'ncgen -b' to generate a new netCDF
file from the edited CDL file.

@par -p \e float_digits[, \e double_digits ]

@par
Specifies default precision (number of significant digits) to use in
displaying floating-point or double precision data values for
attributes and variables. If specified, this value overrides the value
of the C_format attribute, if any, for a variable. Floating-point data
will be displayed with \e float_digits significant digits. If \e
double_digits is also specified, double-precision values will be
displayed with that many significant digits. In the absence of any
'-p' specifications, floating-point and double-precision data are
displayed with 7 and 15 significant digits respectively. CDL files can
be made smaller if less precision is required. If both floating-point
and double precisions are specified, the two values must appear
separated by a comma (no blanks) as a single argument to the command.
(To represent every last bit of precision in a CDL file for all
possible floating-point values would requires '-p 9,17'.)

@par -k
Show \e kind of netCDF file, that is which format variant the file uses.
Other options are ignored if this option is specified.  Output will be
one of 'classic'. '64-bit offset', '64-bit data', 'netCDF-4', or 'netCDF-4 classic
model'.

@par -s
Specifies that \e special virtual and hidden attributes should be output
for the file format variant and for variable properties such as
compression, chunking, and other properties specific to the format
implementation that are primarily related to performance rather
than the logical schema of the data. All the special virtual
attributes begin with '_' followed by an upper-case
letter. Currently they include the global attributes '_Format',
'_NCProperties', '_IsNetcdf4', '_SuperblockVersion' and
the variable attributes '_ChunkSizes', '_DeflateLevel',
'_Endianness', '_Fletcher32', '_NoFill', '_Shuffle', and '_Storage'.
The \b ncgen utility recognizes these attributes and
supports them appropriately. For '_NCProperties',
'_IsNetcdf4', and '_SuperblockVersion', the term 'appropriately'
means that they are ignored.

@par -t
Controls display of time data, if stored in a variable that uses a
udunits compliant time representation such as 'days since 1970-01-01'
or 'seconds since 2009-03-15 12:01:17'.  If this option is specified,
time values are displayed as a human-readable date-time strings rather
than numerical values, interpreted in terms of a 'calendar' variable
attribute, if specified.  For numeric attributes of time variables,
the human-readable time value is displayed after the actual value, in
an associated CDL comment.  Calendar attribute values interpreted with
this option include the CF Conventions values 'gregorian' or
'standard', 'proleptic_gregorian', 'noleap' or '365_day', 'all_leap'
or '366_day', '360_day', and 'julian'.

@par -i
Same as the '-t' option, except output time data as date-time strings
with ISO-8601 standard 'T' separator, instead of a blank.

@par -g \e grp1,...

@par
The output will include data values only for the specified groups.
One or more groups must be specified by name in the comma-delimited
list following this option. The list must be a single argument to the
command. The named groups must be valid netCDF groups in the
input-file. The default, without this option and in the absence of the
'-c' or '-h' options, is to include data values for all groups in the
output.

@par -w
For file names that request remote access using DAP URLs, access data
with client-side caching of entire variables.

@par -x
Output XML (NcML) instead of CDL.  The NcML does not include data values.
The NcML output option currently only works for netCDF classic model data.

\subsection  ncdump_EXAMPLES ncdump examples

Look at the structure of the data in the netCDF file foo.nc:

\code
   ncdump -c foo.nc
\endcode

Produce an annotated CDL version of the structure and data in the
netCDF file foo.nc, using C-style indexing for the annotations:

\code
   ncdump -b c foo.nc > foo.cdl
\endcode

Output data for only the variables uwind and vwind from the netCDF
file foo.nc, and show the floating-point data with only three
significant digits of precision:

\code
   ncdump -v uwind,vwind -p 3 foo.nc
\endcode

Produce a fully-annotated (one data value per line) listing of the
data for the variable omega, using FORTRAN conventions for indices,
and changing the netCDF file name in the resulting CDL file to
omega:

\code
   ncdump -v omega -f fortran -n omega foo.nc > Z.cdl
\endcode

Examine the translated DDS for the DAP source from the specified URL:

\code
   ncdump -h http://test.opendap.org:8080/dods/dts/test.01
\endcode

Without dumping all the data, show the special virtual attributes that indicate
performance-related characteristics of a netCDF-4 file:

\code
   ncdump -h -s nc4file.nc
\endcode

\subsection see_also_ncdump SEE ALSO

ncgen(1), netcdf(3)

- \ref guide_ncgen
- \ref guide_nccopy

\subsection ncdump_string_note NOTE ON STRING OUTPUT

For classic, 64-bit offset, 64-bit data, or netCDF-4 classic model data, \b ncdump
generates line breaks after embedded newlines in displaying character
data.  This is not done for netCDF-4 files, because netCDF-4 supports
arrays of real strings of varying length.

\section guide_nccopy nccopy

Copy a netCDF file, optionally changing format, compression, or chunking in the output.


\subsection  nccopy_SYNOPSIS nccopy synopsis

\code
nccopy [-k kind_name] [-kind_code] [-d n] [-s] [-c chunkspec] [-u] [-w]
       [-[v|V] var1,...] [-[g|G] grp1,...] [-m bufsize] [-h chunk_cache]
       [-e cache_elems] [-r]   infile   outfile
\endcode

\subsection  nccopy_DESCRIPTION nccopy description

The \b nccopy utility copies an input netCDF file in any supported
format variant to an output netCDF file, optionally converting the
output to any compatible netCDF format variant, compressing the data,
or rechunking the data.  For example, if built with the netCDF-3
library, a classic CDF-1 file may be copied to a CDF-2 or CDF-5
file, permitting larger variables.  If built with the netCDF-4
library, a netCDF classic file may be copied to a netCDF-4 file or to
a netCDF-4 classic model file as well, permitting data compression,
efficient schema changes, larger variable sizes, and use of other
netCDF-4 features.

If no output format is specified, with either \b -k  \e kind_name
or \e -kind_code, then the output will use the same
format as the input, unless the input is classic format
and either chunking or compression is specified, in which case the
output will be netCDF-4 classic model format.  Attempting
some kinds of format conversion will result in an error, if the
conversion is not possible.  For example, an attempt to copy a
netCDF-4 file that uses features of the enhanced model, such as
groups or variable-length strings, to any of the other kinds of netCDF
formats that use the classic model will result in an error.

\b nccopy also serves as an example of a generic netCDF-4 program,
with its ability to read any valid netCDF file and handle nested
groups, strings, and user-defined types, including arbitrarily
nested compound types, variable-length types, and data of any valid
netCDF-4 type.

If DAP support was enabled when \b nccopy was built, the file name may
specify a DAP URL. This may be used to convert data on DAP servers to
local netCDF files.

\subsection nccopy_OPTIONS nccopy options

\par -k \e kind_name
Use format name to specify the kind of file to be created
and, by inference, the data model (i.e. netcdf-3 (classic) or
netcdf-4 (enhanced)).  The possible arguments are: \n
    'nc3' or 'classic' => netCDF classic format \n
    'nc6' or '64-bit offset' => netCDF 64-bit offset format \n
    'cdf5' => netCDF 64-bit data format \n
    'nc4' or 'netCDF-4' => netCDF-4 format (enhanced data model) \n
    'nc7' or 'netCDF-4 classic model' => netCDF-4 classic model format \n

\par
Note: The old format numbers '1', '2', '3', '4', equivalent
to the format names 'nc3', 'nc6', 'nc4', or 'nc7' respectively, are
also still accepted but deprecated, due to easy confusion between
format numbers and format names.

\par -k \e kind_code
Use format numeric code (instead of format name) to specify the kind of file to be created
and, by inference, the data model (i.e. netcdf-3 (classic) versus
netcdf-4 (enhanced)).  The numeric codes are: \n
    3 => netcdf classic format \n
    6 => netCDF 64-bit offset format (CDF_2) \n
    5 => netCDF 64-bit data format (CDF-5) \n
    4 => netCDF-4 format (enhanced data model) \n
    7 => netCDF-4 classic model format \n

The numeric code "7" is used because "7=3+4", specifying the format
that uses the netCDF-3 data model for compatibility with the netCDF-4
storage format for performance. Credit is due to NCO for use of these
numeric codes instead of the old and confusing format numbers.

\par -d \e n
For netCDF-4 output, including netCDF-4 classic model, specify
deflation level (level of compression) for variable data output.  0
corresponds to no compression and 9 to maximum compression, with
higher levels of compression requiring marginally more time to
compress or uncompress than lower levels.  Compression achieved may
also depend on output chunking parameters.  If this option is
specified for a classic format input file, it
is not necessary to also specify that the output should be netCDF-4
classic model, as that will be the default.  If this option is not
specified and the input file has compressed variables, the compression
will still be preserved in the output, using the same chunking as in
the input by default.

\par
Note that \b nccopy requires all variables to be compressed using the
same compression level, but the API has no such restriction.  With
a program you can customize compression for each variable independently.

\par -s
For netCDF-4 output, including netCDF-4 classic model, specify
shuffling of variable data bytes before compression or after
decompression.  Shuffling refers to interlacing of bytes in a chunk so
that the first bytes of all values are contiguous in storage, followed
by all the second bytes, and so on, which often improves compression.
This option is ignored unless a non-zero deflation level is specified.
Using -d0 to specify no deflation on input data that has been
compressed and shuffled turns off both compression and shuffling in
the output.

\par -u
Convert any unlimited size dimensions in the input to fixed size
dimensions in the output.  This can speed up variable-at-a-time
access, but slow down record-at-a-time access to multiple variables
along an unlimited dimension.

\par -w
Keep output in memory (as a diskless netCDF file) until output is
closed, at which time output file is written to disk.  This can
greatly speedup operations such as converting unlimited dimension to
fixed size (-u option), chunking, rechunking, or compressing the
input.  It requires that available memory is large enough to hold the
output file.  This option may provide a larger speedup than careful
tuning of the -m, -h, or -e options, and it's certainly a lot simpler.

\par -c  \e  chunkspec
\par
For netCDF-4 output, including netCDF-4 classic model, specify
chunking (multidimensional tiling) for variable data in the output.
This is useful to specify the units of disk access, compression, or
other filters such as checksums.  Changing the chunking in a netCDF
file can also greatly speedup access, by choosing chunk shapes that
are appropriate for the most common access patterns.

\par
The \e chunkspec argument is a string of comma-separated associations,
each specifying a dimension name, a '/' character, and optionally the
corresponding chunk length for that dimension.  No blanks should
appear in the chunkspec string, except possibly escaped blanks that
are part of a dimension name.  A chunkspec names at least one
dimension, and may omit dimensions which are not to be chunked or for
which the default chunk length is desired.  If a dimension name is
followed by a '/' character but no subsequent chunk length, the actual
dimension length is assumed.  If copying a classic model file to a
netCDF-4 output file and not naming all dimensions in the chunkspec,
unnamed dimensions will also use the actual dimension length for the
chunk length.  An example of a chunkspec for variables that use 'm'
and 'n' dimensions might be 'm/100,n/200' to specify 100 by 200
chunks. To see the chunking resulting from copying with a chunkspec,
use the '-s' option of ncdump on the output file.

\par
The chunkspec '/' that omits all dimension names and
corresponding chunk lengths specifies that no chunking is to occur in
the output, so can be used to unchunk all the chunked variables.
To see the chunking resulting from copying with a chunkspec,
use the '-s' option of ncdump on the output file.

\par
As an I/O optimization, \b nccopy has a threshold for the minimum size of
non-record variables that get chunked, currently 8192 bytes.  In the future,
use of this threshold and its size may be settable in an option.

\par
Note that \b nccopy requires variables that share a dimension to also
share the chunk size associated with that dimension, but the
programming interface has no such restriction.  If you need to
customize chunking for variables independently, you will need to use
the library API in a custom utility program.

\par -v \a var1,...

\par
The output will include data values for the specified variables, in
addition to the declarations of all dimensions, variables, and
attributes. One or more variables must be specified by name in the
comma-delimited list following this option. The list must be a single
argument to the command, hence cannot contain unescaped blanks or
other white space characters. The named variables must be valid netCDF
variables in the input-file. A variable within a group in a netCDF-4
file may be specified with an absolute path name, such as
'/GroupA/GroupA2/var'.  Use of a relative path name such as 'var' or
'grp/var' specifies all matching variable names in the file.  The
default, without this option, is to include data values for \e all variables
in the output.

\par -V \a var1,...

\par
The output will include the specified variables only but all dimensions and
global or group attributes. One or more variables must be specified by name in the
comma-delimited list following this option. The list must be a single argument
to the command, hence cannot contain unescaped blanks or other white space
characters. The named variables must be valid netCDF variables in the
input-file. A variable within a group in a netCDF-4 file may be specified with
an absolute path name, such as '/GroupA/GroupA2/var'.  Use of a relative path
name such as 'var' or 'grp/var' specifies all matching variable names in the
file.  The default, without this option, is to include \e all variables in the
output.

\par -g \e grp1,...

\par
The output will include data values only for the specified groups.
One or more groups must be specified by name in the comma-delimited
list following this option. The list must be a single argument to the
command. The named groups must be valid netCDF groups in the
input-file. The default, without this option, is to include data values for all
groups in the output.

\par -G \e grp1,...

\par
The output will include only the specified groups.
One or more groups must be specified by name in the comma-delimited
list following this option. The list must be a single argument to the
command. The named groups must be valid netCDF groups in the
input-file. The default, without this option, is to include all groups in the
output.

\par -m  \e  bufsize
\par
An integer or floating-point number that specifies the size, in bytes,
of the copy buffer used to copy large variables.  A suffix of K, M, G,
or T multiplies the copy buffer size by one thousand, million,
billion, or trillion, respectively.  The default is 5 Mbytes,
but will be increased if necessary to hold at least one chunk of
netCDF-4 chunked variables in the input file.  You may want to specify
a value larger than the default for copying large files over high
latency networks.  Using the '-w' option may provide better
performance, if the output fits in memory.

\par -h \e chunk_cache
\par
For netCDF-4 output, including netCDF-4 classic model, an integer or
floating-point number that specifies the size in bytes of chunk cache
allocated for each chunked variable.  This is not a property of the file, but merely
a performance tuning parameter for avoiding compressing or
decompressing the same data multiple times while copying and changing
chunk shapes.  A suffix of K, M, G, or T multiplies the chunk cache
size by one thousand, million, billion, or trillion, respectively.
The default is 4.194304 Mbytes (or whatever was specified for the
configure-time constant CHUNK_CACHE_SIZE when the netCDF library was
built).  Ideally, the \b nccopy utility should accept only one memory
buffer size and divide it optimally between a copy buffer and chunk
cache, but no general algorithm for computing the optimum chunk cache
size has been implemented yet. Using the '-w' option may provide
better performance, if the output fits in memory.

\par -e \e cache_elems
\par
For netCDF-4 output, including netCDF-4 classic model, specifies
number of chunks that the chunk cache can hold. A suffix of K, M, G,
or T multiplies the number of chunks that can be held in the cache
by one thousand, million, billion, or trillion, respectively.  This is not a
property of the file, but merely a performance tuning parameter for
avoiding compressing or decompressing the same data multiple times
while copying and changing chunk shapes.  The default is 1009 (or
whatever was specified for the configure-time constant
CHUNK_CACHE_NELEMS when the netCDF library was built).  Ideally, the
\b nccopy utility should determine an optimum value for this parameter,
but no general algorithm for computing the optimum number of chunk
cache elements has been implemented yet.

\par -r
Read netCDF classic input file into a diskless netCDF
file in memory before copying.  Requires that input file be small
enough to fit into memory.  For \b nccopy, this doesn't seem to provide
any significant speedup, so may not be a useful option.

\subsection nccopy_EXAMPLES nccopy examples

<H4> Simple Copy </H4>
Make  a copy  of  foo1.nc, a  netCDF  file of  any type,  to
foo2.nc, a netCDF file of the same type:
\code
nccopy foo1.nc foo2.nc
\endcode
Note that the above copy will not be as fast as use of cp or other
simple copy utility, because the file is copied using only the netCDF
API.  If the input file has extra bytes after the end of the netCDF
data, those will not be copied, because they are not accessible
through the netCDF interface.  If the original file was generated in
'No fill' mode so that fill values are not stored for padding for data
alignment, the output file may have different padding bytes.

<H4> Uncompress Data </H4>
Convert a netCDF-4 classic model file, compressed.nc, that uses compression,
to a netCDF-3 file classic.nc:
\code
nccopy -k classic compressed.nc classic.nc
\endcode
Note that 'nc3' could be used instead of 'classic'.

<H4> Remote Access to Data Subset </H4>

Download the variable 'time_bnds' and its associated attributes from
an OPeNDAP server and copy the result to a netCDF file named 'tb.nc':
\code
nccopy 'http://test.opendap.org/opendap/data/nc/sst.mnmean.nc.gz?time_bnds' tb.nc
\endcode
Note that URLs that name specific variables as command-line arguments
should generally be quoted, to avoid the shell interpreting special
characters such as '?'.

<H4> Compress Data </H4>

Compress all the variables in the input file foo.nc, a netCDF file of any
type, to the output file bar.nc:
\code
nccopy -d1 foo.nc bar.nc
\endcode
If foo.nc was a classic netCDF file, bar.nc will be a
netCDF-4 classic model netCDF file, because the classic
formats don't support compression.  If foo.nc was a
netCDF-4 file with some variables compressed using various deflation
levels, the output will also be a netCDF-4 file of the same type, but
all the variables, including any uncompressed variables in the input,
will now use deflation level 1.

<H4>Rechunk Data for Faster Access</H4>

Assume the input data includes gridded variables that use time, lat,
lon dimensions, with 1000 times by 1000 latitudes by 1000 longitudes,
and that the time dimension varies most slowly.  Also assume that
users want quick access to data at all times for a small set of
lat-lon points.  Accessing data for 1000 times would typically require
accessing 1000 disk blocks, which may be slow.

Reorganizing the data into chunks on disk that have all the time in
each chunk for a few lat and lon coordinates would greatly speed up
such access.  To chunk the data in the input file slow.nc, a netCDF
file of any type, to the output file fast.nc, you could use;
\code
nccopy -c time/1000,lat/40,lon/40 slow.nc fast.nc
\endcode
to specify data chunks of 1000 times, 40 latitudes, and 40 longitudes.
If you had enough memory to contain the output file, you could speed
up the rechunking operation significantly by creating the output in
memory before writing it to disk on close:
\code
nccopy -w -c time/1000,lat/40,lon/40 slow.nc fast.nc
\endcode

\subsection see_also_nccopy SEE ALSO

ncdump(1), ncgen(1), netcdf(3)

\section guide_ncgen ncgen

The ncgen tool generates a netCDF file or a C or FORTRAN program that
creates a netCDF dataset. If no options are specified in invoking
ncgen, the program merely checks the syntax of the CDL input,
producing error messages for any violations of CDL syntax.

The ncgen tool is now is capable of producing netcdf-4 files. It
operates essentially identically to the original ncgen.

The CDL input to ncgen may include data model constructs from the
netcdf- data model. In particular, it includes new primitive types
such as unsigned integers and strings, opaque data, enumerations, and
user-defined constructs using vlen and compound types. The ncgen man
page should be consulted for more detailed information.

UNIX syntax for invoking ncgen:

\code
     ncgen [-b] [-o netcdf-file] [-c] [-f] [-k<kind>] [-l<language>] [-x] [input-file]
\endcode

where:

<pre>
-b
Create a (binary) netCDF file. If the '-o' option is absent, a default
file name will be constructed from the netCDF name (specified after
the netcdf keyword in the input) by appending the '.nc'
extension. Warning: if a file already exists with the specified name
it will be overwritten.

-o netcdf-file
Name for the netCDF file created. If this option is specified, it
implies the '-b' option. (This option is necessary because netCDF
files are direct-access files created with seek calls, and hence
cannot be written to standard output.)

-c
Generate C source code that will create a netCDF dataset matching the
netCDF specification. The C source code is written to standard
output. This is only useful for relatively small CDL files, since all
the data is included in variable initializations in the generated
program. The -c flag is deprecated and the -lc flag should be used
instead.

-f
Generate FORTRAN source code that will create a netCDF dataset
matching the netCDF specification. The FORTRAN source code is written
to standard output. This is only useful for relatively small CDL
files, since all the data is included in variable initializations in
the generated program. The -f flag is deprecated and the -lf77 flag
should be used instead.

-k
The -k file specifies the kind of netCDF file to generate. The
arguments to the -k flag can be as follows.
        'classic', 'nc3' – Produce a netcdf classic file format file.
        '64-bit offset', 'nc6' – Produce a netcdf 64 bit classic file format file.
        '64-bit data (CDF-5), 'nc5' – Produce a CDF-5 format file.
        'netCDF-4', 'nc4' – Produce a netcdf-4 format file.
        'netCDF-4 classic model', 'nc7' – Produce a netcdf-4 file format, but restricted to netcdf-3 classic CDL input.

Note that the -v flag is a deprecated alias for -k. The code 'nc7' is
used as a short form for the unwieldy 'netCDF-4 classic model' because
7=3+4, a mnemonic for the format that uses the netCDF-3 data model for
compatibility with the netCDF-4 storage format for performance. The
old version format numbers '1', '2', '3', '4', equivalent to the
format names 'nc3', 'nc6', 'nc4', or 'nc7' respectively, are also
still accepted but deprecated, due to easy confusion between format
numbers and format names. Various old format name aliases are also
accepted but deprecated, e.g. 'hdf5', 'enhanced-nc3', for 'netCDF-4'.


-l
The -l file specifies that ncgen should output (to standard output)
the text of a program that, when compiled and executed, will produce
the corresponding binary .nc file. The arguments to the -l flag can be
as follows.
        c|C => C language output.
        f77|fortran77 => FORTRAN 77 language output; note that currently only the classic model is supported for fortran output.

-x
Use “no fill” mode, omitting the initialization of variable values
with fill values. This can make the creation of large files much
faster, but it will also eliminate the possibility of detecting the
inadvertent reading of values that haven't been written.
</pre>

<h1>Examples</h1>

Check the syntax of the CDL file foo.cdl:

\code
     ncgen foo.cdl
\endcode

From the CDL file foo.cdl, generate an equivalent binary netCDF file
named bar.nc:

\code
     ncgen -o bar.nc foo.cdl
\endcode

From the CDL file foo.cdl, generate a C program containing netCDF
function invocations that will create an equivalent binary netCDF
dataset:

\code
     ncgen -l c foo.cdl > foo.c
\endcode

\section guide_ncgen3 ncgen3

The ncgen3 tool is the new name for the older, original ncgen utility.

The ncgen3 tool generates a netCDF file or a C or FORTRAN program that
creates a netCDF dataset. If no options are specified in invoking
ncgen3, the program merely checks the syntax of the CDL input,
producing error messages for any violations of CDL syntax.

The ncgen3 utility can only generate classic-model netCDF-4 files or
programs.

UNIX syntax for invoking ncgen3:

\code
     ncgen3 [-b] [-o netcdf-file] [-c] [-f] [-v2|-v3|-v5] [-x] [input-file]
\endcode

where:
<pre>
-b
Create a (binary) netCDF file. If the '-o' option is absent, a default
file name will be constructed from the netCDF name (specified after
the netcdf keyword in the input) by appending the '.nc'
extension. Warning: if a file already exists with the specified name
it will be overwritten.

-o netcdf-file
Name for the netCDF file created. If this option is specified, it
implies the '-b' option. (This option is necessary because netCDF
files are direct-access files created with seek calls, and hence
cannot be written to standard output.)

-c
Generate C source code that will create a netCDF dataset matching the
netCDF specification. The C source code is written to standard
output. This is only useful for relatively small CDL files, since all
the data is included in variable initializations in the generated
program.

-f
Generate FORTRAN source code that will create a netCDF dataset
matching the netCDF specification. The FORTRAN source code is written
to standard output. This is only useful for relatively small CDL
files, since all the data is included in variable initializations in
the generated program.

-v2
The generated netCDF file or program will use the version of the
format with 64-bit offsets, to allow for the creation of very large
files. These files are not as portable as classic format netCDF files,
because they require version 3.6.0 or later of the netCDF library.

-v3
The generated netCDF file will be in netCDF-4/HDF5 format. These files
are not as portable as classic format netCDF files, because they
require version 4.0 or later of the netCDF library.

-v5
The generated netCDF file or program will use the version of the
format with 64-bit integers, to allow for the creation of very large
variables. These files are not as portable as classic format netCDF files,
because they require version 4.4.0 or later of the netCDF library.

-x
Use “no fill” mode, omitting the initialization of variable values
with fill values. This can make the creation of large files much
faster, but it will also eliminate the possibility of detecting the
inadvertent reading of values that haven't been written.
</pre>
