# Documentation for Common Data Language {#CDL}

## CDL Syntax {#cdl_syntax}

~~Below is an example of CDL, describing a netCDF dataset with several named dimensions (lat, lon, time), variables (z, t, p, rh, lat, lon, time), variable attributes (units, \_FillValue, valid_range), and some data.~~

````
     netcdf foo { // example netCDF specification in CDL

     dimensions:
     lat = 10, lon = 5, time = unlimited;

     variables:
       int     lat(lat), lon(lon), time(time);
       float   z(time,lat,lon), t(time,lat,lon);
       double  p(time,lat,lon);
       int     rh(time,lat,lon);

       char lat:units = "degrees_north";
       lon:units = "degrees_east";
       time:units = "seconds";
       z:units = "meters";
       float z:valid_range = 0., 5000.;
       double p:_FillValue = -9999.;
       rh:_FillValue = -1;

     data:
       lat   = 0, 10, 20, 30, 40, 50, 60, 70, 80, 90;
       lon   = -140, -118, -96, -84, -52;
     }

````
<!-- NOTE:
The strikethrough text in this section has been moved to NUG-new/pages/cdl.md
-->

~~All CDL statements are terminated by a semicolon.
Spaces, tabs, and newlines can be used freely for readability.
Comments may follow the double slash characters '//' on any line.~~

~~A CDL description for a classic model file consists of three optional parts: dimensions, variables, and data.
The variable part may contain variable declarations and attribute assignments.
For the enhanced model supported by netCDF-4, a CDL description may also include groups, subgroups, and user-defined types.~~

~~A dimension is used to define the shape of one or more of the multidimensional variables described by the CDL description.
A dimension has a name and a length.
At most one dimension in a classic CDL description can have the unlimited length, which means a variable using this dimension can grow to any length (like a record number in a file).
Any number of dimensions can be declared of unlimited length in CDL for an enhanced model file.~~

~~A variable represents a multidimensional array of values of the same type.
A variable has a name, a data type, and a shape described by its list of dimensions.
Each variable may also have associated attributes (see below) as well as data values.
The name, data type, and shape of a variable are specified by its declaration in the variable section of a CDL description.
A variable may have the same name as a dimension; by convention such a variable contains coordinates of the dimension it names.~~

~~An attribute contains information about a variable or about the whole netCDF dataset or containing group.
Attributes may be used to specify such properties as units, special values, maximum and minimum valid values, and packing parameters.
Attribute information is represented by single values or one-dimensional arrays of values.
For example, “units” might be an attribute represented by a string such as “celsius”.
An attribute has an associated variable, a name, a data type, a length, and a value.
In contrast to variables that are intended for data, attributes are intended for ancillary data or metadata (data about data).~~

~~In CDL, an attribute is designated by a data type, a variable, and an attribute name.
The variable and the attribute name are separated by a colon (':').  
If present, the data type precedes the variable name.  
It is possible to assign global attributes to the netCDF dataset as a whole by omitting the variable name and beginning the attribute name with a colon (':').
The data type of an attribute in CDL, if not explicitly specified, is derived from the type of the value assigned to it, with one exception.  
If the value is a string, then the inferred type is char, not string.  
If it is desired to have a string typed attribute, this must be stated explicitly.~~

~~The length of an attribute is the number of data values or the number of characters in the character string assigned to it if the type is char.
Multiple values are assigned to non-character attributes by separating the values with commas (',').
All values assigned to an attribute must be of the same type.
In the netCDF-4 enhanced model, attributes may be declared to be of user-defined type, like variables.~~

~~In CDL, just as for netCDF, the names of dimensions, variables and attributes (and, in netCDF-4 files, groups, user-defined types, compound member names, and enumeration symbols) consist of arbitrary sequences of alphanumeric characters, underscore '_', period '.', plus '+', hyphen '-', or at sign '@', but beginning with a letter or underscore.
However names commencing with underscore are reserved for system use. Case is significant in netCDF names. A zero-length name is not allowed.
Some widely used conventions restrict names to only alphanumeric characters or underscores.
Names that have trailing space characters are also not permitted.~~

~~Beginning with versions `3.6.3` and `4.0`, names may also include UTF-8 encoded Unicode characters as well as other special characters, exceptfor the character '/', which may not appear in a name (because it is reserved for path names of nested groups).
In CDL, most special characters are escaped with a backslash '\' character, but that character is not actually part of the netCDF name. The special characters that do not need to be escaped in CDL names are underscore '_', period '.', plus '+', hyphen '-', or at sign '@'.
For the formal specification of CDL name syntax See \ref classic_format_spec.~~

~~> Note that by using special characters in names, you may make your data not compliant with conventions that have more stringent requirements on valid names for netCDF components, for example the CF Conventions.~~

~~The names for the primitive data types are reserved words in CDL, so names of variables, dimensions, and attributes must not be primitive type names.~~

~~The optional data section of a CDL description is where netCDF variables may be initialized.
The syntax of an initialization is simple:~~

````
     variable = value_1, value_2, ...;

