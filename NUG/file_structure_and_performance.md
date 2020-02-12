# File Structure and Performance {#file_structure_and_performance}

[TOC]

## Parts of a NetCDF Classic File {#classic_file_parts}

A netCDF classic dataset (including CDF-1, 2, and 5 formats) is stored as a
single file comprising two parts:

* a header, containing all the information about dimensions, attributes,
and variables except for the variable data;
* a data part, comprising fixed-size data, containing the data for
variables that don't have an unlimited dimension; and variable-size
data, containing the data for variables that have an unlimited
dimension.

Both the header and data parts are represented in a
machine-independent form. This form is very similar to XDR (eXternal
Data Representation), extended to support efficient storage of arrays
of non-byte data.

The header at the beginning of the file contains information about the
dimensions, variables, and attributes in the file, including their
names, types, and other characteristics. The information about each
variable includes the offset to the beginning of the variable's data
for fixed-size variables or the relative offset of other variables
within a record. The header also contains dimension lengths and
information needed to map multidimensional indices for each variable
to the appropriate offsets.

By default, this header has little usable extra space; it is only as
large as it needs to be for the dimensions, variables, and attributes
(including all the attribute values) in the netCDF dataset, with a
small amount of extra space from rounding up to the nearest disk block
size. This has the advantage that netCDF files are compact, requiring
very little overhead to store the ancillary data that makes the
datasets self-describing. A disadvantage of this organization is that
any operation on a netCDF dataset that requires the header to grow
(or, less likely, to shrink), for example adding new dimensions or new
variables, requires moving the data by copying it. This expense is
incurred when the enddef function is called: nc_enddef() in C,
NF_ENDDEF() in Fortran, after a previous call to the redef function:
nc_redef() in C or NF_REDEF() in Fortran. If you create all necessary
dimensions, variables, and attributes before writing data, and avoid
later additions and renamings of netCDF components that require more
space in the header part of the file, you avoid the cost associated
with later changing the header.

Alternatively, you can use an alternative version of the enddef
function with two underbar characters instead of one to explicitly
reserve extra space in the file header when the file is created: in C
nc__enddef(), in Fortran NF__ENDDEF(), after a previous call to the
redef function. This avoids the expense of moving all the data later
by reserving enough extra space in the header to accommodate
anticipated changes, such as the addition of new attributes or the
extension of existing string attributes to hold longer strings.

When the size of the header is changed, data in the file is moved, and
the location of data values in the file changes. If another program is
reading the netCDF dataset during redefinition, its view of the file
will be based on old, probably incorrect indexes. If netCDF datasets
are shared across redefinition, some mechanism external to the netCDF
library must be provided that prevents access by readers during
redefinition, and causes the readers to call nc_sync/NF_SYNC before
any subsequent access.

The fixed-size data part that follows the header contains all the
variable data for variables that do not employ an unlimited
dimension. The data for each variable is stored contiguously in this
part of the file. If there is no unlimited dimension, this is the last
part of the netCDF file.

The record-data part that follows the fixed-size data consists of a
variable number of fixed-size records, each of which contains data for
all the record variables. The record data for each variable is stored
contiguously in each record.

The order in which the variable data appears in each data section is
the same as the order in which the variables were defined, in
increasing numerical order by netCDF variable ID. This knowledge can
sometimes be used to enhance data access performance, since the best
data access is currently achieved by reading or writing the data in
sequential order.

## Parts of a NetCDF-4 HDF5 File {#parts_of_netcdf4}

NetCDF-4 files are created with the HDF5 library, and are HDF5 files
in every way, and can be read without the netCDF-4 interface. (Note
that modifying these files with HDF5 will almost certainly make them
unreadable to netCDF-4.)

Groups in a netCDF-4 file correspond with HDF5 groups (although the
netCDF-4 tree is rooted not at the HDF5 root, but in group “_netCDF”).

Variables in netCDF coorespond with identically named datasets in
HDF5. Attributes similarly.

