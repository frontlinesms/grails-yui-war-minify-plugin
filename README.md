Grails War Minify Plugin
========================

Grails plugin to run [yui-compressor][1] on css and javascript resources at WAR creation time.

# Usage

Add the following to the `plugins` closure in `BuildConfig.groovy`:

	build ':war-minify:1.0'

# Notes

Minification will only occur on war creation.  Advantages of this over yui-minify-resources-plugin:

* no slow-down in development
* no slow-down on server startup/first page load
* no extra resources are required on server
* still easy to debug javascript and CSS in development mode

# YUI Compressor Settings

Currently no settings can be customised, but the following defaults are applied to the YUI Compressor

## CSS

* line length: -1

## Javascript

* line length: -1
* munge: true
* verbose: true
* verbose output location: target/yuicompressor-javascript-report.txt
* preserveAllSemiColons: true
* disableOptimizations: false

# TODO

* allow configuration of compressor settings
* allow configuration of verbose output location (file or stream?)

[1]: https://github.com/yui/yuicompressor/

