# An Introduction to NetCDF {#netcdf_introduction}

\tableofcontents

## The netCDF Interface {#netcdf_interface}

The Network Common Data Form, or netCDF, is an interface to a library
of data access functions for storing and retrieving data in the form
of arrays. An array is an n-dimensional (where n is 0, 1, 2, ...)
rectangular structure containing items which all have the same data
type (e.g., 8-bit character, 32-bit integer). A scalar (simple single
value) is a 0-dimensional array.

NetCDF is an abstraction that supports a view of data as a collection
of self-describing, portable objects that can be accessed through a
simple interface. Array values may be accessed directly, without
knowing details of how the data are stored. Auxiliary information
about the data, such as what units are used, may be stored with the
data. Generic utilities and application programs can access netCDF
datasets and transform, combine, analyze, or display specified fields
of the data. The development of such applications has led to improved
accessibility of data and improved re-usability of software for
array-oriented data management, analysis, and display.

The netCDF software implements an abstract data type, which means that
all operations to access and manipulate data in a netCDF dataset must
use only the set of functions provided by the interface. The
representation of the data is hidden from applications that use the
interface, so that how the data are stored could be changed without
affecting existing programs. The physical representation of netCDF
data is designed to be independent of the computer on which the data
were written.

Unidata supports the netCDF interfaces for C (see <a
href="http://www.unidata.ucar.edu/netcdf/docs/" >NetCDF-C User's
Guide</a>), Fortran (see <a
href="http://www.unidata.ucar.edu/netcdf/fortran/docs/"
>NetCDF-Fortran User's Guide</a>) and C++ (see <a
href="http://www.unidata.ucar.edu/netcdf/documentation/historic/cxx4/index.html"
>NetCDF C++ Interface Guide</a>).

The netCDF library is supported for various UNIX operating systems. A
MS Windows port is also available. The software is also ported and
tested on a few other operating systems, with assistance from users
with access to these systems, before each major release. Unidata's
netCDF software is freely available <a
href="ftp://ftp.unidata.ucar.edu/pub/netcdf">via FTP</a> to encourage
its widespread use.

For detailed installation instructions, see \ref getting_and_building_netcdf.

## The netCDF File Format {#netcdf_format}

Until version 3.6.0, all versions of netCDF employed only one binary
data format, now referred to as netCDF classic format. NetCDF classic
is the default format for all versions of netCDF.

In version 3.6.0 a new binary format was introduced, 64-bit offset
format. Nearly identical to netCDF classic format, it uses 64-bit
offsets (hence the name), and allows users to create far larger
datasets. This format is also referred as CDF-2, because it bears the signature
string "CDF2" in the file header. After this extension, the classic file
format (i.e. not supporting 64-bit offsets) is now referred as CDF-1.

In version 4.0.0 a third binary format was introduced: the HDF5
format. Starting with this version, the netCDF library can use HDF5
files as its base format. (Only HDF5 files created with netCDF-4 can
be understood by netCDF-4).

