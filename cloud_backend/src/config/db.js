const { Firestore } = require('@google-cloud/firestore');
const dotenv = require('dotenv');
dotenv.config();

const db = new Firestore({
    projectId: 'capstone-426015',
    keyFilename: process.env.FIRESTOREKEY
});

module.exports = db;