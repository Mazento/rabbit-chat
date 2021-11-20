const path = require('path');

module.exports = {
    entry: ['./src/index.js'],
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: ['babel-loader']
            },
            // {
            //     test: /\.js$/,
            //     exclude: /node_modules/,
            //     use: ['babel-loader', 'eslint-loader']
            // },
            // {
            //     test: /\.css$/,
            //     // use: ['style-loader', 'css-loader']
            // }
        ]
    },
    resolve: {
        extensions: ['*', '.js', '.jsx'],
        alias: {
            assets: path.resolve(__dirname, './src/assets/'),
            const: path.resolve(__dirname, './src/const/'),
            containers: path.resolve(__dirname, './src/containers/'),
            components: path.resolve(__dirname, './src/components/'),
            contexts: path.resolve(__dirname, './src/contexts/'),
            utils: path.resolve(__dirname, './src/utils/'),
        }
    },
    output: {
        path: __dirname + '/public',
        publicPath: '/',
        filename: 'bundle.js'
    },
    devServer: {
        port: 3000,
        // contentBase: './public',
        historyApiFallback: true
    }
};
