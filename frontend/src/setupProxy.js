const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            // target: process.env.REACT_APP_BACKEND_URL,
            target: 'http://192.168.2.148:5555',
            changeOrigin: true,
        })
    );
};