const { Firestore } = require('@google-cloud/firestore');
const serviceAccount = require('./path-to-your-service-account-key.json'); // Sesuaikan dengan path ke file JSON Anda

const firestore = new Firestore({
  projectId: 'capstone-426015', // Ganti dengan ID proyek Firebase Anda
  credentials: serviceAccount
});
