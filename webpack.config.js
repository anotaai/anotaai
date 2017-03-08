var path = require('path');
var webpack = require('webpack');
var appScriptsFolder = '/src/main/webapp/scripts';
var appCssFolder = '/src/main/webapp/styles';
 

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
		//loaders sao funcoes do node que pegam um artefato de uma fonte e transformam em javascript
		rules: [
		
			{ test: /bootstrap\/js\//, loader: 'imports?jQuery=jquery' },
			{ test: /\.css$/, loader: "style-loader!css-loader" },
			{ test: /\.woff($|\?)|\.woff2($|\?)|\.ttf($|\?)|\.eot($|\?)|\.svg($|\?)/, loader: 'url-loader' }
		]
	
	},
	
	plugins: [

		new webpack.ProvidePlugin({
			$: "jquery",
			jQuery: "jquery"
		})
		
	]	
}