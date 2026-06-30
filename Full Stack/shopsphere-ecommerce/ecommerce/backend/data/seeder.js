const dotenv = require('dotenv');
const mongoose = require('mongoose');
const Product = require('../models/Product');
const User = require('../models/User');

dotenv.config();

const products = [
  {
    name: 'Premium Wireless Headphones',
    description: 'Experience crystal-clear audio with our premium wireless headphones. Features 40-hour battery life, active noise cancellation, and ultra-comfortable ear cushions. Perfect for music lovers and remote workers.',
    price: 79.99,
    originalPrice: 129.99,
    category: 'Electronics',
    brand: 'SoundWave',
    image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600',
    images: [
      'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600',
      'https://images.unsplash.com/photo-1583394838336-acd977736f90?w=600',
    ],
    countInStock: 50,
    rating: 4.5,
    numReviews: 128,
    isFeatured: true,
    tags: ['audio', 'wireless', 'noise-cancelling'],
  },
  {
    name: 'Slim Fit Cotton T-Shirt',
    description: 'Crafted from 100% organic cotton, this slim-fit t-shirt offers comfort and style. Available in multiple colors. Machine washable and pre-shrunk.',
    price: 24.99,
    originalPrice: 34.99,
    category: 'Clothing',
    brand: 'UrbanEdge',
    image: 'https://images.unsplash.com/photo-1581655353564-df123a1eb820?w=600',
    images: ['https://images.unsplash.com/photo-1581655353564-df123a1eb820?w=600'],
    countInStock: 200,
    rating: 4.2,
    numReviews: 86,
    isFeatured: true,
    tags: ['cotton', 'casual', 'slim-fit'],
  },
  {
    name: 'Stainless Steel Water Bottle',
    description: 'Keep beverages cold for 24 hours or hot for 12 hours with this vacuum-insulated stainless steel water bottle. BPA-free, leak-proof lid, and fits most cup holders.',
    price: 19.99,
    originalPrice: 29.99,
    category: 'Sports & Outdoors',
    brand: 'HydroMax',
    image: 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600',
    images: ['https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600'],
    countInStock: 150,
    rating: 4.7,
    numReviews: 215,
    isFeatured: true,
    tags: ['hydration', 'outdoor', 'insulated'],
  },
  {
    name: 'Mechanical Gaming Keyboard',
    description: 'Take your gaming to the next level with this mechanical keyboard featuring RGB backlighting, Cherry MX switches, and a durable aluminum frame. Anti-ghosting technology for precise inputs.',
    price: 89.99,
    originalPrice: 119.99,
    category: 'Electronics',
    brand: 'GameForce',
    image: 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600',
    images: ['https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600'],
    countInStock: 40,
    rating: 4.6,
    numReviews: 97,
    isFeatured: true,
    tags: ['gaming', 'mechanical', 'RGB'],
  },
  {
    name: 'Leather Crossbody Bag',
    description: 'Elegant genuine leather crossbody bag with multiple compartments. Adjustable strap, gold-tone hardware, and interior zipper pockets. Perfect for everyday use.',
    price: 59.99,
    originalPrice: 89.99,
    category: 'Accessories',
    brand: 'LuxeLeather',
    image: 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=600',
    images: ['https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=600'],
    countInStock: 75,
    rating: 4.4,
    numReviews: 63,
    isFeatured: false,
    tags: ['leather', 'bag', 'fashion'],
  },
  {
    name: 'Smart Fitness Tracker',
    description: 'Track your steps, heart rate, sleep, and calories with this waterproof fitness band. 7-day battery life, smartphone notifications, and built-in GPS. Compatible with iOS and Android.',
    price: 49.99,
    originalPrice: 79.99,
    category: 'Electronics',
    brand: 'FitPulse',
    image: 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=600',
    images: ['https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=600'],
    countInStock: 60,
    rating: 4.3,
    numReviews: 142,
    isFeatured: true,
    tags: ['fitness', 'smartwatch', 'health'],
  },
  {
    name: 'Ceramic Coffee Mug Set',
    description: 'Set of 4 hand-crafted ceramic mugs in earthy tones. Each holds 12oz, dishwasher and microwave safe. A perfect gift for coffee enthusiasts.',
    price: 34.99,
    originalPrice: 44.99,
    category: 'Home & Kitchen',
    brand: 'CeramicCraft',
    image: 'https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=600',
    images: ['https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?w=600'],
    countInStock: 120,
    rating: 4.8,
    numReviews: 189,
    isFeatured: false,
    tags: ['coffee', 'ceramic', 'kitchen'],
  },
  {
    name: 'Running Shoes Pro',
    description: 'Engineered for performance with responsive foam cushioning, breathable mesh upper, and a durable rubber outsole. Ideal for road running and gym workouts.',
    price: 94.99,
    originalPrice: 139.99,
    category: 'Sports & Outdoors',
    brand: 'SwiftStride',
    image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600',
    images: ['https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600'],
    countInStock: 85,
    rating: 4.5,
    numReviews: 174,
    isFeatured: true,
    tags: ['running', 'shoes', 'sport'],
  },
  {
    name: 'Portable Bluetooth Speaker',
    description: '360° surround sound in a compact design. IPX7 waterproof, 20-hour playtime, built-in microphone for hands-free calls. Pair two speakers for true stereo sound.',
    price: 44.99,
    originalPrice: 69.99,
    category: 'Electronics',
    brand: 'SoundWave',
    image: 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600',
    images: ['https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=600'],
    countInStock: 95,
    rating: 4.4,
    numReviews: 110,
    isFeatured: false,
    tags: ['bluetooth', 'speaker', 'portable'],
  },
  {
    name: 'Classic Denim Jacket',
    description: 'Timeless denim jacket with button-front closure, chest pockets, and a comfortable relaxed fit. Washed for a vintage look. A wardrobe essential for all seasons.',
    price: 54.99,
    originalPrice: 79.99,
    category: 'Clothing',
    brand: 'UrbanEdge',
    image: 'https://images.unsplash.com/photo-1551537482-f2075a1d41f2?w=600',
    images: ['https://images.unsplash.com/photo-1551537482-f2075a1d41f2?w=600'],
    countInStock: 60,
    rating: 4.3,
    numReviews: 78,
    isFeatured: false,
    tags: ['denim', 'jacket', 'casual'],
  },
  {
    name: 'Yoga Mat Premium',
    description: 'Extra-thick 6mm non-slip yoga mat with alignment lines. Eco-friendly TPE material, lightweight and easy to carry with included strap. Perfect for yoga, pilates, and stretching.',
    price: 29.99,
    originalPrice: 49.99,
    category: 'Sports & Outdoors',
    brand: 'ZenFlex',
    image: 'https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=600',
    images: ['https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=600'],
    countInStock: 110,
    rating: 4.6,
    numReviews: 201,
    isFeatured: false,
    tags: ['yoga', 'fitness', 'mat'],
  },
  {
    name: 'Scented Soy Candle Set',
    description: 'Hand-poured soy wax candles in three calming scents: Lavender, Vanilla, and Eucalyptus. 40-hour burn time each. Packaged in a beautiful gift box.',
    price: 27.99,
    originalPrice: 39.99,
    category: 'Home & Kitchen',
    brand: 'CalmGlow',
    image: 'https://images.unsplash.com/photo-1602928321679-560bb453f190?w=600',
    images: ['https://images.unsplash.com/photo-1602928321679-560bb453f190?w=600'],
    countInStock: 80,
    rating: 4.7,
    numReviews: 134,
    isFeatured: false,
    tags: ['candle', 'aromatherapy', 'gift'],
  },
];

const seedData = async () => {
  try {
    await mongoose.connect(process.env.MONGO_URI);
    console.log('MongoDB Connected');

    await Product.deleteMany();
    console.log('Products cleared');

    await Product.insertMany(products);
    console.log(`${products.length} products seeded`);

    process.exit(0);
  } catch (error) {
    console.error('Seeder error:', error);
    process.exit(1);
  }
};

seedData();
