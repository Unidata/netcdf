---
title: NetCDF Common Data Language (CDL)
last_updated: 2021-04-12
sidebar: nnug_sidebar
toc: false
permalink: cdl.html
---

NetCDF CDL (Common Data form Language) is a text notation for representing the structure and data of a binary netCDF dataset.
CDL can be read (and edited) by a human. It can also be read and produced by machines.
For instance, a CDL description can be generated, given a netCDF file, by the `ncdump` utility and a netCDF file can be generated, given a CDL desription, by the `ncgen` utility.

[//]: # (TODO: Update the following with more current ncgen man page, Unidata/netcdf-c - ncgen/ncgen.1)
[//]: # (      The following text was taken from the NUG CDL document, NUG/cdl.md, which is not)
[//]: # (      as up to date as the ncgen man page.)

[//]: # (TODO: Clarify what is supported in the classic data model and what is supported in the)
[//]: # (      enhanced data model. The ncgen pages are somewhat focused on the Enhanced data model.)

##  CDL Syntax

### CDL Example: Classic Data Model

<!-- NOTE: From 2012 workshop CDL page: https://www.unidata.ucar.edu/software/netcdf/workshops/most-recent/nc3model/Cdl.html
-->
Below is an example that specifies a netCDF classic dataset
with two dimensions (lon and lat), three variable (lon, lat, and rh),
two variable attributes for each variable (units and long_name),
one global attribute (title), and some data values for the variable.

````
netcdf example {   // example of CDL notation
    dimensions:
        lon = 3 ;
        lat = 8 ;
        time = unlimited ;
    variables:
        float lon(lon) ;
            lon:units = "degrees_north" ;
            lon:long_name = "longitude" ;
        float lat(lat) ;
            lat:units = "degrees_east" ;
            lat:long_name = "latitude" ;
        float time(time) ;
            time:units = "seconds since 1992-1-1 00:00:00" ;
        float rh(time, lon, lat) ;
            rh:units = "percent" ;
            rh:long_name = "Relative humidity" ;
    // global attributes
    :title = "Simple example" ;

    data:
        lon = -120, -105, -90 ;
        lat = 10, 20, 30, 40, 50, 60, 70, 80 ;
        time = 0, 180 ;
        rh =
            2, 3, 5, 7, 11, 13, 17, 19,
            23, 29, 31, 37, 41, 43, 47, 53,
            59, 61, 67, 71, 73, 79, 83, 89,
            1, 3, 4, 7, 9, 11, 13, 15,
            16, 17, 19, 21, 23, 25, 27, 29,
            31, 33, 35, 37, 39, 41, 43, 45 ;
}
````

<!-- NOTE: Text from NUG/cdl.md
-->

All CDL statements are terminated by a semicolon.
Spaces, tabs, and newlines can be used freely for readability.
Comments may follow the double slash characters '//' on any line.

A CDL description for a classic model file consists of three optional parts:
dimensions, variables, and data.
The variable part may contain variable declarations and attribute assignments.
For the enhanced model supported by netCDF-4, a CDL description may also include groups, subgroups, and user-defined types.

### Dimensions
[//]: # (TODO: Consider removing most of this paragraph.)
[//]: # (All but the last sentence is already covered in Data Model page.)

A dimension is used to define the shape of one or more of the multidimensional variables described by the CDL description.
A dimension has a name and a length.
At most one dimension in a classic CDL description can have the unlimited length, which means a variable using this dimension can grow to any length (like a record number in a file).
Any number of dimensions can be declared of unlimited length in CDL for an enhanced model file.

[//]: # (TODO: Similar to above in this paragraph.)
CDL dimension declarations may appear on one or more lines following the CDL keyword dimensions.
Multiple dimension declarations on the same line may be separated by commas.
Each declaration is of the form `name = length`.
Use the “/” character to include group information (netCDF-4 output only).

### Variables
A variable represents a multidimensional array of values of the same type.
A variable has a name, a data type, and a shape described by its list of dimensions.
Each variable may also have associated attributes (see below) as well as data values.
The name, data type, and shape of a variable are specified by its declaration in the variable section of a CDL description.
A variable may have the same name as a dimension; by convention such a variable contains coordinates of the dimension it names.

CDL variable declarations may appear on one or more lines following the CDL keyword `variables`.
Multiple variable declarations on the same line may be separated by commas.
Each declaration is of the form `name = length`.
Use the “/” character to include group information (netCDF-4 output only).

### Attributes
An attribute contains information about a variable or about the whole netCDF dataset or containing group.
Attributes may be used to specify such properties as units, special values, maximum and minimum valid values, and packing parameters.
Attribute information is represented by single values or one-dimensional arrays of values.
For example, “units” might be an attribute represented by a string such as “celsius”.
An attribute has an associated variable, a name, a data type, a length, and a value.
In contrast to variables that are intended for data, attributes are intended for ancillary data or metadata (data about data).

In CDL, an attribute is designated by a data type, a variable, and an attribute name.
The variable and the attribute name are separated by a colon (':').  
If present, the data type precedes the variable name.  
It is possible to assign global attributes to the netCDF dataset as a whole by omitting the variable name and beginning the attribute name with a colon (':').
The data type of an attribute in CDL, if not explicitly specified, is derived from the type of the value assigned to it, with one exception.  
If the value is a string, then the inferred type is char, not string.  
If it is desired to have a string typed attribute, this must be stated explicitly.

The length of an attribute is the number of data values or the number of characters in the character string assigned to it if the type is char.
Multiple values are assigned to non-character attributes by separating the values with commas (',').
All values assigned to an attribute must be of the same type.
In the netCDF-4 enhanced model, attributes may be declared to be of user-defined type, like variables.

### Names of NetCDF Objects
[//]: # (TODO: Update this text to reference data_models.md#netcdf_object_names instead of repeating.)

In CDL, just as for netCDF, the names of dimensions, variables and attributes (and, in netCDF-4 files, groups, user-defined types, compound member names, and enumeration symbols) consist of arbitrary sequences of alphanumeric characters, underscore '_', period '.', plus '+', hyphen '-', or at sign '@', but beginning with a letter or underscore.
However names commencing with underscore are reserved for system use. Case is significant in netCDF names. A zero-length name is not allowed.
Some widely used conventions restrict names to only alphanumeric characters or underscores.
Names that have trailing space characters are also not permitted.

Beginning with versions `3.6.3` and `4.0`, names may also include UTF-8 encoded Unicode characters as well as other special characters, exceptfor the character '/', which may not appear in a name (because it is reserved for path names of nested groups).
In CDL, most special characters are escaped with a backslash '\' character, but that character is not actually part of the netCDF name. The special characters that do not need to be escaped in CDL names are underscore '_', period '.', plus '+', hyphen '-', or at sign '@'.
For the formal specification of CDL name syntax See \ref classic_format_spec.

[//]: # (TODO: May need to expand on last sentence. Not sure file format BNF is enough.)


> Note that by using special characters in names, you may make your data not compliant with conventions that have more stringent requirements on valid names for netCDF components, for example the CF Conventions.

The names for the primitive data types are reserved words in CDL, so names of variables, dimensions, and attributes must not be primitive type names.

### Data
The optional data section of a CDL description is where netCDF variables may be initialized.
The syntax of an initialization is simple:

````
     variable = value_1, value_2, ...;
````

The comma-delimited list of constants may be separated by spaces, tabs, and newlines.
For multidimensional arrays, the last dimension varies fastest.
Thus, row-order rather than column order is used for matrices.
If fewer values are supplied than are needed to fill a variable, it is extended with the fill value.
The types of constants need not match the type declared for a variable; coercions are done to convert integers to floating point, for example.
All meaningful type conversions among primitive types are supported.

[//]: # (TODO: Reword type coercion section to differentiate CDL definition from utility features.)
[//]: # (Maybe: \"All known CDL reader/parser implementations will attempt to convert values to the appropriate type.\")


A special notation for fill values is supported: the ‘_’ character designates a fill value for variables.

#### Dimension order in variable declarations

[//]: # (TODO: Decide if dimension order needs its own section or if mention above is enough.)
[//]: # (From above:)
[//]: # (  For multidimensional arrays, the last dimension in a CDL shape varies fastest.)
[//]: # (From data model section:)
[//]: # (  An unlimited dimension, if there is one, must be the first dimension in a CDL shape.)
-->

### CDL Data Types
[//]: # (TODO: Rewrite and reference data model pages, NUG-new/pages/data_models.md#netcdf_data_types)
[//]: # (Maybe move section on the values that the different types can contain to NUG-new/pages/data_models.md#netcdf_data_types)

The CDL primitive data types for the classic model are:
- char Characters.
- byte Eight-bit integers.
- short 16-bit signed integers.
- int 32-bit signed integers.
- long (Deprecated, synonymous with int)
- float IEEE single-precision floating point (32 bits).
- real (Synonymous with float).
- double IEEE double-precision floating point (64 bits).

NetCDF-4 supports the additional primitive types:
- ubyte Unsigned eight-bit integers.
- ushort Unsigned 16-bit integers.
- uint Unsigned 32-bit integers.
- int64 64-bit signed integers.
- uint64 Unsigned 64-bit signed integers.
- string Variable-length string of characters

Except for the added data-type byte, CDL supports the same primitive data types as C.
For backward compatibility, in declarations primitive type names may be specified in either upper or lower case.

The byte type differs from the char type in that it is intended for numeric data, and the zero byte has no special significance, as it may for character data.
The short type holds values between -32768 and 32767.
The ushort type holds values between 0 and 65536. The int type can hold values between -2147483648 and 2147483647.
The uint type holds values between 0 and 4294967296.
The int64 type can hold values between -9223372036854775808 and 9223372036854775807.
The uint64 type can hold values between 0 and 18446744073709551616.

The float type can hold values between about -3.4+38 and 3.4+38, with external representation as 32-bit IEEE normalized single-precision floating-point numbers.
The double type can hold values between about -1.7+308 and 1.7+308, with external representation as 64-bit IEEE standard normalized double-precision, floating-point numbers.
The string type holds variable length strings.

### CDL Notation for Data Constants {#cdl_notations_for_data_constants}

[//]: # (TODO: Byte constants should be numeric not characters.)
[//]: # (Fixed in ncgen man page docs. So updating this page with man page docs will fix.)

This section describes the CDL notation for constants.

Attributes are initialized in the variables section of a CDL description by providing a list of constants that determines the attribute's length and type (if primitive and not explicitly declared).
CDL defines a syntax for constant values that permits distinguishing among different netCDF primitive types.
The syntax for CDL constants is similar to C syntax, with type suffixes appended to bytes, shorts, and floats to distinguish them from ints and doubles.

#### `byte` Data Type
A byte constant is represented by a single character or multiple character escape sequence enclosed in single quotes. For example:

````
     'a'     // ASCII a
     '\0'    // a zero byte
     '\n'    // ASCII newline character
     '\33'   // ASCII escape character (33 octal)
     '\x2b'  // ASCII plus (2b hex)
     '\376'  // 377 octal = -127 (or 254) decimal
````


#### `char` Data Type
Character constants are enclosed in double quotes.
A character array may be represented as a string enclosed in double quotes. Multiple strings are concatenated into a single array of characters, permitting long character arrays to appear on multiple lines.
To support multiple variable-length string values, a conventional delimiter such as ',' may be used, but interpretation of any such convention for a string delimiter must be implemented in software above the netCDF library layer.
The usual escape conventions for C strings are honored. For
example:

````
     "a"            // ASCII 'a'
     "Two\nlines\n" // a 10-character string with two embedded newlines
     "a bell:\007"  // a string containing an ASCII bell
     "ab","cde"     // the same as "abcde"

````

#### `short` Data Type
The form of a short constant is an integer constant with an 's' or 'S' appended.
If a short constant begins with '0', it is interpreted as octal.
When it begins with '0x', it is interpreted as a hexadecimal constant.
For example:

````
     2s      // a short 2
     0123s   // octal
     0x7ffs  // hexadecimal

````

#### `int` Data Type
The form of an int constant is an ordinary integer constant. If an int constant begins with '0', it is interpreted as octal. When it begins with '0x', it is interpreted as a hexadecimal constant. Examples of valid int constants include:

````

     -2
     0123            // octal
     0x7ff           // hexadecimal
     1234567890L     // deprecated, uses old long suffix

````

#### `float` Data Type
The float type is appropriate for representing data with about seven significant digits of precision.
The form of a float constant is the same as a C floating-point constant with an 'f' or 'F' appended.
A decimal point is required in a CDL float to distinguish it from an integer.]
For example, the following are all acceptable float constants:

````
     -2.0f
     3.14159265358979f       // will be truncated to less precision
     1.f
     .1f

````

#### `double` Data Type
The double type is appropriate for representing floating-point data with about 16 significant digits of precision.
The form of a double constant is the same as a C floating-point constant.
An optional 'd' or 'D' may be appended.
A decimal point is required in a CDL double to distinguish it from an integer.
For example, the following are all acceptable double constants:

````

     -2.0
     3.141592653589793
     1.0e-20
     1.d

````

#### `ubyte` Data Type
````
10ub
100bu
````
10U, 100su, 100000ul, or 1000000llu
#### `ushort` Data Type
````
10us
100su
````
#### `uint` Data Type
````
10u
100U
````
#### `int64` Data Type
````
2.0ll
````
#### `uint64` Data Type
````
10ull
100llU
````
#### `string` Data Type

### CDL Extensions and Reserved Attributes


# Docs from ncgen.1 man page

## CDL Syntax

Below is an example of CDL syntax, describing a netCDF file with several
named dimensions (lat, lon, and time), variables (Z, t, p, rh, lat, lon,
time), variable attributes (units, long_name, valid_range, \_FillValue),
and some data. CDL keywords are in boldface. (This example is intended
to illustrate the syntax; a real CDL file would have a more complete set
of attributes so that the data would be more completely
self-describing.)

```
netcdf foo {  // an example netCDF specification in CDL

    types:
        ubyte enum enum_t {Clear = 0, Cumulonimbus = 1, Stratus = 2};
        opaque(11) opaque_t;
        int(*) vlen_t;

    dimensions:
        lat = 10, lon = 5, time = unlimited ;

    variables:
        long    lat(lat), lon(lon), time(time);
        float   Z(time,lat,lon), t(time,lat,lon);
        double  p(time,lat,lon);
        long    rh(time,lat,lon);

        string  country(time,lat,lon);
        ubyte   tag;

        // variable attributes
        lat:long_name = "latitude";
        lat:units = "degrees_north";
        lon:long_name = "longitude";
        lon:units = "degrees_east";
        time:units = "seconds since 1992-1-1 00:00:00";

        // typed variable attributes
        string Z:units = "geopotential meters";
        float Z:valid_range = 0., 5000.;
        double p:_FillValue = -9999.;
        long rh:_FillValue = -1;
        vlen_t :globalatt = {17, 18, 19};
    data:
        lat   = 0, 10, 20, 30, 40, 50, 60, 70, 80, 90;
        lon   = -140, -118, -96, -84, -52;
    group: g {
        types:
            compound cmpd_t { vlen_t f1; enum_t f2;};
    } // group g
    group: h {
        variables:
            /g/cmpd_t  compoundvar;
        data:
            compoundvar = { {3,4,5}, enum_t.Stratus } ;
    } // group h
}
```

All CDL statements are terminated by a semicolon. Spaces, tabs, and
newlines can be used freely for readability. Comments may follow the
characters \`//' on any line.

A CDL description consists of five optional parts: types,
dimensions, variables, data, beginning with the
keyword `types:`, `dimensions:`, `variables:`, and
`data:`, respectively. Note several things: (1) the keyword
includes the trailing colon, so there must not be any space before the
colon character, and (2) the keywords are required to be lower case.

The `variables:` section may contain variable declarations and
attribute assignments. All sections may contain global attribute
assignments.

In addition, after the `data:` section, the user may define a series
of groups (see the example above). Groups themselves can contain types,
dimensions, variables, data, and other (nested) groups.

The netCDF `types:` section declares the user defined types. These may
be constructed using any of the following types: `enum`, `vlen`, `opaque`,
or `compound`.

A netCDF dimension is used to define the shape of one or more of
the multidimensional variables contained in the netCDF file. A netCDF
dimension has a name and a size. A dimension can have the **unlimited**
size, which means a variable using this dimension can grow to any
length in that dimension.

A variable represents a multidimensional array of values of the
same type. A variable has a name, a data type, and a shape described by
its list of dimensions. Each variable may also have associated
attributes (see below) as well as data values. The name, data
type, and shape of a variable are specified by its declaration in the
variable section of a CDL description. A variable may have the
same name as a dimension; by convention such a variable is
one-dimensional and contains coordinates of the dimension it names.
Dimensions need not have corresponding variables.

A netCDF attribute contains information about a netCDF variable or**
about the whole netCDF dataset. Attributes are used to specify such
properties as units, special values, maximum and minimum valid values,
scaling factors, offsets, and parameters. Attribute information is
represented by single values or arrays of values. For example, "units"
is an attribute represented by a character array such as "celsius". An
attribute has an associated variable, a name, a data type, a length, and
a value. In contrast to variables that are intended for data, attributes
are intended for metadata (data about data). Unlike netCDF-3, attribute
types can be any user defined type as well as the usual built-in types.

In CDL, an attribute is designated by a a type, a variable, a ':', and
then an attribute name. The type is optional and if missing, it will be
inferred from the values assigned to the attribute. It is possible to
assign global attributes** not associated with any variable to the
netCDF as a whole by omitting the variable name in the attribute
declaration. Notice that there is a potential ambiguity in a
specification such as

```
x : a = ...
```

In this situation, x could be either a type for a global attribute, or
the variable name for an attribute. Since there could both be a type
named x and a variable named x, there is an ambiguity. The rule is that
in this situation, x will be interpreted as a type if possible, and
otherwise as a variable.

If not specified, the data type of an attribute in CDL is derived from
the type of the value(s) assigned to it. The length of an attribute is
the number of data values assigned to it, or the number of characters in
the character string assigned to it. Multiple values are assigned to
non-character attributes by separating the values with commas. All
values assigned to an attribute must be of the same type.

The names for CDL dimensions, variables, attributes, types, and groups
may contain any non-control utf-8 character except the forward slash
character ('/'). However, certain characters must be escaped if they are
used in a name, where the escape character is the backward slash ('\').
In particular, if the leading character off the name is a digit (0-9),
then it must be preceded by the escape character. In addition, the
characters `` !"#$%&()*,:;<=>?[]^`'{}|~\ `` must be escaped if
they occur anywhere in a name. Note also that attribute names that begin
with an underscore ('_') are reserved for the use of Unidata and
should not be used in user defined attributes.

Note also that the words "variables", "dimensions", "data", "group",
and "types" are legal CDL names, but be careful that there is a space
between them and any following colon character when used as a variable
name. This is mostly an issue with attribute declarations. For example,
consider this.

```
netcdf ... {
    ...
    variables:
        int dimensions;
            dimensions: attribute=0 ; // this will cause an error
            dimensions : attribute=0 ; // this is ok.
       ...
}
```

The optional `data:` section of a CDL specification is where netCDF
variables may be initialized. The syntax of an initialization is simple:
a variable name, an equals sign, and a comma-delimited list of constants
(possibly separated by spaces, tabs and newlines) terminated with a
semicolon. For multi-dimensional arrays, the last dimension varies
fastest. Thus row-order rather than column order is used for matrices.
If fewer values are supplied than are needed to fill a variable, it is
extended with a type-dependent "fill value", which can be overridden by
supplying a value for a distinguished variable attribute named
`_FillValue`. The types of constants need not match the type declared
for a variable; coercions are done to convert integers to floating
point, for example. The constant "_" can be used to designate the fill
value for a variable. If the type of the variable is explicitly
"string", then the special constant "NIL" can be used to represent a
nil string, which is not the same as a zero length string.

## Primitive Data Types

| `char`   | Characters |
| `byte`   | 8-bit data |
| `short`  | 16-bit signed integers |
| `char`   | Characters |
| `byte`   | 8-bit data |
| `short`  | 16-bit signed integers |
| `int`    | 32-bit signed integers |
| `long`   | (**Deprecated**, synonymous with int) |
| `int64`  | 64-bit signed integers |
| `float`  | IEEE single precision floating point (32 bits) |
| `real`   | (Synonymous with float) |
| `double` | IEEE double precision floating point (64 bits) |
| `ubyte`  | Unsigned 8-bit data |
| `ushort` | Unsigned 16-bit  integers |
| `uint`   | Unsigned 32-bit integers |
| `uint64` | Unsigned 64-bit integers |
| `string` | Variable-length (arbitrary?) length strings |
 

CDL supports a superset of the primitive data types of C. The names for
the primitive data types are reserved words in CDL, so the names of
variables, dimensions, and attributes must not be primitive type names.
In declarations, type names may be specified in either upper or lower
case.

Bytes are intended to hold a full eight bits of data, and the zero byte
has no special significance, as it mays for character data. The **ncgen** utility
converts `byte` declarations to `char` declarations in the output C code
and to the nonstandard `BYTE` declaration in output Fortran code.

Shorts can hold values between -32768 and 32767. **ncgen** converts `short`
declarations to `short` declarations in the output C code and to the
nonstandard `INTEGER*2` declaration in output Fortran code.

Ints can hold values between -2147483648 and 2147483647. **ncgen**
converts `int` declarations to `int` declarations in the output C code and
to `INTEGER** declarations in output Fortran code. `long` is accepted
as a synonym for `int` in CDL declarations, but is deprecated since
there are now platforms with 64-bit representations for C longs.

Int64 can hold values between -9223372036854775808 and
9223372036854775807. **ncgen** converts `int64` declarations to `longlong`
declarations in the output C code.

Floats can hold values between about -3.4+38 and 3.4+38. Their external
representation is as 32-bit IEEE normalized single-precision floating
point numbers. **ncgen** converts `float` declarations to `float`
declarations in the output C code and to `REAL` declarations in output
Fortran code. `real` is accepted as a synonym for `float` in CDL
declarations.

Doubles can hold values between about -1.7+308 and 1.7+308. Their
external representation is as 64-bit IEEE standard normalized
double-precision floating point numbers. **ncgen** converts `double`
declarations to `double` declarations in the output C code and to
`DOUBLE PRECISION` declarations in output Fortran code.

The unsigned counterparts of the above integer types are mapped to the
corresponding unsigned C types. Their ranges are suitably modified to
start at zero.

The technical interpretation of the char type is that it is an unsigned
8-bit value. The encoding of the 256 possible values is unspecified by
default. A variable of char type may be marked with an "_Encoding"
attribute to indicate the character set to be used: US-ASCII,
ISO-8859-1, etc. Note that specifying the encoding of UTF-8 is
equivalent to specifying US-ASCII This is because multi-byte UTF-8
characters cannot be stored in an 8-bit character. The only legal single
byte UTF-8 values are by definition the 7-bit US-ASCII encoding with the
top bit set to zero.

Strings are assumed by default to be encoded using UTF-8. Note that this
means that multi-byte UTF-8 encodings may be present in the string, so
it is possible that the number of distinct UTF-8 characters in a string
is smaller than the number of 8-bit bytes used to store the string.

## CDL Constants

Constants assigned to attributes or variables may be of any of the basic
netCDF types. The syntax for constants is similar to C syntax, except
that type suffixes must be appended to shorts and floats to distinguish
them from longs and doubles.

A byte constant is represented by an integer constant with a "b"
(or "B") appended. In the old netCDF-2 API, byte constants could also
be represented using single characters or standard C character escape
sequences such as "a" or "\n". This is still supported for backward
compatibility, but deprecated to make the distinction clear between the
numeric byte type and the textual char type. Example byte constants
include:

```
  0b           // a zero byte
 -1b           // -1 as an 8-bit byte
255b           // also -1 as a signed 8-bit byte
```

short integer constants are intended for representing 16-bit
signed quantities. The form of a short constant is an integer
constant with an "s" or "S" appended. If a short constant begins
with "0", it is interpreted as octal, except that if it begins with
"0x", it is interpreted as a hexadecimal constant. For example:

```
  -2s    // a short -2
0123s    // octal
0x7ffs   //hexadecimal
```

int integer constants are intended for representing 32-bit signed
quantities. The form of an int constant is an ordinary integer
constant, although it is acceptable to optionally append a single "l"
or "L" (again, deprecated). Be careful, though, the L suffix is
interpreted as a 32 bit integer, and never as a 64 bit integer. This can
be confusing since the C long type can ambiguously be either 32 bit or 64
bit.

If an int constant begins with "0", it is interpreted as octal,
except that if it begins with "0x", it is interpreted as a hexadecimal
constant (but see opaque constants below). Examples of valid int
constants include:

```
-2
1234567890L
0123        // octal
0x7ff       // hexadecimal
```

`int64` integer constants are intended for representing 64-bit
signed quantities. The form of an `int64` constant is an integer
constant with an "ll" or "LL" appended. If an `int64` constant
begins with "0", it is interpreted as octal, except that if it begins
with "0x", it is interpreted as a hexadecimal constant. For example:

```
-2ll      // an unsigned -2
0123LL    // octal
0x7ffLL   //hexadecimal
```

Floating point constants of type `float` are appropriate for
representing floating point data with about seven significant digits
of precision. The form of a `float` constant is the same as a C
floating point constant with an "f or "F" appended. For example the
following are all acceptable `float` constants:

```
-2.0f
 3.14159265358979f   // will be truncated to less precision
 1.f
```

Floating point constants of type `double` are appropriate for
representing floating point data with about sixteen significant digits
of precision. The form of a `double` constant is the same as a C
floating point constant. An optional "d" or "D" may be appended. For
example the following are all acceptable `double` constants:

```
-2.0
 3.141592653589793
 1.0e-20
 1.d
```

Unsigned integer constants can be created by appending the character 'U'
or 'u' between the constant and any trailing size specifier, or
immediately at the end of the size specifier. Thus one could say "10U",
"100su", "100000ul", or "1000000llu", for example.

Single character constants may be enclosed in single quotes. If a
sequence of one or more characters is enclosed in double quotes, then
its interpretation must be inferred from the context. If the dataset is
created using the netCDF classic model, then all such constants are
interpreted as a character array, so each character in the constant is
interpreted as if it were a single character. If the dataset is netCDF
extended, then the constant may be interpreted as for the classic model
or as a true string (see below) depending on the type of the attribute
or variable into which the string is contained.

The interpretation of `char` constants is that those that are in the
printable ASCII range (' '..'~') are assumed to be encoded as the
1-byte subset of UTF-8, which is equivalent to US-ASCII. In all cases,
the usual C string escape conventions are honored for values from 0 thru 127.
Values greater than 127 are allowed, but their encoding is
undefined. For netCDF extended, the use of the char type is deprecated
in favor of the string type.

Some character constant examples are as follows.

```
'a'              // ASCII `a'
"a"              // equivalent to 'a'
"Two\nlines\n"   // a 10-character string with two embedded newlines
"a bell:\007"    // a string containing an ASCII bell
```

Note that the netCDF character array "a" would fit in a one-element
variable, since no terminating NULL character is assumed. However, a
zero byte in a character array is interpreted as the end of the
significant characters by the **ncdump** program, following the C
convention. Therefore, a NULL byte should not be embedded in a
character string unless at the end: use the `byte` data type instead
for byte arrays that contain the zero byte.

String constants are, like character constants, represented using
double quotes. This represents a potential ambiguity since a
multi-character string may also indicate a dimensioned character value.
Disambiguation usually occurs by context, but care should be taken to
specify the `string` type to ensure the proper choice. String constants
are assumed to always be UTF-8 encoded. This specifically means that the
`string` constant may actually contain multi-byte UTF-8 characters. The
special constant "NIL" can be used to represent a nil string, which is
not the same as a zero length string.

Opaque constants are represented as sequences of hexadecimal
digits preceded by "0X" or "0x:", for example, "0xaa34ffff". These constants
can still be used as integer constants and will be either truncated or
extended as necessary.

## Compound Constant Expressions

In order to assign values to variables (or attributes) whose type is
user-defined type, the constant notation has been extended to include
sequences of constants enclosed in curly brackets (e.g. "{"..."}"). Such
a constant is called a compound constant, and compound constants can be
nested.

Given a type "T(*) vlen_t", where T is some other arbitrary base type,
constants for this should be specified as follows.

```
vlen_t var[2] = {t11,t12,...t1N}, {t21,t22,...t2m};
```

The values tij, are assumed to be constants of type T.

Given a type "compound cmpd_t {T1 f1; T2 f2...Tn fn}", where the Ti are
other arbitrary base types, constants for this should be specified as
follows.

```
cmpd_t var[2] = {t11,t12,...t1N}, {t21,t22,...t2n};
```

The values tij, are assumed to be constants of type Ti. If the fields
are missing, then they will be set using any specified or default fill
value for the field's base type.

The general set of rules for using braces are defined in the
[Specifying Datalists section](#specifying-datalists) below.

## Scoping Rules

With the addition of groups, the name space for defined objects is no
longer flat. References (names) of any type, dimension, or variable may
be prefixed with the absolute path specifying a specific declaration.
Thus one might say

```
variables:
    /g1/g2/t1 v1;
```

The type being referenced (t1) is the one within group g2, which in turn
is nested in group g1. The similarity of this notation to Unix file
paths is deliberate, and one can consider groups as a form of directory
structure.

When name is not prefixed, then scope rules are applied to locate the
specified declaration. Currently, there are three rules: one for
dimensions, one for types and enumeration constants, and one for all
others.

1. When an unprefixed name of a dimension is used (as in a variable
declaration), **ncgen** first looks in the immediately enclosing group for
the dimension. If it is not found there, then it looks in the group
enclosing this group. This continues up the group hierarchy until the
dimension is found, or there are no more groups to search.

2. When an unprefixed name of a type or an enumeration constant is
used, ncgen searches the group tree using a pre-order depth-first
search. This essentially means that it will find the matching
declaration that precedes the reference textually in the cdl file and
that is "highest" in the group hierarchy.

3. For all other names, only the immediately enclosing group is
searched.

One final note. Forward references are not allowed. This means that
specifying, for example, /g1/g2/t1 will fail if this reference occurs
before g1 and/or g2 are defined.

## Specifying Enumeration Constants

References to Enumeration constants (in data lists) can be ambiguous
since the same enumeration constant name can be defined in more than one
enumeration. If a cdl file specified an ambiguous constant, then ncgen
will signal an error. Such constants can be disambiguated in two ways.

1.  Prefix the enumeration constant with the name of the enumeration
    separated by a dot: `enum.econst`, for example.

2.  If case one is not sufficient to disambiguate the enumeration
    constant, then one must specify the precise enumeration type using a
    group path: `/g1/g2/enum.econst`, for example.

## Special Attributes

Special, virtual, attributes can be specified to provide
performance-related information about the file format and about variable
properties. The file must be a netCDF-4 file for these to take effect.

These special virtual attributes are not actually part of the file, they
are merely a convenient way to set miscellaneous properties of the data
in CDL

The special attributes currently supported are as follows: `_Format`,
`_Fletcher32`, `_ChunkSizes`, `_Endianness`, `_DeflateLevel`,
`_Shuffle`, and `_Storage`.

`_Format` is a global attribute specifying the netCDF format variant.
Its value must be a single string matching one of `classic`, `64-bit
offset`, `64-bit data`, `netCDF-4`, or `netCDF-4 classic model`.

The rest of the special attributes are all variable attributes.
Essentially all of then map to some corresponding `nc_def_var_XXX`
function as defined in the netCDF-4 C API. For the attributes that are
essentially boolean (`_Fletcher32`, `_Shuffle`, and `_NOFILL`), the value
true can be specified by using the strings `true` or `1`, or by using
the integer 1. The value false expects either `false`, `0`, or the
integer 0. The actions associated with these attributes are as follows.

1.  `_Fletcher32` sets the `fletcher32` property for a variable.

2.  `_Endianness` is either `little` or `big`, depending on how the
    variable is stored when first written.

3.  `_DeflateLevel` is an integer between 0 and 9 inclusive if
    compression has been specified for the variable.

4.  `_Shuffle` specifies if the the shuffle filter should be used.

5.  `_Storage` is `contiguous` or `compact` or `chunked`.

6.  `_ChunkSizes` is a list of chunk sizes for each dimension of the
    variable

Note that attributes such as "add_offset" or "scale_factor" have no
special meaning to **ncgen**. These attributes are currently conventions,
handled above the library layer by other utility packages, for example
NCO.

## Specifying Datalists

Specifying datalists for variables in the `data:` section can be
somewhat complicated. There are some rules that must be followed to
ensure that datalists are parsed correctly by **ncgen**.

First, the top level is automatically assumed to be a list of items, so
it should not be inside {...}. That means that if the variable is a
scalar, there will be a single top-level element and if the variable is
an array, there will be N top-level elements. For each element of the
top level list, the following rules should be applied.

1. Instances of UNLIMITED dimensions (other than the first dimension)
   must be surrounded by {...} in order to specify the size.

2. Compound instances must be embedded in {...}

3. Non-scalar fields of compound instances must be embedded in {...}.

4. Instances of vlens must be surrounded by {...} in order to specify
   the size.

5. Datalists associated with attributes are implicitly a vector (i.e., a
list) of values of the type of the attribute and the above rules must
apply with that in mind.

6. No other use of braces is allowed.

Note that one consequence of these rules is that arrays of values cannot
have subarrays within braces. Consider, for example, int
var(d1)(d2)...(dn), where none of d2...dn are unlimited. A datalist for
this variable must be a single list of integers, where the number of
integers is no more than D=d1\*d2\*...dn values; note that the list can
be less than D, in which case fill values will be used to pad the list.

Rule 5 about attribute datalist has the following consequence. If the
type of the attribute is a compound (or vlen) type, and if the number of
entries in the list is one, then the compound instances must be enclosed
in braces.

## Specifying Character Datalists

Specifying datalists for variables of type char also has some
complications. consider, for example

```
dimensions: u=UNLIMITED; d1=1; d2=2; d3=3;
            d4=4; d5=5; u2=UNLIMITED;
variables: char var(d4,d5);
datalist: var="1", "two", "three";
```

We have twenty elements of var to fill (d5 X d4) and we have three
strings of length 1, 3, 5. How do we assign the characters in the
strings to the twenty elements?

This is challenging because it is desirable to mimic the original ncgen
(ncgen3). The core algorithm is notionally as follows.

1.  Assume we have a set of dimensions D1..Dn, where D1 may optionally
    be an Unlimited dimension. It is assumed that the sizes of the Di
    are all known (including unlimited dimensions).

2.  Given a sequence of string or character constants C1..Cm, our goal
    is to construct a single string whose length is the cross product of
    D1 thru Dn. Note that for purposes of this algorithm, character
    constants are treated as strings of size 1.

3.  Construct Dx = cross product of D1 thru D(n-1).

4.  For each constant Ci, add fill characters as needed so that its
    length is a multiple of Dn.

5.  Concatenate the modified C1..Cm to produce string S.

6.  Add fill characters to S to make its length be a multiple of Dn.

7.  If S is longer than the Dx * Dn, then truncate and generate a
    warning.

There are three other cases of note.

1.  If there is only a single, unlimited dimension, then all of the
    constants are concatenated and fill characters are added to the end
    of the resulting string to make its length be that of the unlimited
    dimension. If the length is larger than the unlimited dimension,
    then it is truncated with a warning.

2.  For the case of character typed vlen, "char(\*) vlen_t" for example.
    we simply concatenate all the constants with no filling at all.

3.  For the case of a character typed attribute, we simply concatenate
    all the constants.

In netcdf-4, dimensions other than the first can be unlimited. Of course
by the rules above, the interior unlimited instances must be delimited
by {...}. For example.

```
variables: char var(u,u2);
datalist: var={"1", "two"}, {"three"};
```

In this case u will have the effective length of two. Within each
instance of u2, the rules above will apply, leading to this.

```
datalist: var={"1","t","w","o"}, {"t","h","r","e","e"};
```

The effective size of u2 will be the max of the two instance lengths
(five in this case) and the shorter will be padded to produce this.

```
datalist: var={"1","t","w","o","\0"}, {"t","h","r","e","e"};
```

Consider an even more complicated case.

```
variables: char var(u,u2,u3);
datalist: var={ {"1", "two"} }, { {"three"},{"four","xy"} };
```

In this case u again will have the effective length of two. The u2
dimensions will have a size = max(1,2) = 2; Within each instance of u2,
the rules above will apply, leading to this.

```
datalist: var={ {"1","t","w","o"} }, { {"t","h","r","e","e"},{"f","o","u","r","x","y"} };
```

The effective size of u3 will be the max of the two instance lengths
(six in this case) and the shorter ones will be padded to produce this.

```
datalist: var={ {"1","t","w","o"," "," "} }, { {"t","h","r","e","e"," "},{"f","o","u","r","x","y"} };
```

Note however that the first instance of u2 is less than the max length
of u2, so we need to add a filler for another instance of u2, producing
this.

```
datalist: var={ {"1","t","w","o"," "," "},{" "," "," "," "," "," "} }, { {"t","h","r","e","e"," "},{"f","o","u","r","x","y"} };
```

# BUGS

The programs generated by **ncgen** when using the -c flag use
initialization statements to store data in variables, and will fail to
produce compilable programs if you try to use them for large datasets,
since the resulting statements may exceed the line length or number of
continuation statements permitted by the compiler.

The CDL syntax makes it easy to assign what looks like an array of
variable-length strings to a netCDF variable, but the strings may simply
be concatenated into a single array of characters. Specific use of the
`string` type specifier may solve the problem.

# Identifiers and Keywords

Under certain conditions, some keywords can be used as identifiers.

1. If a type keyword is not a type supported by the format of the .cdl
file, then it can be used as an identifier. So, for example, when
translating a .cdl file as a netCDF-3 file, then "string" or "uint64"
can be used as identifiers.

2. The keyword "data" can be used as an identifier because it can be tested
in a context sensitive fashion to see if "data" is a keyword versus an
identifier.

# CDL Grammar

The file ncgen.y is the definitive grammar for CDL, but a stripped down
version is included here for completeness.

```
ncdesc: NETCDF
    datasetid
        rootgroup
        ;

datasetid: DATASETID

rootgroup: '{'
           groupbody
           subgrouplist
           '}';

groupbody:
    attrdecllist
        typesection
        dimsection
        vasection
        datasection
        ;

subgrouplist:
      /*empty*/
    | subgrouplist namedgroup
    ;

namedgroup: GROUP ident '{'
    groupbody
    subgrouplist
    '}'
    attrdecllist
    ;

typesection:
      /* empty */
    | TYPES
    | TYPES typedecls
    ;

typedecls:
      type_or_attr_decl
    | typedecls type_or_attr_decl
    ;

typename: ident ;

type_or_attr_decl:
      typedecl
    | attrdecl ';'
    ;

typedecl:
      enumdecl optsemicolon
    | compounddecl optsemicolon
    | vlendecl optsemicolon
    | opaquedecl optsemicolon
    ;

optsemicolon:
      /*empty*/
    | ';'
    ;

enumdecl: primtype ENUM typename ;

enumidlist:
      enumid
    | enumidlist ',' enumid
    ;

enumid: ident '=' constint ;

opaquedecl: OPAQUE '(' INT_CONST ')' typename ;

vlendecl: typeref '(' '*' ')' typename ;

compounddecl: COMPOUND typename '{' fields '}' ;

fields:
      field ';'
    | fields field ';'
    ;

field: typeref fieldlist ;

primtype:
      CHAR_K
    | BYTE_K
    | SHORT_K
    | INT_K
    | FLOAT_K
    | DOUBLE_K
    | UBYTE_K
    | USHORT_K
    | UINT_K
    | INT64_K
    | UINT64_K
    ;

dimsection:
      /* empty */
    | DIMENSIONS
    | DIMENSIONS dimdecls
    ;

dimdecls:
      dim_or_attr_decl ';'
    | dimdecls dim_or_attr_decl ';'
    ;

dim_or_attr_decl: dimdeclist  | attrdecl  ;

dimdeclist:
      dimdecl
    | dimdeclist ',' dimdecl
    ;

dimdecl:
      dimd '=' UINT_CONST
    | dimd '=' INT_CONST
    | dimd '=' DOUBLE_CONST
    | dimd '=' NC_UNLIMITED_K
    ;

dimd:
    ident ;

vasection:
      /* empty */
    | VARIABLES
    | VARIABLES vadecls
    ;

vadecls:
      vadecl_or_attr ';'
    | vadecls vadecl_or_attr ';'
    ;

vadecl_or_attr:
    vardecl  | attrdecl  ;

vardecl:
    typeref varlist ;

varlist:
      varspec
    | varlist ',' varspec
    ;

varspec:
    ident dimspec ;

dimspec:
      /* empty */
    | '(' dimlist ')'
    ;

dimlist:
      dimref
    | dimlist ',' dimref
    ;

dimref: path ;

fieldlist:
      fieldspec
    | fieldlist ',' fieldspec
    ;

fieldspec: ident fielddimspec ;

fielddimspec:
      /* empty */
    | '(' fielddimlist ')'
    ;

fielddimlist:
      fielddim
    | fielddimlist ',' fielddim
    ;

fielddim:
      UINT_CONST
    | INT_CONST
    ;

/* Use this when referencing defined objects */
varref: type_var_ref ;

typeref: type_var_ref      ;

type_var_ref:
      path
    | primtype
    ;

/* Use this for all attribute decls */
/* Watch out; this is left recursive */
attrdecllist:
      /*empty*/
    | attrdecl ';' attrdecllist
    ;

attrdecl:
      ':' ident '=' datalist
    | typeref type_var_ref ':' ident '=' datalist
    | type_var_ref ':' ident '=' datalist
    | type_var_ref ':' _FILLVALUE '=' datalist
    | typeref type_var_ref ':' _FILLVALUE '=' datalist
    | type_var_ref ':' _STORAGE '=' conststring
    | type_var_ref ':' _CHUNKSIZES '=' intlist
    | type_var_ref ':' _FLETCHER32 '=' constbool
    | type_var_ref ':' _DEFLATELEVEL '=' constint
    | type_var_ref ':' _SHUFFLE '=' constbool
    | type_var_ref ':' _ENDIANNESS '=' conststring
    | type_var_ref ':' _NOFILL '=' constbool
    | ':' _FORMAT '=' conststring
    ;

path:
      ident
    | PATH
    ;

datasection:
      /* empty */
    | DATA
    | DATA datadecls
    ;

datadecls:
      datadecl ';'
    | datadecls datadecl ';'
    ;

datadecl: varref '=' datalist ;
datalist:
      datalist0
    | datalist1
    ;

datalist0:
    /*empty*/
    ;

/* Must have at least 1 element */
datalist1:
      dataitem
    | datalist ',' dataitem
    ;

dataitem:
      constdata
    | '{' datalist '}'
    ;

constdata:
      simpleconstant
    | OPAQUESTRING
    | FILLMARKER
    | NIL
    | econstref
    | function
    ;

econstref: path ;

function: ident '(' arglist ')' ;

arglist:
      simpleconstant
    | arglist ',' simpleconstant
    ;

simpleconstant:
      CHAR_CONST /* never used apparently*/
    | BYTE_CONST
    | SHORT_CONST
    | INT_CONST
    | INT64_CONST
    | UBYTE_CONST
    | USHORT_CONST
    | UINT_CONST
    | UINT64_CONST
    | FLOAT_CONST
    | DOUBLE_CONST
    | TERMSTRING
    ;

intlist:
      constint
    | intlist ',' constint
    ;

constint:
      INT_CONST
    | UINT_CONST
    | INT64_CONST
    | UINT64_CONST
    ;

conststring: TERMSTRING ;

constbool:
      conststring
    | constint
    ;
```
