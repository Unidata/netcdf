NetCDF-4 Filter Support
============================
<!-- double header is needed to workaround doxygen bug -->

NetCDF-4 and NCZarr Filter Support {#filters}
============================

[TOC]

The netCDF library supports a general filter mechanism to apply various
kinds of filters to datasets before reading or writing.

The netCDF enhanced (aka netCDF-4) library inherits this capability since it depends on the HDF5 library.
The HDF5 library (1.8.11 and later) supports filters, and netCDF is based closely on that underlying HDF5 mechanism.

The NCZarr/Zarr implementation also supports filters.
It utilizes the HDF5-style filters as its implementation, but extends
them to support the NumCodecs JSON-based format as an alternate to the
HDF5 unsigned integer format.

In all cases, filters assume that a variable has chunking
defined and each chunk is filtered before writing and
"unfiltered" after reading and before passing the data to the
user.

In the event that multiple filters are defined on a variable,
they are applied in first-defined order on writing and on the
reverse order when reading.

The most common kind of filter is a compression-decompression filter, and that is the focus of this document.

This document first covers the HDF5-style filters and then the NCZarr-style filters.

# Enabling A HDF5 Compression Filter {#filters_enable}

HDF5 supports dynamic loading of compression filters using the following process for reading of compressed data.

1. Assume that we have a dataset with one or more variables that were compressed using some algorithm. How the dataset was compressed will be discussed subsequently.
2. Shared libraries or DLLs exist that implement the compress/decompress algorithm. These libraries have a specific API so that the HDF5 library can locate, load, and utilize the compressor. These libraries are expected to installed in a specific directory.

In order to compress a variable with an HDF5 compliant filter, the netcdf-c library must be given three pieces of information:

1. some unique identifier for the filter to be used,
2. a vector of parameters for controlling the action of the compression filter, and
3. a shared library implementation of the filter.

The meaning of the parameters is, of course, completely filter dependent and the filter description [3] needs to be consulted.
For bzip2, for example, a single parameter is provided representing the compression level.
It is legal to provide a zero-length set of parameters.
Defaults are not provided, so this assumes that the filter can operate with zero parameters.

Filter ids are assigned by the HDF group.
See [4] for a current list of assigned filter ids.
Note that ids above 32767 can be used for testing without registration.

The first two pieces of  information can be provided in one of three ways: using __ncgen__, via an API call, or via command line parameters to __nccopy__.
In any case, remember that filtering also requires setting chunking, so the variable must also be marked with chunking information.
If compression is set for a non-chunked variable, the variable will forcibly be
converted to chunked using a default chunking algorithm.

## Using ncgen {#filters_NCGEN}

In a CDL file, compression of a variable can be specified by annotating it with the following attribute:

* ''_Filter'' &mdash; a string containing a comma separated list of constants specifying (1) the filter id to apply, and (2) a vector of constants representing the parameters for controlling the operation of the specified filter.
See the section on the <a href="#filters_syntax">parameter encoding syntax</a> for the details on the allowable kinds of constants.

This is a "special" attribute, which means that it will normally be invisible when using __ncdump__ unless the -s flag is specified.

This attribute may be repeated to specify multiple filters.
For backward compatibility it is probably better to use the ''_Deflate'' attribute instead of ''_Filter''. But using ''_Filter'' to specify deflation will work.

Note that the lexical order of declaration is important when more than one filter is specified for a variable because it determines the order in which the filters are applied.

### Example CDL File (Data elided)

````
netcdf bzip2szip {
dimensions:
  dim0 = 4 ; dim1 = 4 ; dim2 = 4 ; dim3 = 4 ;
variables:
  float var(dim0, dim1, dim2, dim3) ;
    var:_Filter = "307,9|4,32,32" ; // bzip2 then szip
    var:_Storage = "chunked" ;
    var:_ChunkSizes = 4, 4, 4, 4 ;
data:
...
}
````

Note that the assigned filter id for bzip2 is 307 and for szip it is 4.

## Using nccopy {#filters_NCCOPY}

When copying a netcdf file using __nccopy__ it is possible to specify filter information for any output variable by using the "-F" option on the command line; for example:
````
nccopy -F "var,307,9" unfiltered.nc filtered.nc
````
Assume that _unfiltered.nc_ has a chunked but not bzip2 compressed variable named "var".
This command will copy that variable to the _filtered.nc_ output file but using filter with id 307 (i.e. bzip2) and with parameter(s) 9 indicating the compression level.
See the section on the <a href="#filters_syntax">parameter encoding syntax</a> for the details on the allowable kinds of constants.

The "-F" option can be used repeatedly, as long as a different variable is specified for each occurrence.

It can be convenient to specify that the same compression is to be applied to more than one variable. To support this, two additional *-F* cases are defined.

1. ````-F *,...```` means apply the filter to all variables in the dataset.
2. ````-F v1&v2&..,...```` means apply the filter to multiple variables.

Multiple filters can be specified using the pipeline notions '|'.
For example

1. ````-F v1&v2,307,9|4,32,32```` means apply filter 307 (bzip2) then filter 4 (szip) to the multiple variables.

Note that the characters '*', '&', and '|' are shell reserved characters, so you will probably need to escape or quote the filter spec in that environment.

As a rule, any input filter on an input variable will be applied to the equivalent output variable &mdash; assuming the output file type is netcdf-4.
It is, however, sometimes convenient to suppress output compression either totally or on a per-variable basis.
Total suppression of output filters can be accomplished by specifying a special case of "-F", namely this.
````
nccopy -F none input.nc output.nc
````
The expression ````-F *,none```` is equivalent to ````-F none````.

Suppression of output filtering for a specific set of variables can be accomplished using these formats.
````
nccopy -F "var,none" input.nc output.nc
nccopy -F "v1&v2&...,none" input.nc output.nc
````
where "var" and the "vi" are the fully qualified name of a variable.

The rules for all possible cases of the "-F none" flag are defined by this table.

<table>
<tr><th>-F none<th>-Fvar,...<th>Input Filter<th>Applied Output Filter
<tr><td>true<td>undefined<td>NA<td>unfiltered
<tr><td>true<td>none<td>NA<td>unfiltered
<tr><td>true<td>defined<td>NA<td>use output filter(s)
<tr><td>false<td>undefined<td>defined<td>use input filter(s)
<tr><td>false<td>none<td>NA<td>unfiltered
<tr><td>false<td>defined<td>NA<td>use output filter(s)
<tr><td>false<td>undefined<td>undefined<td>unfiltered
<tr><td>false<td>defined<td>defined<td>use output filter(s)
</table> 

# HDF5 Parameter Encode/Decode {#filters_paramcoding}

The filter id for an HDF5 format filter is an unsigned integer.
Further, the parameters passed to an HDF5 format filter are encoded internally as a vector of 32-bit unsigned integers.
It may be that the parameters required by a filter can naturally be encoded as unsigned integers.
The bzip2 compression filter, for example, expects a single integer value from zero thru nine.
This encodes naturally as a single unsigned integer.

Note that signed integers and single-precision (32-bit) float values also can easily be represented as 32 bit unsigned integers by proper casting to an unsigned integer so that the bit pattern is preserved.
Simple integer values of type short or char (or the unsigned versions) can also be mapped to an unsigned integer by truncating to 16 or 8 bits respectively and then zero extending.

Machine byte order (aka endian-ness) is an issue for passing some kinds of parameters.
You might define the parameters when compressing on a little endian machine, but later do the decompression on a big endian machine.

When using HDF5 format filters, byte order is not an issue for 32-bit values because HDF5 takes care of converting them between the local machine byte order and network byte order.

Parameters whose size is larger than 32-bits present a byte order problem.
This specifically includes double precision floats and (signed or unsigned) 64-bit integers.
For these cases, the machine byte order issue must be handled, in part, by the compression code.
This is because HDF5 will treat, for example, an unsigned long long as two 32-bit unsigned integers and will convert each to network order separately.
This means that on a machine whose byte order is different than the machine in which the parameters were initially created, the two integers will be separately
endian converted.
But this will be incorrect for 64-bit values.

So, we have this situation (for HDF5 only):

1. the 8 bytes come in as native machine order for the machine doing the call to *nc_def_var_filter*.
2. HDF5 divides the 8 bytes into 2 four byte pieces and ensures that each piece is in network (big) endian order.
3. When the filter is called, the two pieces are returned in the same order but with the bytes in each piece consistent with the native machine order for the machine executing the filter.

## Encoding Algorithms for HDF5

In order to properly extract the correct 8-byte value, we need to ensure that the values stored in the HDF5 file have a known format independent of the native format of the creating machine.

The idea is to do sufficient manipulation so that HDF5 will store the 8-byte value as a little endian value divided into two 4-byte integers.
Note that little-endian is used as the standard because it is the most common machine format.
When read, the filter code needs to be aware of this convention and do the appropriate conversions.

This leads to the following set of rules.

### Encoding 

1. Encode on little endian (LE) machine: no special action is required.
   The 8-byte value is passed to HDF5 as two 4-byte integers.
   HDF5 byte swaps each integer and stores it in the file.
2. Encode on a big endian (BE) machine: several steps are required:

   1. Do an 8-byte byte swap to convert the original value to little-endian format.
   2. Since the encoding machine is BE, HDF5 will just store the value.
      So it is necessary to simulate little endian encoding by byte-swapping each 4-byte integer separately. 
   3. This doubly swapped pair of integers is then passed to HDF5 and is stored unchanged.

### Decoding 

1. Decode on LE machine: no special action is required.
    HDF5 will get the two 4-bytes values from the file and byte-swap each separately.
    The concatenation of those two integers will be the expected LE value.
2. Decode on a big endian (BE) machine: the inverse of the encode case must be implemented.

   1. HDF5 sends the two 4-byte values to the filter.
   2. The filter must then byte-swap each 4-byte value independently.
   3. The filter then must concatenate the two 4-byte values into a single 8-byte value.
    Because of the encoding rules, this 8-byte value will be in LE format.
   4. The filter must finally do an 8-byte byte-swap on that 8-byte value to convert it to desired BE format.

# Filter Specification Syntax {#filters_syntax}

The utilities <a href="#NCGEN">ncgen</a> and <a href="#NCCOPY">nccopy</a>, and also the output of __ncdump__, support the specification of filter ids, formats, and parameters in text format.
The BNF specification is defined in <a href="#filters_appendixa">Appendix A</a>.
Basically, These specifications consist of a filter id, a comma, and then a sequence of
comma separated constants representing the parameters.
The constants are converted within the utility to a proper set of unsigned int constants (see the <a href="#ParamEncode">parameter encoding section</a>).

To simplify things, various kinds of constants can be specified rather than just simple unsigned integers.
The __ncgen__ and __nccopy__ programs will encode them properly using the rules specified in the section on <a href="#filters_paramcoding">parameter encode/decode</a>.
Since the original types are lost after encoding, __ncdump__ will always show a simple list of unsigned integer constants.

The currently supported constants are as follows.
<table>
<tr halign="center"><th>Example<th>Type<th>Format Tag<th>Notes
<tr><td>-17b<td>signed 8-bit byte<td>b|B<td>Truncated to 8 bits and zero extended to 32 bits
<tr><td>23ub<td>unsigned 8-bit byte<td>u|U b|B<td>Truncated to 8 bits and zero extended to 32 bits
<tr><td>-25S<td>signed 16-bit short<td>s|S<td>Truncated to 16 bits and zero extended to 32 bits
<tr><td>27US<td>unsigned 16-bit short<td>u|U s|S<td>Truncated to 16 bits and zero extended to 32 bits
<tr><td>-77<td>implicit signed 32-bit integer<td>Leading minus sign and no tag<td>
<tr><td>77<td>implicit unsigned 32-bit integer<td>No tag<td>
<tr><td>93U<td>explicit unsigned 32-bit integer<td>u|U<td>
<tr><td>789f<td>32-bit float<td>f|F<td>
<tr><td>12345678.12345678d<td>64-bit double<td>d|D<td>LE encoding
<tr><td>-9223372036854775807L<td>64-bit signed long long<td>l|L<td>LE encoding
<tr><td>18446744073709551615UL<td>64-bit unsigned long long<td>u|U l|L<td>LE encoding
</table>
Some things to note.

1. In all cases, except for an untagged positive integer, the format tag is required and determines how the constant is converted to one or two unsigned int values.
2. For an untagged positive integer, the constant is treated as of the smallest type into which it fits (i.e. 8,16,32, or 64 bit).
3. For signed byte and short, the value is sign extended to 32 bits and then treated as an unsigned int value, but maintaining the bit-pattern.
4. For double, and signed|unsigned long long, they are converted as specified in the section on <a href="#filters_paramcoding">parameter encode/decode</a>.
5. In order to support mutiple filters, the argument to ''_Filter'' may be a pipeline separated  (using '|') to specify a list of filters specs.

# NCZarr/Zarr Filter Support {#filters_nczarr}


# Dynamic Loading Process {#filters_Process}

Each filter is assumed to be compiled into a separate dynamically loaded library.
For HDF5 conformant filters, these filter libraries are assumed to be in some specific location.
The details for writing such a filter are defined in the HDF5 documentation[1,2].

## Plugin directory {#filters_Plugindir}

The HDF5 loader expects plugins to be in a specified plugin directory.
The default directory is:
* "/usr/local/hdf5/lib/plugin” for linux/unix operating systems (including Cygwin)
* “%ALLUSERSPROFILE%\\hdf5\\lib\\plugin” for Windows systems, although the code does not appear to explicitly use this path.

The default may be overridden using the environment variable __HDF5_PLUGIN_PATH__.

## Plugin Library Naming {#filters_Pluginlib}

Given a plugin directory, HDF5 examines every file in that directory that conforms to a specified name pattern as determined by the platform on which the library is being executed.
<table>
<tr halign="center"><th>Platform<th>Basename<th>Extension
<tr halign="left"><td>Linux<td>lib*<td>.so*
<tr halign="left"><td>OSX<td>lib*<td>.so*
<tr halign="left"><td>Cygwin<td>cyg*<td>.dll*
<tr halign="left"><td>Windows<td>*<td>.dll
</table>

# Notes

## Order of Invocation for Multiple Filters 

When multiple filters are defined on a variable, the order of application, when writing data to the file, is same as the order in which the filters are associated with the variable. When reading a file the order of application is of necessity the reverse.

There are some special cases.

1. The fletcher32 filter is always applied first, if enabled.
2. If the same filter id is used, but possibly with different sets of parameters, then the position of that filter in the sequence of applictions does not change.
    However the last set of parameters specified is used when actually writing the dataset.
3. Deflate and shuffle &mdash; these two are inextricably linked in the current API, but have quite different semantics.
    If you apply deflation multiple times, then the previous rule applies with respect to deflate.
    However, the shuffle filter, if enabled, is ''always'' applied before applying any other filters, except fletcher32.
4. Once a filter is defined for a variable, it cannot be removed nor can its position in the filter order be changed.

## SZIP Issues

The current szip plugin code in the HDF5 library has some behaviors that can catch the unwary.
These are handled internally to (mostly) hide them so that they should not affect users.
Specifically, this filter may do two things.

1. Add extra parameters to the filter parameters: going from the two parameters provided by the user to four parameters for internal use.
    It turns out that the two parameters provided when defining the filter correspond to the first two parameters of the four parameters.
2. Change the values of some parameters: the value of the __options_mask__ argument is known to add additional flag bits, and the __pixels_per_block__ parameter may be modified.

The reason for these changes is has to do with the fact that the szip API provided by the underlying H5Pset_szip function is actually a subset of the capabilities of the real szip implementation.
Presumably this is for historical reasons.

In any case, if the caller applies or queries the szip filter, then the parameter values returned may differ from those originally specified.

## Supported Systems

The current matrix of OS X build systems known to work is as follows.
<table>
<tr><th>Build System<th>Supported OS
<tr><td>Automake<td>Linux, Cygwin, OSX
<tr><td>Cmake<td>Linux, Cygwin, OSX, Visual Studio
</table>

# Appendix A. BNF for Specifying Filters in Utilities {#filters_appendixa}

````
speclist:   spec
          | speclist '|' spec
          ;
spec:   filterid
      | filterid ',' parameterlist
      ;
filterid:   unsigned32
          ;
parameterlist:   parameter
               | parameterlist ',' parameter
               ;
parameter: unsigned32

where
unsigned32: <32 bit unsigned integer>
````

# References {#filters_References}

1. https://support.hdfgroup.org/HDF5/doc/Advanced/DynamicallyLoadedFilters/HDF5DynamicallyLoadedFilters.pdf
2. https://support.hdfgroup.org/HDF5/doc/TechNotes/TechNote-HDF5-CompressionTroubleshooting.pdf
3. https://portal.hdfgroup.org/display/support/Contributions#Contributions-filters
4. https://support.hdfgroup.org/services/contributions.html#filters
5. https://support.hdfgroup.org/HDF5/doc/RM/RM_H5.html
6. https://confluence.hdfgroup.org/display/HDF5/Filters

# Point of Contact {#filters_poc}

__Author__: Dennis Heimbigner<br>
__Email__: dmh at ucar dot edu<br>
__Initial Version__: 1/10/2018<br>
__Last Revised__: 7/15/2021