Starting from version 4.4.0, netCDF included the support of CDF-5 format. In
order to allows defining large array variables with more than 4-billion
elements, CDF-5 replaces most of the 32-bit integers used to describe metadata
in file header with 64-bit integers. In addition, it supports the following new
external data types: NC_UBYTE, NC_USHORT, NC_UINT, NC_INT64, and NC_UINT64. The
CDF-5 format specifications can be found in
(http://cucis.ece.northwestern.edu/projects/PnetCDF/CDF-5.html).

The classic file formats are now referring to the collection of CDF-1, 2 and 5
formats. By default, netCDF uses the classic format (CDF-1). To use the CDF-2,
CDF-5, or netCDF-4/HDF5 format, set the appropriate constant in the file mode
argument when creating the file.

To achieve network-transparency (machine-independence), netCDF classic
formats are implemented in terms of an external
representation much like XDR (eXternal Data Representation, see
http://www.ietf.org/rfc/rfc1832.txt), a standard for describing and
encoding data. This representation provides encoding of data into
machine-independent sequences of bits. It has been implemented on a
wide variety of computers, by assuming only that eight-bit bytes can
be encoded and decoded in a consistent way. The IEEE 754
floating-point standard is used for floating-point data
representation.

Descriptions of the overall structure of netCDF classic files are provided
later in this manual. See \ref file_structure_and_performance.

The details of the CDF-1 and CDF-2 formats are described in
an appendix. See \ref netcdf_format. However, users are discouraged from
using the format specification to develop independent low-level
software for reading and writing netCDF files, because this could lead
to compatibility problems if the format is ever modified.

### How to Select the Format {#select_format}

With four different base formats, care must be taken in creating data
files to choose the correct base format.

The format of a netCDF file is determined at create time.

When opening an existing netCDF file the netCDF library will
transparently detect its format and adjust accordingly. However,
netCDF library versions earlier than 3.6.0 cannot read CDF-2
format files, library versions before 4.0 can't read netCDF-4/HDF5
files, and versions before 4.4.0 cannot read CDF-5 files.
NetCDF classic format files (even if created by version 3.6.0
or later) remain compatible with older versions of the netCDF library.

Users are encouraged to use netCDF classic CDF-1 format to distribute data,
for maximum portability.

To select CDF-2, CDF-5 or netCDF-4 format files, C programmers should use flag
NC_64BIT_OFFSET, NC_64BIT_DATA, or NC_NETCDF4 respectively in function
nc_create().

In Fortran, use flag nf_64bit_offset, nf_64bit_data, or nf_format_netcdf4 in
function NF_CREATE. See NF_CREATE.

It is also possible to change the default creation format, to convert
a large body of code without changing every create call. C programmers
see nc_set_default_format(). Fortran programs see NF_SET_DEFAULT_FORMAT.

### NetCDF Classic Format (CDF-1) {#classic_format}

The original netCDF format is identified using four bytes in the file
header. All files in this format have "CDF\001" at the beginning of
the file. In this documentation this format is referred to as CDF-1
format.

NetCDF CDF-1 format is identical to the format used by every
previous version of netCDF. It has maximum portability, and is still
the default netCDF format.

### NetCDF 64-bit Offset Format (CDF-2) {#netcdf_64bit_offset_format}

For some users, the various 2 GiB format limitations of the classic
format become a problem. (see \ref limitations).
For these users, 64-bit offset format is a natural choice. It greatly
eases the size restrictions of netCDF classic files (see \ref
limitations).

Files with the 64-bit offsets are identified with a "CDF\002" at the
beginning of the file. In this documentation this format is called
CDF-2 format.

Since CDF-2 format was introduced in version 3.6.0, earlier
versions of the netCDF library can't read CDF-2 files.

\subsection netcdf_64bit_data_format NetCDF 64-bit Data Format (CDF-5)

To allow large variables with more than 4-billion array elements,
64-bit data format is develop to support such I/O requests.

Files with the 64-bit data are identified with a "CDF\005" at the
beginning of the file. In this documentation this format is called
CDF-5 format.

Since CDF-5 format was introduced in version 4.4.0, earlier
versions of the netCDF library can't read CDF-5 files.

\subsection netcdf_4_format NetCDF-4 Format

In version 4.0, netCDF included another new underlying format: HDF5.

NetCDF-4 format files offer new features such as groups, compound
types, variable length arrays, new unsigned integer types, parallel
I/O access, etc. None of these new features can be used with classic
or 64-bit offset files.

NetCDF-4 files can't be created at all, unless the netCDF configure
script is run with –enable-netcdf-4. This also requires version 1.8.0
of HDF5.

For the netCDF-4.0 release, netCDF-4 features are only available from
the C and Fortran interfaces. We plan to bring netCDF-4 features to
the CXX API in a future release of netCDF.

NetCDF-4 files can't be read by any version of the netCDF library
previous to 4.0. (But they can be read by HDF5, version 1.8.0 or
better).

For more discussion of format issues see <a
href="http://www.unidata.ucar.edu/software/netcdf/docs/tutorial_8dox.html">The
NetCDF Tutorial</a>.

\section architecture NetCDF Library Architecture

\image html netcdf_architecture.png "NetCDF Architecture"
\image latex netcdf_architecture.png "NetCDF Architecture"
\image rtf netcdf_architecture.png "NetCDF Architecture"


The netCDF C-based libraries depend on a core C library and some externally developed libraries.

- NetCDF-Java is an independent implementation, not shown here
- C-based 3rd-party netCDF APIs for other languages include Python, Ruby, Perl, Fortran-2003, MATLAB, IDL, and R
- Libraries that don't support netCDF-4 include Perl and old C++
- 3rd party libraries are optional (HDF5, HDF4, zlib, szlib, PnetCDF, libcurl), depending on what features are needed and how netCDF is configured
- "Apps" in the above means applications, not mobile apps!

\section performance What about Performance?

One of the goals of netCDF is to support efficient access to small
subsets of large datasets. To support this goal, netCDF uses direct
access rather than sequential access. This can be much more efficient
when the order in which data is read is different from the order in
which it was written, or when it must be read in different orders for
different applications.

The amount of overhead for a portable external representation depends
on many factors, including the data type, the type of computer, the
granularity of data access, and how well the implementation has been
tuned to the computer on which it is run. This overhead is typically
small in comparison to the overall resources used by an
application. In any case, the overhead of the external representation
layer is usually a reasonable price to pay for portable data access.

Although efficiency of data access has been an important concern in
designing and implementing netCDF, it is still possible to use the
netCDF interface to access data in inefficient ways: for example, by
requesting a slice of data that requires a single value from each
record. Advice on how to use the interface efficiently is provided in
\ref file_structure_and_performance.

The use of HDF5 as a data format adds significant overhead in metadata
operations, less so in data access operations. We continue to study
the challenge of implementing netCDF-4/HDF5 format without
compromising performance.

\section creating_self Creating Self-Describing Data conforming to Conventions

The mere use of netCDF is not sufficient to make data
"self-describing" and meaningful to both humans and machines. The
names of variables and dimensions should be meaningful and conform to
any relevant conventions. Dimensions should have corresponding
coordinate variables (See \ref coordinate_variables) where sensible.

Attributes play a vital role in providing ancillary information. It is
important to use all the relevant standard attributes using the
relevant conventions. For a description of reserved attributes (used
by the netCDF library) and attribute conventions for generic
application software, see \ref attribute_conventions.

A number of groups have defined their own additional conventions and
styles for netCDF data. Descriptions of these conventions, as well as
examples incorporating them can be accessed from the netCDF
Conventions site, http://www.unidata.ucar.edu/netcdf/conventions.html.

These conventions should be used where suitable. Additional
conventions are often needed for local use. These should be
contributed to the above netCDF conventions site if likely to interest
other users in similar areas.

\section limitations Limitations of netCDF

The netCDF classic data model is widely applicable to data that can be
organized into a collection of named array variables with named
attributes, but there are some important limitations to the model and
its implementation in software. Some of these limitations have been
removed or relaxed in netCDF-4 files, but still apply to netCDF
classic and netCDF 64-bit offset files.

NetCDF classic CDF-1 and CDF-2 formats offer a limited
number of external numeric data types: 8-, 16-, 32-bit integers, or
32- or 64-bit floating-point numbers. The CDF-5 and netCDF-4 formats add 64-bit
integer types and unsigned integer types.

With the netCDF-4/HDF5 format, new unsigned integers (of various
sizes), 64-bit integers, and the string type allow improved expression
of meaning in scientific data. The new VLEN (variable length) and
COMPOUND types allow users to organize data in new ways.

With the classic CDF-1 file format, there are constraints that limit
how a dataset is structured to store more than 2 GiBytes (a GiByte is
2^30 or 1,073,741,824 bytes, as compared to a Gbyte, which is
1,000,000,000 bytes.) of data in a single netCDF dataset. (see \ref
limitations). This limitation is a result of 32-bit offsets used for
storing relative offsets within a classic netCDF format file. Since
one of the goals of netCDF is portable data, and some file systems
still can't deal with files larger than 2 GiB, it is best to keep
files that must be portable below this limit. Nevertheless, it is
possible to create and access netCDF files larger than 2 GiB on
platforms that provide support for such files (see
\ref large_file_support).

The CDF-2 format allows large files, and makes it easy to
create to create fixed variables of about 4 GiB, and record variables
of about 4 GiB per record. (see \ref netcdf_64bit_offset_format). However,
old netCDF applications will not be able to read the 64-bit offset
files until they are upgraded to at least version 3.6.0 of netCDF
(i.e. the version in which 64-bit offset format was introduced).

With the netCDF-4/HDF5 format, size limitations are further relaxed,
and files can be as large as the underlying file system
supports. NetCDF-4/HDF5 files are unreadable to the netCDF library
before version 4.0.

Similarly, CDF-5 format uses 64-bit integers to allow users to define
large variables. CDF-5 files are not unreadable to the netCDF library
before version 4.4.0.

Another limitation of the classic formats (CDF-1, 2 and 5) is that
only one unlimited (changeable) dimension is permitted for each netCDF
data set. Multiple variables can share an unlimited dimension, but
then they must all grow together. Hence the classic netCDF model does
not permit variables with several unlimited dimensions or the use of
multiple unlimited dimensions in different variables within the same
dataset. Variables that have non-rectangular shapes (for example,
ragged arrays) cannot be represented conveniently.

In netCDF-4/HDF5 files, multiple unlimited dimensions are fully
supported. Any variable can be defined with any combination of limited
and unlimited dimensions.

The extent to which data can be completely self-describing is limited:
there is always some assumed context without which sharing and
archiving data would be impractical. NetCDF permits storing meaningful
names for variables, dimensions, and attributes; units of measure in a
form that can be used in computations; text strings for attribute
values that apply to an entire data set; and simple kinds of
coordinate system information. But for more complex kinds of metadata
(for example, the information necessary to provide accurate
georeferencing of data on unusual grids or from satellite images), it
is often necessary to develop conventions.

Specific additions to the netCDF data model might make some of these
conventions unnecessary or allow some forms of metadata to be
represented in a uniform and compact way. For example, adding explicit
georeferencing to the netCDF data model would simplify elaborate
georeferencing conventions at the cost of complicating the model. The
problem is finding an appropriate trade-off between the richness of
the model and its generality (i.e., its ability to encompass many
kinds of data). A data model tailored to capture the shared context
among researchers within one discipline may not be appropriate for
sharing or combining data from multiple disciplines.

The classic netCDF data model (which is used for classic CDF-1, 2 and 5 format
data) does not support nested data structures
such as trees, nested arrays, or other recursive structures. Through
use of indirection and conventions it is possible to represent some
kinds of nested structures, but the result may fall short of the
netCDF goal of self-describing data.

In netCDF-4/HDF5 format files, the introduction of the compound type
allows the creation of complex data types, involving any combination
of types. The VLEN type allows efficient storage of ragged arrays, and
the introduction of hierarchical groups allows users new ways to
organize data.

NetCDF-4 supports parallel read/write access to netCDF-4/HDF5 files,
using the underlying HDF5 library and parallel read/write access to
classic files using the PnetCDf library.

For more information about HDF5, see the HDF5 web site:
http://hdfgroup.org/HDF5/.

For more information about PnetCDF, see their web site:
https://parallel-netcdf.github.io/.

\page netcdf_data_set_components The Components of a NetCDF Data Set

\tableofcontents

\section data_model The Data Model

A netCDF dataset contains dimensions, variables, and attributes, which
all have both a name and an ID number by which they are
identified. These components can be used together to capture the
meaning of data and relations among data fields in an array-oriented
dataset. The netCDF library allows simultaneous access to multiple
netCDF datasets which are identified by dataset ID numbers, in
addition to ordinary file names.

\subsection Enhanced Data Model in NetCDF-4/HDF5 Files

Files created with the netCDF-4 format have access to an enhanced data
model, which includes named groups. Groups, like directories in a Unix
file system, are hierarchically organized, to arbitrary depth. They
can be used to organize large numbers of variables.

\image html nc4-model.png "Enhanced NetCDF Data Model"
\image latex nc4-model.png "Enhanced NetCDF Data Model"
\image rtf nc4-model.png "Enhanced NetCDF Data Model"

Each group acts as an entire netCDF dataset in the classic model. That
is, each group may have attributes, dimensions, and variables, as well
as other groups.

The default group is the root group, which allows the classic netCDF
data model to fit neatly into the new model.

Dimensions are scoped such that they can be seen in all descendant
groups. That is, dimensions can be shared between variables in
different groups, if they are defined in a parent group.

In netCDF-4 files, the user may also define a type. For example a
compound type may hold information from an array of C structures, or a
variable length type allows the user to read and write arrays of
variable length values.

Variables, groups, and types share a namespace. Within the same group,
variables, groups, and types must have unique names. (That is, a type
and variable may not have the same name within the same group, and
similarly for sub-groups of that group.)

Groups and user-defined types are only available in files created in
the netCDF-4/HDF5 format. They are not available for classic format files.

\section dimensions Dimensions

A dimension may be used to represent a real physical dimension, for
example, time, latitude, longitude, or height. A dimension might also
be used to index other quantities, for example station or
model-run-number.

A netCDF dimension has both a name and a length.

A dimension length is an arbitrary positive integer, except that only one
dimension in a classic netCDF dataset can have the
length UNLIMITED. In a netCDF-4 dataset, any number of unlimited
dimensions can be used.

Such a dimension is called the unlimited dimension or the record
dimension. A variable with an unlimited dimension can grow to any
length along that dimension. The unlimited dimension index is like a
record number in conventional record-oriented files.

A netCDF classic dataset can have at most one
unlimited dimension, but need not have any. If a variable has an
unlimited dimension, that dimension must be the most significant
(slowest changing) one. Thus any unlimited dimension must be the first
dimension in a CDL shape and the first dimension in corresponding C
array declarations.

A netCDF-4 dataset may have multiple unlimited dimensions, and there
are no restrictions on their order in the list of a variables
dimensions.

To grow variables along an unlimited dimension, write the data using
any of the netCDF data writing functions, and specify the index of the
unlimited dimension to the desired record number. The netCDF library
will write however many records are needed (using the fill value,
unless that feature is turned off, to fill in any intervening
records).

CDL dimension declarations may appear on one or more lines following
the CDL keyword dimensions. Multiple dimension declarations on the
same line may be separated by commas. Each declaration is of the form
name = length. Use the “/” character to include group information
(netCDF-4 output only).

There are four dimensions in the above example: lat, lon, level, and
time (see \ref data_model). The first three are assigned fixed
lengths; time is assigned the length UNLIMITED, which means it is the
unlimited dimension.

The basic unit of named data in a netCDF dataset is a variable. When a
variable is defined, its shape is specified as a list of
dimensions. These dimensions must already exist. The number of
dimensions is called the rank (a.k.a. dimensionality). A scalar
variable has rank 0, a vector has rank 1 and a matrix has rank 2.

It is possible (since version 3.1 of netCDF) to use the same dimension
more than once in specifying a variable shape. For example,
correlation(instrument, instrument) could be a matrix giving
correlations between measurements using different instruments. But
data whose dimensions correspond to those of physical space/time
should have a shape comprising different dimensions, even if some of
these have the same length.

\section variables Variables

Variables are used to store the bulk of the data in a netCDF
dataset. A variable represents an array of values of the same type. A
scalar value is treated as a 0-dimensional array. A variable has a
name, a data type, and a shape described by its list of dimensions
specified when the variable is created. A variable may also have
associated attributes, which may be added, deleted or changed after
the variable is created.

A variable external data type is one of a small set of netCDF
types. In classic CDF-1 and 2 files, only the original six types
are available (byte, character, short, int, float, and
double). CDF-5 adds unsigned byte, unsigned short, unsigned int, 64-bit int,
and unsigned 64-bit int.  In netCDF-4, variables may also use these additional
data types, plus the string data type. Or the user
may define a type, as an opaque blob of bytes, as an array of variable
length arrays, or as a compound type, which acts like a C struct. (See
\ref data_type).

In the CDL notation, classic data type can be used. They
are given the simpler names byte, char, short, int, float, and
double. The name real may be used as a synonym for float in the CDL
notation. The name long is a deprecated synonym for int. For the exact
meaning of each of the types see External Types. The ncgen utility
supports new primitive types with names ubyte, ushort, uint, int64,
uint64, and string.

CDL variable declarations appear after the variable keyword in a CDL
unit. They have the form

\code
          type variable_name ( dim_name_1, dim_name_2, ... );
\endcode

for variables with dimensions, or

\code
          type variable_name;
\endcode

for scalar variables.

In the above CDL example there are six variables. As discussed below,
four of these are coordinate variables (See \ref
coordinate_variables). The remaining variables (sometimes called
primary variables), temp and rh, contain what is usually thought of as
the data. Each of these variables has the unlimited dimension time as
its first dimension, so they are called record variables. A variable
that is not a record variable has a fixed length (number of data
values) given by the product of its dimension lengths. The length of a
record variable is also the product of its dimension lengths, but in
this case the product is variable because it involves the length of
the unlimited dimension, which can vary. The length of the unlimited
dimension is the number of records.

\section coordinate_variables Coordinate Variables

It is legal for a variable to have the same name as a dimension. Such
variables have no special meaning to the netCDF library. However there
is a convention that such variables should be treated in a special way
by software using this library.

A variable with the same name as a dimension is called a
<em>coordinate variable</em>. It typically defines a physical coordinate corresponding to
that dimension. The above CDL example includes the coordinate
variables lat, lon, level and time, defined as follows:

\code
             int     lat(lat), lon(lon), level(level);
             short   time(time);
     ...
     data:
             level   = 1000, 850, 700, 500;
             lat     = 20, 30, 40, 50, 60;
             lon     = -160,-140,-118,-96,-84,-52,-45,-35,-25,-15;
             time    = 12;
\endcode

These define the latitudes, longitudes, barometric pressures and times
corresponding to positions along these dimensions. Thus there is data
at altitudes corresponding to 1000, 850, 700 and 500 millibars; and at
latitudes 20, 30, 40, 50 and 60 degrees north. Note that each
coordinate variable is a vector and has a shape consisting of just the
dimension with the same name.

A position along a dimension can be specified using an index. This is
an integer with a minimum value of 0 for C programs, 1 in Fortran
programs. Thus the 700 millibar level would have an index value of 2
in the example above in a C program, and 3 in a Fortran program.

If a dimension has a corresponding coordinate variable, then this
provides an alternative, and often more convenient, means of
specifying position along it. Current application packages that make
use of coordinate variables commonly assume they are numeric vectors
and strictly monotonic (all values are different and either increasing
or decreasing).

\section attributes Attributes

NetCDF attributes are used to store data about the data (ancillary
data or metadata), similar in many ways to the information stored in
data dictionaries and schema in conventional database systems. Most
attributes provide information about a specific variable. These are
identified by the name (or ID) of that variable, together with the
name of the attribute.

Some attributes provide information about the dataset as a whole and
are called global attributes. These are identified by the attribute
name together with a blank variable name (in CDL) or a special null
"global variable" ID (in C or Fortran).

In netCDF-4 file, attributes can also be added at the group level.

An attribute has an associated variable (the null "global variable"
for a global or group-level attribute), a name, a data type, a length,
and a value. The current version treats all attributes as vectors;
scalar values are treated as single-element vectors.

Conventional attribute names should be used where applicable. New
names should be as meaningful as possible.

The external type of an attribute is specified when it is created. The
types permitted for attributes are the same as the netCDF external
data types for variables. Attributes with the same name for different
variables should sometimes be of different types. For example, the
attribute valid_max specifying the maximum valid data value for a
variable of type int should be of type int, whereas the attribute
valid_max for a variable of type double should instead be of type
double.

Attributes are more dynamic than variables or dimensions; they can be
deleted and have their type, length, and values changed after they are
created, whereas the netCDF interface provides no way to delete a
variable or to change its type or shape.

The CDL notation for defining an attribute is

\code
         variable_name:attribute_name = list_of_values;
\endcode

for a variable attribute, or

\code
         :attribute_name = list_of_values;
\endcode

for a global attribute.

For the netCDF classic model, the type and length of each attribute
are not explicitly declared in CDL; they are derived from the values
assigned to the attribute. All values of an attribute must be of the
same type. The notation used for constant values of the various netCDF
types is discussed later (see \ref cdl_syntax).

The extended CDL syntax for the enhanced data model supported by
netCDF-4 allows optional type specifications, including user-defined
types, for attributes of user-defined types. See ncdump output or the
reference documentation for ncgen for details of the extended CDL
syntax.

In the netCDF example (see \ref data_model), units is an attribute for
the variable lat that has a 13-character array value
'degrees_north'. And valid_range is an attribute for the variable rh
that has length 2 and values '0.0' and '1.0'.

One global attribute, called “source”, is defined for the example
netCDF dataset. This is a character array intended for documenting the
data. Actual netCDF datasets might have more global attributes to
document the origin, history, conventions, and other characteristics
of the dataset as a whole.

Most generic applications that process netCDF datasets assume standard
attribute conventions and it is strongly recommended that these be
followed unless there are good reasons for not doing so. For
information about units, long_name, valid_min, valid_max, valid_range,
scale_factor, add_offset, _FillValue, and other conventional
attributes, see \ref attribute_conventions.

Attributes may be added to a netCDF dataset long after it is first
defined, so you don't have to anticipate all potentially useful
attributes. However adding new attributes to an existing classic
format dataset can incur the same expense as copying the
dataset. For a more extensive discussion see \ref file_structure_and_performance.

\section differences_atts_vars Differences between Attributes and Variables

In contrast to variables, which are intended for bulk data, attributes
are intended for ancillary data, or information about the data. The
total amount of ancillary data associated with a netCDF object, and
stored in its attributes, is typically small enough to be
memory-resident. However variables are often too large to entirely fit
in memory and must be split into sections for processing.

Another difference between attributes and variables is that variables
may be multidimensional. Attributes are all either scalars
(single-valued) or vectors (a single, fixed dimension).

Variables are created with a name, type, and shape before they are
assigned data values, so a variable may exist with no values. The
value of an attribute is specified when it is created, unless it is a
zero-length attribute.

A variable may have attributes, but an attribute cannot have
attributes. Attributes assigned to variables may have the same units
as the variable (for example, valid_range) or have no units (for
example, scale_factor). If you want to store data that requires units
different from those of the associated variable, it is better to use a
variable than an attribute. More generally, if data require ancillary
data to describe them, are multidimensional, require any of the
defined netCDF dimensions to index their values, or require a
significant amount of storage, that data should be represented using
variables rather than attributes.

\section object_name NetCDF Names

\subsection Permitted Characters in NetCDF Names

The names of dimensions, variables and attributes (and, in netCDF-4
files, groups, user-defined types, compound member names, and
enumeration symbols) consist of arbitrary sequences of alphanumeric
characters, underscore '_', period '.', plus '+', hyphen '-', or at
sign '@', but beginning with an alphanumeric character or
underscore. However names commencing with underscore are reserved for
system use.

Beginning with versions 3.6.3 and 4.0, names may also include UTF-8
encoded Unicode characters as well as other special characters, except
for the character '/', which may not appear in a name.

Names that have trailing space characters are also not permitted.

Case is significant in netCDF names.

\subsection Name Length

A zero-length name is not allowed.

Names longer than ::NC_MAX_NAME will not be accepted any netCDF define
function. An error of ::NC_EMAXNAME will be returned.

All netCDF inquiry functions will return names of maximum size
::NC_MAX_NAME for netCDF files. Since this does not include the
terminating NULL, space should be reserved for NC_MAX_NAME + 1
characters.

\subsection NetCDF Conventions

Some widely used conventions restrict names to only alphanumeric
characters or underscores.

\note Note that, when using the DAP2 protocol to access netCDF data,
there are \em reserved keywords, the use of which may result in
undefined behavior.  See \ref dap2_reserved_keywords for more
information.

\section archival Is NetCDF a Good Archive Format?

NetCDF classic formats can be used as a
general-purpose archive format for storing arrays. Compression of data
is possible with netCDF (e.g., using arrays of eight-bit or 16-bit
integers to encode low-resolution floating-point numbers instead of
arrays of 32-bit numbers), or the resulting data file may be
compressed before storage (but must be uncompressed before it is
read). Hence, using these netCDF formats may require more space than
special-purpose archive formats that exploit knowledge of particular
characteristics of specific datasets.

With netCDF-4/HDF5 format, the zlib library can provide compression on
a per-variable basis. That is, some variables may be compressed,
others not. In this case the compression and decompression of data
happen transparently to the user, and the data may be stored, read,
and written compressed.


\section background Background and Evolution of the NetCDF Interface

The development of the netCDF interface began with a modest goal
related to Unidata's needs: to provide a common interface between
Unidata applications and real-time meteorological data. Since Unidata
software was intended to run on multiple hardware platforms with
access from both C and FORTRAN, achieving Unidata's goals had the
potential for providing a package that was useful in a broader
context. By making the package widely available and collaborating with
other organizations with similar needs, we hoped to improve the then
current situation in which software for scientific data access was
only rarely reused by others in the same discipline and almost never
reused between disciplines (Fulker, 1988).

Important concepts employed in the netCDF software originated in a
paper (Treinish and Gough, 1987) that described data-access software
developed at the NASA Goddard National Space Science Data Center
(NSSDC). The interface provided by this software was called the Common
Data Format (CDF). The NASA CDF was originally developed as a
platform-specific FORTRAN library to support an abstraction for
storing arrays.

The NASA CDF package had been used for many different kinds of data in
an extensive collection of applications. It had the virtues of
simplicity (only 13 subroutines), independence from storage format,
generality, ability to support logical user views of data, and support
for generic applications.

Unidata held a workshop on CDF in Boulder in August 1987. We proposed
exploring the possibility of collaborating with NASA to extend the CDF
FORTRAN interface, to define a C interface, and to permit the access
of data aggregates with a single call, while maintaining compatibility
with the existing NASA interface.

Independently, Dave Raymond at the New Mexico Institute of Mining and
Technology had developed a package of C software for UNIX that
supported sequential access to self-describing array-oriented data and
a "pipes and filters" (or "data flow") approach to processing,
analyzing, and displaying the data. This package also used the "Common
Data Format" name, later changed to C-Based Analysis and Display
System (CANDIS). Unidata learned of Raymond's work (Raymond, 1988),
and incorporated some of his ideas, such as the use of named
dimensions and variables with differing shapes in a single data
object, into the Unidata netCDF interface.

In early 1988, Glenn Davis of Unidata developed a prototype netCDF
package in C that was layered on XDR. This prototype proved that a
single-file, XDR-based implementation of the CDF interface could be
achieved at acceptable cost and that the resulting programs could be
implemented on both UNIX and VMS systems. However, it also
demonstrated that providing a small, portable, and NASA CDF-compatible
FORTRAN interface with the desired generality was not
practical. NASA's CDF and Unidata's netCDF have since evolved
separately, but recent CDF versions share many characteristics with
netCDF.

In early 1988, Joe Fahle of SeaSpace, Inc. (a commercial software
development firm in San Diego, California), a participant in the 1987
Unidata CDF workshop, independently developed a CDF package in C that
extended the NASA CDF interface in several important ways (Fahle,
1989). Like Raymond's package, the SeaSpace CDF software permitted
variables with unrelated shapes to be included in the same data object
and permitted a general form of access to multidimensional
arrays. Fahle's implementation was used at SeaSpace as the
intermediate form of storage for a variety of steps in their
image-processing system. This interface and format have subsequently
evolved into the Terascan data format.

After studying Fahle's interface, we concluded that it solved many of
the problems we had identified in trying to stretch the NASA interface
to our purposes. In August 1988, we convened a small workshop to agree
on a Unidata netCDF interface, and to resolve remaining open
issues. Attending were Joe Fahle of SeaSpace, Michael Gough of Apple
(an author of the NASA CDF software), Angel Li of the University of
Miami (who had implemented our prototype netCDF software on VMS and
was a potential user), and Unidata systems development
staff. Consensus was reached at the workshop after some further
simplifications were discovered. A document incorporating the results
of the workshop into a proposed Unidata netCDF interface specification
was distributed widely for comments before Glenn Davis and Russ Rew
implemented the first version of the software. Comparison with other
data-access interfaces and experience using netCDF are discussed in
Rew and Davis (1990a), Rew and Davis (1990b), Jenter and Signell
(1992), and Brown, Folk, Goucher, and Rew (1993).

In October 1991, we announced version 2.0 of the netCDF software
distribution. Slight modifications to the C interface (declaring
dimension lengths to be long rather than int) improved the usability
of netCDF on inexpensive platforms such as MS-DOS computers, without
requiring recompilation on other platforms. This change to the
interface required no changes to the associated file format.

Release of netCDF version 2.3 in June 1993 preserved the same file
format but added single call access to records, optimizations for
accessing cross-sections involving non-contiguous data, subsampling
along specified dimensions (using 'strides'), accessing non-contiguous
data (using 'mapped array sections'), improvements to the ncdump and
ncgen utilities, and an experimental C++ interface.

In version 2.4, released in February 1996, support was added for new
platforms and for the C++ interface, significant optimizations were
implemented for supercomputer architectures, and the file format was
formally specified in an appendix to the User's Guide.

FAN (File Array Notation), software providing a high-level interface
to netCDF data, was made available in May 1996. The capabilities of
the FAN utilities include extracting and manipulating array data from
netCDF datasets, printing selected data from netCDF arrays, copying
ASCII data into netCDF arrays, and performing various operations (sum,
mean, max, min, product, and others) on netCDF arrays.

In 1996 and 1997, Joe Sirott implemented and made available the first
implementation of a read-only netCDF interface for Java, Bill Noon
made a Python module available for netCDF, and Konrad Hinsen
contributed another netCDF interface for Python.

In May 1997, Version 3.3 of netCDF was released. This included a new
type-safe interface for C and Fortran, as well as many other
improvements. A month later, Charlie Zender released version 1.0 of
the NCO (netCDF Operators) package, providing command-line utilities
for general purpose operations on netCDF data.

Version 3.4 of Unidata's netCDF software, released in March 1998,
included initial large file support, performance enhancements, and
improved Cray platform support. Later in 1998, Dan Schmitt provided a
Tcl/Tk interface, and Glenn Davis provided version 1.0 of netCDF for
Java.

In May 1999, Glenn Davis, who was instrumental in creating and
developing netCDF, died in a small plane crash during a
thunderstorm. The memory of Glenn's passions and intellect continue to
inspire those of us who worked with him.

In February 2000, an experimental Fortran 90 interface developed by
Robert Pincus was released.

John Caron released netCDF for Java, version 2.0 in February
2001. This version incorporated a new high-performance package for
multidimensional arrays, simplified the interface, and included
OpenDAP (known previously as DODS) remote access, as well as remote
netCDF access via HTTP contributed by Don Denbo.

In March 2001, netCDF 3.5.0 was released. This release fully
integrated the new Fortran 90 interface, enhanced portability,
improved the C++ interface, and added a few new tuning functions.

Also in 2001, Takeshi Horinouchi and colleagues made a netCDF
interface for Ruby available, as did David Pierce for the R language
for statistical computing and graphics. Charles Denham released
WetCDF, an independent implementation of the netCDF interface for
Matlab, as well as updates to the popular netCDF Toolbox for Matlab.

In 2002, Unidata and collaborators developed NcML, an XML
representation for netCDF data useful for cataloging data holdings,
aggregation of data from multiple datasets, augmenting metadata in
existing datasets, and support for alternative views of data. The Java
interface currently provides access to netCDF data through NcML.

Additional developments in 2002 included translation of C and Fortran
User Guides into Japanese by Masato Shiotani and colleagues, creation
of a “Best Practices” guide for writing netCDF files, and provision of
an Ada-95 interface by Alexandru Corlan.

In July 2003 a group of researchers at Northwestern University and
Argonne National Laboratory (Jianwei Li, Wei-keng Liao, Alok
Choudhary, Robert Ross, Rajeev Thakur, William Gropp, and Rob Latham)
contributed a new parallel interface for writing and reading netCDF
data, tailored for use on high performance platforms with parallel
I/O. The implementation built on the MPI-IO interface, providing
portability to many platforms.

In October 2003, Greg Sjaardema contributed support for an alternative
format with 64-bit offsets, to provide more complete support for very
large files. These changes, with slight modifications at Unidata, were
incorporated into version 3.6.0, released in December, 2004.

In 2004, thanks to a NASA grant, Unidata and NCSA began a
collaboration to increase the interoperability of netCDF and HDF5, and
bring some advanced HDF5 features to netCDF users.

In February, 2006, release 3.6.1 fixed some minor bugs.

In March, 2007, release 3.6.2 introduced an improved build system that
used automake and libtool, and an upgrade to the most recent autoconf
release, to support shared libraries and the netcdf-4 builds. This
release also introduced the NetCDF Tutorial and example programs.

The first beta release of netCDF-4.0 was celebrated with a giant party
at Unidata in April, 2007. Over 2000 people danced 'til dawn at the
NCAR Mesa Lab, listening to the Flaming Lips and the Denver Gilbert &
Sullivan repertory company. Brittany Spears performed the
world-premire of her smash hit "Format me baby, one more time."

In June, 2008, netCDF-4.0 was released. Version 3.6.3, the same code
but with netcdf-4 features turned off, was released at the same
time. The 4.0 release uses HDF5 1.8.1 as the data storage layer for
netcdf, and introduces many new features including groups and
user-defined types. The 3.6.3/4.0 releases also introduced handling of
UTF8-encoded Unicode names.

NetCDF-4.1.1 was released in April, 2010, provided built-in client
support for the DAP protocol for accessing data from remote OPeNDAP
servers, full support for the enhanced netCDF-4 data model in the
ncgen utility, a new nccopy utility for copying and conversion among
netCDF format variants, ability to read some HDF4/HDF5 data archives
through the netCDF C or Fortran interfaces, support for parallel I/O
on netCDF classic files (CDF-1, 2, and 5) using the PnetCDF
library from Argonne/Northwestern, a new nc-config
utility to help compile and link programs that use netCDF, inclusion
of the UDUNITS library for handling “units” attributes, and inclusion
of libcf to assist in creating data compliant with the Climate and
Forecast (CF) metadata conventions.

In September, 2010, the Netcdf-Java/CDM (Common Data Model) version
4.2 library was declared stable and made available to users. This
100%-Java implementation provided a read-write interface to netCDF-3
classic format files, as well as a read-only interface to
netCDF-4 enhanced model data and many other formats of scientific data
through a common (CDM) interface. More recent releases support
writing netCDF-4 data. The NetCDF-Java library also
implements NcML, which allows you to add metadata to CDM datasets. A ToolsUI
application is also included that provides a graphical user interface
to capabilities similar to the C-based ncdump and ncgen utilities, as
well as CF-compliance checking and many other features.



\section remote_client The Remote Data Access Client

Starting with version 4.1.1 the netCDF C libraries and utilities have
supported remote data access.

\section data_access Data Access

To access (read or write) netCDF data you specify an open netCDF
dataset, a netCDF variable, and information (e.g., indices)
identifying elements of the variable. The name of the access function
corresponds to the internal type of the data. If the internal type has
a different representation from the external type of the variable, a
conversion between the internal type and external type will take place
when the data is read or written.

Access to data in classic formats is direct. Access
to netCDF-4 data is buffered by the HDF5 layer. In either case you can
access a small subset of data from a large dataset efficiently,
without first accessing all the data that precedes it.

Reading and writing data by specifying a variable, instead of a
position in a file, makes data access independent of how many other
variables are in the dataset, making programs immune to data format
changes that involve adding more variables to the data.

In the C and FORTRAN interfaces, datasets are not specified by name
every time you want to access data, but instead by a small integer
called a dataset ID, obtained when the dataset is first created or
opened.

Similarly, a variable is not specified by name for every data access
either, but by a variable ID, a small integer used to identify each
variable in a netCDF dataset.

\section forms_of_data_access Forms of Data Access

The netCDF interface supports several forms of direct access to data
values in an open netCDF dataset. We describe each of these forms of
access in order of increasing generality:
- access to all elements;
- access to individual elements, specified with an index vector;
- access to array sections, specified with an index vector, and count vector;
- access to sub-sampled array sections, specified with an index
  vector, count vector, and stride vector; and
- access to mapped array sections, specified with an index vector,
  count vector, stride vector, and an index mapping vector.

The four types of vector (index vector, count vector, stride vector
and index mapping vector) each have one element for each dimension of
the variable. Thus, for an n-dimensional variable (rank = n),
n-element vectors are needed. If the variable is a scalar (no
dimensions), these vectors are ignored.

An array section is a "slab" or contiguous rectangular block that is
specified by two vectors. The index vector gives the indices of the
element in the corner closest to the origin. The count vector gives
the lengths of the edges of the slab along each of the variable's
dimensions, in order. The number of values accessed is the product of
these edge lengths.

A subsampled array section is similar to an array section, except that
an additional stride vector is used to specify sampling. This vector
has an element for each dimension giving the length of the strides to
be taken along that dimension. For example, a stride of 4 means every
fourth value along the corresponding dimension. The total number of
values accessed is again the product of the elements of the count
vector.

A mapped array section is similar to a subsampled array section except
that an additional index mapping vector allows one to specify how data
values associated with the netCDF variable are arranged in memory. The
offset of each value from the reference location, is given by the sum
of the products of each index (of the imaginary internal array which
would be used if there were no mapping) by the corresponding element
of the index mapping vector. The number of values accessed is the same
as for a subsampled array section.

The use of mapped array sections is discussed more fully below, but
first we present an example of the more commonly used array-section
access.

\section c_array_section_access A C Example of Array-Section Access

Assume that in our earlier example of a netCDF dataset, we wish to
read a cross-section of all the data for the temp variable at one
level (say, the second), and assume that there are currently three
records (time values) in the netCDF dataset. Recall that the
dimensions are defined as

\code
       lat = 5, lon = 10, level = 4, time = unlimited;
\endcode

and the variable temp is declared as

\code
       float   temp(time, level, lat, lon);
\endcode

in the CDL notation.

A corresponding C variable that holds data for only one level might be
declared as:

\code
     #define LATS  5
     #define LONS 10
     #define LEVELS 1
     #define TIMES 3                 /* currently */
         ...
     float   temp[TIMES*LEVELS*LATS*LONS];
\endcode

     to keep the data in a one-dimensional array, or

\code
         ...
     float   temp[TIMES][LEVELS][LATS][LONS];
\endcode

using a multidimensional array declaration.

To specify the block of data that represents just the second level,
all times, all latitudes, and all longitudes, we need to provide a
start index and some edge lengths. The start index should be (0, 1, 0,
0) in C, because we want to start at the beginning of each of the
time, lon, and lat dimensions, but we want to begin at the second
value of the level dimension. The edge lengths should be (3, 1, 5, 10)
in C, (since we want to get data for all three time values, only one
level value, all five lat values, and all 10 lon values. We should
expect to get a total of 150 floating-point values returned (3 * 1 * 5
* 10), and should provide enough space in our array for this many. The
order in which the data will be returned is with the last dimension,
lon, varying fastest:

\code
          temp[0][1][0][0]
          temp[0][1][0][1]
          temp[0][1][0][2]
          temp[0][1][0][3]

                ...

          temp[2][1][4][7]
          temp[2][1][4][8]
          temp[2][1][4][9]
\endcode

Different dimension orders for the C, FORTRAN, or other language
interfaces do not reflect a different order for values stored on the
disk, but merely different orders supported by the procedural
interfaces to the languages. In general, it does not matter whether a
netCDF dataset is written using the C, FORTRAN, or another language
interface; netCDF datasets written from any supported language may be
read by programs written in other supported languages.  3.4.3 More on
General Array Section Access for C

The use of mapped array sections allows non-trivial relationships
between the disk addresses of variable elements and the addresses
where they are stored in memory. For example, a matrix in memory could
be the transpose of that on disk, giving a quite different order of
elements. In a regular array section, the mapping between the disk and
memory addresses is trivial: the structure of the in-memory values
(i.e., the dimensional lengths and their order) is identical to that
of the array section. In a mapped array section, however, an index
mapping vector is used to define the mapping between indices of netCDF
variable elements and their memory addresses.

With mapped array access, the offset (number of array elements) from
the origin of a memory-resident array to a particular point is given
by the inner product[1] of the index mapping vector with the point's
coordinate offset vector. A point's coordinate offset vector gives,
for each dimension, the offset from the origin of the containing array
to the point.In C, a point's coordinate offset vector is the same as
its coordinate vector.

The index mapping vector for a regular array section would have–in
order from most rapidly varying dimension to most slowly–a constant 1,
the product of that value with the edge length of the most rapidly
varying dimension of the array section, then the product of that value
with the edge length of the next most rapidly varying dimension, and
so on. In a mapped array, however, the correspondence between netCDF
variable disk locations and memory locations can be different.

For example, the following C definitions:

\code
     struct vel {
         int flags;
         float u;
         float v;
     } vel[NX][NY];
     ptrdiff_t imap[2] = {
         sizeof(struct vel),
         sizeof(struct vel)*NY
     };
\endcode

where imap is the index mapping vector, can be used to access the
memory-resident values of the netCDF variable, vel(NY,NX), even though
the dimensions are transposed and the data is contained in a 2-D array
of structures rather than a 2-D array of floating-point values.

A detailed example of mapped array access is presented in the
description of the interfaces for mapped array access. See Write a
Mapped Array of Values - nc_put_varm_ type.

Note that, although the netCDF abstraction allows the use of
subsampled or mapped array-section access there use is not
required. If you do not need these more general forms of access, you
may ignore these capabilities and use single value access or regular
array section access instead.
