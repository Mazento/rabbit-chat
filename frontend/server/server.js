const express = require('express');
const path = require('path');
const https = require('https');
const fs = require('fs');
const app = express();

function getServer() {
    if (!process.env.HTTPS)
        return app;

    const httpsServer = https.createServer({
        key: fs.readFileSync(path.join(__dirname, 'server.key'), 'utf8'),
        cert: fs.readFileSync(path.join(__dirname, 'server.crt'), 'utf8')
    }, app);

    return httpsServer;
}

module.exports = getServer();
