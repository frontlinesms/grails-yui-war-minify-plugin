import com.yahoo.platform.yui.compressor.*
import org.mozilla.javascript.EvaluatorException
import grails.util.Holders


eventCreateWarStart = { warName, stagingDir ->
	println "[Events.eventCreateWarStart] ENTRY"
	println "[Events.eventCreateWarStart] The staging dir is: $stagingDir"
	println "[Events.eventCreateWarStart] args: $args"

	def flatConfig = Holders.flatConfig
	def getConfig = { String key, def defaultValue ->
		flatConfig.containsKey(key) ? flatConfig[key] : defaultValue
	}

	def options = [
		cssEnabled: getConfig('grails.build.yuiminify.css.enabled', true),
		cssLineBreak: getConfig('grails.build.yuiminify.css.lineBreak', 8000),
		jsEnabled: getConfig('grails.build.yuiminify.js.enabled', true),
		jsLineBreak: getConfig('grails.build.yuiminify.js.lineBreak', 8000),
		jsMunge: getConfig('grails.build.yuiminify.js.munge', true),
		jsPreserveSemiColons: getConfig('grails.build.yuiminify.js.preserveSemiColons', true),
		jsOptimizations: getConfig('grails.build.yuiminify.js.optimizations', true),
	]

	def createErrorReporter = {
		new org.mozilla.javascript.ErrorReporter() {
			private def writer = new File('target/yuicompressor-javascript-report.txt')
			void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
				out('ERROR', message, sourceName, line, lineSource, lineOffset)
			}

			void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
				out('WARNING', message, sourceName, line, lineSource, lineOffset)
			}

			org.mozilla.javascript.EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
				error(message, sourceName, line, lineSource, lineOffset)
				return new org.mozilla.javascript.EvaluatorException(message)
			}

			private void out(String level, String message, String sourceName, int line, String lineSource, int lineOffset) {
				writer << "\n[$level] $sourceName"
				if(line >= 0) writer << "$line:$lineOffset"
				writer << message
			}
		}
	}

	def compress = { File source, Closure compressize ->
		File outTemp = new File("${source.absolutePath}.min")
		source.withReader { reader ->
			outTemp.withWriter { writer ->
				compressize(reader, writer)
			}
		}
		source.delete()
		if (!outTemp.renameTo(source)) {
			println "WARNING: Can't rename ${outTemp.name} to ${source.name}"
		}
	}

	def compressCss = { File source, lineBreak ->
		compress(source) { reader, writer ->
			new CssCompressor(reader).compress(writer, lineBreak)
		}
	}

	def compressJs = { File source, lineBreak, munge, verbose, preserveAllSemiColons, disableOptimizations ->
		compress(source) { reader, writer ->
			new JavaScriptCompressor(reader, createErrorReporter()).compress(writer, lineBreak, munge, verbose, preserveAllSemiColons, disableOptimizations)
		}
	}

	if (options.cssEnabled) {
		println "[Events.eventCreateWarStart] Calling YUI compressor on CSS..."
		new FileNameFinder().getFileNames(stagingDir.absolutePath, '**/*.css', '**/*.min.css').each { fileName ->
			compressCss(new File(fileName), options.cssLineBreak)
		}
	}

	if (options.jsEnabled) {
		println "[Events.eventCreateWarStart] Calling YUI compressor on Javascript..."
		new FileNameFinder().getFileNames(stagingDir.absolutePath, '**/*.js', '**/*.min.js').each { fileName ->
			compressJs(new File(fileName), options.jsLineBreak, options.jsMunge, true, options.jsPreserveSemiColons, !options.jsOptimizations)
		}
	}
}

