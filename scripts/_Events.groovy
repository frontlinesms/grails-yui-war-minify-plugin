import com.yahoo.platform.yui.compressor.*
import org.mozilla.javascript.EvaluatorException
eventCreateWarStart = { warName, stagingDir ->
	println "[Events.eventCreateWarStart] ENTRY"
	println "[Events.eventCreateWarStart] The staging dir is: $stagingDir"
	println "[Events.eventCreateWarStart] args: $args"

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
		outTemp.renameTo(source)
	}

	def compressCss = { File source ->
		compress(source) { reader, writer ->
			new CssCompressor(reader).compress(writer, -1)
		}
	}

	def compressJs = { File source, munge=true, verbose=true, preserveAllSemiColons=true, disableOptimizations=false ->
		compress(source) { reader, writer ->
			new JavaScriptCompressor(reader, createErrorReporter()).compress(writer, -1, munge, verbose, preserveAllSemiColons, disableOptimizations)
		}
	}

	println "[Events.eventCreateWarStart] Calling YUI compressor on CSS..."
	new FileNameFinder().getFileNames(stagingDir.absolutePath, '**/*.css', '**/*.min.css').each { fileName ->
		compressCss(new File(fileName))
	}

	println "[Events.eventCreateWarStart] Calling YUI compressor on Javascript..."
	new FileNameFinder().getFileNames(stagingDir.absolutePath, '**/*.js', '**/*.min.js').each { fileName ->
		compressJs(new File(fileName))
	}
}

