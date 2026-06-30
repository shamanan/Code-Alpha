# 🛍 ShopSphere — Full Stack E-Commerce Store

A complete, production-ready e-commerce application built with React, Node.js, Express, and MongoDB.

![ShopSphere](https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1200&q=80)

## ✨ Features

- **User Authentication** — Register, login with JWT + bcrypt password hashing
- **Product Catalog** — Browse, search, and filter by category with pagination
- **Product Details** — Image gallery, ratings, stock status, add-to-cart
- **Shopping Cart** — Add, remove, update quantities (persisted to MongoDB)
- **Checkout** — Shipping address, payment method selection
- **Order Placement** — Full order with price breakdown (subtotal, shipping, tax)
- **Order History** — View all past orders with status tracking
- **Order Detail** — Visual progress tracker (pending → processing → shipped → delivered)
- **Responsive UI** — Mobile-first design with Tailwind CSS
- **Loading States** — Spinners and skeleton-friendly structure throughout
- **Error Handling** — Toast notifications and graceful error boundaries
- **Sample Data** — 12 seeded products across 5 categories

---

## 🗂 Project Structure

```
shopsphere/
├── backend/
│   ├── config/
│   │   └── db.js                 # MongoDB connection
│   ├── controllers/
│   │   ├── authController.js     # Register, login, profile
│   │   ├── cartController.js     # Cart CRUD
│   │   ├── orderController.js    # Create & fetch orders
│   │   └── productController.js  # Products, categories, featured
│   ├── data/
│   │   └── seeder.js             # 12 sample products
│   ├── middleware/
│   │   ├── authMiddleware.js     # JWT protect + admin guard
│   │   └── errorMiddleware.js    # 404 + global error handler
│   ├── models/
│   │   ├── Cart.js
│   │   ├── Order.js
│   │   ├── Product.js
│   │   └── User.js
│   ├── routes/
│   │   ├── authRoutes.js
│   │   ├── cartRoutes.js
│   │   ├── orderRoutes.js
│   │   └── productRoutes.js
│   ├── .env.example
│   ├── package.json
│   └── server.js
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── cart/
│   │   │   │   └── CartItem.jsx
│   │   │   ├── common/
│   │   │   │   ├── PrivateRoute.jsx
│   │   │   │   ├── Rating.jsx
│   │   │   │   └── Spinner.jsx
│   │   │   ├── layout/
│   │   │   │   ├── Footer.jsx
│   │   │   │   ├── Layout.jsx
│   │   │   │   └── Navbar.jsx
│   │   │   └── product/
│   │   │       ├── ProductCard.jsx
│   │   │       └── ProductGrid.jsx
│   │   ├── context/
│   │   │   ├── AuthContext.jsx
│   │   │   └── CartContext.jsx
│   │   ├── pages/
│   │   │   ├── CartPage.jsx
│   │   │   ├── CheckoutPage.jsx
│   │   │   ├── HomePage.jsx
│   │   │   ├── LoginPage.jsx
│   │   │   ├── NotFoundPage.jsx
│   │   │   ├── OrderDetailPage.jsx
│   │   │   ├── OrderHistoryPage.jsx
│   │   │   ├── OrderSuccessPage.jsx
│   │   │   ├── ProductDetailPage.jsx
│   │   │   ├── ProductsPage.jsx
│   │   │   └── RegisterPage.jsx
│   │   ├── utils/
│   │   │   ├── api.js            # Axios instance + interceptors
│   │   │   └── helpers.js        # formatPrice, formatDate, etc.
│   │   ├── App.jsx
│   │   ├── index.css
│   │   └── main.jsx
│   ├── index.html
│   ├── package.json
│   ├── postcss.config.js
│   ├── tailwind.config.js
│   └── vite.config.js
│
├── .gitignore
├── package.json
└── README.md
```

---

## 🚀 Quick Start

### Prerequisites

