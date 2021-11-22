require('./server/server').listen(3000, () => {
    const protocol = process.env.HTTPS ? 'https' : 'http';
    console.log('Server is running at ' + protocol + '://localhost:3000');
})
