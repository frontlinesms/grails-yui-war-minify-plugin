Grails YUI War Minify Plugin
========================

Grails plugin to run [yui-compressor][1] on css and javascript resources at WAR creation time.

# Usage

Add the following to the `plugins` closure in `BuildConfig.groovy`:

	build ':yui-war-minify:1.3'

# Notes

Minification will only occur on war creation.  Advantages of this over yui-minify-resources-plugin:

* no slow-down in development
* no slow-down on server startup/first page load
* no extra resources are required on server
* still easy to debug javascript and CSS in development mode

# YUI Compressor Settings

## CSS

    // enable/disable CSS minification
    grails.build.yuiminify.css.enabled = true

    // line break after the specified column number 
    // or 0 to force line break after each semi-colon
    grails.build.yuiminify.css.lineBreak = 8000

## Javascript

    // enable/disable JavaScript minification
    grails.build.yuiminify.js.enabled = true

    // line break after the specified column number
    // or 0 to force line break after each semi-colon
    grails.build.yuiminify.js.lineBreak = 8000

    // obfuscate local symbols
    grails.build.yuiminify.js.munge = true

    // preserve unnecessary semicolons
    grails.build.yuiminify.js.preserveSemiColons = true

    // enable/disable micro optimizations
    grails.build.yuiminify.js.optimizations = true

* verbose output location: target/yuicompressor-javascript-report.txt

# TODO

* allow configuration of verbose output location (file or stream?)

[1]: https://github.com/yui/yuicompressor/