- Node.js v18+
- MongoDB (local or [MongoDB Atlas](https://cloud.mongodb.com))
- Git

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/shopsphere.git
cd shopsphere
```

### 2. Backend setup

```bash
cd backend
cp .env.example .env
```

Edit `.env`:
```env
PORT=5000
MONGO_URI=mongodb://localhost:27017/ecommerce
JWT_SECRET=your_super_secret_jwt_key_change_this_in_production
NODE_ENV=development
```

Install dependencies and seed data:
```bash
npm install
npm run seed
```

Start the backend server:
```bash
npm run dev
```

The API will be running at `http://localhost:5000`

### 3. Frontend setup

```bash
cd ../frontend
npm install
npm run dev
```

The frontend will be running at `http://localhost:5173`

> The Vite dev server proxies `/api` requests to `http://localhost:5000` automatically.

---

## 📡 REST API Reference

### Auth
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login, returns JWT |
| GET | `/api/auth/profile` | Private | Get current user |

### Products
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/products` | Public | List products (paginated, filterable) |
| GET | `/api/products/featured` | Public | Featured products |
| GET | `/api/products/categories` | Public | All categories |
| GET | `/api/products/:id` | Public | Product details |

Query params for `/api/products`: `keyword`, `category`, `page`

### Cart
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/cart` | Private | Get user's cart |
| POST | `/api/cart` | Private | Add item `{ productId, quantity }` |
| PUT | `/api/cart/:productId` | Private | Update quantity `{ quantity }` |
| DELETE | `/api/cart/:productId` | Private | Remove item |
| DELETE | `/api/cart` | Private | Clear entire cart |

### Orders
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/orders` | Private | Place new order |
| GET | `/api/orders/myorders` | Private | Get all user orders |
| GET | `/api/orders/:id` | Private | Get single order |

---

## 🛠 Tech Stack

### Backend
- **Node.js** — Runtime environment
- **Express.js** — Web framework
- **MongoDB** — NoSQL database
- **Mongoose** — ODM for MongoDB
- **JWT** — JSON Web Token authentication
- **bcryptjs** — Password hashing
- **cors** — Cross-origin resource sharing
- **morgan** — HTTP request logger
- **express-async-handler** — Clean async error handling

### Frontend
- **React 18** — UI library
- **Vite** — Lightning-fast build tool
- **Tailwind CSS** — Utility-first CSS framework
- **React Router v6** — Client-side routing
- **Axios** — HTTP client with interceptors
- **react-hot-toast** — Toast notifications
- **Context API** — Global state (Auth + Cart)

---

## 🌐 Deployment

### Backend (Railway / Render / Heroku)

1. Set these environment variables on your platform:
   ```
   MONGO_URI=your_mongodb_atlas_uri
   JWT_SECRET=your_long_random_secret
   NODE_ENV=production
   PORT=5000
   ```

2. Set start command to: `node server.js`

### Frontend (Vercel / Netlify)

1. Update `vite.config.js` — change the proxy target to your deployed backend URL **or** set `VITE_API_URL` env variable and update `src/utils/api.js`:
   ```js
   baseURL: import.meta.env.VITE_API_URL || '/api'
   ```

2. Build command: `npm run build`  
   Publish directory: `dist`

3. Add a `_redirects` file (Netlify) or `vercel.json` for SPA routing:
   ```
   /* /index.html 200
   ```

### MongoDB Atlas

1. Create a free cluster at [cloud.mongodb.com](https://cloud.mongodb.com)
2. Whitelist your server IP (or `0.0.0.0/0` for all)
3. Get connection string and replace `MONGO_URI`
4. Run `npm run seed` once after deployment to populate products

---

## 🧪 Sample Data

Running `npm run seed` in the backend directory inserts 12 products across these categories:

| Category | Products |
|----------|----------|
| Electronics | Headphones, Keyboard, Fitness Tracker, Bluetooth Speaker |
| Clothing | T-Shirt, Denim Jacket |
| Sports & Outdoors | Water Bottle, Running Shoes, Yoga Mat |
| Home & Kitchen | Coffee Mug Set, Scented Candles |
| Accessories | Crossbody Bag |

---

## 🔐 Security Notes

- Passwords are hashed with bcrypt (salt rounds: 10)
- JWT tokens expire in 30 days
- All cart and order routes require valid Bearer token
- Change `JWT_SECRET` to a long random string in production
- Never commit your `.env` file

---

## 📦 Scripts Reference

From the **root** directory:
```bash
npm run install:all    # Install all dependencies (root + backend + frontend)
npm run dev:backend    # Start backend dev server (nodemon)
npm run dev:frontend   # Start frontend dev server (Vite)
npm run seed           # Seed the database with sample products
npm run build          # Build the frontend for production
```

From **backend/**:
```bash
npm run dev    # nodemon server.js
npm start      # node server.js
npm run seed   # Run seeder
```

From **frontend/**:
```bash
npm run dev      # Vite dev server
npm run build    # Production build
npm run preview  # Preview production build
```

---

## 📝 License

MIT License — free to use for personal and commercial projects.

---

Built with ❤️ using React, Node.js, Express, and MongoDB.
