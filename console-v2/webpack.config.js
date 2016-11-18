var HtmlWebpackPlugin = require('html-webpack-plugin');
var webpack = require('webpack');
let path = require("path");

module.exports = {
    entry: "./src/ui/app.ts",
    output: {
        filename: "bundle.js",
        path: "dist/ui"
    },
    resolve: {
        // Add '.ts' and '.tsx' as a resolvable extension.
        extensions: ["", ".webpack.js", ".web.js", ".ts", ".tsx", ".js"]
    },
    module: {
        loaders: [
            // all files with a '.ts' or '.tsx' extension will be handled by 'ts-loader'
            { test: /\.tsx?$/, loader: "ts" },
            { test: /\.css$/, loader: "style!css" },
            { test: /\.scss$/, loader: "style!css!sass" },
            { test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,loader: "url?limit=10000&mimetype=application/font-woff" }, 
            { test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/font-woff" }, 
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=application/octet-stream" }, 
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file" }, 
            { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: "url?limit=10000&mimetype=image/svg+xml" },
            { test: /\.(png|jpe?g|gif)$/, loader: 'url?limit=25000' },
            { test: /\.html$/, exclude: /node_modules/, loader: 'html' }
        ]
    },
    plugins: [
        // new webpack.optimize.UglifyJsPlugin(
        //     {
        //         warning: false,
        //         mangle: true,
        //         comments: false
        //     }
        // ),
        new HtmlWebpackPlugin({
            template: './src/ui/index.html',
            inject: 'body',
            hash: true
        }),
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery',
            'window.jQuery': 'jquery',
            'window.jquery': 'jquery'
        })
    ]
}
