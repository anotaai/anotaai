var path = require('path');
var webpack = require('webpack');
var appScriptsFolder = '/src/main/webapp/scripts';
var appCssFolder = '/src/main/webapp/styles';

console.log('Environment - ' +process.env.NODE_ENV);

module.exports = {
	
	entry: ['./webpack-app-module.js'],

	output: {
		path: path.resolve(__dirname+ appScriptsFolder) ,
		filename: 'bundle.js'
	},

	resolve: {
		alias: {
			'npm': __dirname + '/node_modules',
			'app-js': __dirname + appScriptsFolder,
			'app-css': __dirname + appCssFolder
		}
	},

	module: {
		//Loaders sao funcoes do node que pegam um artefato de uma fonte e transformam em javascript
		rules: [
			//Loader do babel para conversao de js mais novo para o mais antigo, caso necessario descomentar e adicionar as libs do babel no node
			//{  test: /\.jsx$/,  exclude: [/node_modules/],  use: [{ loader: 'babel-loader', options: { presets: ['es2015'] }  }]},
			{ test: /bootstrap\/js\//, loader: 'imports?jQuery=jquery' },
			{ test: /\.css$/, loader: "style-loader!css-loader" },
			{ test: /\.woff($|\?)|\.woff2($|\?)|\.ttf($|\?)|\.eot($|\?)|\.svg($|\?)/, loader: 'url-loader' }
		]
	
	},

	//stats are informations to find possible compilation errors
	stats: {
		// Add asset Information
		assets: true,
		// Sort assets by a filed
		assetsSort: "field",
		// Add information about cached (not built) modules
		cached: true,
		// Add children information
		children: true,
		// Add chunk information (setting this to `false` allows for a less verbose output)
		chunks: true,
		// Add built modules information to chunk information
		chunkModules: true,
		// Add the origins of chunks and chunk merging info
		chunkOrigins: true,
		// Sort the chunks by a field
		chunksSort: "field",
		// Context directory for request shortening
		context: "src",
		// Add errors
		errors: true,
		// Add details to errors (like resolving log)
		errorDetails: true,
		// Add the hash of the compilation
		hash: true,
		// Add built modules information
		modules: true,
		// Sort the modules by a field
		modulesSort: "field",
		// Add public path information
		publicPath: true,
		// Add information about the reasons why modules are included
		reasons: true,
		// Add the source code of modules
		source: true,
		// Add timing information
		timings: true,
		// Add webpack version information
		version: true,
		// Add warnings
		warnings: true
	},

	plugins: [

		new webpack.ProvidePlugin({
			$: "jquery",
			jQuery: "jquery"
		}),
		new webpack.optimize.UglifyJsPlugin({
			
			//remove warnings
			compress: {
				warnings: false
			},
			
			//remove comments
			output: {
			   comments: false
			},

			//Use SourceMaps to map error message locations to modules. This slows down the compilation.
			sourceMap: true,
			
			// false, some variable runtime errors if it's true
			mangle: false  

		}),

		//Where loaders can be switched to minimize mode.
		 new webpack.LoaderOptionsPlugin({
			minimize: true
		}),

		//Webpack can vary the distribution of the ids to get the smallest id length for often used ids
		 new webpack.optimize.OccurrenceOrderPlugin()
	]	
}