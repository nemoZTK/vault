import axios from 'axios';

const protectedApiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
  },
  withCredentials: true,
});

export default protectedApiClient;