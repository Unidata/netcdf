# NUG Style Guide

The NUG is being converted from either straight doxygen (typically `.dox` files) or mixed doxygen/markdown files (typically `.md` files).  During the conversion, we will apply the following style conventions.  This list may expand.

For the time being, the final files will use some `doxygen` conventions, as we are converting the source files but we are **not**, as of yet, removing the NUG from the `doxygen` ecosystem.

## References

* [Doxygen Documentation](http://www.doxygen.nl/)
	* [Doxygen Markdown Documentation](http://www.doxygen.nl/manual/markdown.html)
	* [Doxygen Tags](http://www.doxygen.nl/manual/commands.html)
* [Basic Markdown Syntax Cheatsheet](https://www.markdownguide.org/basic-syntax/)


## Style Conventions

* File names end with the `.md` prefix.
* Files are organized one rendered page per file, in markdown format.  *This may require splitting some .dox files (which can contain multiple rendered pages) into multiple files*.
* Paragraphs written one sentence per line, with no blank space between them.  Blank spaces denote different paragraphs. This helps with diff viewing.
