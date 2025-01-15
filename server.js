const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();
const port = 3001;

app.use(express.json());

app.get('/orders', (req, res) => {
  const filePath = path.join(__dirname, 'order.json');
  fs.readFile(filePath, 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading file:', err);
      return res.status(500).json({ error: 'Error reading file' });
    }
    res.json(JSON.parse(data));
  });
});

app.post('/orders', (req, res) => {
  console.log('Received POST data:');
  console.log(JSON.stringify(req.body, null, 2));
  res.status(200).json({ message: 'Data received successfully' });
});

app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});