Since there is more metadata in a netCDF file than an HDF5 file,
special datasets are used to hold netCDF metadata.

\deprecated The _netcdf_dim_info dataset (in group _netCDF) contains the ids of
the shared dimensions, and their length (0 for unlimited dimensions).

\deprecated The _netcdf_var_info dataset (in group _netCDF) holds an array of
compound types which contain the variable ID, and the associated
dimension ids.

## The Extended XDR Layer {#xdr_layer}

XDR is a standard for describing and encoding data and a library of
functions for external data representation, allowing programmers to
encode data structures in a machine-independent way. Classic
netCDF employs an extended form of XDR for representing
information in the header part and the data parts. This extended XDR
is used to write portable data that can be read on any other machine
for which the library has been implemented.

The cost of using a canonical external representation for data varies
according to the type of data and whether the external form is the
same as the machine's native form for that type.

For some data types on some machines, the time required to convert
data to and from external form can be significant. The worst case is
reading or writing large arrays of floating-point data on a machine
that does not use IEEE floating-point as its native representation.

## Large File Support {#large_file_support}

It is possible to write netCDF files that exceed 2 GiByte on platforms
that have "Large File Support" (LFS). Such files are
platform-independent to other LFS platforms, but trying to open them
on an older platform without LFS yields a "file too large" error.

Without LFS, no files larger than 2 GiBytes can be used. The rest of
this section applies only to systems with LFS.

The original binary format of netCDF (classic format) limits the size
of data files by using a signed 32-bit offset within its internal
structure. Files larger than 2 GiB can be created, with certain
limitations. See \ref limitations.

In version 3.6.0, netCDF included its first-ever variant of the
underlying data format. The new format introduced in 3.6.0 uses 64-bit
file offsets in place of the 32-bit offsets. The new format is also referred as
CDF-2 format as it bears a signature string of "CDF2" in the file header.
There are still some
limits on the sizes of variables, but the new format can create very
large datasets. See \ref netcdf_64bit_offset_format.

