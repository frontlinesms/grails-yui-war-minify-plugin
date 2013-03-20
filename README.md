Grails War Minify Plugin
========================

Grails plugin to run yui-compressor on css and javascript resources at WAR creation time.

# Usage

Add the following to the `plugins` closure in `BuildConfig.groovy`:

	build ':war-minify:0.1'

# Notes

Minification will only occur on war creation.  Advantages of this over yui-minify-resources-plugin:

* no slow-down in development
* no slow-down on server startup/first page load
* no extra resources are required on server
* still easy to debug javascript and CSS in development mode

