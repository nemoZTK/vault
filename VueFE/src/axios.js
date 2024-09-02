import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api', // Assicurati di usare HTTPS e il numero di porta corretto
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // se il tuo backend richiede l'invio dei cookie
});

export default apiClient;