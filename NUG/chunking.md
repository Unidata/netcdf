# Improving Performance with Chunking {#netcdf_perf_chunking}

[TOC]

# The Chunk Cache {#chunk_cache}

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

# The Default Chunking Scheme {#default_chunking_4_1}

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

# Chunking and Parallel I/O {#chunking_parallel_io}

When files are opened for read/write parallel I/O access, the chunk
cache is not used. Therefore it is important to open parallel files
with read only access when possible, to achieve the best performance.

# A Utility to Help Benchmark Results: bm_file {#bm_file}

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
