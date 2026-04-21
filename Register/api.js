// ============================================
// API CONFIGURATION
// ============================================
const API_BASE_URL = 'http://localhost:8080';

// ============================================
// TOKEN HELPERS
// ============================================
function saveToken(token) {
    localStorage.setItem('token', token);
}

function getToken() {
    return localStorage.getItem('token');
}

function saveUser(user) {
    localStorage.setItem('user', JSON.stringify(user));
}

function getUser() {
    return JSON.parse(localStorage.getItem('user'));
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

function isLoggedIn() {
    return getToken() !== null;
}

// ============================================
// API CALL HELPER
// ============================================
async function apiCall(endpoint, method = 'GET', body = null, requiresAuth = true) {
    const headers = { 'Content-Type': 'application/json' };

    if (requiresAuth) {
        const token = getToken();
        if (!token) {
            window.location.href = 'index.html';
            return;
        }
        headers['Authorization'] = `Bearer ${token}`;
    }

    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);

    const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
    const data = await response.json();
    return data;
}

// ============================================
// AUTH APIs
// ============================================
async function loginUser(email, password) {
    return await apiCall('/api/auth/login', 'POST', { email, password }, false);
}

async function registerUser(userData) {
    return await apiCall('/api/auth/register', 'POST', userData, false);
}

// ============================================
// BOOTH APIs
// ============================================
async function getAllBooths() {
    return await apiCall('/api/booths', 'GET', null, false);
}

async function getBoothById(id) {
    return await apiCall(`/api/booths/${id}`, 'GET', null, false);
}

async function getBoothHistory(id, hours = 6) {
    return await apiCall(`/api/booths/${id}/history?hours=${hours}`, 'GET', null, false);
}

// ============================================
// ADMIN APIs
// ============================================
async function createBooth(boothData) {
    return await apiCall('/api/admin/booths', 'POST', boothData);
}

async function updateQueue(boothId, queueLength) {
    return await apiCall('/api/admin/queue/update', 'PUT', { boothId, queueLength });
}