# Instructions for Building Markdown Documentation

## Requirements

* Java 11 (or greater)
* docker

## Quick-Start

List build options:

    $ ./gradlew tasks # get a list of tasks

To build the NUG documentation, run:

```shell
./gradlew buildJekyllSite
```

The rendered html will appear under `./build/site`.

To live edit the docs, run:

```shell
./gradlew serveJekyllSite
```

Open [http://127.0.0.1:4000](http://127.0.0.1:4000) for a live view.
As you make changes to the markdown files, the site content will automatically regenerate.

When finished editing, run `./gradlew stopServe` to shut down the container.
