const API_URL = 'http://localhost:8080/api';

function initNavbar() {
  const token = localStorage.getItem('token');
  const user = localStorage.getItem('user');
  let parsedUser = null;
  try { parsedUser = user ? JSON.parse(user) : null; } catch (e) {}

  const isLoggedIn = !!token;
  const isAdmin = parsedUser?.rol === 'ADMIN';

  const navbar = document.createElement('header');
  navbar.className = 'navbar';

  navbar.innerHTML = `
    <a href="index.html" class="logo">🛒 ElectroShop</a>
    <nav class="nav-links">
      <a href="index.html">Inicio</a>
      <a href="products.html">Productos</a>
      <a href="cart.html">Carrito <span id="cart-count">0</span></a>
      <a href="orders.html">Pedidos</a>
      ${isAdmin ? '<a href="admin.html">Admin</a>' : ''}
      ${isLoggedIn ? '<a href="#" onclick="logout()">Salir</a>' : '<a href="login.html">Ingresar</a>'}
      <button class="btn btn-dark-mode" id="darkToggle" onclick="toggleDarkMode()">🌙</button>
    </nav>
  `;

  document.body.insertBefore(navbar, document.body.firstChild);
}

function initFooter() {
  const footer = document.createElement('footer');
  footer.innerHTML = '<p>&copy; 2026 ElectroShop</p>';
  document.body.appendChild(footer);
}

function initDarkMode() {
  const theme = localStorage.getItem('theme');
  if (theme === 'dark') {
    document.body.classList.add('dark-mode');
    const btn = document.getElementById('darkToggle');
    if (btn) btn.textContent = '☀️';
  }
}

function formatCOP(value) {
  return new Intl.NumberFormat('es-CO', {
    style: 'currency',
    currency: 'COP',
    minimumFractionDigits: 0
  }).format(value);
}

function toggleDarkMode() {
  const body = document.body;
  body.classList.toggle('dark-mode');
  const isDark = body.classList.contains('dark-mode');
  localStorage.setItem('theme', isDark ? 'dark' : 'light');
  const btn = document.getElementById('darkToggle');
  if (btn) btn.textContent = isDark ? '☀️' : '🌙';
}

async function updateCartCount() {
  const token = localStorage.getItem('token');
  if (!token) {
    const el = document.getElementById('cart-count');
    if (el) el.textContent = '0';
    return;
  }
  try {
    const response = await fetch(API_URL + '/cart', {
      headers: { 'Authorization': 'Bearer ' + token }
    });
    if (response.ok) {
      const cart = await response.json();
      const el = document.getElementById('cart-count');
      if (el) el.textContent = cart.cantidadTotal || 0;
    }
  } catch (e) {}
}

function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  window.location.href = 'index.html';
}

document.addEventListener('DOMContentLoaded', () => {
  initNavbar();
  initFooter();
  initDarkMode();
  updateCartCount();
});