Starting from version 4.4.0, netCDF included the support of CDF-5 format.  In
order to allows defining large array variables with more than 4-billion
elements, CDF-5 replaces most of the 32-bit integers used to describe metadata
with 64-bit integers. In addition, it supports the following new external data
types: NC_UBYTE, NC_USHORT, NC_UINT, NC_INT64, and NC_UINT64. The CDF-5 format
specifications can be found in
(http://cucis.ece.northwestern.edu/projects/PnetCDF/CDF-5.html).

NetCDF-4 variables and files can be any size supported by the
underlying file system.

The original data format (netCDF classic), is still the default data
format for the netCDF library.

The following table summarizes the size limitations of various
permutations of LFS support, netCDF version, and data format. Note
that 1 GiB = 2^30 bytes or about 1.07e+9 bytes, 1 EiB = 2^60 bytes or
about 1.15e+18 bytes. Note also that all sizes are really 4 bytes less
than the ones given below. For example the maximum size of a fixed
variable in netCDF 3.6 classic format is really 2 GiB - 4 bytes.

Limits                                     | No LFS     |  v3.5   |  v3.6/classic |  v3.6/64-bit offset |    v4.0/netCDF-4 and CDF-5
-------------------------------------------|------------|---------|---------------|---------------------|-----------------
Max File Size                              | 2 GiB      | 8 EiB   |  8 EiB        |  8 EiB              | unlimited
Max Number of Fixed Vars > 2 GiB           |     0      | 1 (last)|  1 (last)     |  2^32               | unlimited
Max Record Vars w/ Rec Size > 2 GiB        |     0      | 1 (last)|  1 (last)     |  2^32               | unlimited
Max Size of Fixed/Record Size of Record Var| 2 GiB      | 2 GiB   |  2 GiB        |  4 GiB              | unlimited
Max Record Size                            | 2 GiB/nrecs| 4 GiB   |  8 EiB/nrecs  |  8 EiB/nrecs        | unlimited

For more information about the different file formats of netCDF see
\ref select_format "How to Select the Format".

## NetCDF 64-bit Offset Format Limitations {#offset_format_limitations}

Although the 64-bit offset format (CDF-2) allows the creation of much larger
netCDF files than was possible with the classic format, there are
still some restrictions on the size of variables.

It's important to note that without Large File Support (LFS) in the
operating system, it's impossible to create any file larger than 2
GiBytes. Assuming an operating system with LFS, the following
restrictions apply to the CDF-2 format.

No fixed-size variable can require more than 2^32 - 4 bytes (i.e. 4GiB
minus 4 bytes, or 4,294,967,292 bytes) of storage for its data, unless
it is the last fixed-size variable and there are no record
variables. When there are no record variables, the last fixed-size
variable can be any size supported by the file system, e.g. terabytes.

A CDF-2 file can have up to 2^32 - 1 fixed sized
variables, each under 4GiB in size. If there are no record variables
in the file the last fixed variable can be any size.

No record variable can require more than 2^32 - 4 bytes of storage for
each record's worth of data, unless it is the last record variable. A
CDF-2 file can have up to 2^32 - 1 records, of
up to 2^32 - 1 variables, as long as the size of one record's data for
each record variable except the last is less than 4 GiB - 4.

Note also that all netCDF variables and records are padded to 4 byte
boundaries.

## NetCDF Classic Format Limitations {#classic_format_limitations}

There are important constraints on the structure of large netCDF
classic files that result from the 32-bit relative offsets that are
part of the netCDF classic file format (CDF-1):

The maximum size of a record in the classic format in versions 3.5.1
and earlier is 2^32 - 4 bytes, or about 4 GiB. In versions 3.6.0 and
later, there is no such restriction on total record size for the
classic formats.

If you don't use the unlimited dimension, only one variable can exceed
2 GiB in size, but it can be as large as the underlying file system
permits. It must be the last variable in the dataset, and the offset
to the beginning of this variable must be less than about 2 GiB.

The limit is really 2^31 - 4. If you were to specify a variable size
of 2^31 -3, for example, it would be rounded up to the nearest
multiple of 4 bytes, which would be 2^31, which is larger than the
largest signed integer, 2^31 - 1.

For example, the structure of the data might be something like:

\code
     netcdf bigfile1 {
         dimensions:
            x=2000;
            y=5000;
            z=10000;
         variables:
            double x(x);         // coordinate variables
            double y(y);
            double z(z);
            double var(x, y, z); // 800 Gbytes
         }
\endcode

If you use the unlimited dimension, record variables may exceed 2 GiB
in size, as long as the offset of the start of each record variable
within a record is less than 2 GiB - 4. For example, the structure of
the data in a 2.4 Tbyte file might be something like:

\code
     netcdf bigfile2 {
         dimensions:
            x=2000;
            y=5000;
            z=10;
            t=UNLIMITED;         // 1000 records, for example
         variables:
            double x(x);         // coordinate variables
            double y(y);
            double z(z);
            double t(t);
                                 // 3 record variables, 2400000000 bytes per record
            double var1(t, x, y, z);
            double var2(t, x, y, z);
            double var3(t, x, y, z);
         }
\endcode

## The NetCDF-3 I/O Layer {#netcdf_3_io}

The following discussion applies only to netCDF classic files (i.e. CDF-1, 2,
and 5 formats). For netCDF-4 files, the I/O layer is the HDF5 library.

For netCDF classic offset files, an I/O layer implemented
much like the C standard I/O (stdio) library is used by netCDF to read
and write portable data to netCDF datasets. Hence an understanding of
the standard I/O library provides answers to many questions about
multiple processes accessing data concurrently, the use of I/O
buffers, and the costs of opening and closing netCDF files. In
particular, it is possible to have one process writing a netCDF
dataset while other processes read it.

Data reads and writes are no more atomic than calls to stdio fread()
and fwrite(). An nc_sync/NF_SYNC call is analogous to the fflush call
in the C standard I/O library, writing unwritten buffered data so
other processes can read it; The C function nc_sync(), or
the Fortran function NF_SYNC(), also brings header changes
up-to-date (for example, changes to attribute values). Opening the
file with the NC_SHARE (in C) or the NF_SHARE (in Fortran) is
analogous to setting a stdio stream to be unbuffered with the _IONBF
flag to setvbuf.

As in the stdio library, flushes are also performed when "seeks" occur
to a different area of the file. Hence the order of read and write
operations can influence I/O performance significantly. Reading data
in the same order in which it was written within each record will
minimize buffer flushes.

You should not expect netCDF classic format data
access to work with multiple writers having the same file open for
writing simultaneously.

It is possible to tune an implementation of netCDF for some platforms
by replacing the I/O layer with a different platform-specific I/O
layer. This may change the similarities between netCDF and standard
I/O, and hence characteristics related to data sharing, buffering, and
the cost of I/O operations.

The distributed netCDF implementation is meant to be
portable. Platform-specific ports that further optimize the
implementation for better I/O performance are practical in some cases.

## Parallel Access with NetCDF-4 {# parallel_access}

Use the special parallel open (or create) calls to open (or create) a
file, and then to use parallel I/O to read or write that file (see
nc_open_par()).

Note that the chunk cache is turned off if a file is opened for
parallel I/O in read/write mode. Open the file in read-only mode to
engage the chunk cache.

NetCDF uses the HDF5 parallel programming model for parallel I/O with
netCDF-4/HDF5 files. The HDF5 tutorial
(http://hdfgroup.org/HDF5//HDF5/Tutor) is a good reference.

For classic files, netCDF uses the PnetCDF library from Argonne National
Labs/Northwestern University. For parallel access of files in classic formats,
netCDF must be configured with the --with-pnetcdf option at build time. See the
PnetCDF site for more information (https://parallel-netcdf.github.io).
Addition information and example programs can be found in
(http://cucis.ece.northwestern.edu/projects/PnetCDF/#InteroperabilityWithNetCDF4)

## Interoperability with HDF5 {#interoperability_with_hdf5}

To create HDF5 files that can be read by netCDF-4, use the latest in
the HDF5 1.8.x or 1.10.x series.

HDF5 has some features that will not be supported by netCDF-4, and
will cause problems for interoperability:
  - HDF5 allows a Group to be both an ancestor and a descendant of
    another Group, creating cycles in the subgroup graph. HDF5 also
    permits multiple parents for a Group. In the netCDF-4 data model,
    Groups form a tree with no cycles, so each Group (except the
    top-level unnamed Group) has a unique parent.
  - HDF5 supports "references" which are like pointers to objects and
    data regions within a file. The netCDF-4 data model omits
    references.
  - HDF5 supports some primitive types that are not included in the
    netCDF-4 data model, including H5T_TIME and H5T_BITFIELD.
  - HDF5 supports multiple names for data objects like Datasets
    (netCDF-4 variables) with no distinguished name. The netCDF-4 data
    model requires that each variable, attribute, dimension, and group
    have a single distinguished name.
  - HDF5 (like netCDF) supports scalar attributes, but netCDF-4 cannot
    read scalar HDF5 attributes (unless it is a string
    attribute). This limitation will be removed in a future release of
    netCDF.

These are fairly easy requirements to meet, but there is one relating
to shared dimensions which is a little more challenging. Every HDF5
dataset must have a dimension scale attached to each dimension.

Dimension scales are a new feature for HF 1.8, which allow
specification of shared dimensions.

Without creation order in the HDF5 file, the files will still be
readable to netCDF-4, it's just that netCDF-4 will number the
variables in alphabetical, rather than creation, order.

Interoperability is a complex task, and all of this is in the alpha
release stage. It is tested in libsrc4/tst_interops.c, which contains
some examples of how to create HDF5 files, modify them in netCDF-4,
and then verify them in HDF5. (And vice versa).
