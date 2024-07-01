const express = require('express');
const bodyParser = require('body-parser');
const db = require('./src/config/db'); // Mengimpor inisialisasi Firestore

const app = express();
const port = 8080;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Route untuk endpoint '/'
app.get('/', (req, res) => {
  res.send('Rodex-Capstone');
});

// Route untuk register user
app.post('/register', async (req, res) => {
    const { username, email, password } = req.body;
  
    // Pastikan username, email, dan password tidak undefined
    if (!username || !email || !password) {
      return res.status(400).send('Username, email, or password is missing');
    }
  
    try {
      // Simpan data user ke Firestore
      await db.collection('users').add({
        username: username,
        email: email,
        password: password
      });
  
      res.send(`Registered user: ${username}`);
    } catch (error) {
      console.error('Error registering user:', error);
      res.status(500).send('Error registering user');
    }
  });
  
// Route untuk login user
app.post('/login', async (req, res) => {
  const { username, password } = req.body;

  try {
    // Query user berdasarkan username dan password
    const snapshot = await db.collection('users').where('username', '==', username).where('password', '==', password).get();

    if (snapshot.empty) {
      res.status(404).send('User not found');
      return;
    }

    // Contoh sederhana untuk menanggapi
    res.send(`Logged in user: ${username}`);
  } catch (error) {
    console.error('Error logging in user:', error);
    res.status(500).send('Error logging in user');
  }
});

// Route untuk memulai inspeksi baru
// Route untuk memulai inspeksi baru
app.post('/inspection/new', async (req, res) => {
  const { name_of_officer, name_of_road, length_of_road, type_of_road_surface, location_start } = req.body;

  // Pastikan semua data yang diperlukan tersedia
  if (!name_of_officer || !name_of_road || !length_of_road || !type_of_road_surface || !location_start) {
    return res.status(400).send('Missing required data for inspection');
  }

  try {
    // Simpan data inspeksi baru ke Firestore
    await db.collection('inspections').add({
      name_of_officer: name_of_officer,
      name_of_road: name_of_road,
      length_of_road: length_of_road,
      type_of_road_surface: type_of_road_surface,
      location_start: location_start,
      status: 'ongoing' // Menambahkan status inspeksi
    });

    res.send(`New inspection started at ${name_of_road}`);
  } catch (error) {
    console.error('Error starting new inspection:', error);
    res.status(500).send('Failed to start new inspection');
  }
});
// Route untuk menyimpan kerusakan
app.post('/inspection/detected', async (req, res) => {
  const { image, count_damages, count_damages_type_0, count_damages_type_1, count_damages_type_2, count_damages_type_3, location, detected } = req.body;

  try {
    // Simpan data kerusakan ke Firestore
    await db.collection('damages').add({
      image: image,
      count_damages: count_damages,
      count_damages_type_0: count_damages_type_0,
      count_damages_type_1: count_damages_type_1,
      count_damages_type_2: count_damages_type_2,
      count_damages_type_3: count_damages_type_3,
      location: location,
      detected: detected
    });

    res.send('Damages saved');
  } catch (error) {
    console.error('Error saving damages:', error);
    res.status(500).send('Error saving damages');
  }
});

// Route untuk mengakhiri inspeksi
app.post('/inspection/end', async (req, res) => {
  const { location_end } = req.body;

  try {
    // Mengupdate status inspeksi yang sedang berlangsung menjadi selesai di Firestore
    const snapshot = await db.collection('inspections').where('status', '==', 'ongoing').limit(1).get();
    if (!snapshot.empty) {
      const doc = snapshot.docs[0];
      await doc.ref.update({
        location_end: location_end,
        status: 'completed'
      });
    }

    res.send('Inspection ended');
  } catch (error) {
    console.error('Error ending inspection:', error);
    res.status(500).send('Error ending inspection');
  }
});

// Route untuk mendapatkan riwayat inspeksi
app.get('/inspection/history', async (req, res) => {
    try {
      const snapshot = await db.collection('inspections').get();
      const inspections = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
      
      res.json(inspections);
    } catch (error) {
      console.error('Error fetching inspection history:', error);
      res.status(500).send('Error fetching inspection history');
    }
  });
  
  // Route untuk mendapatkan detail inspeksi berdasarkan ID
  app.get('/inspection/detail/:id', async (req, res) => {
    const { id } = req.params;
  
    try {
      const doc = await db.collection('inspections').doc(id).get();
  
      if (!doc.exists) {
        res.status(404).send('Inspection not found');
        return;
      }
  
      res.json({ id: doc.id, ...doc.data() });
    } catch (error) {
      console.error('Error fetching inspection detail:', error);
      res.status(500).send('Error fetching inspection detail');
    }
  });
  
// Mulai server
app.listen(port, () => {
  console.log(`App running at http://localhost:${port}`);
});