````

~~The comma-delimited list of constants may be separated by spaces, tabs, and newlines.
For multidimensional arrays, the last dimension varies fastest.
Thus, row-order rather than column order is used for matrices.
If fewer values are supplied than are needed to fill a variable, it is extended with the fill value.
The types of constants need not match the type declared for a variable; coercions are done to convert integers to floating point, for example.
All meaningful type conversions among primitive types are supported.~~

~~A special notation for fill values is supported: the ‘_’ character designates a fill value for variables.~~

## CDL Data Types {#cdl_data_types}

~~The CDL primitive data types for the classic model are:~~
~~- char Characters.
- byte Eight-bit integers.
- short 16-bit signed integers.
- int 32-bit signed integers.
- long (Deprecated, synonymous with int)
- float IEEE single-precision floating point (32 bits).
- real (Synonymous with float).
- double IEEE double-precision floating point (64 bits).~~

~~NetCDF-4 supports the additional primitive types:
- ubyte Unsigned eight-bit integers.
- ushort Unsigned 16-bit integers.
- uint Unsigned 32-bit integers.
- int64 64-bit signed integers.
- uint64 Unsigned 64-bit signed integers.
- string Variable-length string of characters~~

~~Except for the added data-type byte, CDL supports the same primitive data types as C.
For backward compatibility, in declarations primitive type names may be specified in either upper or lower case.~~

~~The byte type differs from the char type in that it is intended for numeric data, and the zero byte has no special significance, as it may for character data.
The short type holds values between -32768 and 32767.
The ushort type holds values between 0 and 65536. The int type can hold values between -2147483648 and 2147483647.
The uint type holds values between 0 and 4294967296.
The int64 type can hold values between -9223372036854775808 and 9223372036854775807.
The uint64 type can hold values between 0 and 18446744073709551616.~~

~~The float type can hold values between about -3.4+38 and 3.4+38, with external representation as 32-bit IEEE normalized single-precision floating-point numbers.
The double type can hold values between about -1.7+308 and 1.7+308, with external representation as 64-bit IEEE standard normalized double-precision, floating-point numbers.
The string type holds variable length strings.~~

## CDL Notation for Data Constants {#cdl_notations_for_data_constants}

~~This section describes the CDL notation for constants.~~

~~Attributes are initialized in the variables section of a CDL description by providing a list of constants that determines the attribute's length and type (if primitive and not explicitly declared).
CDL defines a syntax for constant values that permits distinguishing among different netCDF primitive types.
The syntax for CDL constants is similar to C syntax, with type suffixes appended to bytes, shorts, and floats to distinguish them from ints and doubles.~~

~~A byte constant is represented by a single character or multiple character escape sequence enclosed in single quotes. For example:~~

````

     'a'     // ASCII a
     '\0'    // a zero byte
     '\n'    // ASCII newline character
     '\33'   // ASCII escape character (33 octal)
     '\x2b'  // ASCII plus (2b hex)
     '\376'  // 377 octal = -127 (or 254) decimal

````

~~Character constants are enclosed in double quotes.
A character array may be represented as a string enclosed in double quotes. Multiple strings are concatenated into a single array of characters, permitting long character arrays to appear on multiple lines.
To support multiple variable-length string values, a conventional delimiter such as ',' may be used, but interpretation of any such convention for a string delimiter must be implemented in software above the netCDF library layer.
The usual escape conventions for C strings are honored. For
example:~~

````
     "a"            // ASCII 'a'
     "Two\nlines\n" // a 10-character string with two embedded newlines
     "a bell:\007"  // a string containing an ASCII bell
     "ab","cde"     // the same as "abcde"

````

~~The form of a short constant is an integer constant with an 's' or 'S' appended.
If a short constant begins with '0', it is interpreted as octal.
When it begins with '0x', it is interpreted as a hexadecimal constant.
For example:~~

````
     ~~2s      // a short 2
     0123s   // octal
     0x7ffs  // hexadecimal~~

````

~~The form of an int constant is an ordinary integer constant. If an int constant begins with '0', it is interpreted as octal. When it begins with '0x', it is interpreted as a hexadecimal constant. Examples of valid int constants include:~~

````

     -2
     0123            // octal
     0x7ff           // hexadecimal
     1234567890L     // deprecated, uses old long suffix

````

~~The float type is appropriate for representing data with about seven significant digits of precision.
The form of a float constant is the same as a C floating-point constant with an 'f' or 'F' appended.
A decimal point is required in a CDL float to distinguish it from an integer.]
For example, the following are all acceptable float constants:~~

````
     -2.0f
     3.14159265358979f       // will be truncated to less precision
     1.f
     .1f

````

~~The double type is appropriate for representing floating-point data with about 16 significant digits of precision.
The form of a double constant is the same as a C floating-point constant.
An optional 'd' or 'D' may be appended.
A decimal point is required in a CDL double to distinguish it from an integer.
For example, the following are all acceptable double constants:~~

````

     -2.0
     3.141592653589793
     1.0e-20
     1.d

````
