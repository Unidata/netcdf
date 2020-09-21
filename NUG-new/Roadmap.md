# NUG Roadmap

Discussed during 17 Sept 2020 call -- Ward, Dennis, Doug, Ryan, Sean, Ethan
* End goal: pure Markdown (no Doxygen); Publish to docs.unidata.ucar.edu; use Jekyll or ???
* What is NUG? (see [below](#what-is-nug))
* Target audience(s) (see [below](#target-audience))
* Next Steps (see [below](#next-steps))

## Next Steps
1) Create framework:
   * Create Markdown files for NUG Outline ([below](#nug-outline))
   * Setup auto build and publish workflow 
   * Add Contributing guidelines
   * Add NUG Styleguide
2) Expand the "outline" docs with content from current NUG
   * While doing this, need to start conversations about where to put content that will not be in the new NUG.
     * Current [data types page](https://www.unidata.ucar.edu/software/netcdf/docs/data_type.html) uses C libary names.
       Should that information go into netcdf-c repo?
       If so, who should create a netcdf-c PR?
     * Current CDL content is on [same page](https://www.unidata.ucar.edu/software/netcdf/docs/netcdf_utilities_guide.html) with ncdump and ncgen.
       CDL should be in new NUG but ncdump and ncgen docs should probably be in C docs.

## NUG Outline
* What is the NUG?
* What is netCDF?
  * Data models
  * CDL (?? here or elsewhere?)
  * File (Dataset) formats -- Encodings
  * APIs
  * Libraries
* Data Models
  * Classic Data Model
  * Enhanced Data model
* File Formats
  * Nc-3, 64-bit, CDF5, nc-4, ncZarr, ncstream?
    * Each one’s relationship with data models
  * Read-able in each library (DAP2, DAP4, CDM?)  --- Not in NUG??? Some middle ground
    * Mappings
* Best Practices
* Attribute conventions (currently Appendix A)
* External Conventions list/page
* How to use netCDF (target non-programmer, data producer/user that knows CDL, ncgen, ncdump, NCO?)
* Details on File Formats and other Encodings
  * File format / Encoding specifications (nc-3, 64-bit, CDF5, nc-4, ncZarr, ncstream)
  * Things like DAP2 and DAP4
    * Mappings between data models   

## What is NUG?
NUG is a document that describes netCDF to any user of netCDF, independent of which library/tool they use. Including
* Best practices
* Basic attribute conventions (units, valid_min, etc.)
* How to use netCDF (gallery of tools)
* External conventions (e.g., CF, UGRID) - see current [list/page](https://www.unidata.ucar.edu/software/netcdf/conventions.html)

NUG is a document to captures technical details (maybe in Appendix) needed by developer of an implementation of netCDF

Document to describe netCDF to e.g. program manager, data producer, data user, data manager

Document with technical details for anyone working on a netCDF implementation based on one of the formats (e.g. netCDF-4 based on HDF5 for h5netcdf, ncZarr based on Zarr for netCDF-java)

## Target Audience
* Non-programmer, data producer/user that knows CDL, ncgen, ncdump, NCO, etc.)
* Application developer (aka user of a netCDF library -- useful no matter which library they use)
* Developer of a netCDF implementation
  * Provide a shared understanding of netCDF concepts
