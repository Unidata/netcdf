# NetCDF Utilities {#netcdf_utilities_guide}

[TOC]
\tableofcontents

# ncdump {#ncdump_guide}

Convert NetCDF file to text form (CDL)

## ncdump synopsis {#ncdump_SYNOPSIS}

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
