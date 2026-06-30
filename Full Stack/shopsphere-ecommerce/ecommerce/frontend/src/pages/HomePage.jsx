import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import api from '../utils/api'
import ProductGrid from '../components/product/ProductGrid'
import { PageLoader } from '../components/common/Spinner'

export default function HomePage() {
  const [featured, setFeatured] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [featRes, catRes] = await Promise.all([
          api.get('/products/featured'),
          api.get('/products/categories'),
        ])
        setFeatured(featRes.data)
        setCategories(catRes.data)
      } catch (err) {
        console.error(err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  const categoryIcons = {
    'Electronics': '💻',
    'Clothing': '👕',
    'Sports & Outdoors': '🏃',
    'Home & Kitchen': '🏠',
    'Accessories': '👜',
  }

  return (
    <div>
      {/* Hero */}
      <section className="bg-gradient-to-br from-blue-700 via-blue-600 to-indigo-700 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 md:py-28 flex flex-col md:flex-row items-center gap-10">
          <div className="flex-1 text-center md:text-left">
            <span className="inline-block bg-white/20 text-white text-xs font-semibold px-3 py-1 rounded-full mb-4 tracking-wide uppercase">
              🛍 New Season, New Deals
            </span>
            <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold leading-tight mb-4">
              Shop Everything<br />
              <span className="text-yellow-300">You Love</span>
            </h1>
            <p className="text-blue-100 text-lg mb-8 max-w-xl">
              Discover thousands of products across electronics, fashion, home, and sports. Premium quality at unbeatable prices.
            </p>
            <div className="flex flex-col sm:flex-row gap-3 justify-center md:justify-start">
              <Link
                to="/products"
                className="bg-white text-blue-700 font-bold px-8 py-3 rounded-xl hover:bg-blue-50 transition-colors shadow-lg"
              >
                Shop Now →
              </Link>
              <Link
                to="/register"
                className="border-2 border-white/60 text-white font-semibold px-8 py-3 rounded-xl hover:bg-white/10 transition-colors"
              >
                Create Account
              </Link>
            </div>
          </div>
          <div className="hidden md:flex flex-1 justify-center">
            <div className="grid grid-cols-2 gap-3 max-w-sm">
              {['https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=200', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=200', 'https://images.unsplash.com/photo-1581655353564-df123a1eb820?w=200', 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=200'].map((src, i) => (
                <img key={i} src={src} alt="" className="rounded-2xl w-32 h-32 object-cover shadow-xl" style={{ transform: i % 2 === 0 ? 'translateY(12px)' : 'translateY(-12px)' }} />
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* Stats */}
      <section className="bg-white border-b border-gray-100">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6 text-center">
            {[['10,000+', 'Products'], ['50K+', 'Happy Customers'], ['Free', 'Returns'], ['24/7', 'Support']].map(([val, label]) => (
              <div key={label}>
                <p className="text-2xl font-extrabold text-blue-600">{val}</p>
                <p className="text-sm text-gray-500 font-medium">{label}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Categories */}
      {categories.length > 0 && (
        <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <h2 className="text-2xl font-bold text-gray-900 mb-6">Shop by Category</h2>
          <div className="flex flex-wrap gap-3">
            {categories.map((cat) => (
              <Link
                key={cat}
                to={`/products?category=${encodeURIComponent(cat)}`}
                className="flex items-center gap-2 bg-white border border-gray-200 hover:border-blue-400 hover:bg-blue-50 text-gray-700 font-medium px-4 py-2.5 rounded-xl transition-all shadow-sm"
              >
                <span className="text-lg">{categoryIcons[cat] || '🛒'}</span>
                {cat}
              </Link>
            ))}
          </div>
        </section>
      )}

      {/* Featured Products */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-16">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-gray-900">Featured Products</h2>
          <Link to="/products" className="text-blue-600 font-semibold hover:underline text-sm">
            View all →
          </Link>
        </div>
        {loading ? <PageLoader /> : <ProductGrid products={featured} />}
      </section>

      {/* CTA Banner */}
      <section className="bg-gradient-to-r from-orange-500 to-pink-600 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-14 text-center">
          <h2 className="text-3xl font-extrabold mb-3">Ready to start shopping?</h2>
          <p className="text-white/80 mb-6">Join thousands of satisfied customers today.</p>
          <Link
            to="/products"
            className="inline-block bg-white text-orange-600 font-bold px-8 py-3 rounded-xl hover:bg-orange-50 transition-colors shadow-lg"
          >
            Browse All Products
          </Link>
        </div>
      </section>
    </div>
  )
}
